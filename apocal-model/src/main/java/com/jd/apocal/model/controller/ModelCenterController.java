package com.jd.apocal.model.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.core.doc.ModelDocument;
import com.jd.apocal.model.entity.Column;
import com.jd.apocal.model.entity.IO;
import com.jd.apocal.model.entity.Model;
import com.jd.apocal.model.entity.ModelUser;
import com.jd.apocal.model.message.ModelMessage;
import com.jd.apocal.model.service.ModelCenterService;
import com.jd.apocal.model.utils.CompressUtil;
import com.jd.apocal.model.utils.FileUtil;
import com.jd.apocal.model.utils.UuidUtil;
import com.jd.common.exception.ServiceException;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.PageVO;
import com.jd.common.vo.UserVO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@RestController
@RequestMapping("/modelCenter")
public class ModelCenterController {

  @Autowired
  private ModelCenterService mcs;

  @Value("${export.dir}")
  private String root;

  @Value("${base.image.repository}")
  private String baseImageRepository;

  @Value("${model.image.repository}")
  private String modelImageRepository;

  @Value("${harbor.api.url}")
  private String harbor;

  @Value("${scripts.home}")
  private String scriptsHome;

  @Autowired
  private JmsMessagingTemplate sender;


  @RequestMapping(value = "/getListByConditionForPage.json")
  public RestServiceResult<Page<Model>> getListByCondition(UserVO user, PageVO pageVO) {

    long userId = user.getId();
    long officeId = user.getOfficeId();
    int userType = user.getUserType();

    ImmutableMap<String, Object> of = ImmutableMap.<String, Object>of("user_type", userType,
        "dept_id", officeId,
        "user_id", userId);

    Page<Model> result = (Page<Model>) mcs
        .page(new Page<Model>(), new QueryWrapper<Model>().allEq(of));

    for (Model model : result.getRecords()) {
      model.setCurrentUserType(userType);
      model.setCurrentUserId(userId);
    }

    return new RestServiceResult<Page<Model>>(result);

  }

  @RequestMapping(value = "/del.json")
  public RestServiceResult<String> del(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    if (mcs.existApp(id)) {

      throw new ServiceException("该模型存在服务,不可删除");
    }

    // 删镜像

    Model model = mcs.getById(id);

    ModelMessage message = ModelMessage.builder().modelId(id).harbor(harbor)
        .modelImage(model.getModelImage())
        .scriptsHome(scriptsHome).build();

    mcs.removeById(id);

    sender.convertAndSend(Constant.MESSAGE_DESTINATION_IMAGE_DELETE, message);

    return new RestServiceResult<String>();
  }

  @RequestMapping(value = "/detail.json")
  public RestServiceResult<Model> detail(
      @RequestParam(value = "id", defaultValue = "", required = true) String id)
      throws IOException {

    Model model = mcs.getById(id);

    return new RestServiceResult<Model>(model);
  }

  @RequestMapping(value = "/performance.json")
  public RestServiceResult<Map<String, Object>> performance(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    Map<String, Object> metrics = mcs.getMetrics(id);

    return new RestServiceResult<Map<String, Object>>(metrics);

  }

  @RequestMapping(value = "/update.json")
  public RestServiceResult<String> update(Model model) {

    mcs.updateById(model);

    return new RestServiceResult<String>();
  }

  @RequestMapping(value = "/getModelDoc.json")
  public void getModelDoc(@RequestParam(value = "id", defaultValue = "", required = true) String id,
      HttpServletResponse response) {

    Model model = mcs.getById(id);

    FileUtil.downloadFile(model.getLocation(), response);

  }

  @RequestMapping(value = "/getModelExampleDoc.json")
  public void getModelExample(HttpServletResponse response) {

    FileUtil.downloadFile(this.getClass().getResource("/ModelProject.zip").getPath(), response);

  }

  // TODO 自身或授权的images
  @Deprecated
  @RequestMapping(value = "/getImages.json")
  public RestServiceResult<List<Model>> getImages(UserVO user) {

    long userId = user.getId();
    long officeId = user.getOfficeId();
    int userType = user.getUserType();
    Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);

    if (userType == 3) {
      // admin

    } else if (userType == 4) {
      // 部门
      param.put("deptId", officeId);

    } else if (userType == 1) {

      param.put("userId", userId);
    }

    param.put("status", "S");

    List<Model> iamges = (List<Model>) mcs.listByMap(param);

    return new RestServiceResult<List<Model>>(iamges);

  }

  @RequestMapping(value = "/getIORelationGraphics.json")
  public RestServiceResult<Map<String, IO>> getIORelationGraphics(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    Map<String, IO> map = Maps.newHashMapWithExpectedSize(2);

    Model model = mcs.getById(id);

    String output = model.getOutput();

    String input = model.getInput();

    JSONObject inData = JSON.parseObject(input);

    JSONObject datas = (JSONObject) inData.get("datas");

    JSONObject in = (JSONObject) datas.get("in");

    JSONObject spec = (JSONObject) in.get("spec");

    JSONArray base = (JSONArray) spec.get("base");

    List<Column> columns = JSONArray.parseArray(base.toJSONString(), Column.class);

    IO i = IO.builder().tableName("TABLE_IN").columns(columns).build();

    JSONObject outData = JSON.parseObject(output);

    JSONObject outDatas = (JSONObject) outData.get("datas");

    JSONObject out = (JSONObject) outDatas.get("out");

    JSONArray outSpec = (JSONArray) out.get("spec");

    List<Column> outColumns = JSONArray.parseArray(outSpec.toJSONString(), Column.class);

    IO o = IO.builder().tableName("TABLE_OUT").columns(outColumns).build();

    map.put("input", i);

    map.put("output", o);

    return new RestServiceResult<Map<String, IO>>(map);

  }

  @Deprecated
  @RequestMapping(value = "/rebuild.json")
  public RestServiceResult<String> rebuild(
      @RequestParam(value = "id", defaultValue = "", required = true) String id) {

    Model model = mcs.getById(id);

    String location = model.getLocation();

    String modelImage = model.getModelImage();

    String[] split = modelImage.split("/");

    String address = split[0];

    String imageNameWithVersion = split[1];

    String[] split2 = imageNameWithVersion.split(":");

    String imageName = split2[0];

    String version = split2[1];

    double newVersion = Double.valueOf(version) + 0.1;

    String newModelImageName = imageName + ":" + newVersion;

    String modelDir = location.substring(0, location.indexOf(".zip")) + File.separator + "modules";

    ModelMessage message = ModelMessage.builder().modelId(id).baseImage(model.getBaseImage())
        .modelDir(modelDir)
        .modelImage(newModelImageName).renamedModelImage(address + "/" + newModelImageName)
        .scriptsHome(scriptsHome).programLanguage(model.getProgramLanguage())
        .runtimeImage(baseImageRepository + "/seldon-core-s2i-java-runtime:0.1").build();

    // 发送消息创建docker镜像
    sender.convertAndSend(Constant.MESSAGE_DESTINATION_IMAGE_BUILDER, message);

    return null;

  }

  @RequestMapping(value = "/add.json")
  public RestServiceResult<String> add(UserVO user, @RequestParam CommonsMultipartFile file)
      throws Exception {

    long userId = user.getId();
    long officeId = user.getOfficeId();
    String userName = user.getUsername();
    String officeName = user.getOfficeName();

    String uUid = UuidUtil.getInstance().getUUid();

    String originalFilename = file.getOriginalFilename();

    String modleDir = root + File.separator + uUid + File.separator;

    String modelZipFile = modleDir + originalFilename;

    String modelUnzipFileDir =
        modleDir + originalFilename.substring(0, originalFilename.indexOf("."));

    new File(modleDir).mkdirs();

    // 存储到web服务器上
    // TODO 存到单独的文件服务器上
    file.transferTo(new File(modelZipFile));
    // 解压
    CompressUtil.unzip(modelZipFile, modleDir, null);

    // 解析model文件
    ModelDocument modelDocument = new ModelDocument(new File(modelUnzipFileDir), uUid, userId,
        officeId, userName,
        officeName);

    // 将模型数据入库
    Model model = mcs.addModel(modelDocument);

    String programLanguage = model.getProgramLanguage();

    String modelImage = uUid + ":0.1";

    String baseImage = "";

    if ("java".equalsIgnoreCase(programLanguage)) {

      baseImage = "seldon-core-s2i-java-build:0.1";

    } else if ("python".equalsIgnoreCase(programLanguage)) {

      baseImage = "seldon-core-s2i-python37:0.7";

    } else {

      throw new ServiceException("元数据 programLanguage:【" + programLanguage + "】非法");
    }

    Model build = Model.builder().id(model.getId()).baseImage(baseImageRepository + "/" + baseImage)
        .modelImage(modelImageRepository + "/" + modelImage).build();

    mcs.updateById(build);

    ModelMessage message = ModelMessage.builder().modelId(model.getId())
        .baseImage(baseImageRepository + "/" + baseImage).modelDir(modelDocument.getModulesDir())
        .modelImage(modelImage).renamedModelImage(modelImageRepository + "/" + modelImage)
        .scriptsHome(scriptsHome).programLanguage(programLanguage)
        .runtimeImage(baseImageRepository + "/seldon-core-s2i-java-runtime:0.1").build();

    // 发送消息创建docker镜像
    sender.convertAndSend(Constant.MESSAGE_DESTINATION_IMAGE_BUILDER, message);

    return new RestServiceResult<String>();
  }

  @RequestMapping(value = "/auth.json")
  public RestServiceResult<String> auth(@RequestParam String modelId, @RequestParam String auth) {

    List<ModelUser> auths = JSONArray.parseArray(auth, ModelUser.class);

    mcs.deleteAuth(modelId);

    if (!CollectionUtils.isEmpty(auths)) {

      mcs.addAuth(modelId, auths);

    }

    return new RestServiceResult<String>();

  }


}
