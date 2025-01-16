package com.example.luklak.student.handler;

import com.example.luklak.student.IStudentService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class EventBusStudentHandler {
  @NonNull
  private final Vertx vertx;

  @NonNull
  private final IStudentService studentService;

  public void handler() {
    EventBus eventBus = vertx.eventBus();

    eventBus.consumer("student", message -> {
      String dataString = message.body().toString();
      JsonObject dataObject = new JsonObject(dataString);

      String path = dataObject.getString("path");
      String method = dataObject.getString("method");
      String body = dataObject.getString("body");
      JsonObject bodyObject = new JsonObject();
      if (body != null) {
        bodyObject = new JsonObject(body);
      }

      System.out.println("Path from Luklak api: " + path + " (" + method + ")");
      handleRoutes(message, path, method, bodyObject);
    });
  }

  @SneakyThrows
  public void handleRoutes(Message<Object> message, String path, String method, JsonObject body) {
//    if (path.equals("/api/v1/students/delete") && method.equals("POST")) {
//      int id = body.getInteger("id");
//      studentService.delete(id).whenComplete((item, err) -> {
//        if (err != null) {
//          message.fail(500, err.getMessage());
//        }
//        message.reply(Json.encode("success"));
//      });
//    }
    CompletableFuture.completedFuture("Xin chao")
      .whenComplete((item, err) -> {
        if (err != null) {
          message.fail(500, err.getMessage());
        }
        message.reply(Json.encode(item));
      });
  }
}
