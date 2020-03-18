package top.zeus2.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;
import org.junit.Before;
import org.junit.Test;

public class PagingOperatorTest {

  private DataSource dataSource;

  @Before
  public void init() throws SQLException {

    OracleDataSource odataSource = new OracleDataSource();
    odataSource.setURL("jdbc:oracle:thin:@127.0.0.1:1521:XE");
    odataSource.setUser("scott");
    odataSource.setPassword("tiger");
    dataSource = odataSource;
  }

  @Test
  public void test1() {

    PageJdbcTemplate pageJdbcTemplate = new PageJdbcTemplate(this.dataSource);

    List<Emp> re =
        pageJdbcTemplate.queryForBeanList("select * from EMP t order by empno", null, Emp.class);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    String s = gson.toJson(re);
    System.out.println(s);
  }

  @Test
  public void test2() {
    PageJdbcTemplate pageJdbcTemplate = new PageJdbcTemplate(dataSource);

    PageRequest pageRequest = PageRequest.buildwithStartEnd(11, 20);
    PageResult<Emp> re =
        pageJdbcTemplate.queryForPageResult(
            "select * from EMP t order by empno", null, pageRequest, Emp.class);
    Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd").create();

    String s = gson.toJson(re);
    System.out.println(s);
  }
}
