package top.zeus2.data.page;

import top.zeus2.data.PageRequest;
import top.zeus2.data.sql.SqlBuilder;

public interface IPageSqlAnalysis {

    /**
     * 设置待查询的sql语句.
     */
    void setSqlText(String sqlText);

    /**
     * 获取待查询的SQL语句.
     *
     * @return
     */
    String getSqlText();

    /**
     * 使用SqlBuilder
     *
     * @param sqlBuilder
     */
    void setSqlBuilder(SqlBuilder sqlBuilder);

    /**
     * 获取执行数量的sql语句.
     *
     * @return 获取查询数量的sql语句
     */
    String getCountSql();

    /**
     * 获取指定页码数据的查询语句.
     *
     * @param pageRequest 查询第几页数据
     * @return 获取查询指定页数的sql语句
     */
    String getPageSql(PageRequest pageRequest);
}
