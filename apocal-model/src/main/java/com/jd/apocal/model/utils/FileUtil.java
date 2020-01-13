package com.jd.apocal.model.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class FileUtil {

  /**
   * 功能描述: 文件类型校验工具类
   *
   * @param fileName 文件名
   * @param strings  文件后缀数组
   * @return String 校验结果
   * @author YaoS
   * @date 19/1/21 15:46
   */
  public static String fileCheck(String fileName, String... strings) {
    if (!FilenameUtils.isExtension(fileName, strings)) {
      return "文件扩展名只能是" + StringUtils.join(strings, "、") + "，请重新选择文件上传！";
    }
    return null;
  }

  /**
   * 下载文件
   *
   * @param path
   * @param response
   */
  public static boolean downloadFile(String path, HttpServletResponse response) {
    try {
      // path是指欲下载的文件的路径。
      File file = new File(path);
      // 取得文件名。
      String filename = file.getName();
      // 取得文件的后缀名。
      // String ext = filename.substring(filename.lastIndexOf(".") +
      // 1).toUpperCase();

      // 以流的形式下载文件。
      InputStream fis = new BufferedInputStream(new FileInputStream(path));
      byte[] buffer = new byte[fis.available()];
      fis.read(buffer);
      fis.close();
      // 清空response
      response.reset();
      // 设置response的Header
      String codedFileName = URLEncoder.encode(filename, "UTF-8");
      response.addHeader("Content-Disposition", "attachment;filename=" + codedFileName);
      response.addHeader("Content-Length", "" + file.length());
      OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
      response.setContentType("application/octet-stream");
      toClient.write(buffer);
      toClient.flush();
      toClient.close();
    } catch (IOException ex) {
      response.setContentType("text/html; charset=utf-8");
      try {
        PrintWriter writer = response.getWriter();
        writer.write("该文件找不到！");
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return false;
      }
      ex.printStackTrace();
      return false;

    }
    return true;
  }

  /**
   * 下载文件
   *
   * @param path
   * @param response
   */
  public static void uploadFile(String path, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      // path是指欲下载的文件的路径。
      File file = new File(path);
      // 取得文件名。
      String filename = file.getName();
      filename = filename.replaceAll(" ", "");
      // 解决中文文件名乱码问题
      if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
        filename = new String(filename.getBytes("UTF-8"), "ISO8859-1"); // firefox浏览器
      } else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {
        filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");// 谷歌
      } else {
        // IE浏览器
        filename = URLEncoder.encode(filename, "UTF-8");
      }
      // 取得文件的后缀名。
      // String ext = filename.substring(filename.lastIndexOf(".") +
      // 1).toUpperCase();

      // 以流的形式下载文件。
      InputStream fis = new BufferedInputStream(new FileInputStream(path));
      byte[] buffer = new byte[fis.available()];
      fis.read(buffer);
      fis.close();
      // 清空response
      response.reset();
      // 设置response的Header
      response.addHeader("Content-Disposition", "attachment;filename=" + filename);
      response.addHeader("Content-Length", "" + file.length());
      OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
      response.setContentType("application/octet-stream;charset=UTF-8");
      toClient.write(buffer);
      toClient.flush();
      toClient.close();
    } catch (IOException ex) {
      ex.printStackTrace();

    }
  }

  /**
   * @param templatePath          job_template.yaml 模板路径
   * @param out                   job.yaml 输出存放路径
   * @param jobId                 job 名
   * @param image                 镜像名
   * @param knwfLocation          worklow.knfw 文件
   * @param controllerYamLocation workflow.yaml 文件
   */

  public static void generateJobYamlFormTemplate(String templatePath, String out, String jobId,
      String image,
      String controllerYamLocation) {

    String s = null;

    BufferedReader reader = null;

    BufferedWriter writer = null;

    try {

      writer = new BufferedWriter(new FileWriter(out));

      reader = new BufferedReader(new FileReader(templatePath));

      while ((s = reader.readLine()) != null) {

        if (s.contains("${name}")) {

          s = s.replace("${name}", jobId);

        } else if (s.contains("${image}")) {

          s = s.replace("${image}", image);

        } else if (s.contains("${workflowYaml}")) {

          s = s.replace("${workflowYaml}", controllerYamLocation);

        }
        writer.write(s);

        writer.newLine();

      }

      reader.close();

      writer.close();

    } catch (IOException e) {

      log.error("部署文件生成失败", e);

    }
  }

  public static void generateDeployConfig(String templatePath, String out, String deploymentName,
      String specName,
      String cpu, String memory, String image, String replicas) {

    String s = null;

    BufferedReader reader = null;

    BufferedWriter writer = null;

    try {

      if (!(new File(out).exists())) {

        new File(out).mkdirs();
      }

      writer = new BufferedWriter(new FileWriter(out + File.separator + "deployment.json"));

      reader = new BufferedReader(new FileReader(templatePath));

      while ((s = reader.readLine()) != null) {

        if (s.contains("${deploymentName}")) {

          s = s.replace("${deploymentName}", deploymentName);

        } else if (s.contains("${specName}")) {

          s = s.replace("${specName}", specName);

        } else if (s.contains("${image}")) {

          s = s.replace("${image}", image);

        } else if (s.contains("${memory}")) {

          s = s.replace("${memory}", memory);

        } else if (s.contains("${cpu}")) {
          s = s.replace("${cpu}", cpu);

        } else if (s.contains("${replicas}")) {

          s = s.replace("${replicas}", replicas);
        }

        writer.write(s);

        writer.newLine();

      }

      reader.close();

      writer.close();

    } catch (IOException e) {

      log.error("部署文件生成失败", e);

    }

  }

  public static void generateDockFile(String templatePath, String baseImage, String outPath) {
    String s = null;

    BufferedReader reader = null;

    BufferedWriter writer = null;

    try {

      if (!(new File(outPath).exists())) {

        new File(outPath).mkdirs();
      }

      writer = new BufferedWriter(new FileWriter(outPath + File.separator + "workflowDockerfile"));

      reader = new BufferedReader(new FileReader(templatePath));

      while ((s = reader.readLine()) != null) {

        if (s.contains("${baseImage}")) {

          s = s.replace("${baseImage}", baseImage);

        }
        writer.write(s);

        writer.newLine();

      }

      reader.close();

      writer.close();

    } catch (IOException e) {

      log.error("dockerfile文件生成失败", e);

    }
  }
}
