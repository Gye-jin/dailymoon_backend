package com.example.dailymoon.dto;

import com.example.dailymoon.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberDTO {
	
	private Long userId;
	private String nickname;
	private String email;
	private String gender;
	private String birth;
	
	public static Member userDTOToEntity(MemberDTO memberDTO) {
		Member member = Member.builder()
								.userId(memberDTO.getUserId())
								.nickname(memberDTO.getNickname())
								.email(memberDTO.getEmail())
								.gender(memberDTO.getGender())
								.birth(memberDTO.getBirth())
								 .build();
		return member;
	}
}
