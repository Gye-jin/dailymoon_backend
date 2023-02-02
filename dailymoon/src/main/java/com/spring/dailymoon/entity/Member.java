package com.spring.dailymoon.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.dailymoon.dto.MemberDTO;

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
	/* [Column]
	 * 기본키: userId
	 * 기본키를 제외한 나머지 column들은 회원가입(카카오API) 시 정보를 제공한 회원만 삽입
	 */
	@Id
	@Column(name = "user_id")
	private long userId;
	
	private String nickname;
	private String email;
	private String gender;
	private String birth;
	
	/* [Join]
	 * @JsonIgnore: toString 실행될 경우 해당 객체 무시
	 * @OneToMany: 일대다
	 * mappedBy: 조인의 주최로부터 조인되는 테이블
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "member")
	private List<Diary> diarys = new ArrayList<Diary>();
	
	/* [Entity to DTO]
	 * Member 타입의 객체 삽입 시 MemberDTO 타입의 객체 반환
	 */
	public static MemberDTO memberEntityToDTO(Member member)  {
		MemberDTO memberDTO = MemberDTO.builder().userId(member.userId)
				.nickname(member.nickname).email(member.email)
				.gender(member.gender).birth(member.birth).build();
		return memberDTO;
	}
}