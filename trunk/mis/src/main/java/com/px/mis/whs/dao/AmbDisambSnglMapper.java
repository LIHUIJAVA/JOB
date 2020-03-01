package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.AmbDisambSngl;
import com.px.mis.whs.entity.AmbDisambSnglMap;
import com.px.mis.whs.entity.AmbDisambSnglubTab;


public interface AmbDisambSnglMapper {

    //������װ��ж�� 
    public int insertAmbDisambSngl(AmbDisambSngl record);

    public int insertAmbDisambSnglubTab(List<AmbDisambSnglubTab> aList);//�����ӱ�

    public int exInsertAmbDisambSngl(AmbDisambSngl record);

    public int exInsertAmbDisambSnglubTab(List<AmbDisambSnglubTab> aList);//�����ӱ�

    //�޸���װ��ж��
    public int updateAmbDisambSngl(AmbDisambSngl record);

    //ɾ����װ��ж��
    public int deleteAmbDisambSnglubTab(String formNum);

    public int deleteAllAmbDisambSngl(List<String> formNum);

    //�򵥲� ��װ��ж��
    public AmbDisambSngl selectAmbDisambSngl(@Param("formNum") String formNum);

    public List<AmbDisambSnglubTab> selectAmbDisambSnglubTabList(@Param("formNum") String formNum);

    //����
    public List<AmbDisambSngl> selectAmbDisambSnglList(@Param("formNum") List<String> formNum);

    //��ҳ��
    public List<AmbDisambSnglMap> selectList(Map map);

    public int selectCount(Map map);

    //���
    public int updateASnglChk(List<AmbDisambSngl> cList);

    public AmbDisambSngl selectISChr(@Param("formNum") String formNum);//��ѯ�Ƿ������

    //����
    public int updateASnglNoChk(List<AmbDisambSngl> cList);

    //��ӡ
    public List<AmbDisambSngl> selectListDaYin(Map map);

    //������װ��ж��ɾ��������
    public Integer insertAmbDisambSnglDl(List<String> list);

    public Integer insertAmbDisambSnglubTabDl(List<String> list);//�����ӱ�


}