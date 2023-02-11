package com.example.dailymoon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.example.dailymoon.config.jwt.CustomAuthenticationEntryPoint;
import com.example.dailymoon.config.jwt.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CorsFilter corsfilter;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
		.antMatchers("/","/api/kakao").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.permitAll()
		.and()
		.logout()
		.permitAll()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
	
		http.httpBasic().disable()
		.formLogin().disable()
		.addFilter(corsfilter);	// 기존의 crossorigin(인증 x), 시큐리티 필터에 등록 인증(o)
	
		http.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.maximumSessions(1)
		.expiredUrl("/login?expire=true");
		
		 http.addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
	}
	
	
}
