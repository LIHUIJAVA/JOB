package com.px.mis.account.thread;

import java.util.List;
/**
 * 线程执行类
 * 2019-12-24
 *
 */
public class WorkThread extends Thread{
	
	// 本线程待执行的任务列表，你也可以指为任务索引的起始值  
    private List<Task> taskList = null;  
    @SuppressWarnings("unused")  
    private int threadId;  
 
    /**
           * 构造工作线程，为其指派任务列表，及命名线程 ID
     *
     * @param taskList
            *            欲执行的任务列表
     * @param threadId
             *            线程 ID
     */  
    @SuppressWarnings("unchecked")  
    public WorkThread(List taskList, int threadId) {  
        this.taskList = taskList;  
        this.threadId = threadId;  
    }  
 
  
    /** 
     * 执行被指派的所有任务 
     */  
    public void run() {  
        for (Task task : taskList) {  
            task.execute();  
        }  
    } 

}
