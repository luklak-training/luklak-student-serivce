package com.example.luklak.student.handler;

import com.example.luklak.common.ResponseEntity;
import com.example.luklak.common.constant.Constant;
import com.example.luklak.common.response.Listing;
import com.example.luklak.student.IStudentService;
import com.example.luklak.student.Student;
import com.example.luklak.student.StudentResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.concurrent.CompletionStage;

@AllArgsConstructor
public class HTTPStudentHandler {
  @NonNull
  private final IStudentService studentService;

  public CompletionStage<StudentResponse> create(RoutingContext rc) {
    final Student student = rc.body().asPojo(Student.class);

    return studentService.create(student)
      .whenComplete((item, err) -> {
        if (err != null) {
          ResponseEntity.buildFailResponse(rc, err);
        } else {
          ResponseEntity.buildSuccessResponse(rc, item);
        }
      });
  }

  public CompletionStage<Listing> listing(RoutingContext rc) {
    final int page = Integer.parseInt(rc.queryParams().get(Constant.PAGE_PARAMETER));
    final int pageSize = Integer.parseInt(rc.queryParams().get(Constant.PAGE_SIZE_PARAMETER));

    return studentService.listing(page, pageSize)
      .whenComplete((item, err) -> {
        if (err != null) {
          ResponseEntity.buildFailResponse(rc, err);
        } else {
          ResponseEntity.buildSuccessResponse(rc, item);
        }
      });
  }
}
