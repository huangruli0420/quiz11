package com.example.quiz11.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quiz11.entity.Feedback;
import com.example.quiz11.entity.FeedbackId;

@Repository
public interface FeedbackDao extends JpaRepository<Feedback, FeedbackId > {

}
