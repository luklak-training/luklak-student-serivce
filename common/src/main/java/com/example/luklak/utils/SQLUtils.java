package com.example.luklak.utils;

public class SQLUtils {
  public static int getOffset(int page,
                              int limit) {
    if ((page - 1) * limit >= 0) {
      return (page - 1) * limit;
    } else {
      throw new NumberFormatException("Error by get offset");
    }
  }
}
