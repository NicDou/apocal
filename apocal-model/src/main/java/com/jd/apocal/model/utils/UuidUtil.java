package com.jd.apocal.model.utils;

import java.util.UUID;

/**
 * 类UuidUtil.java的实现描述：生成UUID序列
 */
public enum UuidUtil {
  INSTANCE;

  public static UuidUtil getInstance() {
    return INSTANCE;
  }

  private static String separater = "-";

  public String getUUid() {
    return UUID.randomUUID().toString().replace(separater, "");
  }

}
