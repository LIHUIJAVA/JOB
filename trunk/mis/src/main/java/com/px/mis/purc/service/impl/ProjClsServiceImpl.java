package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.purc.dao.ProjClsDao;
import com.px.mis.purc.entity.ProjCls;
import com.px.mis.purc.service.ProjClsService;
import com.px.mis.util.BaseJson;

/*��Ŀ���๦��ʵ��*/
@Transactional
@Service
public class ProjClsServiceImpl  implements ProjClsService {
	@Autowired
	private ProjClsDao projClsDao;

	@Override
	public String insertProjCls(ProjCls projCls) {
		// TODO Auto-generated method stub

		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
				if(projClsDao.selectProjClsByProjEncd(projCls.getProjEncd())!=null) {
					isSuccess=false;
					message="���"+projCls.getProjEncd()+"�Ѵ��ڣ����������룡";
				}else {
					projClsDao.insertProjCls(projCls);
					isSuccess=true;
					message="��Ŀ���������ɹ�";
				}
			
	    resp=BaseJson.returnRespObj("ec/projCls/insertProjCls", isSuccess, message, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
		
	}

	@Override
	public String updateProjCls(ProjCls projCls) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			int a=projClsDao.updateProjClsByOrdrNum(projCls);;
			if(a==1) {
				isSuccess=true;
				message="��Ŀ�����޸ĳɹ�";
			}else {
				isSuccess=false;
				message="��Ŀ�����޸�ʧ��";
			}
		resp=BaseJson.returnRespObj("ec/projCls/updateProjCls", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

//	@Override
//	public String deleteProjClsByOrdrNum(Integer ordrNum) {
//		String message="";
//		Boolean isSuccess=true;
//		String resp="";
//		try {
//			int a= projClsDao.deleteProjClsByOrdrNum(ordrNum);
//
//			if(a==1) {
//				isSuccess=true;
//				message="��Ŀ����ɾ���ɹ�";
//			}else {
//				isSuccess=false;
//				message="��Ŀ����ɾ��ʧ��";
//			}
//		resp=BaseJson.returnRespObj("ec/projCls/deleteProjClsByOrdrNum", isSuccess, message, null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return resp;
//	}

	@Override
	public String selectProjClsByOrdrNum(Integer ordrNum) {
		String message="��ѯ�ɹ�";
		Boolean isSuccess=true;
		String resp="";
		try {
			ProjCls projCls = projClsDao.selectProjClsByOrdrNum(ordrNum);
			if(projCls==null) {
				isSuccess=false;
				message="����Ŀ���಻���ڣ�";
			}
			resp=BaseJson.returnRespObj("ec/projCls/selectProjClsByOrdrNum", isSuccess, message, projCls);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	@Override
	public String queryList(Map map) {
		String message = "��ѯ�ɹ���";
		Boolean isSuccess = true;
		String resp = "";
		try {
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<ProjCls> mList = projClsDao.selectList(map);
			int count = projClsDao.selectCount(map);

			resp = BaseJson.returnRespList("ec/projCls/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, mList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
						e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectProjClsByProjEncd(String projEncd) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			ProjCls projCls = projClsDao.selectProjClsByProjEncd(projEncd);
			if(projCls==null) {
				isSuccess=false;
				message="����Ŀ���಻���ڣ�";
			}
			resp=BaseJson.returnRespObj("ec/projCls/selectProjClsByProjEncd", isSuccess, message, projCls);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
		}

//	
//	@Override
//	public ObjectNode delProjCls(String jsonBody) {
//		ObjectNode objectNode = null;
//		try {
//			objectNode = JacksonUtil.getObjectNode("");
//			Integer  list=BaseJson.getReqBody(jsonBody).get("list").asText();
//			System.err.println(list);
////			List<String> ordrNumList = new ObjectMapper().readValue(BaseJson.getReqBody(jsonBody).get("ordrNum").toString(), ArrayList.class);
//			List<Integer> ordrNumList=getList(list);
//			if (ordrNumList != null && ordrNumList.size() > 0) {
//				projClsDao.delProjCls(ordrNumList);
//				objectNode.put("isSuccess", true);
//				objectNode.put("message", "ɾ���ɹ���");
//			} else {
//				objectNode.put("isSuccess", false);
//				objectNode.put("message", "ɾ��ʧ�ܣ�");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//						e.printStackTrace();
//		}
//		return objectNode;
//	}
	/**
	 * ordrNum����list
	 * 
	 * @param ordrNum ordrNum(����Ѷ��ŷָ�)
	 * @return List����
	 */
	public List<String> getList(String ordrNum) {
		if (ordrNum == null || ordrNum.equals("")) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		String[] str = ordrNum.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}

		return list;
	}

	@Override
	public String delProjCls(String ordrNum) {
		
		String message="";
		Boolean isSuccess=false;
		String resp="";
		List<String> ordrNumList=getList(ordrNum);
		if (ordrNumList != null && ordrNumList.size() > 0) {
			projClsDao.delProjCls(ordrNumList);
			isSuccess=true;
			message="ɾ���ɹ���";
		} else {
			isSuccess=false;
			message="ɾ��ʧ�ܣ�";
		}
		try {
			resp = BaseJson.returnRespObj("ec/projCls/delProjCls", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

}
	