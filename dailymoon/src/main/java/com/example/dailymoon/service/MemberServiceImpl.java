package com.example.dailymoon.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.dailymoon.dto.MemberDTO;
import com.example.dailymoon.entity.Member;
import com.example.dailymoon.repository.MemberRepository;
import com.google.gson.JsonElement;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	
	@Value("${kakao.app.admin.key}")
	String adminKey;
	
	@Autowired
	MemberRepository memberRepo;
	
	public Member createUser(JsonElement element) {

		Long id = element.getAsJsonObject().get("id").getAsLong();
	      String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
	      
	      UUID uuid = UUID.randomUUID();
	      String encPassword = new BCryptPasswordEncoder().encode(uuid.toString());

	      String birth = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("birthday").getAsString();

	      String gender = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("gender").getAsString();

	      String email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
	      Member member = Member.builder().userId(id).password(encPassword).nickname(nickname).email(email).gender(gender).birth(birth)
	            .build();
	      if (!memberRepo.existsByUserId(id)) {
	         memberRepo.save(member);
	         
	      }
	      return member;
	}
	
	
//	  로그아웃 할 때 액세스코드가 아닌 해당 카카오ID로 로그인 된 모든 기기에서 로그아웃하기
//	  해당 사용자의 모든 토큰 만료 처리(브라우저 달라도 로그아웃 되지 않을까!?)
//	
	@Override
	public ResponseEntity<String> logout(HttpServletRequest request) {
		// request 안에 들어있는 jwt 토큰의 payload 부분에서 kakaoId 정보를 불러옴
		Long kakaoId = (Long) request.getAttribute("id");
		
		// 통신에 필요한 RestTemplate 객체를 만든다
		RestTemplate rt = new RestTemplate();
		
		// http 헤더
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + adminKey);
		
		// http 바디
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("target_id_type", "user_id");
		params.add("target_id", kakaoId.toString());
		
		// HttpHeader 정보를 http 엔터티에 담아준다
		HttpEntity<MultiValueMap<String, String>> logoutRequest =
				new HttpEntity<>(params, headers);
		
		// 토큰 갱신
		ResponseEntity<String> kakaoLogoutResponse = rt.exchange(
				"https://kapi.kakao.com/v1/user/logout",
				HttpMethod.POST,
				logoutRequest,
				String.class
		);
		
		System.out.println("###### logout2 : " + kakaoLogoutResponse);
		
		return kakaoLogoutResponse;
	}
	
	
	
	@Override
	public MemberDTO getMember(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Member member = memberRepo.findByUserId(userId);
       MemberDTO memberDTO = MemberDTO.memberEntityToDTO(member);
        return memberDTO;
	}
}
