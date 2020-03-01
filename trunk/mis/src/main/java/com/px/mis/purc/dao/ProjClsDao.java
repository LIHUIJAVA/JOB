package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.ProjCls;



public interface ProjClsDao {

	//������Ŀ����
	int insertProjCls(ProjCls projCls);
	
	//�޸���Ŀ����
	int updateProjClsByOrdrNum(ProjCls projCls);
	
	//ɾ����Ŀ����
	int deleteProjClsByOrdrNum(Integer OrdrNum);
	
	//ͨ��������Ų�ѯ��Ŀ������ϸ
	ProjCls selectProjClsByOrdrNum(Integer OrdrNum);
	//ͨ����Ŀ�����ѯ��Ŀ������ϸ
	ProjCls selectProjClsByProjEncd(String projEncd);

	List<ProjCls> selectList(Map map);

	int selectCount(Map map);

	void delProjCls(List<String> ordrNumList);
	
	
}
