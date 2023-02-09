package com.example.dailymoon.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dailymoon.entity.Diary;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

	@Query(value = "SELECT * FROM (SELECT * FROM diary fd WHERE fd.user_id=:id) d\r\n" + 
				   "WHERE d.`date` = :date", nativeQuery = true)
	public Diary findByUserIdAndDate(@Param("id") Long userId, @Param("date") LocalDate date);
}
