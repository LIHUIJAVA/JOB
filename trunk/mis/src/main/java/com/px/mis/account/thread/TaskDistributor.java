package com.px.mis.account.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Lists;
import com.px.mis.account.dao.FormBookEntryDao;
import com.px.mis.account.entity.FormBookEntry;

/**
 *  任务分发类
 *  2019-12-24
 */
public class TaskDistributor {
	  
	/**
	 * 测试方法 
	 * @param args
	 */
    @SuppressWarnings("unchecked")  
    public static void main(String[] args) {  
    	//得到Spring配置文件对象
        ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
        //从配置文件中取出id为udao的UserDao的对象
        FormBookEntryDao formBookEntryDao  =(FormBookEntryDao)context.getBean("formBookEntryDao");
    	
    	
    	Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isNtBookOk", "0");
		//paramMap.put("formCodeList", formCodeList);
		String loginTime = "2019-01-31 00:00:00";
		paramMap.put("loginTime", loginTime);
		paramMap.put("index", 0);
		paramMap.put("num", 100);
		paramMap.put("isPage", "1");
		List<FormBookEntry> formList = formBookEntryDao.selectStreamALLList(paramMap);
		
        // 初始化要执行的任务列表  
        List<Task> taskList = new ArrayList<>(); 
        List<List<FormBookEntry>> list = Lists.partition(formList, 100);
        for (int i = 0,l=list.size();i<l;i++) { 
        	
            taskList.add(new Task(i,list.get(i),"root",loginTime));  
        }  
        // 设定要启动的工作线程数为 4 个  
        int threadCount = 4;  
        List[] taskListPerThread = distributeTasks(taskList, threadCount);
        
        System.out.println("实际要启动的工作线程数：" + taskListPerThread.length);  
        
        for (int i = 0; i < taskListPerThread.length; i++) {  
            Thread workThread = new WorkThread(taskListPerThread[i], i);  
            workThread.start();  
        }  
 
    }  
    
    
 
	@SuppressWarnings("unchecked")
	public static List[] distributeTasks(List taskList, int threadCount) {  
 
        // 每个线程至少要执行的任务数,假如不为零则表示每个线程都会分配到任务  
        int minTaskCount = taskList.size() / threadCount;  
        // 平均分配后还剩下的任务数，不为零则还有任务依个附加到前面的线程中  
        int remainTaskCount = taskList.size() % threadCount;  
        // 实际要启动的线程数,如果工作线程比任务还多  
        // 自然只需要启动与任务相同个数的工作线程，一对一的执行  
        // 毕竟不打算实现了线程池，所以用不着预先初始化好休眠的线程  
        int actualThreadCount = minTaskCount > 0 ? threadCount:remainTaskCount;  
 
        // 要启动的线程数组，以及每个线程要执行的任务列表  
        List[] taskListPerThread = new List[actualThreadCount];  
        int taskIndex = 0;  
        // 平均分配后多余任务，每附加给一个线程后的剩余数，重新声明与 remainTaskCount  
        // 相同的变量，不然会在执行中改变 remainTaskCount 原有值，产生麻烦  
        int remainIndces = remainTaskCount;  
        for (int i = 0; i < taskListPerThread.length; i++) {  
            taskListPerThread[i] = new ArrayList();  
            // 如果大于零，线程要分配到基本的任务  
            if (minTaskCount > 0) {  
                for (int j = taskIndex; j < minTaskCount + taskIndex; j++) {  
                    taskListPerThread[i].add(taskList.get(j));  
                }  
                taskIndex += minTaskCount;  
            }  
            // 假如还有剩下的，则补一个到这个线程中  
            if (remainIndces > 0) {  
                taskListPerThread[i].add(taskList.get(taskIndex++));  
                remainIndces--;  
            }  
        }  
 
        // 打印任务的分配情况  
        for (int i = 0; i < taskListPerThread.length; i++) {  
            System.out.println("线程 "+i+ "的任务数：" + taskListPerThread[i].size()+ " 区间["  
                    + ((Task) taskListPerThread[i].get(0)).getTaskId()  
                    + ","  
                    + ((Task) taskListPerThread[i].get(taskListPerThread[i].size() - 1))  
                            .getTaskId() + "]");  
        }  
        return taskListPerThread;  
    } 

}
