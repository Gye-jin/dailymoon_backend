package com.example.dailymoon.service;

import com.example.dailymoon.entity.Member;

public interface MemberService {

	
	public String getKaKaoAccessToken(String code);
	
//	public boolean getKakaoUser(String token);
	
	public String createKakaoUser(String token);
}
