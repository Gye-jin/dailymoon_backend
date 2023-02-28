package com.example.dailymoon.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.dailymoon.dto.MemberDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<Diary> boards = new ArrayList<Diary>();
	
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
