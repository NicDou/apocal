package com.jd.apocal.model.entity;

import lombok.Data;

@Data
public class DictDetailEntity {

  /**
   * 字典主表ID
   **/
  private long dictId;
  /**
   * KEY值
   **/
  private String dtlKey;
  /**
   * 字典值
   **/
  private String dtlValue;
  /**
   * 字典扩展
   **/
  private String dtlEx;
  /**
   * 排序
   **/
  private int orderBy;
  /**
   * 是否使用，1否，2是
   **/
  private int enabled;

}