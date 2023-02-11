package com.example.dailymoon.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.dailymoon.config.JwtProperties;
import com.example.dailymoon.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {

	@Override
	public String createToken(Member member, String accesstoken) {
		System.out.println("######## createToken User : " + member);

		String jwtToken = JWT.create().withSubject(member.getNickname())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.withClaim("id", member.getUserId())
				.withClaim("accessToken",accesstoken)
				.withClaim("password", member.getPassword())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));

		return jwtToken;
	}

}
