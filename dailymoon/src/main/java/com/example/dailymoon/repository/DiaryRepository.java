package com.example.dailymoon.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dailymoon.entity.Diary;
import com.example.dailymoon.entity.Member;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

	@Query(value = "SELECT * FROM (SELECT * FROM diary fd WHERE fd.user_id=:id) d\r\n" + 
				   "WHERE d.`date` = :date", nativeQuery = true)
	public Diary findByUserIdAndDate(@Param("id") Long userId, @Param("date") LocalDate date);

	public boolean ExistByMemberAndDate(Member member, LocalDate date);

	@Query(value = "SELECT * FROM (SELECT * FROM diary fd WHERE fd.user_id=:id) d", nativeQuery = true)
	public List<Diary> findByUserId(@Param("id") Long userId);
	
}
