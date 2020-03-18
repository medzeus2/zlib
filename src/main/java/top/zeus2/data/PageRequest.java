package top.zeus2.data;

import lombok.Data;

/** 自动生成分页请求，支持start end模式及 页面大小页码模式。 */
@Data
public class PageRequest {

  private int start;
  private int end;
  private int pagesize;
  private int pageindex;
  /** 是否获取记录总数，在分页时，第二页就没必要获取记录总数 */
  private boolean fetchcount = true;

  private PageRequest() {}

  /**
   * 根据start end创建分页请求
   *
   * @param start 起始记录数
   * @param end 结束记录数
   * @return
   */
  public static PageRequest buildwithStartEnd(int start, int end) {
    if (start <= 0) {
      throw new IllegalArgumentException("start cannot less than 1");
    }
    if (end - start <= 0) {
      throw new IllegalArgumentException("end cannot less than start");
    }

    PageRequest request = new PageRequest();
    request.setPagesize(end - start + 1);
    request.setStart(start);
    request.setEnd(end);
    if ((start - 1) % request.getPagesize() != 0) {
      throw new IllegalArgumentException(
          "start end not correct, cannot convert to pageindex and pagesize");
    }
    int pageindex = (start - 1) / request.getPagesize() + 1;
    request.setPageindex(pageindex);
    return request;
  }

  /**
   * 按照index索引分页
   *
   * @param pageindex 分页索引 第几页
   * @param pagesize 分页大小 每页多少条
   * @return PageRequest instance
   */
  public static PageRequest buildwithIndexSize(int pageindex, int pagesize) {
    if (pageindex <= 0) {
      throw new IllegalArgumentException("pageindex cannot less than 1");
    }
    if (pagesize <= 0) {
      throw new IllegalArgumentException("pagesize cannot less than 0");
    }
    int start = (pageindex - 1) * pagesize + 1;
    int end = start + pagesize;
    PageRequest request = new PageRequest();
    request.setPageindex(pageindex);
    request.setPagesize(pagesize);
    request.setStart(start);
    request.setEnd(end);
    return request;
  }
}
