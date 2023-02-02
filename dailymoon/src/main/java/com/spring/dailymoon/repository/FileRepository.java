package com.spring.dailymoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.dailymoon.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

}
