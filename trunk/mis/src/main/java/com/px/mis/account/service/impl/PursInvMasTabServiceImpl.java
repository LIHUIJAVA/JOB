package com.px.mis.account.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.PursInvMasTabDao;
import com.px.mis.account.dao.PursInvSubTabDao;
import com.px.mis.account.entity.PursInvMasTab;
import com.px.mis.account.entity.PursInvSubTab;
import com.px.mis.account.service.PursInvMasTabService;
import com.px.mis.account.service.PursInvSubTabService;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.IntoWhsSubDao;
import com.px.mis.purc.dao.ProvrDocDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
//�ɹ�ר�÷�Ʊ����
@Service
@Transactional
public class PursInvMasTabServiceImpl extends poiTool implements PursInvMasTabService {
	
	@Autowired
	private PursInvMasTabDao pursInvMasTabDao;
	@Autowired
	private PursInvSubTabDao pursInvSubTabDao;
	@Autowired
	private IntoWhsSubDao intoWhsSubDao;//�ɹ���ⵥ�ӱ�
	@Autowired
	private IntoWhsDao intoWhsDao;//�ɹ���ⵥ�ӱ�
	@Autowired
	private ProvrDocDao provrDocDao;
	@Autowired
	private PursInvSubTabService pursInvSubTabService;
	
	//�����ɹ���Ʊ
	@Override
	public ObjectNode insertPursInvMasTab(PursInvMasTab pursInvMasTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pursInvNum = pursInvMasTab.getPursInvNum();
		if(pursInvNum==null || pursInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,�ɹ���Ʊ�Ų���Ϊ��");
		}else if(pursInvMasTabDao.selectPursInvMasTabById(pursInvNum)!=null){
			on.put("isSuccess", false);
			on.put("message", "�ɹ���Ʊ��"+pursInvNum+"�Ѵ��ڣ�����ʧ�ܣ�");
		}else if(provrDocDao.selectProvrDocByProvrId(pursInvMasTab.getProvrId())==null) {
			on.put("isSuccess", false);
			on.put("message", "�ù�Ӧ��"+pursInvMasTab.getProvrId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int InsertResult = pursInvMasTabDao.insertPursInvMasTab(pursInvMasTab);
			if(InsertResult==1) {
				List<PursInvSubTab> pursList = pursInvMasTab.getPursList();
				for(PursInvSubTab pursInvSubTab:pursList) {
					pursInvSubTab.setPursInvNum(pursInvNum);
					on = pursInvSubTabService.insertPursInvSubTab(pursInvSubTab);
				}
			}else {
				on.put("isSuccess", false);
				on.put("message", "�ɹ���Ʊ����ʧ��");
			}
		}
		return on;
	}

	//�޸Ĳɹ���Ʊ
	@Override
	public ObjectNode updatePursInvMasTabById(PursInvMasTab pursInvMasTab,List<PursInvSubTab> pursInvSubTabList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pursInvNum = pursInvMasTab.getPursInvNum();
		if(pursInvNum==null || pursInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,�ɹ���Ʊ�Ų���Ϊ��");
		}else if(pursInvMasTabDao.selectPursInvMasTabById(pursInvNum)==null){
			on.put("isSuccess", false);
			on.put("message", "�����вɹ���Ʊ��"+pursInvNum+"�����ڣ�����ʧ�ܣ�");
		}else if(provrDocDao.selectProvrDocByProvrId(pursInvMasTab.getProvrId())==null) {
			on.put("isSuccess", false);
			on.put("message", "�����й�Ӧ�̱��"+pursInvMasTab.getProvrId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int updateResult = pursInvMasTabDao.updatePursInvMasTabById(pursInvMasTab);
			int deleteResult = pursInvSubTabService.deletePursInvSubTabByPursInvNum(pursInvMasTab.getPursInvNum());
			if(updateResult==1 && deleteResult >=1) {
				for(PursInvSubTab pursInvSubTab:pursInvSubTabList) {
					pursInvSubTab.setPursInvNum(pursInvNum);
					pursInvSubTabDao.insertPursInvSubTab(pursInvSubTab);
				}
				on.put("isSuccess", true);
				on.put("message", "�ɹ���Ʊ���³ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "�ɹ���Ʊ����ʧ��");
			}
		}
			
		return on;
	}

	//����ɾ���ɹ���Ʊ
	@Override
	public ObjectNode deletePursInvMasTabByPursInvNum(String pursInvNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer deleteResult = pursInvSubTabService.deletePursInvSubTabByPursInvNum(pursInvNum);
		deleteResult = pursInvMasTabDao.deletePursInvMasTabById(pursInvNum);
		if(deleteResult==1) {
			on.put("isSuccess", true);
			on.put("message", "ɾ���ɹ�");
		}else if(deleteResult==0){
			on.put("isSuccess", true);
			on.put("message", "û��Ҫɾ��������");		
		}else {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ��");
		}
			
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public PursInvMasTab selectPursInvMasTabByPursInvNum(String pursInvNum) {
		PursInvMasTab pursInvMasTab = pursInvMasTabDao.selectPursInvMasTabById(pursInvNum);
		return pursInvMasTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectPursInvMasTabList(Map map) {
		String resp="";
		List<PursInvMasTab> list = pursInvMasTabDao.selectPursInvMasTabList(map);
		int count = pursInvMasTabDao.selectPursInvMasTabCount(map);
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/pursInvMasTab/selectPursInvMasTab", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//����ɾ���ɹ���Ʊ
	@Override
	public String deletePursInvMasTabList(String pursInvNum) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<String> list = getList(pursInvNum);
		if(pursInvMasTabDao.selectPursInvMasTabIsNtChk(pursInvNum)==0) {
			int a = pursInvMasTabDao.deletePursInvMasTabList(list);
			if(a>=1) {
			    isSuccess=true;
			    message="ɾ���ɹ���";
			}else {
				isSuccess=false;
				message="ɾ��ʧ�ܣ�";
			}
		}else {
			isSuccess=false;
			message="���ݺ�Ϊ��"+pursInvNum+"�ķ�Ʊ�ѱ���ˣ��޷�ɾ����";
		}		
		try {
			resp=BaseJson.returnRespObj("/account/pursInvMasTab/deletePursInvMasTabList", isSuccess, message, null);
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
	public String updatePursInvMasTabIsNtChkList(List<PursInvMasTab> pursInvMasTabList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		for(PursInvMasTab pursInvMasTab:pursInvMasTabList) {
			if(pursInvMasTab.getIsNtChk()==1) {
				if(pursInvMasTabDao.selectPursInvMasTabIsNtChk(pursInvMasTab.getPursInvNum())==0) {
					//��ѯ�ɹ���ⵥ��
					String intoWhsSnglId = pursInvMasTabDao.selectIntoWhsSnglIdByPursInvMasTab(pursInvMasTab.getPursInvNum());
					if(intoWhsSnglId!=null) {
						List<String> intoWhsIdList = getintoWhsList(intoWhsSnglId);//��ȡ��ⵥ���
						for(String intoWhsId : intoWhsIdList) {
							//�޸��Ƿ�Ʊ״̬
							intoWhsDao.updateIntoWhsIsNtBllgOK(intoWhsId);
						}
					}
					//�޸����״̬
					int a = pursInvMasTabDao.updatePursInvMasTabIsNtChk(pursInvMasTab);
					if(a>=1) {
					    isSuccess=true;
					    message+="���ݺ�Ϊ"+pursInvMasTab.getPursInvNum()+ "�Ĳɹ�ר�÷�Ʊ��˳ɹ���\n";
					}else {
						isSuccess=false;
						message +="���ݺ�Ϊ"+pursInvMasTab.getPursInvNum()+ "�Ĳɹ�ר�÷�Ʊ���ʧ�ܣ�\n";
					}
				}else if(pursInvMasTabDao.selectPursInvMasTabIsNtChk(pursInvMasTab.getPursInvNum())==1){
					isSuccess=false;
					message += "���ݺ�Ϊ"+pursInvMasTab.getPursInvNum()+ "�Ĳɹ�ר�÷�Ʊ����ˣ�����Ҫ�ظ����\n";
				}
			}else if(pursInvMasTab.getIsNtChk()==0) {
				if(pursInvMasTabDao.selectPursInvMasTabIsNtBookEntry(pursInvMasTab.getPursInvNum())==0){
					if(pursInvMasTabDao.selectPursInvMasTabIsNtChk(pursInvMasTab.getPursInvNum())==1) {
						//��ѯ�ɹ���ⵥ��
						String intoWhsSnglId = pursInvMasTabDao.selectIntoWhsSnglIdByPursInvMasTab(pursInvMasTab.getPursInvNum());
						if(intoWhsSnglId != null) {
							List<String> intoWhsIdList = getintoWhsList(intoWhsSnglId);//��ȡ��ⵥ���
							for(String intoWhsId : intoWhsIdList) {
								//�޸��Ƿ�Ʊ״̬
								intoWhsDao.updateIntoWhsIsNtBllgNO(intoWhsId);
							}
						}
						int a = pursInvMasTabDao.updatePursInvMasTabIsNtChk(pursInvMasTab);
						if(a>=1){
						    isSuccess=true;
						    message+="���ݺ�Ϊ"+pursInvMasTab.getPursInvNum()+ "�Ĳɹ�ר�÷�Ʊ����ɹ���\n";
						}else {
							isSuccess=false;
							message +="���ݺ�Ϊ"+pursInvMasTab.getPursInvNum()+ "�Ĳɹ�ר�÷�Ʊ����ʧ�ܣ�\n";
						}
					}else{
						isSuccess=false;
						message += "���ݺ�Ϊ"+pursInvMasTab.getPursInvNum()+ "�Ĳɹ�ר�÷�Ʊδ��ˣ�������˸õ���\n";
					}
				}else {
					isSuccess=false;
					message += "���ݺ�Ϊ"+pursInvMasTab.getPursInvNum()+ "�Ĳɹ�ר�÷�Ʊ�Ѽ����޷�����\n";
				}
				
			}
		}
	    try {
			resp=BaseJson.returnRespObj("/account/pursInvMasTab/updatePursInvMasTabIsNtChk", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
	 */
	public List<String> getintoWhsList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}

	//�ϲ��ɹ���ⵥ����ɹ���Ʊ��
	@Override
	public String selectPursInvMasTabBingList(List<IntoWhs> intoWhsList) {
		String resp="";
		String message="";
		Boolean isSuccess=true;
		//ʵ����һ���µĶ���    �ɹ���ͨ��Ʊ����
		PursInvMasTab pursInvMasTab = new PursInvMasTab();
		List<PursInvSubTab> pursInvSubTabList = new ArrayList<>();
		Map<String,Object> map  = new HashMap<>();
		//��ȡList�����е�һ����Ӧ��������map��
		IntoWhs intoWhs = intoWhsList.get(0);
		//����Ӧ�̱��룬���ű��룬ҵ��Ա����ƴ������
		String str1 = intoWhs.getProvrId()+intoWhs.getDeptId()+intoWhs.getAccNum();
		map.put("uniqInd", str1);
		String intoWhsId="";
		for(IntoWhs intoWh: intoWhsList) {
			intoWhsId+=intoWh.getIntoWhsSnglId()+",";
			String str2 = intoWh.getProvrId()+intoWh.getDeptId()+intoWh.getAccNum();
			if(str2.equals(map.get("uniqInd").toString())==false) {
				isSuccess=false;
				message="����ѡ����ͬ��Ӧ�̡����š�ҵ��Ա�ĵ��ݽ���������������ѡ��";
			}else if(str2.equals(map.get("uniqInd").toString())==true){
				try {
					BeanUtils.copyProperties(pursInvMasTab,intoWh);//���ɹ���������������ɹ�ר�÷�Ʊ����
					pursInvMasTab.setCrspdIntoWhsSnglNum(intoWhsId.substring(0,intoWhsId.length()-1));
					//��������������ѯ��ص��ӱ���Ϣ
					List<IntoWhsSub> intoWhsSubList = intoWhsSubDao.selectIntoWhsSubByIntoWhsSnglId(intoWh.getIntoWhsSnglId());
					
					for(IntoWhsSub intoWhsSub:intoWhsSubList) {
						PursInvSubTab pursInvSubTab = new PursInvSubTab();
						BeanUtils.copyProperties(pursInvSubTab,intoWhsSub);//���ɹ���ⵥ�ӱ������ɹ�ר�÷�Ʊ�ӱ�
						pursInvSubTabList.add(pursInvSubTab);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException("��ѯ�������������ٽ���������");
				}
				
				isSuccess=true;
				message="�����ɹ���";
			}
			
		}
		pursInvMasTab.setPursList(pursInvSubTabList);
		try {
			resp=BaseJson.returnRespObj("/account/pursInvMasTab/selectPursInvMasTabBingList", isSuccess, message,pursInvMasTab);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//����
	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, PursInvMasTab> pursInvMasTabMap = uploadScoreInfo(file);

		for (Map.Entry<String, PursInvMasTab> entry : pursInvMasTabMap.entrySet()) {
			PursInvMasTab pursInvMasTab =pursInvMasTabDao.selectPursInvMasTabById(entry.getValue().getPursInvNum());
			if (pursInvMasTab != null) {
				isSuccess = false;
				message = "�ɹ���Ʊ�в��ֵ��ݱ���Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}
			pursInvMasTabDao.insertPursInvMasTabUpload(entry.getValue());
			for (PursInvSubTab pursInvSubTab : entry.getValue().getPursList()) {
				pursInvSubTab.setPursInvNum(entry.getValue().getPursInvNum());
				
			}
			pursInvSubTabDao.insertPursInvSubTabUpload(entry.getValue().getPursList());
			isSuccess = true;
			message = "�ɹ���Ʊ����ɹ���";
		}

		try {
			resp = BaseJson.returnRespObj("purc/pursInvMasTab/uploadPursInvMasTabFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, PursInvMasTab> uploadScoreInfo(MultipartFile file) {
		Map<String, PursInvMasTab> temp = new HashMap<>();
		int j = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			InputStream fileIn = file.getInputStream();
			// ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			// ��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			// ��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			// ��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			// ���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "�ɹ���Ʊ����");
				// ����ʵ����
				PursInvMasTab pursInvMasTab = new PursInvMasTab();
				if (temp.containsKey(orderNo)) {
					pursInvMasTab = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				pursInvMasTab.setPursInvNum(orderNo);//�ɹ���Ʊ����
				if (GetCellData(r, "��Ʊ����") == null || GetCellData(r, "��Ʊ����").equals("")) {
					pursInvMasTab.setBllgDt(null);
				} else {
					pursInvMasTab.setBllgDt(GetCellData(r, "��Ʊ����").replaceAll("[^0-9:-]", " "));//��Ʊ����
				}
				pursInvMasTab.setAccNum(GetCellData(r, "ҵ��Ա����")); // ҵ��Ա����', varchar(200
				pursInvMasTab.setUserName(GetCellData(r, "��Ӧ�̱���")); // '��Ӧ�̱���', varchar(200
				pursInvMasTab.setPursTypId(GetCellData(r, "�ɹ����ͱ���")); // '�ɹ����ͱ���', varchar(200
				pursInvMasTab.setDeptId(GetCellData(r, "���ű���")); // '���ű���', varchar(200
				pursInvMasTab.setFormTypEncd(GetCellData(r, "�������ͱ���"));// �������ͱ���

				pursInvMasTab.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue()); // '�Ƿ����', int(11
				pursInvMasTab.setChkr(GetCellData(r, "�����")); // '�����', varchar(200
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					pursInvMasTab.setChkTm(null);
				} else {
					pursInvMasTab.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				
				pursInvMasTab.setIsNtBookEntry(new Double(GetCellData(r,"�Ƿ����")).intValue()); //�Ƿ����
				pursInvMasTab.setBookEntryPers(GetCellData(r,"������")); // ������',
				if(GetCellData(r,"����ʱ��")==null || GetCellData(r,"����ʱ��").equals("")) {
					pursInvMasTab.setBookEntryTm(null); 
				}else {
					pursInvMasTab.setBookEntryTm(GetCellData(r,"����ʱ��").replaceAll("[^0-9:-]"," "));//����ʱ�� 
				}
				 
				pursInvMasTab.setSetupPers(GetCellData(r, "������")); // '������', varchar(200
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					pursInvMasTab.setSetupTm(null);
				} else {
					pursInvMasTab.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				pursInvMasTab.setMdfr(GetCellData(r, "�޸���")); // '�޸���', varchar(200
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					pursInvMasTab.setModiTm(null);
				} else {
					pursInvMasTab.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
//				pursInvMasTab.setPursOrdrId(GetCellData(r, "�ɹ���������"));
				pursInvMasTab.setCrspdIntoWhsSnglNum(GetCellData(r, "�ɹ���ⵥ����"));
				pursInvMasTab.setMemo(GetCellData(r, "����ע")); // '��ע', varchar(2000
				List<PursInvSubTab> pursInvSubTabList = pursInvMasTab.getPursList();//�ɹ���Ʊ�ӱ�
				if (pursInvSubTabList == null) {
					pursInvSubTabList = new ArrayList<>();
				}
				PursInvSubTab pursInvSubTab = new PursInvSubTab();

				pursInvSubTab.setWhsEncd(GetCellData(r, "�ֿ����")); // '�ֿ����',
				pursInvSubTab.setInvtyEncd(GetCellData(r, "�������")); // '�������',
				pursInvSubTab.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				pursInvSubTab.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				pursInvSubTab.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));//���
				pursInvSubTab.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				pursInvSubTab.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8)); // '��˰�ϼ�',
				pursInvSubTab.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				pursInvSubTab.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // '��˰���',
				pursInvSubTab.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				pursInvSubTab.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8)); // '˰��',
				pursInvSubTab.setIntlBat(GetCellData(r, "��������")); // ��������
				pursInvSubTab.setBatNum(GetCellData(r, "����")); // '����',
				pursInvSubTab.setMemo(GetCellData(r, "�ӱ�ע")); // '��ע',
				pursInvSubTabList.add(pursInvSubTab);//�ӱ�
				pursInvMasTab.setPursList(pursInvSubTabList);
				temp.put(orderNo, pursInvMasTab);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	@Override
	public String upLoadPursInvMasTabList(Map map) {
		String resp = "";
		List<PursInvMasTab> poList = pursInvMasTabDao.printingPursInvMasTabList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/pursInvMasTab/printingPursInvMasTabList", true, "��ѯ�ɹ���", null, poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}


}
