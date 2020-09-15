package top.zeus2.data.page;

import lombok.Getter;
import lombok.Setter;
import top.zeus2.data.DbType;
import top.zeus2.data.PageRequest;
import top.zeus2.data.sql.SqlBuilder;
import top.zeus2.data.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public abstract class PagedSqlManager implements IPageSqlAnalysis {

    private final Pattern pattern =
            Pattern.compile("order\\s+by", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private static final ConcurrentHashMap<DbType, IPageSqlAnalysis> instanceMap =
            new ConcurrentHashMap<>();

    /**
     * 获取共享的分页分析语句
     *
     * @param dbType
     * @return
     */
    private static IPageSqlAnalysis getPageSqlAnalysisInstance(DbType dbType) {
        if (instanceMap.contains(dbType)) {
            return instanceMap.get(dbType);
        } else {
            IPageSqlAnalysis re = null;
            switch (dbType) {
                case DB2:
                    re = new DB2PageSqlAnalysis();
                    break;
                case MYSQL:
                    re = new MysqlPagedSqlAnalyis();
                    break;
                default:
                    re = new OraclePageSqlAnalysis();
                    break;
            }

            instanceMap.put(dbType, re);
            return re;
        }
    }

    /**
     * 创建分页分析对象
     *
     * @param dbType 数据库类型
     * @return
     */
    public static IPageSqlAnalysis create(DbType dbType) {
        return getPageSqlAnalysisInstance(dbType);
    }

    @Getter
    @Setter
    private String sqlText;

    @Getter
    @Setter
    private SqlBuilder sqlBuilder;

    /**
     * 获取执行数量的sql语句
     *
     * @return
     * @throws Exception
     */
    @Override
    public String getCountSql() {
        if (StringUtils.isEmpty(sqlText)) {
            throw new IllegalArgumentException("sql connot be null.");
        }

        return String.format(
                "select count(1) count from (%s) c", StringUtils.regReplacefromlast(pattern, sqlText, ""));
    }

    /**
     * 获取指定页码数据的查询语句
     *
     * @param pageRequest 查询第几页数据
     * @return
     * @throws Exception
     */
    @Override
    public abstract String getPageSql(PageRequest pageRequest);
}
