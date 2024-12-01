package com.example.quiz11.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

// 三個省略黃蚯蚓的方式其一，選哪個都可以
@SuppressWarnings("serial")

// 集中管理Id，只要生成 getters setters
public class QuesId implements Serializable {

	private int quizId;

	private int quesId;

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

}
