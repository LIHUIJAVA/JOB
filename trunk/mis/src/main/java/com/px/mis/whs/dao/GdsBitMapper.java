package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.GdsBitTyp;

//��λ����
public interface GdsBitMapper {

    //������λ����
    public int insertGdsBit(GdsBit record);

    //�޸Ļ�λ����
    public int updateGdsBit(GdsBit record);

    //ɾ����λ����
    public int deleteGdsBit(@Param("gdsBitEncd") String gdsBitEncd);

    public int deleteGdsBitList(@Param("list") List<String> gdsBitEncd);//��ɾ

    //�򵥲�  ��λ����
    public GdsBit selectGdsBitbyId(@Param("gdsBitEncd") String gdsBitEncd);

    public GdsBit selectGdsBit(@Param("gdsBitEncd") String gdsBitEncd);

    public List<GdsBit> selectGdsBitList(@Param("gdsBitEncd") String gdsBitEncd);

    //����
    public List<GdsBit> selectGBitList(@Param("gdsBitEncd") List<String> gdsBitEncd);

    //��ҳ��
    public List<GdsBit> selectList(Map map);

    public int selectCount(Map map);

    //��ѯ���л�λ����
    public List<GdsBitTyp> selectgTypList();

    //��ӡ
    public List<GdsBit> selectListDaYin(Map map);


}