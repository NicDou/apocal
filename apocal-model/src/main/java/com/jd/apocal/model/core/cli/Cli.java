package com.jd.apocal.model.core.cli;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Cli {

  public CliResult execute(String command) {

    String s = null;
    CliResult result = new CliResult();
    Process p = null;
    try {

      ProcessBuilder pb = new ProcessBuilder();

      pb.redirectErrorStream(true);

      p = pb.command("sh", "-c", command).start();

      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

      // 读取输出
      List<String> out = null;
      while ((s = stdInput.readLine()) != null) {
        if (out == null) {
          out = Lists.newArrayList();
        }
        out.add(s);
      }
      if (out != null) {
        result.setOutput(out);
        result.setStatus(1);
      }
      stdInput.close();

    } catch (Exception e) {

      result.setStatus(-2);
      result.setOutput(Lists.newArrayList(e.getMessage()));
      log.error("获取命令行:{}结果出错", command, e);
    } finally {
      if (p != null) {
        p.destroy();
      }
    }
    return result;
  }
}
