package com.project.app.dao;

        import com.project.app.bean.ThreadInfo;

        import java.util.List;

public interface ThreadDao {

    //插入线程信息
    void insertThread(ThreadInfo threadInfo);

    //删除线程信息
    void deleteThread(String url,int threadId);

    //删除线程信息
    void deleteThread(String url);

    //更新线程id
    void updateThread(String url,int threadId,int finished);

    //查询线程信息
    List<ThreadInfo> queryThread(String url);

    //线程信息是否存在
    boolean threadIsExist(String url,int threadId);

}
