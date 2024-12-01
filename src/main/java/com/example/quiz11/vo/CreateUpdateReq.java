package com.example.quiz11.vo;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;

import com.example.quiz11.entity.Ques;
import com.example.quiz11.entity.Quiz;

public class CreateUpdateReq extends Quiz {

	// 1張問卷應該會有很多題，除了繼承Quiz原有的name, description, startDate, endDate, published
	private List<Ques> quesList;

	public CreateUpdateReq() {
		super();
	}

	// 沒有 quiz_id
	public CreateUpdateReq(String name, String description, LocalDate startDate, LocalDate endDate, //
			boolean published, List<Ques> quesList) {
		super(name, description, startDate, endDate, published);
		this.quesList = quesList;
	}

	// 有 quiz_id
	public CreateUpdateReq(int id, String name, String description, LocalDate startDate, LocalDate endDate, //
			boolean published, List<Ques> quesList) {
		super(id, name, description, startDate, endDate, published);
		this.quesList = quesList;
	}

	public List<Ques> getQuesList() {
		return quesList;
	}

}
