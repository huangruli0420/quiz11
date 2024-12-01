package com.example.quiz11.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "quiz")
public class Quiz {

	// 因為 PK 是 AI(Auto Incremental)，所以要加上此 @GeneratedValue
	// strategy: 指的是 AI 的生成策略
	// GenerationType.IDENTITY: 代表 PK 數字由資料庫來自動增加
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

//	@JsonProperty("start_date")，若要改變在 postman JSON格式裡的 KEY 名稱
	@Column(name = "start_date")
	private LocalDate startDate = LocalDate.now(); //給定預設值，若不給預設值是null，則無法通過參數檢查

	@Column(name = "end_date")
	private LocalDate endDate = LocalDate.now(); //給定預設值，若不給預設值是null，則無法通過參數檢查

	@Column(name = "published")
	private boolean published;

	public Quiz() {
		super();
	}

	// 因為id 是 AutoIncrements 所以刪掉
	public Quiz(String name, String description, LocalDate startDate, LocalDate endDate, boolean published) {
		super();
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.published = published;
	}

	public Quiz(int id, String name, String description, LocalDate startDate, LocalDate endDate, boolean published) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.published = published;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

}
