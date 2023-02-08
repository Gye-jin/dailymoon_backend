package com.example.dailymoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dailymoon.api.AcessToken;
import com.example.dailymoon.api.KakaoAPI;
import com.example.dailymoon.service.MemberServiceImpl;
import com.google.gson.JsonElement;

@RestController
@RequestMapping(value = "/api",produces = "application/json")
@CrossOrigin(origins = {"*"})
public class MemberController {
	
	
	@Autowired 
	MemberServiceImpl MemberService;
	
	
	@GetMapping("/kakao")
//	public @ResponseBody String login(@RequestParam String accestoken)  {
	public @ResponseBody String login(@RequestParam String code)  {
		String accestoken = AcessToken.getKaKaoAccessToken(code);
		JsonElement element = KakaoAPI.UserInfo(accestoken);
		MemberService.createUser(element);
		return "카카오 로그인 성공";
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
