package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckPrftLossMap;
import com.px.mis.whs.entity.CheckPrftLossSubTab;


public interface CheckPrftLossMapper {

    //1.�����̵������
    public int insertCheckSnglLoss(CheckPrftLoss record);

    public int insertCheckSnglLossSubTab(List<CheckPrftLossSubTab> record);

    //2.�޸��̵������
    public int updateCheckSnglLoss(CheckPrftLoss record);

    //3.ɾ���̵������   ��ɾ
    public int deleteCheckSnglSubLossTab(String checkFormNum);

    public int deleteAllCheckSnglLoss(List<String> checkFormNum);

    //��ѯ
    public CheckPrftLoss selectCheckSnglLoss(@Param("checkFormNum") String checkFormNum);

    public List<CheckPrftLossSubTab> selectCheckSnglLossSubTab(@Param("checkFormNum") String checkFormNum);
    //��ѯ��Դ
    public CheckPrftLoss selectSrcCheckSnglLoss(@Param("srcFormNum") String srcFormNum);

    //����
    public List<CheckPrftLoss> selectCSnglLossList(@Param("checkFormNum") List<String> checkFormNum);

    //��ҳ��
    public List<CheckPrftLossMap> selectList(Map map);

    public int selectCount(Map map);

    //���
    public int updateCSnglLossChk(List<CheckPrftLoss> cList);

    public CheckPrftLoss selectIsChr(@Param("checkFormNum") String checkFormNum);//��ѯ�Ƿ����

    //����
    public int updateCSnglLossNoChk(List<CheckPrftLoss> cList);

    //��ӡ
    public List<CheckPrftLoss> selectListDaYin(Map map);

    //�޸��̵������ӱ�
    public int updateCheckPrftLossSubTab(List<CheckPrftLossSubTab> record);

    //�̵������ɾ��������
    public Integer insertCheckSnglLossDl(List<String> checkFormNum);

    public Integer insertCheckSnglLossSubTabDl(List<String> checkFormNum);

    public Integer exInsertCSngl(CheckPrftLoss value);

}