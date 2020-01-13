package com.jd.common.utils;

public class RestServiceResult<T> {


  public static final int NO_LOGIN = -1;

  public static final int SUCCESS = 200;

  public static final int FAIL = 500;

  public static final int BAD_REQUEST = 400;

  public static final int NO_PERMISSION = 403;

  private String msg = "success";

  private int code = SUCCESS;

  private T data;

  public RestServiceResult() {
    super();
  }

  public RestServiceResult(T data) {
    super();
    this.data = data;
  }

  public RestServiceResult(T data, String msg) {
    super();
    this.data = data;
    this.msg = msg;
  }

  public RestServiceResult(int code, String msg) {
    super();
    this.code = code;
    this.msg = msg;
  }

  public RestServiceResult(Throwable e) {
    super();
    this.msg = e.getMessage();
    this.code = FAIL;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public static RestServiceResult<Boolean> rest(boolean result) {
    RestServiceResult<Boolean> r = new RestServiceResult<Boolean>();
    if (!result) {
      r.setCode(RestServiceResult.FAIL);
      r.setData(false);
    }
    return r;
  }


}
