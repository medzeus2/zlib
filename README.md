# zlib是我在工程经验中遇到一些可以提取为公共能力的类库
## jdbc模块的完善
实现jdbc模板大多数方法，扩展分页获取的方法用于完善。

## 分布式锁
使用简单的分布式锁功能，基于redis setnx及lua脚本通俗易懂

## 批量异步任务执行框架
非常容易使用的异步执行框架，将同步操作通过内存队列执行为异步批量执行或异步单次执行。
