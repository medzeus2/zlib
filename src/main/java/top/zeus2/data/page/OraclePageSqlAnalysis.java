package top.zeus2.data.page;

import top.zeus2.data.PageRequest;

public class OraclePageSqlAnalysis extends PagedSqlManager implements IPageSqlAnalysis {

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
        String sql2 =
                "SELECT * FROM (SELECT A.* ,ROWNUM ROW_NEXT FROM (%s)  A WHERE ROWNUM <= %d) A where ROW_NEXT >= %d";

        return String.format(sql2, getSqlText(), pageRequest.getEnd(), pageRequest.getStart());
    }
}
