package com.jd.apocal.model.controller;

import com.baomidou.mybatisplus.core.conditions.Condition;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.entity.DataSet;
import com.jd.apocal.model.service.DataSetService;
import com.jd.apocal.model.utils.UuidUtil;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api
@RestController
@RequestMapping("/dataSet")
public class DataSetController {

  @Value("${export.dir}")
  private String exportDir;

  @Autowired
  private DataSetService dataSetSvc;

  @ApiOperation(value = "添加数据集", notes = "添加数据集")
  @RequestMapping(value = "/add.json")
  public RestServiceResult<Boolean> add(UserVO user, @RequestParam MultipartFile file)
      throws IOException {

    String id = UuidUtil.getInstance().getUUid();

    String originalFilename = file.getOriginalFilename();

    String location = exportDir + Constant.WORKFLOW_DATA_PATH_NAME + id;

    String destination = location + File.separator + originalFilename;

    FileUtils.copyInputStreamToFile(file.getInputStream(), new File(destination));

    DataSet set = DataSet.builder().id(id).creator("").creatorId(1L)
        .name(originalFilename).deptId(1L).deptName("")
        .location(destination)
        .build();

    dataSetSvc.save(set);

    return RestServiceResult.rest(true);

  }

  @ApiOperation(value = "分页获取数据集列表", notes = "分页获取数据集列表")
  @RequestMapping(value = "/getDataSetsList.json")
  public Page<DataSet> getDataSetsList(Page page, DataSet dataSet) {

    Page<DataSet> result = (Page<DataSet>) dataSetSvc.page(page, Condition.create(dataSet));

    return result;

  }

  @ApiOperation(value = "删除数据集", notes = "删除数据集")
  @RequestMapping(value = "/delete.json")
  public RestServiceResult<Boolean> delete(@RequestParam String id) throws IOException {

    FileUtils.deleteDirectory(
        new File(
            StringUtils.substringBeforeLast(dataSetSvc.getById(id).getLocation(), File.separator)));

    dataSetSvc.removeById(id);

    return new RestServiceResult<Boolean>(true);
  }
}
