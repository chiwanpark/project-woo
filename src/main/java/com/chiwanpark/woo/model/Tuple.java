package com.chiwanpark.woo.model;

public class Tuple<T1, T2, T3> {
  private T1 v1;
  private T2 v2;
  private T3 v3;

  public Tuple(T1 v1, T2 v2, T3 v3) {
    this.v1 = v1;
    this.v2 = v2;
    this.v3 = v3;
  }

  public T1 getV1() {
    return v1;
  }

  public T2 getV2() {
    return v2;
  }

  public T3 getV3() {
    return v3;
  }

  public String toString(String u1, String u2, String u3) {
    return String.valueOf(v1) + u1 + " " + String.valueOf(v2) + u2 + " " + String.valueOf(v3) + u3;
  }
}
