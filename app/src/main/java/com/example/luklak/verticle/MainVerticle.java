package com.example.luklak.verticle;

import com.example.luklak.student.handler.EventBusStudentHandler;
import com.example.luklak.student.module.StudentModule;
import com.example.luklak.student.router.StudentRouter;
import com.google.inject.Guice;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    var guice = Guice.createInjector(
      new StudentModule(vertx)
    );

    StudentRouter studentRouter = guice.getInstance(StudentRouter.class);
    EventBusStudentHandler eventBusStudentHandler = guice.getInstance(EventBusStudentHandler.class);

    final Router router = Router.router(vertx);
    studentRouter.setRouter(router);
    eventBusStudentHandler.handler();

    buildHttpServer(vertx, startPromise, router);
  }

  private void buildHttpServer(Vertx vertx,
                               Promise<Void> promise,
                               Router router) {
    final int port = 8888;

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(port, http -> {
        if (http.succeeded()) {
          promise.complete();
          System.out.println("Server running on port "+port);
        } else {
          promise.fail(http.cause());
          System.out.println("Run server error");
        }
      });
  }
}
