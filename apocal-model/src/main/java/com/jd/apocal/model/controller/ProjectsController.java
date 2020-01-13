package com.jd.apocal.model.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Condition;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ImmutableMap;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.core.doc.ProjectDocument;
import com.jd.apocal.model.entity.ExpGroup;
import com.jd.apocal.model.entity.Project;
import com.jd.apocal.model.message.ProjectMessage;
import com.jd.apocal.model.service.ExpGroupService;
import com.jd.apocal.model.service.ExperimentService;
import com.jd.apocal.model.service.ImageService;
import com.jd.apocal.model.service.ProjectService;
import com.jd.apocal.model.service.TaskService;
import com.jd.common.exception.ServiceException;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.PageVO;
import com.jd.common.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Api
@RestController
@RequestMapping("/projects")
public class ProjectsController {

  @Autowired
  private ProjectService projectSvc;

  @Autowired
  private ExpGroupService expGroupSvc;

  @Autowired
  private ExperimentService expSvc;

  @Autowired
  private TaskService taskService;

  @Autowired
  private ImageService imageService;

  @Autowired
  private JmsMessagingTemplate sender;

  @Value("${export.dir}")
  private String root;
  @Value("${project.base.image}")
  private String baseImage;

  @Value("${workflow.image.repository}")
  private String imageRepository;

  @Value("${scripts.home}")
  private String scriptsHome;

  @SuppressWarnings("unchecked")
  @ApiOperation(value = "新增项目", notes = "新增项目")
  @RequestMapping(value = "/add.json")
  public RestServiceResult<Boolean> add(UserVO userVO, @RequestParam CommonsMultipartFile file) {

    // 解析入库
    ProjectDocument doc = new ProjectDocument(file, root + Constant.WORKFLOW_PROJECT_PATH_NAME);

    Map<String, Object> yaml = doc.getWorkflowYaml();

    Map<String, Object> metas = (Map<String, Object>) yaml.get("metas");
    Map<String, Object> params = (Map<String, Object>) yaml.get("params");
    Map<String, Object> resource = (Map<String, Object>) yaml.get("resource");

    // 构建project
    Project entity = Project.builder().id(doc.getDocId()).cpu(resource.get("cpu").toString())
        .storage(resource.get("storage").toString()).creator(userVO.getUsername())
        .kind(metas.get("kind").toString()).memory(resource.get("memory").toString())
        .name(metas.get("name").toString()).params(JSON.toJSONString(params, true))
        .version(metas.get("version").toString()).deptId(userVO.getOfficeId())
        .deptName(userVO.getOfficeName())
        .creatorId(userVO.getId())
        .location(
            doc.getLocation() + doc.getFileName().substring(0, doc.getFileName().lastIndexOf(".")))
        .build();
    // 保存到数据库
    projectSvc.save(entity);

    return new RestServiceResult<Boolean>(true);
  }

  @ApiOperation(value = "获取项目详情", notes = "获取项目详情")
  @RequestMapping(value = "/detail.json")
  public RestServiceResult<Project> getProjectsDetail(@RequestParam String id) {

    Project project = projectSvc.getById(id);

    return new RestServiceResult<Project>(project);
  }

  @ApiOperation(value = "删除项目", notes = "删除项目")
  @RequestMapping(value = "/del.json")
  public RestServiceResult<Boolean> deleteProject(@RequestParam String id) throws IOException {

    boolean canDelete = projectSvc.canDelete(id);

    if (canDelete) {

      // 删除项目目录
      FileUtils.deleteDirectory(new File(root + Constant.WORKFLOW_PROJECT_PATH_NAME + id));

      List<ExpGroup> expgroups = (List<ExpGroup>) expGroupSvc
          .listByMap(ImmutableMap.<String, Object>of("projectId", id));

      expgroups.stream().forEach(group -> {

        try {
          // 删除实验组目录(包含任务和实验目录)
          FileUtils.deleteDirectory(
              new File(root + Constant.WORKFLOW_EXP_GROUP_PATH_NAME + group.getId()));

        } catch (Exception e) {

          e.printStackTrace();
        }

      });

      // 删除项目记录
      projectSvc.removeById(id);

      Map<String, Object> params = ImmutableMap.<String, Object>of("projectId", id);

      // 删除实验记录

      expSvc.removeByMap(params);
      // 删除任务记录
      taskService.removeByMap(params);

      // 删除镜像记录
      imageService.removeByMap(params);

      // 删除实验组记录
      expGroupSvc.removeByMap(params);

      return new RestServiceResult<Boolean>(true);

    } else {

      throw new ServiceException("无法删除项目,该项目下有未完成的实验或已启动的任务");

    }

  }

  @ApiOperation(value = "更新项目", notes = "更新项目")
  @RequestMapping(value = "/update.json")
  public RestServiceResult<Boolean> updateProject(Project entity) {

    projectSvc.updateById(entity);

    return new RestServiceResult<Boolean>(true);
  }

  @ApiOperation(value = "分页获取项目列表", notes = "分页获取项目列表")
  @RequestMapping(value = "/getProjectsList.json")
  public RestServiceResult<Page<Project>> getProjectsList(PageVO pageVO, Project project) {

    Page<Project> result = (Page<Project>) projectSvc
        .page(new Page<Project>(pageVO.getCurrentPage(), pageVO.getPageSize()),
            Condition.create(project));

    return new RestServiceResult<Page<Project>>(result);
  }

  @ApiOperation(value = "获取构建成功项目列表", notes = "获取构建成功项目列表")
  @RequestMapping(value = "/getSuccessfullyBuiltProjectsList.json")
  public RestServiceResult<List<Project>> getSuccessfullyBuiltProjectsList() {
    // 实验组拉状态为成功的项目：项目状态与最新构建状态一致
    List<Project> projects = projectSvc.getSuccessfullyBuiltProjectsList();

    return new RestServiceResult<List<Project>>(projects);
  }

  @ApiOperation(value = "构建项目镜像", notes = "构建项目镜像")
  @RequestMapping(value = "/build.json")
  public RestServiceResult<Boolean> build(@RequestParam String id) {

    Project project = projectSvc.getById(id);

    ProjectMessage message = ProjectMessage.builder().id(id).baseImage(baseImage)
        .projectLocation(project.getLocation()).scriptsHome(scriptsHome)
        .imageRepository(imageRepository)
        .build();

    projectSvc.updateById(Project.builder().id(id).status("P").build());

    sender.convertAndSend(Constant.MESSAGE_DESTINATION_PROJECT_BUILD, message);

    return new RestServiceResult<Boolean>(true);
  }

  @ApiOperation(value = "获取项目控制文件内容", notes = "获取项目控制文件内容")
  @RequestMapping(value = "/getProjectsYaml.json")
  public RestServiceResult<List<String>> getProjectsYaml(@RequestParam String id)
      throws IOException {

    String projectYamlFile = projectSvc.getById(id).getLocation() + File.separator
        + Constant.WORKFLOW_CONTROLLER_YAML_NAME;
    List<String> lines = null;
    if (new File(projectYamlFile).exists()) {

      lines = FileUtils.readLines(new File(projectYamlFile), "UTF-8");

    } else {

      throw new ServiceException("未找到控制文件");
    }

    return new RestServiceResult<List<String>>(lines);
  }


}
