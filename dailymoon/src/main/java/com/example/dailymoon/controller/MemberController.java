package com.example.dailymoon.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.dailymoon.service.MemberService;
import com.example.dailymoon.service.MemberServiceImpl;

@RestController
@RequestMapping(value = "/api",produces = "application/json")
@CrossOrigin(origins = {"*"})
public class MemberController {
	
	
	@Autowired 
	MemberServiceImpl MemberService;
	
	
	@GetMapping("/kakao")
	public @ResponseBody String login(@RequestParam String code, HttpSession session) {
		System.out.println(code);
		String accestoken = MemberService.getKaKaoAccessToken(code);
		MemberService.createKakaoUser(accestoken);
		return "카카오 로그인 성공"+code+"    "+accestoken;
	}
	
//	@RequestMapping(value="/logout")
//	public ModelAndView logout(HttpSession session) {
//		ModelAndView mav = new ModelAndView();
//		
//		kakaoApi.kakaoLogout((String)session.getAttribute("accessToken"));
//		session.removeAttribute("accessToken");
//		session.removeAttribute("userId");
//		mav.setViewName("index");
//		return mav;
//	}
	
	
}
