package top.zeus2.data.page;

import java.util.Locale;

/** sql清理，语法分析 */
public class SqlClean {

  /**
   * 清理order by 子语句
   *
   * @param sql
   * @return
   */
  public static String replaceOrderby(String sql) {
    /** 避免换行 */
    sql = sql.replace("\n", "");
    /** 2个空格替换为2个空格 */
    sql = sql.replace("  ", " ");

    // 强制转换为小写
    String sqllowercase = sql.toLowerCase(Locale.ENGLISH);
    int index = sqllowercase.lastIndexOf("order by");
    if (index < 0) {
      return sql;
    } else {

      return sql.substring(0, index);
    }
  }
}
