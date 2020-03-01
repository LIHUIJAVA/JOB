package com.px.mis.account.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.px.mis.account.dao.MthEndStlDao;
import com.px.mis.account.entity.MthEndStl;

@Component
public class AopAdvice {
	@Autowired
	private MthEndStlDao mthEndStlDao; //��ĩ����
	   
	/*
	 * ����ǰ��֪ͨ,ʹ���ڷ���aspect()��ע��������
	 * ͬʱ����JoinPoint��������,����û�иò���
	 */
	public void beforeIsInMonth(){
			
		String loginTime = "2019-03-04 12:00:00";
		Boolean isInMonth = true;
		Map<String,String> map = new HashMap<>();
		String year = loginTime.substring(0,4);
		String month = loginTime.substring(loginTime.indexOf("-")+1,loginTime.lastIndexOf("-"));
		map.put("isMthEndStl","0");
		map.put("year", year);
		map.put("month", month);
		List<MthEndStl> mthList = mthEndStlDao.selectByMap(map);
		if(mthList.size() != 0) {
			isInMonth = true;
		} else {
			isInMonth = false;
		}
		System.out.println("----"+isInMonth);
	}
}