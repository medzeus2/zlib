package top.zeus2.data;

import org.junit.Ignore;
import org.junit.Test;
import top.zeus2.data.page.SqlClean;
import top.zeus2.data.sql.SqlBuilder;

public class SqlTest {

  @Test
  @Ignore
  public void testsql() {

    String sql =
        SqlBuilder.select("ID,NAME")
            .from("STU A")
            .leftjoin("TEACH T")
            .where("A.ID>10 AND T.NAME = '孙'")
            .orderBy("ID DESC")
            .build();
    System.out.println(sql);
  }

  @Test
  public void testsql2() {

    String sql = "select * from acb order by ccc desc order by xx";
    System.out.println(SqlClean.replaceOrderby(sql));
  }

  @Test
  public void testsql3() {

    String sql = "select * from acb  order " + "\n by xx";
    System.out.println(SqlClean.replaceOrderby(sql));
  }
}
