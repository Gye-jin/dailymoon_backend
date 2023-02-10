package com.example.dailymoon.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api",produces = "application/json")
@CrossOrigin(origins = {"*"})
public class DiaryController {
	
	// Create
}
