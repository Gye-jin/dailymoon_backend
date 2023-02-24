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
		   http.csrf().disable()
           .sessionManagement()
           .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

           .and()
           .httpBasic().disable()
           .formLogin().disable()
           .addFilter(corsfilter);

         http.authorizeRequests()
         	.antMatchers("http://localhost:3000/**")
         	.authenticated()
           .anyRequest()
           .permitAll()
           .and()
           .exceptionHandling()
           .authenticationEntryPoint(new CustomAuthenticationEntryPoint());;

   //(2)
   http.addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
      
   }
}
