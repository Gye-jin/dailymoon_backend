package com.spring.dailymoon.repository;

import org.springframework.stereotype.Repository;

import com.spring.dailymoon.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>{

}
