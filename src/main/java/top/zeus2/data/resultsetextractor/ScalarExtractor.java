package top.zeus2.data.resultsetextractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 从结果集中获取一个结果
 */
public class ScalarExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {

        if (rs.next()) {
            return rs.getObject(1);
        } else {
            return null;
        }
    }
}
