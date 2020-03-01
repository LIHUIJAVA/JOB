package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.purc.dao.EntrsAgnAdjDao;
import com.px.mis.purc.dao.EntrsAgnAdjSubDao;
import com.px.mis.purc.dao.EntrsAgnDelvDao;
import com.px.mis.purc.entity.EntrsAgnAdj;
import com.px.mis.purc.entity.EntrsAgnAdjSub;
import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.service.EntrsAgnAdjService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
/*ί�д���������*/
@Transactional
@Service
public class EntrsAgnAdjServiceImpl implements EntrsAgnAdjService {
	@Autowired
	private EntrsAgnAdjDao eaad;//ί�д�����������
	@Autowired
	private EntrsAgnAdjSubDao eaasd;//ί�д�����������
	@Autowired
	private EntrsAgnDelvDao eadd;//ί�д�����������
	//������
	@Autowired
	private GetOrderNo getOrderNo;

	//ί�д���������ҳ���ѯ�ӿ�
	@Override
	public String queryEntrsAgnAdjOrEntrsAgn(Map map) {
		String resp="";
		try {
			List<EntrsAgnDelv> list =  new ArrayList<>();
			List<EntrsAgnDelv> entrsAgnDelvList = eadd.selectEntrsAgnDelvByEntrsAgnAdjList(map);//ί�д���������
			for(EntrsAgnDelv entrsAgnDelv:entrsAgnDelvList) {
				//����ί�д����������Ų�ѯ�Ƿ����ί�д���������
				EntrsAgnAdj  entrsAgnAdj = eaad.selectAdjSnglIdByDelvSnglId(entrsAgnDelv.getDelvSnglId());
				//��������ڲ�ѯί�д���������   ������ڲ�ѯί�д���������
				if(entrsAgnAdj==null) {
					list.add(entrsAgnDelv);
				}else {
					EntrsAgnDelv delv = new EntrsAgnDelv();
					BeanUtils.copyProperties(delv, entrsAgnAdj);
					List<EntrsAgnAdjSub> adj = entrsAgnAdj.getEntrsAgnAdjSub();//ί�д�����������
					List<EntrsAgnDelvSub> agns = new ArrayList<>();//ί�д�����������
					for(EntrsAgnAdjSub agn :adj) {
						EntrsAgnDelvSub delvs = new EntrsAgnDelvSub();
						BeanUtils.copyProperties(delvs, agn);
						agns.add(delvs);
					}
					delv.setEntrsAgnDelvSub(agns);
					list.add(delv);
				}
			}
			resp=BaseJson.returnRespObjList("purc/EntrsAgnAdj/queryEntrsAgnAdjOrEntrsAgn", true, "��ѯ�ɹ���", null,list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	//ί�д����������е�������۹���
	@Override
	public String addEntrsAgnAdj(String userId,List<EntrsAgnAdj> entrsAgnAdjList,String loginTime) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			//ί�д����������ӱ�
			List<EntrsAgnAdjSub> EntrsAgnAdjSubList = new ArrayList<EntrsAgnAdjSub>();
			for(EntrsAgnAdj entrsAgnAdj:entrsAgnAdjList) {
				//����ί�д�������������ж��Ƿ���ί�д���������
				if(eaad.selectAdjSnglIdByDelvSnglId(entrsAgnAdj.getDelvSnglId())==null){
					//��ѯί�д��������������״̬
					if(eadd.selectEntrsAgnDelvIsNtChk(entrsAgnAdj.getDelvSnglId())==1) {
						//��ѯί�д����������Ľ���״̬
						if(eadd.selectEntrsAgnDelvIsNtStl(entrsAgnAdj.getDelvSnglId())==0) {
							//��ȡ������
							String number=getOrderNo.getSeqNo("WTTZ", userId, loginTime);
							if (eaad.selectEntrsAgnAdjByAdjSnglId(number)!=null) {
								message+="���"+number+"�Ѵ��ڣ����������룡";
								isSuccess=false;
							}else {
								//���Զ����ɵ�ί�д�����������set��ί�д�����������
								entrsAgnAdj.setAdjSnglId(number);
								//����ί�д�������������
								int a = eaad.insertEntrsAgnAdj(entrsAgnAdj);
								//��ȡί�д����ӱ�
								EntrsAgnAdjSubList = entrsAgnAdj.getEntrsAgnAdjSub();
								for(EntrsAgnAdjSub entrsAgnAdjSub:EntrsAgnAdjSubList) {
									BigDecimal noTaxUprc = entrsAgnAdjSub.getNoTaxUprc();//��˰����
									BigDecimal qty = entrsAgnAdjSub.getQty(); //����
									//��ȡ˰��  ҳ��˰��δ������������Ҫ��˰��/100
									entrsAgnAdjSub.setTaxRate(entrsAgnAdjSub.getTaxRate().divide(new BigDecimal(100)));
									BigDecimal taxRate = entrsAgnAdjSub.getTaxRate(); //˰��
									//����δ˰���  ���=δ˰����*δ˰����
									entrsAgnAdjSub.setNoTaxAmt(CalcAmt.noTaxAmt(noTaxUprc, qty));
									//����˰��  ˰��=δ˰���*˰��
									entrsAgnAdjSub.setTaxAmt(CalcAmt.taxAmt(noTaxUprc, qty, taxRate));
									//���㺬˰����  ��˰����=��˰����*˰��+��˰����
									entrsAgnAdjSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(noTaxUprc, qty, taxRate));
									//�����˰�ϼ�  ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
									entrsAgnAdjSub.setPrcTaxSum(CalcAmt.prcTaxSum(noTaxUprc, qty, taxRate));
									
									entrsAgnAdjSub.setAdjSnglId(entrsAgnAdj.getAdjSnglId());
								}
								//����ί�д����������ӱ�
								int b = eaasd.insertEntrsAgnAdjSub(EntrsAgnAdjSubList);
								if(a>=1 && b>=1) {
									isSuccess=true;
									message+="����ɹ���";
								}else {
									isSuccess=false;
									message+="��������"+entrsAgnAdj.getAdjSnglId()+"����ʧ�ܣ�";
									throw new RuntimeException(message);
								}
							}
								
						}else {
							isSuccess=false;
							message+="ί�д�����������Ϊ"+entrsAgnAdj.getDelvSnglId()+"�ĵ����ѽ��㣬���ܽ��е���������";
						}
					}else {
						isSuccess=false;
						message+="ί�д�����������Ϊ"+entrsAgnAdj.getDelvSnglId()+"�ĵ���δ��ˣ�����ȥ����ٽ��е���������";
					}
				}else {
					//�޸�ί�д�������������
					int a = eaad.updateEntrsAgnAdjByAdjSnglId(entrsAgnAdj);
					//ɾ��ί�д����������ӱ�
					int b = eaasd.deleteEntrsAgnAdjSubByAdjSnglId(entrsAgnAdj.getAdjSnglId());
					EntrsAgnAdjSubList = entrsAgnAdj.getEntrsAgnAdjSub();
					for(EntrsAgnAdjSub entrsAgnAdjSub:EntrsAgnAdjSubList) {
						entrsAgnAdjSub.setAdjSnglId(entrsAgnAdj.getAdjSnglId());
					}
					//����ί�д����������ӱ�
					int c = eaasd.insertEntrsAgnAdjSub(EntrsAgnAdjSubList);
					if(a>=1 && b>=1 && c>=1) {
						isSuccess=true;
						message+="����ɹ�";
					}else {
						isSuccess=false;
						message+="��������"+entrsAgnAdj.getAdjSnglId()+"����ʧ��";
						throw new RuntimeException(message);
					}
				}
			}
			resp=BaseJson.returnRespList("purc/EntrsAgnAdj/addEntrsAgnAdj", isSuccess, message, entrsAgnAdjList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String editEntrsAgnAdj(EntrsAgnAdj entrsAgnAdj,List<EntrsAgnAdjSub> entrsAgnAdjSubList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		eaasd.deleteEntrsAgnAdjSubByAdjSnglId(entrsAgnAdj.getAdjSnglId());
		eaad.updateEntrsAgnAdjByAdjSnglId(entrsAgnAdj);
		eaasd.insertEntrsAgnAdjSub(entrsAgnAdjSubList);
		message="���³ɹ���";
		
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnAdj/editEntrsAgnAdj", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@Override
	public String deleteEntrsAgnAdj(String adjSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		if(eaad.selectEntrsAgnAdjByAdjSnglId(adjSnglId)!=null){
			eaad.deleteEntrsAgnAdjByAdjSnglId(adjSnglId);
			eaasd.deleteEntrsAgnAdjSubByAdjSnglId(adjSnglId);
			isSuccess=true;
			message="ɾ���ɹ���";
		}else {
			isSuccess=false;
			message="���"+adjSnglId+"�����ڣ�";
		}
		
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnAdj/deleteEntrsAgnAdj", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryEntrsAgnAdj(String adjSnglId) {
		// TODO Auto-generated method stub
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<EntrsAgnAdjSub> entrsAgnAdjSub = new ArrayList<>();
		EntrsAgnAdj entrsAgnAdj=eaad.selectEntrsAgnAdjByAdjSnglId(adjSnglId);
		if(entrsAgnAdj!=null) {
			entrsAgnAdjSub=eaasd.selectEntrsAgnAdjSubByAdjSnglId(adjSnglId);
			message="��ѯ�ɹ���";
		}else {
			isSuccess=false;
			message="���"+adjSnglId+"�����ڣ�";
		}
		try {
			resp=BaseJson.returnRespObjList("purc/EntrsAgnAdj/queryEntrsAgnAdj", isSuccess, message, entrsAgnAdj, entrsAgnAdjSub);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@Override
	public String queryEntrsAgnAdjList(Map map) {
		String resp="";
		List<EntrsAgnAdj> poList= eaad.selectEntrsAgnAdjList(map);
		int count = eaad.selectEntrsAgnAdjCount(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;
		
		try {
			resp=BaseJson.returnRespList("purc/EntrsAgnAdj/queryEntrsAgnAdjList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages,poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteEntrsAgnAdjList(String adjSnglId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<String> list = getList(adjSnglId);
		int a = eaad.deleteEntrsAgnAdjList(list);
		if(a>=1) {
		    isSuccess=true;
		    message="ɾ���ɹ���";
		}else {
			isSuccess=false;
			message="ɾ��ʧ�ܣ�";
		}
		try {
			resp=BaseJson.returnRespObj("purc/EntrsAgnAdj/deleteEntrsAgnAdjList", isSuccess, message, null);
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

	//��ӡ���������ʱ�鿴ί�д�����������Ϣ
	@Override
	public String printingEntrsAgnAdjList(Map map) {
		String resp="";
		List<EntrsAgnAdj> entrsAgnAdjList = eaad.printingEntrsAgnAdjList(map);
		try {
			resp=BaseJson.returnRespObjList("purc/EntrsAgnAdj/printingEntrsAgnAdjList", true, "��ѯ�ɹ���", null,entrsAgnAdjList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	
}
