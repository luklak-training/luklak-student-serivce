package com.example.luklak.student;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IStudentRepository {
  public CompletionStage<Student> insert(Student student);
  public CompletionStage<Integer> count();
  public CompletionStage<List<Student>> listing(int pageSize, int offset);

  public CompletionStage<Object> delete(int id);
}
