package top.zeus2.data.impl;

import java.lang.reflect.InvocationTargetException;
import javax.sql.DataSource;
import top.zeus2.data.DbType;
import top.zeus2.data.util.StringUtils;

public class DataSourceTypeUtil {

  /**
   * 根据数据源判断数据库类型.
   *
   * @param dataSource 数据源
   * @return 数据库类型枚举
   */
  public static DbType getDbTypefromDataSource(DataSource dataSource) {

    String systemdbType = System.getProperty("DbType");

    if (!StringUtils.isBlank(systemdbType)) {

      DbType dbType = Enum.valueOf(DbType.class, systemdbType);
      return dbType;
    }

    DbType dbtype = DbType.UNKNOWN;

    // try to get JDBCURL
    String jdbcurl = null;
    try {
      jdbcurl = (String) dataSource.getClass().getMethod("getUrl").invoke(dataSource, null);
    } catch (IllegalAccessException e) {
      // Ignore
    } catch (InvocationTargetException e) {
      // Ignore

    } catch (NoSuchMethodException e) {
      // Ignore

    }
    if (StringUtils.isBlank(jdbcurl)) {
      // 使用默认的oracle
      dbtype = DbType.ORACLE;
      return dbtype;
    }

    if (jdbcurl.toLowerCase().startsWith("jdbc:oracle")) {
      dbtype = DbType.ORACLE;
    } else if (jdbcurl.toLowerCase().startsWith("jdbc:mysql")) {
      dbtype = DbType.MYSQL;
    } else if (jdbcurl.toLowerCase().startsWith("jdbc:db2")) {
      dbtype = DbType.DB2;
    } else {

      dbtype = DbType.ORACLE;
    }

    return dbtype;
  }
}
