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
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(exclude = {"diarys"})
public class Member {
	@Id
	private Long userId;
	
	private String nickname;
	private String password;
	private String email;
	private String gender;
	private String birth;
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<Diary> diarys = new ArrayList<Diary>();
	
	public static Member userDTOToEntity(MemberDTO memberDTO) {
		Member member = Member.builder()
								.userId(memberDTO.getUserId())
								.password(memberDTO.getPassword())
								.nickname(memberDTO.getNickname())
								.email(memberDTO.getEmail())
								.gender(memberDTO.getGender())
								.birth(memberDTO.getBirth())
								 .build();
		return member;
	}
}
