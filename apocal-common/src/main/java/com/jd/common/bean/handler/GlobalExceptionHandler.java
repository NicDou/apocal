package com.jd.common.bean.handler;

import com.jd.common.utils.RestServiceResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局的的异常拦截器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 全局异常.
   *
   * @param e the e
   * @return RestServiceResult
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestServiceResult<Exception> exceptionHandler(Exception e) {
    log.info("保存全局异常信息 ex={}", e.getMessage(), e);
    return new RestServiceResult<>(e);
  }

  //json格式提交参数
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public RestServiceResult<String> methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException ex) {
    StringBuilder errorMsg = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(e -> {
      errorMsg.append(e.getDefaultMessage()).append(",");
    });
    errorMsg.delete(errorMsg.length() - 1, errorMsg.length());
    return new RestServiceResult<>(RestServiceResult.BAD_REQUEST, errorMsg.toString());
  }

  //表单格式提交参数
  @ExceptionHandler(value = BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public RestServiceResult<String> bindExceptionHandler(BindException ex) {
    StringBuilder errorMsg = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(e -> {
      errorMsg.append(e.getDefaultMessage()).append(",");
    });
    errorMsg.delete(errorMsg.length() - 1, errorMsg.length());
    return new RestServiceResult<>(RestServiceResult.BAD_REQUEST, errorMsg.toString());
  }


}
