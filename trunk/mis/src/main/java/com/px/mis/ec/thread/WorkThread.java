package com.px.mis.ec.thread;

/**
 * 线程执行类
 * 2019-12-27
 */
public class WorkThread extends Thread {
    private Task task = null;

    /**
     * 构造工作线程
     */
    @SuppressWarnings("unchecked")
    public WorkThread(Task task) {
        this.task = task;
    }


    /**
     * 执行被指派的所有任务
     */
    public void run() {
        task.execute();
    }

}
