package com.example.luklak.student.module;

import com.example.luklak.repository.PgStudentRepository;
import com.example.luklak.student.IStudentRepository;
import com.example.luklak.student.IStudentService;
import com.example.luklak.student.StudentService;
import com.example.luklak.student.handler.EventBusStudentHandler;
import com.example.luklak.student.handler.HTTPStudentHandler;
import com.example.luklak.student.router.StudentRouter;
import com.example.luklak.utils.DbUtils;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgPool;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StudentModule extends AbstractModule {
  private final Vertx vertx;

  @Provides
  @Singleton
  public IStudentRepository studentRepository(PgPool dbClient) {
    return new PgStudentRepository(dbClient);
  }

  @Provides
  @Singleton
  public PgPool pgPool() {
    return DbUtils.buildDbClient(vertx);
  }

  @Provides
  @Singleton
  public Vertx vertx() {
    return vertx;
  }

  @Provides
  @Singleton
  public IStudentService studentService(IStudentRepository studentRepository) {
    return new StudentService(studentRepository);
  }

  @Provides
  @Singleton
  public HTTPStudentHandler studentHandler(IStudentService studentService) {
    return new HTTPStudentHandler(studentService);
  }

  @Provides
  @Singleton
  public EventBusStudentHandler evenBusStudentHandler(IStudentService studentService) {
    return new EventBusStudentHandler(vertx, studentService);
  }


  @Provides
  @Singleton
  public StudentRouter studentRouter(HTTPStudentHandler HTTPStudentHandler) {
    return new StudentRouter(vertx, HTTPStudentHandler);
  }
}
