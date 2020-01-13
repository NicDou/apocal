package com.jd.apocal.model.core.doc;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.google.common.collect.Maps;
import com.jd.apocal.model.core.metas.deploy.DeployYml;
import com.jd.apocal.model.utils.CsvUtil;
import com.jd.apocal.model.utils.UuidUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ModelDocument {

  private long userId;
  private long officeId;
  private String userName;
  private String officeName;

  private String docId;
  private String name;
  private String type;
  private String location;
  private String zipLocation;

  private Map<String, Map<String, List<String[]>>> metric = Maps.newHashMap();
  private DeployYml deployYml;
  private Map<String, Object> modelYml;
  private List<Map<String, Object>> dataSpecYml;

  private String metricsDir;

  private String modulesDir;

  private String dataSpeYmlDir;
  private String deployYmlDir;
  private String modelYmlDir;

  private String binaryClassifierDir;
  private String multiClassifierDir;
  private String clusterDir;
  private String regressionDir;

  public ModelDocument(File file, String docId, long userId, long officeId, String userName,
      String officeName) {

    this.userId = userId;
    this.userName = userName;
    this.officeId = officeId;
    this.officeName = officeName;
    this.name = file.getName();
    this.docId = StringUtils.isBlank(docId) ? UuidUtil.getInstance().getUUid() : docId;
    this.location = file.getAbsolutePath();

    this.modulesDir = this.location + File.separator + "modules";
    this.metricsDir = this.location + File.separator + "metrics";

    this.binaryClassifierDir = metricsDir + File.separator + "Binary-Classifier";
    this.multiClassifierDir = metricsDir + File.separator + "Multi-Classifier";
    this.regressionDir = metricsDir + File.separator + "Regression";
    this.clusterDir = metricsDir + File.separator + "Cluster";

    this.dataSpeYmlDir = this.location + File.separator + "metas/dataSpec.yml";
    this.deployYmlDir = this.location + File.separator + "metas/deploy.yml";
    this.modelYmlDir = this.location + File.separator + "metas/model.yml";

    init();

  }

  @SuppressWarnings("unchecked")
  private void init() {

    try {

      deployYml = new YamlReader(new InputStreamReader(new FileInputStream(deployYmlDir), "UTF-8"))
          .read(DeployYml.class);
      modelYml = (Map<String, Object>) new YamlReader(
          new InputStreamReader(new FileInputStream(modelYmlDir),
              "UTF-8")).read();
      dataSpecYml = (List<Map<String, Object>>) new YamlReader(
          new InputStreamReader(new FileInputStream(
              dataSpeYmlDir), "UTF-8")).read();

      Map<String, Object> modelInfo = (Map<String, Object>) modelYml.get("model");

      String type = (String) modelInfo.get("class");

      String trainResultDir = null;

      String testResultDir = null;

      if ("Binomial_Classification".equals(type)) {

        trainResultDir = binaryClassifierDir + File.separator + "TrainResult";

        testResultDir = binaryClassifierDir + File.separator + "TestResult";

      } else if ("Multinomial_Classification".equals(type)) {

        trainResultDir = multiClassifierDir + File.separator + "TrainResult";

        testResultDir = multiClassifierDir + File.separator + "TestResult";

      } else if ("Regression".equals(type)) {

        trainResultDir = regressionDir + File.separator + "TrainResult";

        testResultDir = regressionDir + File.separator + "TestResult";

      } else if ("Cluster".equals(type)) {

        trainResultDir = clusterDir + File.separator + "TrainResult";

        testResultDir = clusterDir + File.separator + "TestResult";

      }

      File trainCsv = new File(trainResultDir);

      if (trainCsv != null && trainCsv.exists() && trainCsv.isDirectory()) {

        for (File file : trainCsv.listFiles()) {

          String fileName = file.getName();

          if (fileName.endsWith(".csv")) {

            try {

              Map<String, List<String[]>> csvData = CsvUtil.getCsvData(file);

              metric.put("Train_" + fileName, csvData);

            } catch (Exception e) {
              e.printStackTrace();
            }
          }

        }

      }

      File testCsv = new File(testResultDir);

      if (testCsv != null && testCsv.exists() && testCsv.isDirectory()) {

        for (File file : testCsv.listFiles()) {

          String fileName = file.getName();

          if (fileName.endsWith(".csv")) {

            try {

              Map<String, List<String[]>> csvData = CsvUtil.getCsvData(file);

              metric.put("Test_" + fileName, csvData);

            } catch (Exception e) {
              e.printStackTrace();
            }
          }

        }

      }

    } catch (Exception e) {

      e.printStackTrace();

    }

  }


}
