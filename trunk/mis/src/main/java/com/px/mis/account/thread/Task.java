package com.px.mis.account.thread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.px.mis.account.dao.FormBookEntryDao;
import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.service.impl.FormBookEntryUtil;

/**
 *  定义任务类
 *  2019-12-24
 */
public class Task {
	
	
	public static final int READY = 0;  
    public static final int RUNNING = 1;  
    public static final int FINISHED = 2;  
 
    @SuppressWarnings("unused")  
    private int status;  
    // 声明一个任务的自有业务含义的变量，用于标识任务  
    private int taskId;  
    
    private String userName;
    private String loginTime;
    private List<FormBookEntry> formBookEntryList;
    
    ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
    FormBookEntryUtil formBookEntryUtil  =(FormBookEntryUtil)context.getBean("formBookEntryUtil");
    
    // 任务的初始化方法  
    public Task(int taskId,List<FormBookEntry> formBookEntryList, String userName, String loginTime) {  
        this.status = READY;  
        this.taskId = taskId;  
        this.formBookEntryList=formBookEntryList;
        this.userName = userName;
        this.loginTime = loginTime;
    }  
 
    /** 
           * 执行任务 
     */  
    public void execute() {  
        // 设置状态为运行中  
        setStatus(Task.RUNNING);  
        
        try {
        	System.out.println("--->"+Thread.currentThread().getName());
			formBookEntryUtil.costUprc(this.formBookEntryList,this.loginTime,this.userName,1);
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
		}
         
        try {  
            Thread.sleep(1000);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        // 执行完成，改状态为完成  
        setStatus(FINISHED);  
    }  
 
    public void setStatus(int status) {  
        this.status = status;  
    }  
 
    public int getTaskId() {  
        return taskId;  
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public List<FormBookEntry> getFormBookEntryList() {
		return formBookEntryList;
	}

	public void setFormBookEntryList(List<FormBookEntry> formBookEntryList) {
		this.formBookEntryList = formBookEntryList;
	}  
    
    

}
