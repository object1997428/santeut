package com.santeut.party.common.util;

public class CollectionIdGenerator {

  private static Long id = 0L;

  public static Long generateId() {
    id += 1L;
    return id;
  }

}
