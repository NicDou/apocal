package com.jd.apocal.model.core.doc;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.jd.apocal.model.constant.Constant;
import com.jd.apocal.model.utils.CompressUtil;
import com.jd.apocal.model.utils.UuidUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Slf4j
@Data
public class ProjectDocument {

  private String docId;

  private String location;

  private Map<String, Object> workflowYaml;

  private String fileName;

  private String file;

  public ProjectDocument(CommonsMultipartFile file, String dir) {

    this.docId = UuidUtil.getInstance().getUUid();

    this.fileName = file.getOriginalFilename();

    this.location = dir + docId + File.separator;

    parse(file);
  }

  @SuppressWarnings("unchecked")
  private void parse(CommonsMultipartFile file) {

    try {

      // 创建目录
      boolean mkdirs = new File(location).mkdirs();

      if (mkdirs) {

        String zipFile = location + fileName;

        // 保存ZIP文件
        file.transferTo(new File(zipFile));
        // 解压ZIP文件
        CompressUtil.unzip(zipFile, location, null);

        // 读取workflow.yaml
        workflowYaml = (Map<String, Object>) new YamlReader(
            new InputStreamReader(new FileInputStream(location
                + fileName.substring(0, fileName.lastIndexOf(".")) + File.separator
                + Constant.WORKFLOW_CONTROLLER_YAML_NAME), "UTF-8")).read();

      } else {

        log.error("创建文件夹失败:{}", location);

        //	throw new ServiceException("创建文件夹失败：" + location);

      }
    } catch (Exception e) {

      log.error("解析文件失败:{}", location, e);

    }

  }
}
