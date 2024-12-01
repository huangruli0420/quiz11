package com.example.quiz11.vo;

public class BasicRes {

	private int code;

	private String Message;

	public BasicRes() {
		super();
	}

	public BasicRes(int code, String message) {
		super();
		this.code = code;
		Message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return Message;
	}

}
