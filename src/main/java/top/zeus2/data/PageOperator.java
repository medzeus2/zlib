package top.zeus2.data;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import top.zeus2.data.sql.SqlBuilder;

import java.util.List;

/**
 * 关于分页的接口
 */
public interface PageOperator {

    /**
     * 分页查询操作.
     *
     * @param sql         sql语句
     * @param pageRequest 分页请求，例如第几页，每页多少条
     * @param args        查询参数
     * @param requireType 泛型对象类
     * @param <T>         返回对象泛型类型
     * @return 分页结果
     */
    <T> PageResult<T> queryForPageResult(
            String sql, @Nullable Object[] args, PageRequest pageRequest, Class<T> requireType)
            throws DataAccessException;

    /**
     * 分页查询操作
     *
     * @param sql         sql语句
     * @param pageRequest 分页请求，例如第几页，每页多少条
     * @param args        查询参数
     * @param <T>         返回对象泛型类型
     * @return 分页结果
     */
    <T> PageResult<T> queryForPageResult(
            String sql, @Nullable Object[] args, PageRequest pageRequest, RowMapper<T> rowMapper)
            throws DataAccessException;

    /**
     * 分页查询操作
     *
     * @param sqlBuilder  sql分解片段
     * @param pageRequest 分页请求，例如第几页，每页多少条
     * @param args        查询参数
     * @param <T>         返回对象泛型类型
     * @return 分页结果
     */
    <T> PageResult<T> queryForPageResult(
            SqlBuilder sqlBuilder,
            @Nullable Object[] args,
            PageRequest pageRequest,
            RowMapper<T> rowMapper)
            throws DataAccessException;

    /**
     * 通过SQL查询BeanList对象
     *
     * @param sql         sql 文本
     * @param args        查询参数，如果没有传入null
     * @param <T>         泛型实体对象
     * @param requireType T的类型
     * @return t的List数组
     * @throws DataAccessException
     */
    <T> List<T> queryForBeanList(String sql, @Nullable Object[] args, Class<T> requireType)
            throws DataAccessException;

    /**
     * 通过SQL查询BeanList对象
     *
     * @param sql       sql 文本
     * @param args      查询参数，如果没有传入null
     * @param <T>       泛型实体对象
     * @param rowMapper 自定义对象映射关系
     * @return t的List数组
     * @throws DataAccessException
     */
    <T> List<T> queryForBeanList(String sql, @Nullable Object[] args, RowMapper<T> rowMapper)
            throws DataAccessException;
}
