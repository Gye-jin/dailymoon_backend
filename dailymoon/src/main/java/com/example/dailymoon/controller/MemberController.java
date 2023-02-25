package com.example.dailymoon.controller;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dailymoon.api.AcessToken;
import com.example.dailymoon.api.KakaoAPI;
import com.example.dailymoon.config.JwtProperties;
import com.example.dailymoon.entity.Member;
import com.example.dailymoon.service.JWTServiceImpl;
import com.example.dailymoon.service.MemberServiceImpl;
import com.google.gson.JsonElement;

@RestController
@RequestMapping(value = "/api",produces = "application/json")
public class MemberController {
	
	
	@Autowired 
	MemberServiceImpl MemberService;
	
	@Autowired
	JWTServiceImpl jwtservice;
	
	// 로그인 기능
	@GetMapping("/kakao")
	public ResponseEntity<String> kakao(@RequestParam String code)  {
		System.out.println("aaa");
		String accesstoken = AcessToken.getKaKaoAccessToken(code);
		JsonElement element = KakaoAPI.UserInfo(accesstoken);
		Member member = MemberService.createUser(element);
		String jwttoken = jwtservice.createToken(member,accesstoken);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("key", "login");
		headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwttoken);
		
		// JWT 가 담긴 헤더와 200 ok 스테이터스 값, "success" 라는 바디값을 ResponseEntity 에 담아 프론트 측에 전달한다
		System.out.println("##### " + ResponseEntity.ok().headers(headers).body("success"));

		return ResponseEntity.ok().headers(headers).body("성공");
	}
		
    // 로그 아웃 기능
    @GetMapping("/kakaoLogout")
    public ResponseEntity<String> getLogout(HttpServletRequest request) {
    	
    	
    	ResponseEntity<String> response = MemberService.logout(request);
    	System.out.println(response+"logout완료");
    	
		return ResponseEntity.ok().body("success");
    }
	
	
}
