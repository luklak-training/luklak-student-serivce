package com.example.luklak.student;

public class StudentMapper {
  public static StudentResponse toResponse(Student student){
    StudentResponse studentResponse = new StudentResponse()
      .setId(student.getId())
      .setName(student.getName())
      .setAge(student.getAge());

    return studentResponse;
  }
}
