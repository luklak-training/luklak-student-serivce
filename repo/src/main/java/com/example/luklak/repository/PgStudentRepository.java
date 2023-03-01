package com.example.luklak.repository;

import com.example.luklak.student.IStudentRepository;
import com.example.luklak.student.Student;
import io.vertx.core.Future;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.RowIterator;
import io.vertx.sqlclient.templates.RowMapper;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletionStage;

@Slf4j
@AllArgsConstructor
public class PgStudentRepository implements IStudentRepository {
  @NonNull
  private final PgPool dbClient;
  private static final String SQL_INSERT = """
    INSERT INTO
    students (name, age)
    VALUES (#{name}, #{age})
    RETURNING id
    """;

  private static final String SQL_COUNT = """
    SELECT COUNT(*) AS total
     FROM students
     """;

  private static final String SQL_SELECT_ALL = """
    SELECT * FROM students
    LIMIT #{limit}
    OFFSET #{offset}
    """;

  private static final String SQL_DELETE = """
    DELETE FROM students
    WHERE id = #{id}
    """;


  public CompletionStage<Student> insert(Student student) {
    return SqlTemplate
      .forUpdate(dbClient, SQL_INSERT)
      .mapFrom(Student.class)
      .mapTo(Student.class)
      .execute(student)
      .map(rowSet -> {
        final RowIterator<Student> iterator = rowSet.iterator();

        if (iterator.hasNext()) {
          student.setId(iterator.next().getId());
          return student;
        } else {
          throw new IllegalStateException("Can not create new student");
        }
      })
      .onSuccess(success -> log.info("Create student success: " + SQL_INSERT))
      .onFailure(throwable -> log.error("Create student fail " + throwable.getMessage()))
      .toCompletionStage();
  }

  public CompletionStage<List<Student>> listing(int limit,
                                                int offset) {
    return SqlTemplate
      .forQuery(dbClient, SQL_SELECT_ALL)
      .mapTo(Student.class)
      .execute(Map.of("limit", limit, "offset", offset))
      .map(rowSet -> {
        final List<Student> students = new ArrayList<>();
        rowSet.forEach(students::add);

        return students;
      })
      .onSuccess(success -> log.info("listing student " + SQL_SELECT_ALL))
      .onFailure(throwable -> log.error("listing student " + throwable.getMessage()))
      .toCompletionStage();
  }


  public CompletionStage<Integer> count() {
    final RowMapper<Integer> ROW_MAPPER = row -> row.getInteger("total");

    return SqlTemplate
      .forQuery(dbClient, SQL_COUNT)
      .mapTo(ROW_MAPPER)
      .execute(Collections.emptyMap())
      .map(rowSet -> rowSet.iterator().next())
      .onSuccess(success -> log.info("Count student " + SQL_COUNT))
      .onFailure(throwable -> log.error("Count student  " + throwable.getMessage()))
      .toCompletionStage();
  }

  public CompletionStage<Object> delete(int id) {
    return SqlTemplate
      .forUpdate(dbClient, SQL_DELETE)
      .execute(Collections.singletonMap("id", id))
      .toCompletionStage()
      .thenCompose(rowSet -> {
        if (rowSet.rowCount() > 0) {
          log.info("Delete student", SQL_DELETE);
          return Future.succeededFuture().toCompletionStage();
        } else {
          log.error("Delete book error");
          throw new NoSuchElementException("Student id = " + id);
        }
      });
  }
}
