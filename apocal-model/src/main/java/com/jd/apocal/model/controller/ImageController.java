package com.jd.apocal.model.controller;

import com.jd.apocal.model.entity.Image;
import com.jd.apocal.model.service.ImageService;
import com.jd.common.utils.RestServiceResult;
import com.jd.common.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping("/images")
public class ImageController {

  @Autowired
  private ImageService imgSvc;

  @Value("${export.dir}")
  private String root;

  @ApiOperation(value = "分页获取镜像列表", notes = "分页获取镜像列表")
  @RequestMapping(value = "/getImagesList.json")
  public RestServiceResult<List<Image>> getProjectsList(PageVO pageVO) {

    return new RestServiceResult<List<Image>>();
  }

  @ApiOperation(value = "删除镜像", notes = "删除镜像")
  @RequestMapping(value = "/del.json")
  public RestServiceResult<Boolean> del(@RequestParam String id) {

    imgSvc.removeById(id);

    return new RestServiceResult<Boolean>(true);
  }

  @ApiOperation(value = "获取构建日志", notes = "获取构建日志")
  @RequestMapping(value = "/getLogs.json")
  public RestServiceResult<String> getLogs(@RequestParam String id) {

    Image image = imgSvc.getById(id);

    return new RestServiceResult<String>(image.getLog());
  }

}
