package com.example.dailymoon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.dailymoon.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
	@Query(value = "SELECT * FROM file f\r\n" + 
			"WHERE f.diary_no = :diary_no", nativeQuery = true)
	public List<File> findByDiaryNo(@Param("diary_no") Long diaryNo);
}
