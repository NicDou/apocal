package com.jd.apocal.model.utils;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtil {

  public static <T> List<T> getCsvData(File file, Class<T> clazz) throws Exception {

    return new CsvToBeanBuilder<T>(new FileReader(file)).withType(clazz).build().parse();

  }

  public static Map<String, List<String[]>> getCsvData(File file) {

    Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
    List<String[]> header = new ArrayList<String[]>();
    List<String[]> content = new ArrayList<String[]>();
    CSVReader reader = null;
    try {
      reader = new CSVReader(new FileReader(file));

      String[] nextLine;

      int i = 0;

      while ((nextLine = reader.readNext()) != null) {

        if (i == 0) {

          header.add(nextLine);

        } else {

          content.add(nextLine);
        }

        i++;
      }

    } catch (Exception e) {

    } finally {

      try {

        if (reader != null) {

          reader.close();

        }

      } catch (IOException e) {

        e.printStackTrace();
      }

    }

    map.put("header", header);
    map.put("content", content);

    return map;

  }

}