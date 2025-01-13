package com.example.luklak.student.router;

import com.example.luklak.student.handler.HTTPStudentHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class StudentRouter {
  @NonNull
  private final Vertx vertx;
  @NonNull
  private final HTTPStudentHandler HTTPStudentHandler;


  public void setRouter(Router router) {
    router.mountSubRouter("/api/v1", buildStudentRouter());
  }

  private Router buildStudentRouter() {
    final Router bookRouter = Router.router(vertx);

    bookRouter.route("/students*").handler(BodyHandler.create());
    bookRouter.get("/students/hello").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(HTTPStudentHandler::hello);
    bookRouter.post("/students").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(HTTPStudentHandler::create);
    bookRouter.get("/students").handler(LoggerHandler.create(LoggerFormat.DEFAULT)).handler(HTTPStudentHandler::listing);
    return bookRouter;
  }
}
