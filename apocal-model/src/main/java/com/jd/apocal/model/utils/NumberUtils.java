package com.jd.apocal.model.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/**
 * 数字工具类
 *
 * @author Leegern
 * @date 2018年1月15日
 */
public class NumberUtils {

  /**
   * 浮点型
   **/
  private static final String PARRERN_DOUBLE = "[0-9]*(\\.?)[0-9]*";
  /**
   * 整型
   **/
  private static final String PATTERN_INTEGER = "[0-9]+";

  /**
   * 判断是否是浮点型
   *
   * @param inputStr
   * @return
   */
  public static boolean isDecimal(String inputStr) {
    if (StringUtils.isEmpty(inputStr)) {
      return false;
    }
    Pattern pattern = Pattern.compile(PARRERN_DOUBLE);
    return pattern.matcher(inputStr).matches();
  }

  /**
   * 判断是否是整型
   *
   * @param inputStr
   * @return
   */
  public static boolean isInteger(String inputStr) {
    if (StringUtils.isEmpty(inputStr)) {
      return false;
    }
    Pattern pattern = Pattern.compile(PATTERN_INTEGER);
    return pattern.matcher(inputStr).matches();
  }

  /**
   * 判断是否是正数(包括浮点型和整型)
   *
   * @param inputStr
   * @return
   */
  public static boolean isPositiveNumber(String inputStr) {
    if (StringUtils.isEmpty(inputStr)) {
      return false;
    }
    return (isDecimal(inputStr) || isInteger(inputStr));
  }

  /**
   * 过滤掉中文
   *
   * @param str 待过滤中文的字符串
   * @return 过滤掉中文后字符串
   */
  public static String filterChinese(String str) {
    // 用于返回结果
    String result = str;
    boolean flag = isContainChinese(str);
    if (flag) {// 包含中文
      // 用于拼接过滤中文后的字符
      StringBuffer sb = new StringBuffer();
      // 用于校验是否为中文
      boolean flag2 = false;
      // 用于临时存储单字符
      char chinese = 0;
      // 5.去除掉文件名中的中文
      // 将字符串转换成char[]
      char[] charArray = str.toCharArray();
      // 过滤到中文及中文字符
      for (int i = 0; i < charArray.length; i++) {
        chinese = charArray[i];
        flag2 = isChinese(chinese);
        if (!flag2) {// 不是中日韩文字及标点符号
          sb.append(chinese);
        }
      }
      result = sb.toString();
    }
    return result;
  }

  /**
   * 判断字符串中是否包含中文
   *
   * @param str 待校验字符串
   * @return 是否为中文
   * @warn 不能校验是否为中文标点符号
   */
  public static boolean isContainChinese(String str) {
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    Matcher m = p.matcher(str);
    if (m.find()) {
      return true;
    }
    return false;
  }

  /**
   * 判定输入的是否是汉字
   *
   * @param c 被校验的字符
   * @return true代表是汉字
   */
  public static boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
      return true;
    }
    return false;
  }

  /**
   * 身份证脱敏
   *
   * @param id
   * @return
   */
  public static String idEncrypt(String id) {
    if (org.apache.commons.lang3.StringUtils.isBlank(id) || (id.length() < 8)) {
      return id;
    }
    return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
  }

  /**
   * 保留小数位
   *
   * @param id
   * @return
   */
  public static double getDouble(int scale, double f) {
    BigDecimal bg = new BigDecimal(f);
    double f1 = bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    return f1;
  }

}