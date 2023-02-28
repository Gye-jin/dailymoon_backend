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
	private String password;
	private String nickname;
	private String email;
	private String gender;
	private String birth;
	
	public static MemberDTO memberEntityToDTO(Member member) {
		MemberDTO memberDTO = MemberDTO.builder()
				.userId(member.getUserId())
				.password(member.getPassword())
				.nickname(member.getNickname())
				.email(member.getEmail())
				.gender(member.getGender())
				.birth(member.getBirth())
				.build();
		
		return memberDTO;
	}
	

}
