package com.spring.dailymoon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dailymoon.dto.DiaryDTO;
import com.spring.dailymoon.service.MemberService;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
@CrossOrigin(origins = {"*"})
public class MemberController {
	@Autowired
	MemberService memberService;
	
	@GetMapping(value = "/join")
	public long joinDailymoon(@RequestParam long userId) {
		System.out.println("controller: "+userId);
		memberService.joinMember(userId);
		return userId;
	}
	
	@PostMapping(value = "/post")
	public boolean postDiary(@ModelAttribute DiaryDTO diaryDTO) {
		System.out.println("controller: "+diaryDTO.getDetail());
		System.out.println("controller: "+diaryDTO.getFeeling());
		return memberService.postDiary(diaryDTO);
	}
}
