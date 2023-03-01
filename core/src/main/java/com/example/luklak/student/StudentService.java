package com.example.luklak.student;

import com.example.luklak.common.response.Listing;
import com.example.luklak.utils.SQLUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class StudentService implements IStudentService {
  @NonNull
  private final IStudentRepository studentRepository;


  public CompletionStage<StudentResponse> create(Student student) {
    return studentRepository.insert(student)
      .whenComplete((item, err) -> {
        if (err != null) {
          log.info("create student " + err.getMessage());
        } else {
          log.error("Create student " + item);
        }
      }).thenApply(StudentMapper::toResponse);
  }

  public CompletionStage<Listing> listing(int page, int pageSize) {
    final int offset = SQLUtils.getOffset(page, pageSize);

    return studentRepository.count()
      .thenCompose(total -> studentRepository.listing(pageSize, offset).thenApply(result -> {
        final List<StudentResponse> hits = result.stream()
          .map(StudentMapper::toResponse)
          .collect(Collectors.toList());

        return new Listing().setTotal(total).setHits(hits);
      }));
  }

  @Override
  public CompletionStage<Object> delete(int id) {
    return studentRepository.delete(id);
  }
}
