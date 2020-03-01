package com.px.mis.system.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.MisLog;

public interface MisLogDAO {
//����
	int insert(MisLog record);

	int insertSelective(MisLog record);

//�޸�
	int updateByPrimaryKeySelective(MisLog record);

	int updateByPrimaryKey(MisLog record);

	int updateByList(List<MisLog> record);

//ɾ��
	int deleteList(List<String> list);

	int deleteByPrimaryKey(Long id);

//��ѯ
	List<MisLog> selectList(Map map);

	int selectCount(Map map);

	MisLog selectByPrimaryKey(Long id);

}