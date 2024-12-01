package com.example.quiz11;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz11.service.ifs.QuizService;

@SpringBootTest
class Quiz11ApplicationTests {

	@Autowired
	private QuizService quizservice;

	@Test
	public void createTest() {
		
	}
	
}
