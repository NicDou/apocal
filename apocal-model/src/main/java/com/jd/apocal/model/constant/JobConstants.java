package com.jd.apocal.model.constant;

/**
 * 任务静态属性
 *
 * @author Leegern
 * @date 2018年5月7日
 */
public final class JobConstants {

  /**
   * 调度名称
   **/
  public static final String SCHEDULER_NAME = "scheduler";
  /**
   * 任务状态：开启
   **/
  public static final int STATUS_RUN = 1;
  /**
   * 任务状态：停用
   **/
  public static final int STATUS_STOP = 2;

  /**
   * 数字2
   **/
  public static final int NUM_TWO = 2;

  /**
   * 默认执行方法名
   **/
  public static final String DEFAULT_JOB_METHOD = "doJob";

  /**
   * Job状态
   */
  public static class JobStatus {

    /**
     * 未处理
     */
    public static final String JOB_UNPROCESSED = "0";

    /**
     * 执行成功
     */
    public static final String JOB_SUCCESSED = "1";

    /**
     * 执行失败
     */
    public static final String JOB_FAILED = "-1";

    /**
     * 过期
     */
    public static final String JOB_EXPIRED = "-2";
  }

  /**
   * 任务组
   */
  public static final class JobGruop {

    /**
     * 默认的任务组
     */
    public static final String JOB_DEFAULT_GROUP_ID = "job_default_group_id";

    /**
     * 默认的任务组
     */
    public static final String JOB_DEFAULT_GROUP_NAME = "job_default_group_name";
  }

  /**
   * 任务名称
   */
  public static final class JobName {

    /**
     * 默认的任务名
     */
    public static final String JOB_DEFAULT_ID = "job_default_id";

    /**
     * 默认的任务名
     */
    public static final String JOB_DEFAULT_NAME = "job_default_name";
  }

}