package top.zeus2.data.resultsetextractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * TODO 获取实体列表
 *
 * @param <T>
 */
public class BeanListExtractor<T> implements ResultSetExtractor<List<T>> {
    @Override
    public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
        return null;
    }
}
