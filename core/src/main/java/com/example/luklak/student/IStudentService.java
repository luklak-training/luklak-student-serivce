package com.example.luklak.student;

import com.example.luklak.common.response.Listing;

import java.util.concurrent.CompletionStage;

public interface IStudentService {
  CompletionStage<StudentResponse> create(Student student);

  CompletionStage<Listing> listing(int page, int pageSize);

  CompletionStage<Object> delete(int id);
}
