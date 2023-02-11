package com.example.dailymoon.service;

import com.example.dailymoon.entity.Member;

public interface JWTService {
	public String createToken(Member member, String accesstoken);
}
