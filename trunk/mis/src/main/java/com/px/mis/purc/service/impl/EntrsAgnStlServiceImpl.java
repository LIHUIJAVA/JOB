package com.px.mis.purc.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.dao.SellComnInvSubDao;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.purc.dao.EntrsAgnDelvDao;
import com.px.mis.purc.dao.EntrsAgnDelvSubDao;
import com.px.mis.purc.dao.EntrsAgnStlDao;
import com.px.mis.purc.dao.EntrsAgnStlSubDao;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.entity.EntrsAgnStl;
import com.px.mis.purc.entity.EntrsAgnStlSub;
import com.px.mis.purc.service.EntrsAgnStlService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
/*ί�д������㵥*/
@Transactional
@Service
public class EntrsAgnStlServiceImpl extends poiTool  implements EntrsAgnStlService {
	@Autowired
	private EntrsAgnStlDao ead;//ί�д������㵥����
	@Autowired
	private EntrsAgnStlSubDao easd;//ί�д������㵥�ӱ�
	//������
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private SellComnInvDao sellComnInvDao;//���۷�Ʊ����
	@Autowired
	private SellComnInvSubDao sellComnInvSubDao;//���۷�Ʊ�ӱ�
	@Autowired
	private EntrsAgnDelvDao entrsAgnDelvDao;//ί�д�������������
	@Autowired
	private EntrsAgnDelvSubDao entrsAgnDelvSubDao;//ί�д����������ӱ�
	
	public static class zizhu{
		//����
		@JsonProperty("���㵥����")
		public String stlSnglId;//���㵥����
		@JsonProperty("���㵥����")
		public String stlSnglDt;//���㵥����
		@JsonProperty("�������ͱ���")
		public String formTypEncd;//�������ͱ���
		@JsonProperty("�������ͱ��")
		public String sellTypId;//�������ͱ��
		@JsonProperty("ҵ�����ͱ��")
		public String bizTypId;//ҵ�����ͱ��
		@JsonProperty("�ͻ����")
		public String custId;//�ͻ����
		@JsonProperty("�û����")
		public String accNum;//�û����
		@JsonProperty("�û�����")
		public String userName;//�û�����
		@JsonProperty("���ű��")
		public String deptId;//���ű��
		@JsonProperty("��������")
		public String deptName;//��������
		@JsonProperty("��������")
		public String sellOrdrId;//��������
		@JsonProperty("������")
		public String setupPers;//������
		@JsonProperty("����ʱ��  ")
		public String setupTm;//����ʱ��  
		@JsonProperty("�޸���")
		public String mdfr;//�޸���
		@JsonProperty("�޸�ʱ��")
		public String modiTm;//�޸�ʱ��
		@JsonProperty("�Ƿ�Ʊ")
		public Integer isNtBllg;//�Ƿ�Ʊ
		@JsonProperty("�Ƿ����")
		public Integer isNtChk;//�Ƿ����
		@JsonProperty("�����")
		public String chkr;//�����
		@JsonProperty("���ʱ��")
		public String chkDt;//���ʱ��
		@JsonProperty("��Ʊ���")
		public String invId;//��Ʊ���
		@JsonProperty("�ͻ�������")
		public String custOrdrNum;//�ͻ�������
		@JsonProperty("�Ƿ��˻�")
		public Integer isNtRtnGood;//�Ƿ��˻�
		@JsonProperty("��Դ�������ͱ��")
		public String toFormTypId;//��Դ�������ͱ���
		@JsonProperty("�ͻ���������")
		public String custOpnBnk;//�ͻ���������
		@JsonProperty("�����˺�")
		public String bkatNum;//�����˺�
		@JsonProperty("����λ���б��")
		public String dvlprBankId;//����λ���б��
		@JsonProperty("��ͷ��ע  ")
		public String memo;//��ע  
		@JsonProperty("���屸ע  ")
		public String memos;//��ע  
		@JsonProperty("�ͻ�����")
		public String custNm;//�ͻ�����
		@JsonProperty("ҵ����������")
		public String bizTypNm;//ҵ����������
		@JsonProperty("������������")
		public String formTypName;//������������
		@JsonProperty("������������")
		public String sellTypNm;//������������
		@JsonProperty("��Դ�������ͱ���")
		public String toFormTypEncd;//��Դ�������ͱ���
		@JsonProperty("�Ƿ�����ƾ֤")
		public Integer isNtMakeVouch;//�Ƿ�����ƾ֤
		@JsonProperty("��ƾ֤��")
		public String makVouchPers;//��ƾ֤��
		@JsonProperty("��ƾ֤ʱ��")
		public String makVouchTm;//��ƾ֤ʱ��
		@JsonProperty("���")
		public Long ordrNum;//���
		@JsonProperty("�ֿ����")
		public String whsEncd;//�ֿ����
		@JsonProperty("�������")
		public String invtyEncd;//�������
		@JsonProperty("����")
		public BigDecimal qty;//����
		@JsonProperty("��˰����")
		public BigDecimal noTaxUprc;//��˰����
		@JsonProperty("��˰���")
		public BigDecimal noTaxAmt;//��˰���
		@JsonProperty("˰��")
		public BigDecimal taxAmt;//˰��
		@JsonProperty("˰��")
		public BigDecimal taxRate;//˰��
		@JsonProperty("��˰����")
		public BigDecimal cntnTaxUprc;//��˰����
		@JsonProperty("��˰�ϼ�")
		public BigDecimal prcTaxSum;//��˰�ϼ�
		@JsonProperty("��������")
		public String prdcDt;//��������
		@JsonProperty("ʧЧ����")
		public String invldtnDt;//ʧЧ����
		@JsonProperty("����")
		public String batNum;//����
		@JsonProperty("��������")
		public String intlBat;//��������
		@JsonProperty("�Ƿ���Ʒ")
		public Integer isComplimentary;//�Ƿ���Ʒ
		@JsonProperty("�������")
		public String invtyNm;//�������
		@JsonProperty("����ͺ�")
		public String spcModel;//����ͺ�
		@JsonProperty("�������")
		public String invtyCd;//�������
		@JsonProperty("���")
		public BigDecimal bxRule;//���
		@JsonProperty("����˰��")
		public BigDecimal iptaxRate;//����˰��
		@JsonProperty("����˰��")
		public BigDecimal optaxRate;//����˰��
		@JsonProperty("��߽���")
		public BigDecimal highestPurPrice;//��߽���
		@JsonProperty("����ۼ�")
		public BigDecimal loSellPrc;//����ۼ�
		@JsonProperty("�ο��ɱ�")
		public BigDecimal refCost;//�ο��ɱ�
		@JsonProperty("�ο��ۼ�")
		public BigDecimal refSellPrc;//�ο��ۼ�
		@JsonProperty("���³ɱ�")
		public BigDecimal ltstCost;//���³ɱ�
		@JsonProperty("������λ����")
		public String measrCorpNm;//������λ����
		@JsonProperty("�ֿ�����")
		public String whsNm;//�ֿ�����
		@JsonProperty("��Ӧ������")
		public String crspdBarCd;//��Ӧ������
		@JsonProperty("δ��Ʊ����")
		public BigDecimal unBllgQty;//δ��Ʊ����
		@JsonProperty("��Դ�ӱ����")
		public Long toOrdrNum;//��Դ�ӱ����
		@JsonProperty("����")
		public BigDecimal bxQty;//����
		@JsonProperty("������")
		public Integer baoZhiQi;//������
	}
	
	//����ί�д������㵥
	@Override
	public String addEntrsAgnStl(String userId,EntrsAgnStl entrsAgnStl,List<EntrsAgnStlSub> entrsAgnStlSubList,String loginTime) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			//��ȡ������
			String number=getOrderNo.getSeqNo("WJ", userId,loginTime);
			if (ead.selectEntrsAgnStlByStlSnglId(number)!=null){
				message="���"+number+"�Ѵ��ڣ����������룡";
				isSuccess=false;
			}else {
				entrsAgnStl.setStlSnglId(number);
				ead.insertEntrsAgnStl(entrsAgnStl);
				for(EntrsAgnStlSub entrsAgnStlSub: entrsAgnStlSubList) {
					entrsAgnStlSub.setStlSnglId(entrsAgnStl.getStlSnglId());
					entrsAgnStlSub.setUnBllgQty(entrsAgnStlSub.getQty().abs());
					if (entrsAgnStlSub.getPrdcDt() == "") {
						entrsAgnStlSub.setPrdcDt(null);
					}
					if (entrsAgnStlSub.getInvldtnDt() == "") {
						entrsAgnStlSub.setInvldtnDt(null);
					}
				}
				
				easd.insertEntrsAgnStlSub(entrsAgnStlSubList);
				message="�����ɹ���";
				isSuccess=true;
			}
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/addEntrsAgnStl", isSuccess, message, entrsAgnStl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	//�޸�ί�д������㵥
	@Override
	public String editEntrsAgnStl(EntrsAgnStl entrsAgnStl,List<EntrsAgnStlSub> entrsAgnStlSubList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		easd.deleteEntrsAgnStlSubByStlSnglId(entrsAgnStl.getStlSnglId());
		ead.updateEntrsAgnStlByStlSnglId(entrsAgnStl);
		for(EntrsAgnStlSub entrsAgnStlSub: entrsAgnStlSubList) {
			entrsAgnStlSub.setStlSnglId(entrsAgnStl.getStlSnglId());
			entrsAgnStlSub.setUnBllgQty(entrsAgnStlSub.getQty().abs());
			if (entrsAgnStlSub.getPrdcDt() == "") {
				entrsAgnStlSub.setPrdcDt(null);
			}
			if (entrsAgnStlSub.getInvldtnDt() == "") {
				entrsAgnStlSub.setInvldtnDt(null);
			}
		}
		easd.insertEntrsAgnStlSub(entrsAgnStlSubList);
		message="���³ɹ���";
		
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/editEntrsAgnStl", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//����ɾ��ί�д������㵥
	@Override
	public String deleteEntrsAgnStl(String stlSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		if(ead.selectEntrsAgnStlByStlSnglId(stlSnglId)!=null) {
			ead.deleteEntrsAgnStlByStlSnglId(stlSnglId);
			isSuccess=true;
			message="ɾ���ɹ���";
		}else {
			isSuccess=false;
			message="���"+stlSnglId+"�����ڣ�";
		}
		
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/deleteEntrsAgnStl", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//��ѯί�д������㵥����
	@Override
	public String queryEntrsAgnStl(String stlSnglId) {
		// TODO Auto-generated method stub
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<EntrsAgnStlSub> entrsAgnStlSub = new ArrayList<>();
		EntrsAgnStl entrsAgnStl = ead.selectEntrsAgnStlByStlSnglId(stlSnglId);
		if(entrsAgnStl!=null) {
			entrsAgnStlSub = easd.selectEntrsAgnStlSubByStlSnglId(stlSnglId);
			message="��ѯ�ɹ���";
		}else {
			isSuccess=false;
			message="���"+stlSnglId+"�����ڣ�";
		}
		
		try {
			resp=BaseJson.returnRespObjList("purc/EntrsAgnStl/queryEntrsAgnStl", isSuccess, message, entrsAgnStl, entrsAgnStlSub);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//��ѯί�д������㵥�б�
	@Override
	public String queryEntrsAgnStlList(Map map) {
		String resp="";
		List<EntrsAgnStl> poList = ead.selectEntrsAgnStlList(map);
		int count = ead.selectEntrsAgnStlCount(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;
		
		try {
			resp=BaseJson.returnRespList("purc/EntrsAgnStl/queryEntrsAgnStl", true, "��ѯ�ɹ���", count,
					pageNo, pageSize,
					listNum, pages,poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//����ɾ��ί�д������㵥
	@Override
	public String deleteEntrsAgnStlList(String stlSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			List<String> lists = getList(stlSnglId);
			List<String> lists2 =  new ArrayList<>();
			List<String> lists3 =  new ArrayList<>();
			for(String list:lists) {
				if(ead.selectEntrsAgnStlByIsNtChk(list)==0) {
					lists2.add(list);
				}else {
					lists3.add(list);
				}
			}
			if(lists2.size()>0){
				int a = ead.deleteEntrsAgnStlList(lists2);
				if(a>=1) {
				    isSuccess=true;
				    message+="���ݺ�Ϊ��"+lists2.toString()+"�Ķ���ɾ���ɹ�!\n";
				}else {
					isSuccess=false;
					message+="���ݺ�Ϊ��"+lists2.toString()+"�Ķ���ɾ��ʧ�ܣ�\n";
				}
			}
			if(lists3.size()>0) {
				isSuccess=false;
				message+="���ݺ�Ϊ��"+lists3.toString()+"�Ķ����ѱ���ˣ��޷�ɾ����\n";
			}
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/deleteEntrsAgnStlList", isSuccess, message, null);
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
		if(StringUtils.isNotEmpty(id)) {
			if(id.contains(",")) {
				String[] str = id.split(",");
				for (int i = 0; i < str.length; i++) {
					list.add(str[i]);
				}	
			}else{
				if(StringUtils.isNotEmpty(id)) {
					list.add(id);
				}
			}
		}
		return list;
	}

	//��ӡ�������ί�д������㵥
	@Override
	public String printingEntrsAgnStlList(Map map) {
		String resp="";
		List<EntrsAgnStl> entrsAgnStlList = ead.printingEntrsAgnStlList(map);
		try {
//			
			resp=BaseJson.returnRespObjListAnno("purc/EntrsAgnStl/printingEntrsAgnStlList", true, "��ѯ�ɹ���", null,entrsAgnStlList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	//ί�д������㵥��ĺ˼�����
	@Override
	public Map<String,Object> updateEntrsAgnStlIsNtChk(String userId,EntrsAgnStl entrsAgnStl,String loginTime) {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		try {
			// ǰ�˴���ֵΪ1ʱ��˵��Ҫ������˲���
			if (entrsAgnStl.getIsNtChk() == 1) {
                message.append(updateEntrsAgnStlIsNtChkOK(userId,entrsAgnStl,loginTime).get("message"));
			} else if (entrsAgnStl.getIsNtChk() == 0) {
                message.append(updateEntrsAgnStlIsNtChkNO(entrsAgnStl).get("message"));
			} else {
				isSuccess = false;
				message.append("���״̬�����޷���ˣ�\n");
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());	
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}
	
	//���ί�д������㵥
	public Map<String,Object> updateEntrsAgnStlIsNtChkOK(String userId,EntrsAgnStl entrsAgnStl,String loginTime) throws RuntimeException {
		String message = "";
		Boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		if(ead.selectEntrsAgnStlByIsNtChk(entrsAgnStl.getStlSnglId()) == 0) {
			EntrsAgnStl entAgStl = ead.selectEntrsAgnStlByStlSnglId(entrsAgnStl.getStlSnglId());//��ѯί�д������㵥������Ϣ
			List<EntrsAgnStlSub> entrsAgnStlSubList = entAgStl.getEntrsAgnStlSub();//��ѯί�д������㵥�ӱ���Ϣ
			if(entAgStl.getSellOrdrId()!=null && !entAgStl.getSellOrdrId().equals("") ) {
				map.put("delvSnglId", entAgStl.getSellOrdrId());//��ȡ��������
				List<EntrsAgnDelvSub> entrsAgnDelvSubList=entrsAgnDelvSubDao.selectEntrsAgnDelvSubByInvBatWhs(map);//���ݷ������Ų�ѯ�������ӱ���Ϣ
				
				for(EntrsAgnStlSub entrsAgnStlSub:entrsAgnStlSubList) {
					map.put("qty", entrsAgnStlSub.getQty().abs());//����
					map.put("prcTaxSum", entrsAgnStlSub.getPrcTaxSum().abs());//��˰���
					if(entrsAgnStlSub.getToOrdrNum()!=null && entrsAgnStlSub.getToOrdrNum() != 0){
						map.put("ordrNum",entrsAgnStlSub.getToOrdrNum());//��Դ�����ӱ����
						EntrsAgnDelvSub aaa = entrsAgnDelvSubDao.selectEntDeSubToOrdrNum(entrsAgnStlSub.getToOrdrNum());//���ݷ����ӱ���Ų�ѯ�������ӱ���Ϣ
						//�޸�ί�д����������ϵ����������ۡ����
						if(entrsAgnStlSub.getQty().compareTo(aaa.getQty())==1) {
							isSuccess = false;
							message +="����Ϊ��"+entrsAgnStl.getStlSnglId() + "��ί�д������㵥�в�����ϸ�����������ڷ����������޷���ˣ�\n";
							throw new RuntimeException(message); 
						}else {
							entrsAgnDelvSubDao.updateEntrsAgnDelvSubByEntrsAgnStlSubJia(map);

						}
					}
				}
				List<EntrsAgnDelvSub> entrsAgnDelvSubLists=entrsAgnDelvSubDao.selectEntrsAgnDelvSubByInvBatWhs(map);//���ݷ������Ų�ѯ�������ӱ���Ϣ
				//��������ר�÷�Ʊ
				insertSellComnInv(userId,loginTime,entAgStl,entrsAgnStlSubList);
				//���ݴ�����ֿ⡢���Ρ�ί�д����������Ų�ѯί�д����������ӱ�����
				if(entrsAgnDelvSubList.size()>0) {
					int num=0;
					for(EntrsAgnDelvSub entrsAgnDelvSub: entrsAgnDelvSubLists) {
						if(entrsAgnDelvSub.getStlQty().compareTo(entrsAgnDelvSub.getQty())==0) {
							num++;
						}	
					}
					if(num==entrsAgnDelvSubList.size()) {
						entrsAgnDelvDao.updateEntrsAgnDelvToIsNtStlOK(entAgStl.getSellOrdrId());//�޸ķ������еĽ���״̬
					}
				}
				int a = ead.updateEntrsAgnStlIsNtChk(entrsAgnStl);
				if (a >= 1) {
						isSuccess = true;
						message += entrsAgnStl.getStlSnglId() +"ί�д������㵥��˳ɹ���\n";
				} else {
					isSuccess = false;
					message += entrsAgnStl.getStlSnglId() + "ί�д������㵥���ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}
			}else {
				isSuccess = false;
				message +="����Ϊ��"+entrsAgnStl.getStlSnglId() + "��ί�д������㵥û�ж�Ӧ�ķ��������޷���ˣ�\n";
				throw new RuntimeException(message);
			}
		}else {
			isSuccess=false;
			message += entrsAgnStl.getStlSnglId()+ "�õ�������ˣ�����Ҫ�ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
		
	}
	//����ί�д������㵥
	public Map<String,Object> updateEntrsAgnStlIsNtChkNO(EntrsAgnStl entrsAgnStl) throws RuntimeException {
		String message = "";
		Boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		if (ead.selectEntrsAgnStlByIsNtChk(entrsAgnStl.getStlSnglId()) == 1) {
			EntrsAgnStl entAgStl = ead.selectEntrsAgnStlByStlSnglId(entrsAgnStl.getStlSnglId());//��ѯί�д������㵥������Ϣ
			List<EntrsAgnStlSub> entrsAgnStlSubList = entAgStl.getEntrsAgnStlSub();//��ѯί�д������㵥�ӱ���Ϣ
			if(entAgStl.getSellOrdrId()!=null && !entAgStl.getSellOrdrId().equals("") ) {
				for(EntrsAgnStlSub entrsAgnStlSub:entrsAgnStlSubList) {
					map.put("qty", entrsAgnStlSub.getQty().abs());//����
					map.put("prcTaxSum", entrsAgnStlSub.getPrcTaxSum().abs());//��˰���
					if(entrsAgnStlSub.getToOrdrNum()!=null && entrsAgnStlSub.getToOrdrNum() != 0){
						map.put("ordrNum",entrsAgnStlSub.getToOrdrNum());//��Դ�����ӱ����
						//���ݽ��㵥��Դ�ӱ���Ų�ѯ�������ӱ���Ϣ
						EntrsAgnDelvSub entrsAgnDelvSub = entrsAgnDelvSubDao.selectEntDeSubToOrdrNum(entrsAgnStlSub.getToOrdrNum());
						if(entrsAgnStlSub.getQty().compareTo(entrsAgnDelvSub.getStlQty())==0 || 
								entrsAgnStlSub.getQty().compareTo(entrsAgnDelvSub.getStlQty())==1) {
							//�޸�ί�д����������ϵ����������ۡ����
							entrsAgnDelvSubDao.updateEntrsAgnDelvSubByEntrsAgnStlSubJian(map);
						}else {
							isSuccess=false;
							message += "���ݺ�Ϊ"+entrsAgnStl.getStlSnglId()+ "���㵥�д����"+entrsAgnStlSub.getInvtyEncd()+"�����Σ���"+entrsAgnStlSub.getIntlBat()+"���Ľ����������ڷ����������޷�����\n";
							throw new RuntimeException(message);
						}
					}
					
				}
				//�޸�ί�д����������Ľ���״̬
				entrsAgnDelvDao.updateEntrsAgnDelvToIsNtStlNO(entAgStl.getSellOrdrId());//�޸ķ������еĽ���״̬
				//����ί�д������㵥����ɾ����Ӧ��Ʊ��Ϣ
				sellComnInvDao.deleteSellComnInvBySellInvNum(entAgStl.getStlSnglId());
				
				int a = ead.updateEntrsAgnStlIsNtChk(entrsAgnStl);
				if (a >= 1) {
						isSuccess = true;
						message += entrsAgnStl.getStlSnglId() +"ί�д������㵥����ɹ���\n";
				} else {
					isSuccess = false;
					message += entrsAgnStl.getStlSnglId() + "ί�д������㵥����ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}
			}else {
				isSuccess = false;
				message +="����Ϊ��"+entrsAgnStl.getStlSnglId() + "��ί�д������㵥û�ж�Ӧ�ķ��������޷�����\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += entrsAgnStl.getStlSnglId() + "�õ���δ��ˣ�������˸õ��ݣ�\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}
	
	//���ί�д������㵥��ʱ����������ר�÷�Ʊ
	private Map<String,Object> insertSellComnInv(String userId,String loginTime,EntrsAgnStl entrsAgnStl,List<EntrsAgnStlSub> entrsAgnStlSubList) {
		Boolean isSuccess=true;
		String message = "";
		Map<String,Object> map = new HashMap<>();
		
		SellComnInv sellComnInv = new SellComnInv();//����ר�÷�Ʊ����
		List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();//����ר�÷�Ʊ�ӱ���
		try {
			BeanUtils.copyProperties(sellComnInv, entrsAgnStl);//��ί�д��������Ƹ�����ר�÷�Ʊ
			// ��ȡ������
			String number = getOrderNo.getSeqNo("XSFP", userId,loginTime);
			if(sellComnInvDao.selectSellComnInvBySellInvNum(number) != null) {
				isSuccess=false;
				message+="���۷�Ʊ��"+number+"�Ѵ��ڣ��Զ��������۷�Ʊʧ�ܣ�";
			}else {
				sellComnInv.setSellInvNum(number);//�Զ��������۷�Ʊ����
				sellComnInv.setBllgDt(CommonUtil.getLoginTime(loginTime));//���ɷ�Ʊʱ��Ĭ�ϵ��ڵ�¼ʱ��
				sellComnInv.setInvTyp("����ר�÷�Ʊ");//��Ʊ����
				sellComnInv.setBizTypId("1");//ҵ�����ͣ�ί�д���
				if (entrsAgnStl.getBizTypId().equals("1")) {
					sellComnInv.setRecvSendCateId("7");// �շ����ͱ�ţ���������
				}
				if (entrsAgnStl.getBizTypId().equals("2")) {
					sellComnInv.setRecvSendCateId("6");// �շ����ͱ�ţ���������
				}
				sellComnInv.setSetupPers(entrsAgnStl.getChkr());//������--Ĭ�ϵ��������
				sellComnInv.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//����ʱ��
				sellComnInv.setSellSnglNum(entrsAgnStl.getStlSnglId());//ί�д������㵥����
				sellComnInv.setFormTypEncd("021");//�������ͱ���
				sellComnInv.setToFormTypEncd(entrsAgnStl.getFormTypEncd());//��Դ��������
				
				for(EntrsAgnStlSub entrsAgnStlSub:entrsAgnStlSubList) {
					SellComnInvSub sellComnInvSub = new SellComnInvSub();//����ר�÷�Ʊ�ӱ�
					BeanUtils.copyProperties(sellComnInvSub, entrsAgnStlSub);//��ί�д������㵥�ӱ��Ƹ�����ר�÷�Ʊ�ӱ�
					sellComnInvSub.setSellInvNum(sellComnInv.getSellInvNum());//������ר�÷�Ʊ�������set���ӱ��е�����ר�÷�Ʊ����
					sellComnInvSub.setSellSnglNums(entrsAgnStlSub.getStlSnglId());//��������
					sellComnInvSub.setSellSnglSubId(entrsAgnStlSub.getOrdrNum());//��Դ�����ӱ����
					
					sellComnInvSubList.add(sellComnInvSub);
				}
				sellComnInvDao.insertSellComnInv(sellComnInv);//��������ר�÷�Ʊ����
				sellComnInvSubDao.insertSellComnInvSubList(sellComnInvSubList);//��������ר�÷�Ʊ�ӱ�
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,EntrsAgnStl> entrsAgnStlMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,EntrsAgnStl> entry : entrsAgnStlMap.entrySet()) {
			if(ead.selectStlSnglId(entry.getValue().getStlSnglId())!=null) {
				isSuccess=false;
				message="ί�д������㵥�в��ֶ�������Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}else {
				ead.insertEntrsAgnStlUpload(entry.getValue());
				for(EntrsAgnStlSub entrsAgnStlSub: entry.getValue().getEntrsAgnStlSub()) {
					entrsAgnStlSub.setStlSnglId(entry.getValue().getStlSnglId());
				}
				easd.insertEntrsAgnStlSub(entry.getValue().getEntrsAgnStlSub());
				isSuccess=true;
				message="ί�д������㵥����ɹ���";
			}
		}
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnStl/uploadEntrsAgnStlFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//����excle
	private Map<String, EntrsAgnStl> uploadScoreInfo(MultipartFile file) {
		Map<String, EntrsAgnStl> temp = new HashMap<>();
		int j = 0;
		try {
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
				String orderNo = GetCellData(r, "���㵥��");
				// ����ʵ����
				EntrsAgnStl entrsAgnStl = new EntrsAgnStl();
				if (temp.containsKey(orderNo)) {
					entrsAgnStl = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				entrsAgnStl.setStlSnglId(orderNo);// �ɹ���������
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					entrsAgnStl.setStlSnglDt(null);
				} else {
					entrsAgnStl.setStlSnglDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// �ɹ���������
				}
				entrsAgnStl.setCustId(GetCellData(r, "�ͻ�����"));// ��Ӧ�̱���
				entrsAgnStl.setDeptId(GetCellData(r, "���ű���"));// ���ű���
				entrsAgnStl.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				entrsAgnStl.setUserName(GetCellData(r, "ҵ �� Ա"));// �û�����
				entrsAgnStl.setCustOrdrNum(GetCellData(r, "�ͻ�������"));;// ��Ӧ�̶�����
				entrsAgnStl.setSellTypId("2");// �������ͱ���
				entrsAgnStl.setBizTypId("1");//ҵ������
				entrsAgnStl.setFormTypEncd("025");// �������ͱ���
				entrsAgnStl.setSellOrdrId(GetCellData(r, "��������"));//��������
				entrsAgnStl.setSetupPers(GetCellData(r, "�Ƶ���"));// ������
				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					entrsAgnStl.setSetupTm(null);
				} else {
					entrsAgnStl.setSetupTm(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				entrsAgnStl.setIsNtChk(0);// �Ƿ����
				entrsAgnStl.setIsNtBllg(0);//�Ƿ�Ʊ
				if (GetCellData(r, "�˻���ʶ") != null && GetCellData(r, "�˻���ʶ").equals("��")) {
					entrsAgnStl.setIsNtRtnGood(0); //�Ƿ��˻�
				} else if (GetCellData(r, "�˻���ʶ") != null && GetCellData(r, "�˻���ʶ").equals("��")) {
					entrsAgnStl.setIsNtRtnGood(1); //�Ƿ��˻�
				}
				if (GetCellData(r, "�˻���ʶ") != null && GetCellData(r, "�˻���ʶ").equals("��")) {
					entrsAgnStl.setFormTypEncd("023");// �������ͱ���
				} else if (GetCellData(r, "�˻���ʶ") != null && GetCellData(r, "�˻���ʶ").equals("��")) {
					entrsAgnStl.setFormTypEncd("024");// �������ͱ���
				}
				entrsAgnStl.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					entrsAgnStl.setChkDt(null);
				} else {
					entrsAgnStl.setChkDt(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				entrsAgnStl.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					entrsAgnStl.setModiTm(null);
				} else {
					entrsAgnStl.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				entrsAgnStl.setMemo(GetCellData(r, "�� ע"));// ��ע
				entrsAgnStl.setIsNtMakeVouch(0);//�Ƿ�����ƾ֤
				entrsAgnStl.setMakVouchPers(GetCellData(r, "��ƾ֤��"));//��ƾ֤��
				entrsAgnStl.setMakVouchTm(GetCellData(r, "��ƾ֤ʱ��"));//��ƾ֤��
				List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStl.getEntrsAgnStlSub();//ί�д������㵥�ӱ�
				if (entrsAgnStlSubList == null) {
					entrsAgnStlSubList = new ArrayList<>();
				}
				EntrsAgnStlSub entrsAgnStlSub = new EntrsAgnStlSub();
				entrsAgnStlSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));
				entrsAgnStlSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				entrsAgnStlSub.setWhsEncd(GetCellData(r, "�ֿ���"));// �ֿ����
				entrsAgnStlSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				entrsAgnStlSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				entrsAgnStlSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				entrsAgnStlSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				entrsAgnStlSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8));// 8��ʾС��λ�� //��˰���
				entrsAgnStlSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				entrsAgnStlSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
				entrsAgnStlSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰�ʣ�%��"), 8));// 8��ʾС��λ�� //˰��
				entrsAgnStlSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8));// 8��ʾС��λ�� //δ��Ʊ����
				
				entrsAgnStlSub.setIntlBat(GetCellData(r, "��������")); // ��������
				entrsAgnStlSub.setBatNum(GetCellData(r, "����")); // '����',
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					entrsAgnStlSub.setPrdcDt(null);
				} else {
					entrsAgnStlSub.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// �ƻ�����ʱ��
				}
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					entrsAgnStlSub.setInvldtnDt(null);
				} else {
					entrsAgnStlSub.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// �ƻ�����ʱ��
				}
				if (GetCellData(r, "��Ʒ") != null && GetCellData(r, "��Ʒ").equals("��")) {
					entrsAgnStlSub.setIsComplimentary(0); //�Ƿ���Ʒ
				} else if (GetCellData(r, "��Ʒ") != null && GetCellData(r, "��Ʒ").equals("��")) {
					entrsAgnStlSub.setIsComplimentary(1); //�Ƿ���Ʒ
				}
				entrsAgnStlSub.setBaoZhiQi(new Double(GetCellData(r, "������")).intValue()); // '������',
				entrsAgnStlSub.setMemo(GetCellData(r, "���屸ע")); //'���屸ע',
				entrsAgnStlSub.setSellOrdrIds(GetCellData(r, "��������")); //��������
				entrsAgnStlSub.setToOrdrNum(Long.parseLong(GetCellData(r, "��Դ�ӱ����")));
				entrsAgnStlSubList.add(entrsAgnStlSub);
				entrsAgnStl.setEntrsAgnStlSub(entrsAgnStlSubList);
				temp.put(orderNo, entrsAgnStl);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}
	//��ҳ+�����ѯί�д������㵥�б�
		@Override
		public String queryEntrsAgnStlListOrderBy(Map map) {
			String resp="";
			
			List<?> poList;	
			
			if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
				map.put("sort","eas.stl_sngl_dt");
				map.put("sortOrder","desc");
			}
				poList = ead.selectEntrsAgnStlListOrderBy(map);
				 Map tableSums = ead.selectEntrsAgnStlListSums(map);
					if (null!=tableSums) {
						DecimalFormat df = new DecimalFormat("#,##0.00");
						for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
							String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
							entry.setValue(s);
						}
					}
			
		
			int count = ead.selectEntrsAgnStlCount(map);
			
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;
			
			try {
				resp=BaseJson.returnRespList("purc/EntrsAgnStl/queryPayApplFormListOrderBy", true, "��ѯ�ɹ���", count,
						pageNo, pageSize,
						listNum, pages,poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resp;
		}
	

}
