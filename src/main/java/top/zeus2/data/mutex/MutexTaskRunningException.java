package top.zeus2.data.mutex;

public class MutexTaskRunningException extends RuntimeException {
    public MutexTaskRunningException(String s) {
        super(s);
    }
}
