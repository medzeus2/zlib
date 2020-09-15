package top.zeus2.data.page;

import top.zeus2.data.PageRequest;

public class MysqlPagedSqlAnalyis extends PagedSqlManager implements IPageSqlAnalysis {

    /**
     * 获取指定页码数据的查询语句
     *
     * @param pageRequest 查询第几页数据
     * @return
     * @throws Exception
     */
    @Override
    public String getPageSql(PageRequest pageRequest) {
        if (pageRequest.getPagesize() == 0) {
            return getSqlText();
        }

        // 拼接分页语句
        String sql2 = "%s limit %d,%d";

        return String.format(sql2, getSqlText(), pageRequest.getStart(), pageRequest.getPagesize());
    }
}
