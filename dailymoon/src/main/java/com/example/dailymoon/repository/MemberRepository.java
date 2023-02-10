package com.example.dailymoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.dailymoon.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>{
	
	public Member findByUserId(Long Userid);
	
	public boolean existsByUserId(Long Userid);
}
