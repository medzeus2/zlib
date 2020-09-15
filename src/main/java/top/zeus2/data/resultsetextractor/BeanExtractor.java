package top.zeus2.data.resultsetextractor;

import org.apache.commons.dbutils.RowProcessor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanExtractor<T> implements ResultSetExtractor<T> {

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
     * Creates a new instance of BeanHandler.
     *
     * @param type The Class that objects returned from <code>handle()</code>
     *             are created from.
     */
    public BeanExtractor(Class<? extends T> type) {
        this.type = type;
        this.convert = RowProcessorConsts.INSTANCE;

    }


    @Override
    public T extractData(ResultSet rs) throws SQLException, DataAccessException {
        return rs.next() ? this.convert.toBean(rs, this.type) : null;
    }
}
