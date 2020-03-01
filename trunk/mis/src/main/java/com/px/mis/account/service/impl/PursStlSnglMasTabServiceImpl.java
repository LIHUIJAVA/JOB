package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.account.dao.PursStlSnglMasTabDao;
import com.px.mis.account.dao.PursStlSubTabDao;
import com.px.mis.account.entity.PursStlSnglMasTab;
import com.px.mis.account.entity.PursStlSubTab;
import com.px.mis.account.service.PursStlSnglMasTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
//�ɹ���������
@Service
@Transactional
public class PursStlSnglMasTabServiceImpl implements PursStlSnglMasTabService {
	@Autowired
	private PursStlSnglMasTabDao pursStlSnglMasTabDao;
	@Autowired
	private PursStlSubTabDao pursStlSubTabDao;
	@Autowired
	private GetOrderNo getOrderNo;//������
	
	//�����ɹ����㵥
	@Override
	public String addPursStlSnglMasTab(String userId,PursStlSnglMasTab pursStlSnglMasTab,String loginTime) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		String stlSnglId = pursStlSnglMasTab.getStlSnglId();
		if(stlSnglId==null) {
			isSuccess=false;
			message = "����ʧ��,��������Ϊ��";
		}else {
			//��ȡ������
			String number=getOrderNo.getSeqNo("JS", userId,loginTime);
			if(pursStlSnglMasTabDao.selectPursStlSnglMasTabByStlSnglId(stlSnglId)!=null){
				isSuccess=false;
				message = "����"+pursStlSnglMasTabDao+"�Ѵ��ڣ�����ʧ�ܣ�";
			}else {
				pursStlSnglMasTab.setStlSnglId(number);
				int insertResult = pursStlSnglMasTabDao.insertPursStlSnglMasTab(pursStlSnglMasTab);
				if(insertResult>=1) {
					List<PursStlSubTab> pursSubList = pursStlSnglMasTab.getPursSubList();
					for(PursStlSubTab pursStlSubTab:pursSubList) {
						pursStlSubTab.setStlSnglId(pursStlSnglMasTab.getStlSnglId());
						if(pursStlSubTab.getPrdcDt()=="") {
							pursStlSubTab.setPrdcDt(null);
						}
						if(pursStlSubTab.getInvldtnDt()=="") {
							pursStlSubTab.setInvldtnDt(null);
						}
					}
					pursStlSubTabDao.insertPursStlSubTab(pursSubList);
					message="�����ɹ���";
					isSuccess=true;
				}else {
					isSuccess=false;
					message="����ʧ�ܣ�";
				}
			}
		}
		try {
			resp=BaseJson.returnRespObj("account/PursStlSnglMasTab/addPursStlSnglMasTab", isSuccess, message, pursStlSnglMasTab);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String updatePursStlSnglMasTab(PursStlSnglMasTab pursStlSnglMasTab) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		int updateResult = pursStlSnglMasTabDao.updatePursStlSnglMasTab(pursStlSnglMasTab);
		int deleteResult = pursStlSubTabDao.deletePursStlSubTabByStlSnglId(pursStlSnglMasTab.getStlSnglId());
		if(updateResult==1 && deleteResult >=1) {
			List<PursStlSubTab> pursStlSubTabList = pursStlSnglMasTab.getPursSubList();
			for(PursStlSubTab pursStlSubTab:pursStlSubTabList) {
				pursStlSubTab.setStlSnglId(pursStlSnglMasTab.getStlSnglId());
				if(pursStlSubTab.getPrdcDt()=="") {
					pursStlSubTab.setPrdcDt(null);
				}
				if(pursStlSubTab.getInvldtnDt()=="") {
					pursStlSubTab.setInvldtnDt(null);
				}
			}
			int a = pursStlSubTabDao.insertPursStlSubTab(pursStlSubTabList);
			if(a>=0) {
				isSuccess=true;
				message="�޸ĳɹ���";
			}else {
				isSuccess=false;
				message="�޸�ʧ�ܣ�";
			}
		}else {
			isSuccess=false;
			message="�޸�ʧ��";
		}
		try {
			resp=BaseJson.returnRespObj("account/PursStlSnglMasTab/updatePursStlSnglMasTab", isSuccess, message, pursStlSnglMasTab);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deletePursStlSnglMasTabList(String stlSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";		
		try {
			List<String> lists = getList(stlSnglId);
			List<String> lists2 =  new ArrayList<>();
			List<String> lists3 =  new ArrayList<>();
			for(String list:lists) {
				if(pursStlSnglMasTabDao.selectPursStlSnglMasTabIsNtChk(list)==0) {
					lists2.add(list);
				}else {
					lists3.add(list);
				}
			}
			if(lists2.size()>0){
				int a = pursStlSnglMasTabDao.deletePursStlSnglMasTabList(lists2);
				if(a>=1) {
				    isSuccess=true;
				    message+="���ݺ�Ϊ��"+lists2.toString()+"�Ķ���ɾ���ɹ�!\n";
				}else {
					isSuccess=false;
					message+="���ݺ�Ϊ��"+lists2.toString()+"ɾ��ʧ�ܣ�\n";
				}
			}
			if(lists3.size()>0) {
				isSuccess=false;
				message+="���ݺ�Ϊ��"+lists3.toString()+"�Ķ����ѱ���ˣ��޷�ɾ����\n";
			}
			resp=BaseJson.returnRespObj("account/PursStlSnglMasTab/deletePursStlSnglMasTabList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	/**
	 * id����list
	 * 
	 * @param id
	 *            id(����Ѷ��ŷָ�)
	 * @return List����
	*/
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		    list.add(str[i]);
	    }
		return list;
   }

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectPursStlSnglMasTabList(Map map) {
		String resp="";
		List<PursStlSnglMasTab> list = pursStlSnglMasTabDao.selectPursStlSnglMasTabList(map);
		int count = pursStlSnglMasTabDao.selectPursStlSnglMasTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/PursStlSnglMasTab/selectPursStlSnglMasTabList", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectPursStlSnglMasTabByStlSnglId(String stlSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		PursStlSnglMasTab pursStlSnglMasTab = pursStlSnglMasTabDao.selectPursStlSnglMasTabByStlSnglId(stlSnglId);
		if(pursStlSnglMasTab!=null) {
			isSuccess=true;
			message="��ѯ�ɹ���";
		}else {
			isSuccess=false;
			message="���"+stlSnglId+"�����ڣ�";
		}
		try {
			resp=BaseJson.returnRespObj("/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId", isSuccess, message, pursStlSnglMasTab);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
