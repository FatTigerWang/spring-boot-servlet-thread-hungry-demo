package com.example.demo.controller;

import java.util.concurrent.Callable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlowController {

	/**
	 * 此方法直接在Servlet线程执行耗时操作，这在请求并发的情况下会导致Servlet的线程饥饿，从而影响接口吞吐量
	 * 
	 * @param servletRequest
	 * @return
	 * @throws InterruptedException
	 */
	@GetMapping("/slow")
	public String index() throws InterruptedException {
		System.out.println("Servlet-ThredName：" + Thread.currentThread().getName());
		var str = slowGetString();
		System.out.println("Servlet Thred Released");
		return str;
	}

	/**
	 * 此方法中的耗时操作通过后台线程执行，Servlet线程会立即返回线程池，当后台线程处理完成后，Servlet线程继续执行。
	 * 此操作能在并发请求时不阻塞Servlet线程而影响接口的吞吐量 Callable的线程池设置在WebMvcConfiguration.java
	 * https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-async-callable
	 * https://stackoverflow.com/questions/56055148/how-to-create-a-non-blocking-restcontroller-webservice-in-spring
	 * 
	 * @param servletRequest
	 * @return
	 * @throws InterruptedException
	 */
	@GetMapping("/callable")
	public Callable<String> indexOfCallable() throws InterruptedException {
		System.out.println("Servlet-ThredName：" + Thread.currentThread().getName());
		var callableCall = new Callable<String>() {
			public String call() throws Exception {
				return slowGetString();
			}
		};
		System.out.println("Servlet Thred Released");

		return callableCall;
	}

	private String slowGetString() throws InterruptedException {
		System.out.println("SlowGetString-ThredName：" + Thread.currentThread().getName());
		Thread.sleep(2000);
		return "Greetings from Spring Boot!";
	}

}
