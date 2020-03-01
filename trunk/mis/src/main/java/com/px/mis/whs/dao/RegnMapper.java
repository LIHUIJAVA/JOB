package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.Regn;

//���򵵰�
public interface RegnMapper {

    //�������򵵰�
    public int insertRegn(Regn record);

    //�޸����򵵰�
    public int updateRegn(Regn record);

    //ɾ�����򵵰�
    public int deleteRegn(@Param("regnEncd") String regnEncd);

    public int deleteRegnList(@Param("list") List<String> regnEncd);//��ɾ

    //�򵥲�  ���򵵰�
    public Regn selectRegn(@Param("regnEncd") String regnEncd);

    public List<Regn> selectRegnList(@Param("regnEncd") String regnEncd);

    //����
    public List<Regn> selectRegnAllList(@Param("regnEncd") List<String> regnEncd);

    //��ҳ��
    public List<Regn> selectList(Map map);

    public int selectCount(Map map);

    //��ӡ
    public List<Regn> selectListDaYin(Map map);
}