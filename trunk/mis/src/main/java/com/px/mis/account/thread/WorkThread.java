package com.px.mis.account.thread;

import java.util.List;
/**
 * �߳�ִ����
 * 2019-12-24
 *
 */
public class WorkThread extends Thread{
	
	// ���̴߳�ִ�е������б���Ҳ����ָΪ������������ʼֵ  
    private List<Task> taskList = null;  
    @SuppressWarnings("unused")  
    private int threadId;  
 
    /**
           * ���칤���̣߳�Ϊ��ָ�������б��������߳� ID
     *
     * @param taskList
            *            ��ִ�е������б�
     * @param threadId
             *            �߳� ID
     */  
    @SuppressWarnings("unchecked")  
    public WorkThread(List taskList, int threadId) {  
        this.taskList = taskList;  
        this.threadId = threadId;  
    }  
 
  
    /** 
     * ִ�б�ָ�ɵ��������� 
     */  
    public void run() {  
        for (Task task : taskList) {  
            task.execute();  
        }  
    } 

}
