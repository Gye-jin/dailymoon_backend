package com.example.dailymoon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dailymoon.entity.Member;
import com.example.dailymoon.repository.MemberRepository;
import com.google.gson.JsonElement;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberRepository memberRepo;

	public String createUser(JsonElement element) {

		Long id = element.getAsJsonObject().get("id").getAsLong();
		String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();

		String birth = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("birthday").getAsString();

		String gender = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("gender").getAsString();

		String email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();

		if (!memberRepo.existsByUserId(id)) {
			Member member = Member.builder().userId(id).nickname(nickname).email(email).gender(gender).birth(birth)
					.build();
			memberRepo.save(member);
		}
		return null;
	}
}
