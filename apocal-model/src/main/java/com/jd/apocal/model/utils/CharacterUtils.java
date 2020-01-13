package com.jd.apocal.model.utils;

import java.util.Random;

public class CharacterUtils {

  public static String getRandomString(int length) {
    // 定义一个字符串（a-z，0-9）即位；
    String str = "zxcvbnmlkjhgfdsaqwertyuiop1234567890";
    // 由Random生成随机数
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    // 长度为几就循环几次
    for (int i = 0; i < length; ++i) {

      int number = random.nextInt(36);
      // 将产生的数字通过length次承载到sb中
      sb.append(str.charAt(number));
    }

    return sb.toString();
  }
}
