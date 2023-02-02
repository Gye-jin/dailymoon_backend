package com.spring.dailymoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.dailymoon.entity.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {

}
