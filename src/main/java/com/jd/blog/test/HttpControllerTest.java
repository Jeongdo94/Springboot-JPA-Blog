package com.jd.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 사용자 요청 -> 응답(HTML파일)
// @Controller

// 사용자 요청 -> 응답(Data)
// @RestController

@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest : ";
	
	@GetMapping("/http/lombok")
	public String lombokTest() {
		Member m = Member.builder().username("jd").build();
		System.out.println(TAG + "getter : " + m.getUsername());
		System.out.println(TAG + "getter : " + m.getId());
		m.setId(5000);
		System.out.println(TAG + "setter : " + m.getUsername());
		System.out.println(TAG + "setter : " + m.getId());
		return "lombok test 완료";
	}
	
	// http://localhost:8080/http/get (select)
	@GetMapping("/http/get")
	public String getTest(Member m) { // id=1&username=jd&password=1234&email=jd@gmail.com
		return "get 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	// http://localhost:8080/http/post (insert)
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) { //@RequestBody 문서로 전송(text, json)
		return "post 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	// http://localhost:8080/http/put (update)
	@PutMapping("/http/put")
	public String putTest() {
		return "put 요청";
	}
	
	// http://localhost:8080/http/delete (delete)
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
