package com.jd.apocal.model.controller;

import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.entity.ModelSet;
import com.jd.apocal.model.service.ModelSetService;
import com.jd.apocal.model.utils.FileUtil;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/modelSet")
public class ModelSetController {

  @Autowired
  private ModelSetService modelSetSvc;

  @ApiOperation(value = "分页获取模型集列表", notes = "分页获取模型集列表")
  @RequestMapping(value = "/getModelSetsList.json")
  public RestServiceResult<List<ModelSet>> getModelSetsList(PageVO pageVO) {

    return new RestServiceResult<List<ModelSet>>();
  }

  @ApiOperation(value = "删除模型集", notes = "删除模型集")
  @RequestMapping(value = "/delete.json")
  public RestServiceResult<Boolean> delete(@RequestParam String id) throws IOException {

    ModelSet model = modelSetSvc.getById(id);
    FileUtils.deleteDirectory(new File(model.getLocation()));
    modelSetSvc.removeById(id);
    return new RestServiceResult<Boolean>(true);
  }

  @ApiOperation(value = "下载模型", notes = "下载模型")
  @RequestMapping(value = "/downloadModelZip.json")
  public void downloadModelZip(HttpServletResponse response, @RequestParam String id) {

    ModelSet model = modelSetSvc.getById(id);

    FileUtil
        .downloadFile(model.getLocation() + File.separator + Constant.MODEL_ZIP_FILE_NAME,
            response);

  }
}
