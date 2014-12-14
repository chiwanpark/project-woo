package com.chiwanpark.woo.model;

public class Tuple4<T1, T2, T3, T4> {
  private T1 v1;
  private T2 v2;
  private T3 v3;
  private T4 v4;

  public Tuple4(T1 v1, T2 v2, T3 v3, T4 v4) {
    this.v1 = v1;
    this.v2 = v2;
    this.v3 = v3;
    this.v4 = v4;
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

  public T4 getV4() {
    return v4;
  }

  public String toString(String pattern) {
    return String.format(pattern, v1, v2, v3, v4);
  }
}
