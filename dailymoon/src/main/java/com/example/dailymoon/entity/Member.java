package com.example.dailymoon.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

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
	private String email;
	private String gender;
	private String birth;
}
