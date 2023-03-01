package com.example.luklak.common.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Listing<T> {
  private int total;
  private List<T> hits;
}
