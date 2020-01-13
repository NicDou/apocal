package com.jd.apocal.model.constant;

public class Constant {

  public static final String MESSAGE_DESTINATION_IMAGE_BUILDER = "image.builder";

  public static final String MESSAGE_DESTINATION_SERVICE_DEPLOYMENT = "service.deployment";

  public static final String MESSAGE_DESTINATION_IMAGE_DELETE = "image.delete";

  public static final String MESSAGE_DESTINATION_IMAGE_COPY = "image.copy";

  public static final String DEPLOY_NAME_PREFIX = "model-";

  public static final String SPEC_NAME_PREFIX = "spec-";

  public static final String GET_CURRENT_USER = "/auth/getCurrentUser.do";

  public static final String GET_DEPT_USERS = "/auth/getDirectUsersByOfficeId.do";

  public static final String GET_DICT_DETAIL = "/auth/getDictDetails.do";

  public static final String WORKFLOW_PROJECT_PATH_NAME = "/project/";

  public static final String WORKFLOW_EXP_GROUP_PATH_NAME = "/expgroup/";

  public static final String WORKFLOW_MODEL_PATH_NAME = "/archive/";

  public static final String DOCKER_EXP_PATH_NAME = "/knime/exp/";

  public static final String WORKFLOW_EXP_PATH_NAME = "/experiment/";

  public static final String MODEL_PROJECT_NAME = "ModelProject";

  public static final String MODEL_DATA_PATH_NAME = "/data/";

  public static final String MODEL_ZIP_FILE_NAME = "model.zip";

  public static final String MODEL_ZIP_FILE_PATH_NAME = MODEL_DATA_PATH_NAME + MODEL_ZIP_FILE_NAME;

  public static final String WORKFLOW_CONTROLLER_YAML_NAME = "workflow.yaml";

  public static final String WORKFLOW_K8S_JOB_YAML_NAME = "job.yaml";

  public static final String WORKFLOW_KNWF_FILE_NAME = "workflow.knwf";

  public static final String WORKFLOW_DATA_PATH_NAME = "/data/";

  public static final String MESSAGE_DESTINATION_PROJECT_BUILD = "project.builder";

  public static final String STATUS_SUCCESS = "S";

  public static final String STATUS_RUNNING = "P";

  public static final String STATUS_FAILURE = "F";

  public static final String STATUS_UNKNOWN = "N";

  public static final String HEADER_JD_TOKEN = "JD-Token";
}
