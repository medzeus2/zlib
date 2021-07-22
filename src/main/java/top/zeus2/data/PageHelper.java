package top.zeus2.data;

public class PageHelper {

   static ThreadLocal<PageRequest> pageRequestThreadLocal = new ThreadLocal<>();

    /**
     * 开始分页，后面执行语句将自动返回分页结果
     *
     * @param pageRequest
     */
    public static void startPage(PageRequest pageRequest) {

        pageRequestThreadLocal.set(pageRequest);
    }
}
