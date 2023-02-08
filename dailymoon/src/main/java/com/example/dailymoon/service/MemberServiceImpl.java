package com.example.dailymoon.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dailymoon.entity.Member;
import com.example.dailymoon.repository.MemberRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberRepository memberRepo;
	

	@Override
	public String getKaKaoAccessToken(String code) {
		String access_Token = "";
		String refresh_Token = "";
		String reqURL = "https://kauth.kakao.com/oauth/token";

		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// POST 요청을 위해 기본값이 false인 setDoOutput을 true로
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			// POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=45285e0960a657197d7e58601b2e8c97"); // TODO REST_API_KEY 입력
			sb.append("&redirect_uri=http://localhost:8080/api/kakao"); // TODO 인가코드 받은 redirect_uri 입력
			sb.append("&code=" + code);
			bw.write(sb.toString());
			bw.flush();
		
			// 결과 코드가 200이라면 성공
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);

			// Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);

			access_Token = element.getAsJsonObject().get("access_token").getAsString();
			refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

			System.out.println("access_token : " + access_Token);
			System.out.println("refresh_token : " + refresh_Token);

			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return access_Token;
	}


	public String createKakaoUser(String token) {
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		

		// access_token을 이용하여 사용자 정보 조회
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Authorization", "Bearer " + token); // 전송할 header 작성, access_token전송

			// 결과 코드가 200이라면 성공
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);

			// Gson 라이브러리로 JSON파싱
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);

			Long id = element.getAsJsonObject().get("id").getAsLong();
			String nickname = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname")
					.getAsString();
			boolean hasBirthday = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_birthday")
					.getAsBoolean();
			boolean hasGender = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_gender")
					.getAsBoolean();
			boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email")
					.getAsBoolean();
			String birth = "";
			String gender = "";
			String email = "";
			if (hasBirthday) {
				birth = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("birthday").getAsString();
			}
			if (hasGender) {
				gender = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("gender").getAsString();
			}
			if (hasEmail) {
				email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
			}
			System.out.println("id : " + id);
			System.out.println("email : " + email);

			if (!memberRepo.existsByUserId(id)) {
			Member member = Member.builder().userId(id).nickname(nickname).email(email).gender(gender)
						.birth(birth).build();
				memberRepo.save(member);
		
			} 
			br.close();
//			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(id, cosKey));
//			SecurityContextHolder.getContext().setAuthentication(authentication);


		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
