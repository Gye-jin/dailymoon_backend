package com.spring.dailymoon.dto;

import com.spring.dailymoon.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberDTO {
	private long userId;
	private String nickname;
	private String email;
	private String gender;
	private String birth;
	
	public static Member memberDTOToEntity(MemberDTO memberDTO) {
		Member member = Member.builder().userId(memberDTO.userId)
				.nickname(memberDTO.nickname).email(memberDTO.email)
				.gender(memberDTO.gender).birth(memberDTO.birth).build();
		return member;
	}
}
