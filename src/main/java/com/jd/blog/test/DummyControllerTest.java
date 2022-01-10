package com.jd.blog.test;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jd.blog.model.RoleType;
import com.jd.blog.model.User;
import com.jd.blog.repository.UserRepository;

// html 파일이 아니라 data를 리턴해주는 controller = @Restcontroller
@RestController
public class DummyControllerTest {

	@Autowired // 의정존 주입(DI)
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 ID는 DB에 없습니다. ID : " + id;
		}
		
		return "삭제되었습니다 ID : " + id;
	}
	
	// GetMapping, PutMapping 둘이 기능이 다르기때문에 주소가 같아도 상관없다.
	@Transactional // @Transactional의 더티 체킹을 이용해 Commit을한다 -> DB에 값 입력됨
	@PutMapping("/dummy/user/{id}")           // @RequestBody -> json데이터를 받아올 수 있음
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { // json 데이터를 요청 -> Java Object(MessageConverter의 Jackson라이브러리가)로 변환해서 받아줌 
		System.out.println("id : " + id);
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		// @Transactional의 더티 체킹을 이용해 Commit을한다 -> DB에 값 입력됨
		/* userRepository.save(user); */
		return user;
	}
	
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> pagingUser = userRepository.findAll(pageable);
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	// {id} 주소로 파라미터를 전달 받을 수 있음.
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	private User detail(@PathVariable int id) {
		// user/4를 찾을때 데이터베이스에서 못찾아오게 되면 user가 null이다
		// 그럼 return null 이 리턴이 됨 그럼 프로그렘에 문제가 생긴다.
		// 그래서 Optional로 너의 User객체를 감싸서 가져올테니 null인지 아닌지 판단해서 return한다.
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다 ID : " + id);
			}
		});
//     람다식
//		User user = userRepository.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("해당 사용자가 없습니다.");
//		});
		
		// 요청 : 웹브라우저
		// user 객체 = 자바 오브젝트
		// return을 하면 요청하는 웹브라우저는 user객체(자바 오브젝트)를 이해하지 못함
		// 그래서 웹브라우저가 이해 할 수 있는 데이터로 변환한다 -> json
		// 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출
		// user 오브젝트를 json으로 변환해서 브라우저에게 던져준다.
		return user;
	}
	
	@PostMapping("/dummy/join")
	public String join(User user) {
		System.out.println("id : " + user.getId());
		System.out.println("username : " + user.getUsername());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		System.out.println("role : " + user.getRole());
		System.out.println("createDate : " + user.getCreateDate());

		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}
