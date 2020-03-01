package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.PlatWhsMapp;


public interface PlatWhsMappDao {
	
	public String select(Map map);
	//����ƽ̨��ӳ��
	public void insert(PlatWhsMapp platWhsMapp);
	//�޸�ƽ̨��ӳ��
	public void update(PlatWhsMapp platWhsMapp);
	//ɾ��ƽ̨��ӳ��
	public void delete(PlatWhsMapp platWhsMapp);
	//��ѯƽ̨��ӳ���б�
	public List<PlatWhsMapp> selectList(Map map);

	public int selectCount(Map map);
}
