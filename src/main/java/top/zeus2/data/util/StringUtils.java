package top.zeus2.data.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringUtils {

  /**
   * 检查指定的字符串是否为空.
   *
   * <ul>
   *   <li>StringUtils.isBlank(null) = true
   *   <li>StringUtils.isBlank("") = true
   *   <li>StringUtils.isBlank(" ") = true
   *   <li>StringUtils.isBlank("abc") = false
   * </ul>
   *
   * @param value 待检查的字符串
   * @return true/false
   */
  public static boolean isBlank(String value) {
    int strLen;
    if (value == null || (strLen = value.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if ((Character.isWhitespace(value.charAt(i)) == false)) {
        return false;
      }
    }
    return true;
  }

  /**
   * 检查指定的字符串是否为非空.
   *
   * <ul>
   *   <li>StringUtils.isNotBlank(null) = false
   *   <li>StringUtils.isNotBlank("") = false
   *   <li>StringUtils.isNotBlank(" ") = false
   *   <li>StringUtils.isNotBlank("abc") = true
   * </ul>
   *
   * @param value 待检查的字符串
   * @return true/false
   */
  public static boolean isNotBlank(String value) {
    return !isBlank(value);
  }

  /**
   * 检查指定的字符串是否为空字符.
   *
   * <ul>
   *   <li>StringUtils.isEmpty(null) = true
   *   <li>StringUtils.isEmpty("") = true
   *   <li>StringUtils.isEmpty(" ") = false
   *   <li>StringUtils.isEmpty("abc") = false
   * </ul>
   *
   * @param value 待检查的字符串
   * @return true/false
   */
  public static boolean isEmpty(String value) {
    int strLen;
    return value == null || (strLen = value.length()) == 0;
  }

  /**
   * 检查指定的字符串是否为空字符.
   *
   * <ul>
   *   <li>StringUtils.isNotEmpty(null) = false
   *   <li>StringUtils.isNotEmpty("") = false
   *   <li>StringUtils.isNotEmpty(" ") = true
   *   <li>StringUtils.isNotEmpty("abc") = true
   * </ul>
   *
   * @param value 待检查的字符串
   * @return true/false
   */
  public static boolean isNotEmpty(String value) {
    return !isEmpty(value);
  }

  /**
   * 将符合正则的字符替换为 replacement
   *
   * @param pattern 正则表达式
   * @param src 待替换的字符
   * @param replacement 替换的字符
   * @return
   */
  public static String regReplace(Pattern pattern, String src, String replacement) {

    return pattern.matcher(src).replaceFirst(replacement);
  }

  /**
   * 寻找匹配结果最后一个结果 一直到结束 都替换成默认字符。
   *
   * @param pattern
   * @param src
   * @param replacement
   * @return
   */
  public static String regReplacefromlast(Pattern pattern, String src, String replacement) {
    Matcher matcher = pattern.matcher(src);

    int start = 0, end = src.length();
    boolean find = false;
    // 找到最后的 位置
    while (matcher.find()) {
      start = matcher.start();
      end = matcher.end();
      find = true;
    }
    // 如果没有找到，直接返回源字符串
    if (!find) {
      return src;
    }

    // 将替代拼接起来
    String result = src.substring(0, start) + replacement;

    return result;
  }

  /**
   * 判断字符串是否匹配正则
   *
   * @param pattern 正则表达式
   * @param src 字符串
   * @return 返回匹配的字符
   */
  private static String isMatch(Pattern pattern, String src) {

    Matcher matcher = pattern.matcher(src);
    String s = "";
    int index = 0;
    while (matcher.find(index)) {
      log.debug("found \"" + matcher.group() + " [" + matcher.start() + "," + matcher.end() + "]");

      s = matcher.group();
      index = matcher.start() + 1;
      matcher.reset();
    }
    return s;
  }
}
