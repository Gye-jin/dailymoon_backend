package com.example.dailymoon.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.example.dailymoon.dto.MemberDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member {
	@Id
	private Long userId;
	
	private String nickname;
	private String password;
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
