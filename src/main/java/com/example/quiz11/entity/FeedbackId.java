package com.example.quiz11.entity;

import java.io.Serializable;

import javax.persistence.Column;

@SuppressWarnings("serial")
public class FeedbackId implements Serializable {

	private int quizId;

	private int quesId;

	private String email;

	public FeedbackId() {
		super();
	}

	public FeedbackId(int quizId, int quesId, String email) {
		super();
		this.quizId = quizId;
		this.quesId = quesId;
		this.email = email;
	}

	public int getQuizId() {
		return quizId;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getEmail() {
		return email;
	}

}
