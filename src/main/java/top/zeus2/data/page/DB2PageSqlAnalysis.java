package top.zeus2.data.page;

import top.zeus2.data.PageRequest;
import top.zeus2.data.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DB2PageSqlAnalysis extends PagedSqlManager implements IPageSqlAnalysis {
    private final Pattern sqlregex =
            Pattern.compile("order\\s+by(.*)$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private final Pattern replaceorderby =
            Pattern.compile("(order\\s+by)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    /**
     * 获取指定页码数据的查询语句.
     *
     * @param pageRequest 查询第几页数据
     * @return
     */
    @Override
    public String getPageSql(PageRequest pageRequest) {

        if (pageRequest.getPagesize() == 0) {
            return getSqlText();
        }

        // select * from (
        // select empno, row_number() over (order by empno) as rownext from emp
        // ) a where rownext between 21 and 40
        // System.out.println(m_SqlString);

        // 如果没有找到order by 直接出结果
        if (!sqlregex.matcher(getSqlText()).find()) {
            throw new IllegalArgumentException(
                    String.format("分页语句必须需要Order By排序\r\nError Sql:%s", getSqlText()));
        }

        Matcher match = replaceorderby.matcher(getSqlText());

        String order = "";
        boolean b = false;

        while ((b = match.find())) {

            int begin = match.start();
            order = getSqlText().substring(begin);
        }

        order = replaceorderby.matcher(order).replaceFirst("").trim();
        String sqlwithoutorderby = StringUtils.regReplacefromlast(replaceorderby, getSqlText(), "");

        StringBuilder sqlcopy = new StringBuilder(32);

        sqlcopy.append(
                String.format(
                        "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY %s) ROW_NEXT, TABLE_TEMP.* FROM (%s",
                        order, sqlwithoutorderby));

        sqlcopy.append(
                String.format(" FETCH FIRST %d ROWS ONLY) AS TABLE_TEMP ) AS T", pageRequest.getEnd()));

        return String.format(
                sqlcopy.toString() + " WHERE T.ROW_NEXT BETWEEN %d AND %d",
                pageRequest.getStart(),
                pageRequest.getEnd());
    }
}
