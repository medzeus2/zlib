package top.zeus2.data;

import java.util.Date;
import lombok.Data;

@Data
public class Emp {

  private Integer empno;
  private String ename;
  private String job;
  private Date hiredate;
  private Integer sal;
}
