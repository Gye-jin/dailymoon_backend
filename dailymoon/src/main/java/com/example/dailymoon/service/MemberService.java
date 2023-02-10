package com.example.dailymoon.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.example.dailymoon.entity.Member;
import com.google.gson.JsonElement;

public interface MemberService {

	public Member createUser(JsonElement element);
	
	public ResponseEntity<String> logout(HttpServletRequest request);
}
