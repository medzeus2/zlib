package top.zeus2.data;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import top.zeus2.data.impl.DataSourceTypeUtil;
import top.zeus2.data.page.IPageSqlAnalysis;
import top.zeus2.data.page.PagedSqlManager;
import top.zeus2.data.sql.SqlBuilder;

import javax.sql.DataSource;
import java.util.List;

public class PageJdbcTemplate extends JdbcTemplate implements PageOperator {

  /** 默认构造函数. */
  public PageJdbcTemplate() {}

  public PageJdbcTemplate(DataSource dataSource) {
    super(dataSource);
  }

  /**
   * 分页查询操作.
   *
   * @param sql sql语句
   * @param args 查询参数
   * @param pageRequest 分页请求，例如第几页，每页多少条
   * @param requireType 泛型对象类
   * @return 分页结果
   */
  @Override
  public <T> PageResult<T> queryForPageResult(
      String sql, Object[] args, PageRequest pageRequest, Class<T> requireType)
      throws DataAccessException {
    return queryForPageResult(sql, args, pageRequest, new BeanPropertyRowMapper<T>(requireType));
  }

  /**
   * 分页查询操作
   *
   * @param sql sql语句
   * @param args 查询参数
   * @param pageRequest 分页请求，例如第几页，每页多少条
   * @param rowMapper
   * @return 分页结果
   */
  @Override
  public <T> PageResult<T> queryForPageResult(
      String sql, Object[] args, PageRequest pageRequest, RowMapper<T> rowMapper)
      throws DataAccessException {
    IPageSqlAnalysis psa =
        PagedSqlManager.create(DataSourceTypeUtil.getDbTypefromDataSource(this.getDataSource()));
    PageResult<T> pageResult = new PageResult<T>();
    psa.setSqlText(sql);

    String sql_count = psa.getCountSql();
    String sql_data = psa.getPageSql(pageRequest);
    Long count = -1L;
    if (pageRequest.isFetchcount()) {
      count = this.queryForObject(sql_count, args, Long.class);
    }
    pageResult.setCount(count);

    List<T> data = queryForBeanList(sql_data, args, rowMapper);
    pageResult.setData(data);

    return pageResult;
  }

  /**
   * 分页查询操作
   *
   * @param sqlBuilder sql分解片段
   * @param args 查询参数
   * @param pageRequest 分页请求，例如第几页，每页多少条
   * @param rowMapper
   * @return 分页结果
   */
  @Override
  public <T> PageResult<T> queryForPageResult(
      SqlBuilder sqlBuilder, Object[] args, PageRequest pageRequest, RowMapper<T> rowMapper)
      throws DataAccessException {
    return null;
  }

  /**
   * 通过SQL查询BeanList对象
   *
   * @param sql sql 文本
   * @param args 查询参数，如果没有传入null
   * @param requireType T的类型
   * @return t的List数组
   * @throws DataAccessException
   */
  @Override
  public <T> List<T> queryForBeanList(String sql, Object[] args, Class<T> requireType)
      throws DataAccessException {
    return queryForBeanList(sql, args, new BeanPropertyRowMapper<T>(requireType));
  }

  /**
   * 通过SQL查询BeanList对象
   *
   * @param sql sql 文本
   * @param args 查询参数，如果没有传入null
   * @param rowMapper 自定义对象映射关系
   * @return t的List数组
   * @throws DataAccessException
   */
  @Override
  public <T> List<T> queryForBeanList(String sql, Object[] args, RowMapper<T> rowMapper)
      throws DataAccessException {
    List<T> results = this.query(sql, args, new RowMapperResultSetExtractor<>(rowMapper));
    return results;
  }
}
