package com.example.luklak.common;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseEntity {
  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final String APPLICATION_JSON = "application/json";

  int code;
  String message;
  Object data;

  public ResponseEntity(){
    this.code = 0;
    this.message = "OK";
  }

  public ResponseEntity(int code, String message){
    this.code = code;
    this.message = message;
  }

  public ResponseEntity(int code, String message, Object data){
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static ResponseEntity okEntity(String message){
    return new ResponseEntity(0, message, null);
  }

  public static ResponseEntity okEntity(int code, String message, Object data){
    return new ResponseEntity(code, message, data);
  }

  public static ResponseEntity okEntity(String message, Object data){
    if(message == null){
      return ResponseEntity.okEntity(data);
    }

    return ResponseEntity.okEntity(200, message, data);
  }

  public static ResponseEntity okEntity(Object data){
    return new ResponseEntity(0, "OK", data);
  }

  public static void buildSuccessResponse(RoutingContext rc,
                                          Object response) {
    rc.response()
      .setStatusCode(200)
      .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
      .end(Json.encodePrettily(response));
  }

  public static void buildFailResponse(RoutingContext rc,
                                       Object response) {
    rc.response()
      .setStatusCode(500)
      .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
      .end(Json.encodePrettily(response));
  }

  public static void buildNoContentResponse(RoutingContext rc) {
    rc.response()
      .setStatusCode(204)
      .end();
  }
}
