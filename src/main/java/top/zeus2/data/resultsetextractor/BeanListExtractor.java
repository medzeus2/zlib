package top.zeus2.data.resultsetextractor;

import org.apache.commons.dbutils.RowProcessor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 获取实体列表
 *
 * @param <T>
 */
public class BeanListExtractor<T> implements ResultSetExtractor<List<T>> {

    /**
     * The Class of beans produced by this handler.
     */
    private final Class<? extends T> type;

    /**
     * The RowProcessor implementation to use when converting rows
     * into beans.
     */
    private final RowProcessor convert;

    /**
     * 根据ResultSet获取Beanlist
     *
     * @param rs ResultSet
     * @return
     * @throws SQLException
     * @throws DataAccessException
     */
    @Override
    public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
        return this.convert.toBeanList(rs, type);
    }

    /**
     * Creates a new instance of BeanListHandler.
     *
     * @param type The Class that objects returned from <code>handle()</code>
     *             are created from.
     */
    public BeanListExtractor(Class<? extends T> type) {
        this.type = type;
        this.convert = RowProcessorConsts.INSTANCE;
    }


}
