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
 *  ����ַ���
 *  2019-12-24
 */
public class TaskDistributor {
	  
	/**
	 * ���Է��� 
	 * @param args
	 */
    @SuppressWarnings("unchecked")  
    public static void main(String[] args) {  
    	//�õ�Spring�����ļ�����
        ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
        //�������ļ���ȡ��idΪudao��UserDao�Ķ���
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
		
        // ��ʼ��Ҫִ�е������б�  
        List<Task> taskList = new ArrayList<>(); 
        List<List<FormBookEntry>> list = Lists.partition(formList, 100);
        for (int i = 0,l=list.size();i<l;i++) { 
        	
            taskList.add(new Task(i,list.get(i),"root",loginTime));  
        }  
        // �趨Ҫ�����Ĺ����߳���Ϊ 4 ��  
        int threadCount = 4;  
        List[] taskListPerThread = distributeTasks(taskList, threadCount);
        
        System.out.println("ʵ��Ҫ�����Ĺ����߳�����" + taskListPerThread.length);  
        
        for (int i = 0; i < taskListPerThread.length; i++) {  
            Thread workThread = new WorkThread(taskListPerThread[i], i);  
            workThread.start();  
        }  
 
    }  
    
    
 
	@SuppressWarnings("unchecked")
	public static List[] distributeTasks(List taskList, int threadCount) {  
 
        // ÿ���߳�����Ҫִ�е�������,���粻Ϊ�����ʾÿ���̶߳�����䵽����  
        int minTaskCount = taskList.size() / threadCount;  
        // ƽ�������ʣ�µ�����������Ϊ�����������������ӵ�ǰ����߳���  
        int remainTaskCount = taskList.size() % threadCount;  
        // ʵ��Ҫ�������߳���,��������̱߳����񻹶�  
        // ��Ȼֻ��Ҫ������������ͬ�����Ĺ����̣߳�һ��һ��ִ��  
        // �Ͼ�������ʵ�����̳߳أ������ò���Ԥ�ȳ�ʼ�������ߵ��߳�  
        int actualThreadCount = minTaskCount > 0 ? threadCount:remainTaskCount;  
 
        // Ҫ�������߳����飬�Լ�ÿ���߳�Ҫִ�е������б�  
        List[] taskListPerThread = new List[actualThreadCount];  
        int taskIndex = 0;  
        // ƽ��������������ÿ���Ӹ�һ���̺߳��ʣ���������������� remainTaskCount  
        // ��ͬ�ı�������Ȼ����ִ���иı� remainTaskCount ԭ��ֵ�������鷳  
        int remainIndces = remainTaskCount;  
        for (int i = 0; i < taskListPerThread.length; i++) {  
            taskListPerThread[i] = new ArrayList();  
            // ��������㣬�߳�Ҫ���䵽����������  
            if (minTaskCount > 0) {  
                for (int j = taskIndex; j < minTaskCount + taskIndex; j++) {  
                    taskListPerThread[i].add(taskList.get(j));  
                }  
                taskIndex += minTaskCount;  
            }  
            // ���绹��ʣ�µģ���һ��������߳���  
            if (remainIndces > 0) {  
                taskListPerThread[i].add(taskList.get(taskIndex++));  
                remainIndces--;  
            }  
        }  
 
        // ��ӡ����ķ������  
        for (int i = 0; i < taskListPerThread.length; i++) {  
            System.out.println("�߳� "+i+ "����������" + taskListPerThread[i].size()+ " ����["  
                    + ((Task) taskListPerThread[i].get(0)).getTaskId()  
                    + ","  
                    + ((Task) taskListPerThread[i].get(taskListPerThread[i].size() - 1))  
                            .getTaskId() + "]");  
        }  
        return taskListPerThread;  
    } 

}
