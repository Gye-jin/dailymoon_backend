package com.example.dailymoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dailymoon.entity.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
