package top.zeus2.data.sql;

import top.zeus2.data.util.StringUtils;

/**
 * Sql语句分析器
 */
public class SqlBuilder {

    private String select;
    private String from;
    private String join = "";
    private String where;
    private String order;

    public static SqlBuilder select(String s) {
        SqlBuilder sb = new SqlBuilder();
        sb.select = s;
        return sb;
    }

    public SqlBuilder from(String s) {

        this.from = s;
        return this;
    }

    public SqlBuilder join(String s) {

        this.join += " inner join " + s;

        return this;
    }

    public SqlBuilder leftjoin(String s) {

        this.join += " left join " + s;

        return this;
    }

    public SqlBuilder rightjoin(String s) {
        this.join += " right join " + s;

        return this;
    }

    public SqlBuilder where(String s) {

        this.where = s;
        return this;
    }

    public SqlBuilder orderBy(String s) {
        this.order = s;
        return this;
    }

    public String build() {

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("select ").append(this.select).append(" from ").append(this.from);
        if (StringUtils.isNotEmpty(this.join)) {
            sqlStringBuilder.append(this.join);
        }
        if (StringUtils.isNotEmpty(this.where)) {
            sqlStringBuilder.append(" where ").append(this.where);
        }

        if (StringUtils.isNotEmpty(this.order)) {
            sqlStringBuilder.append(" order by ").append(this.order);
        }
        return sqlStringBuilder.toString();
    }
}
