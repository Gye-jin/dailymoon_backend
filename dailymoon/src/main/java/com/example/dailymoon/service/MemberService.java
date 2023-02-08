package com.example.dailymoon.service;

public interface MemberService {
	
	public String getKaKaoAccessToken(String code);
	
//	public boolean getKakaoUser(String token);
	
	public String createKakaoUser(String token);
}
