package com.px.mis.ec.thread;

/**
 * �߳�ִ����
 * 2019-12-27
 */
public class WorkThread extends Thread {
    private Task task = null;

    /**
     * ���칤���߳�
     */
    @SuppressWarnings("unchecked")
    public WorkThread(Task task) {
        this.task = task;
    }


    /**
     * ִ�б�ָ�ɵ���������
     */
    public void run() {
        task.execute();
    }

}
