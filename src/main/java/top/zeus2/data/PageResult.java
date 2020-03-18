package top.zeus2.data;

import lombok.Data;

import java.util.List;

/**
 * 分页结果.
 *
 * @param <T>
 */
@Data
public class PageResult<T> {

  /** 数据总数 */
  private Long count;
  /** 分页数据 */
  private List<T> data;
}
