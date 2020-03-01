package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckSngl;
import com.px.mis.whs.entity.CheckSnglMap;
import com.px.mis.whs.entity.CheckSnglSubTab;
import com.px.mis.whs.entity.InvtyTab;


public interface CheckSnglMapper {

    //1.�����̵㵥
    public int insertCheckSngl(CheckSngl record);

    public int insertCheckSnglSubTab(List<CheckSnglSubTab> record);

    //����
    public int exInsertCheckSngl(CheckSngl record);

    public int exInsertCheckSnglSubTab(List<CheckSnglSubTab> record);

    //2.�޸��̵㵥
    public int updateCheckSngl(CheckSngl record);

    //3.ɾ���̵㵥   ��ɾ
    public int deleteCheckSnglSubTab(String checkFormNum);

    public int deleteAllCheckSngl(List<String> checkFormNum);

    //��ѯ
    public CheckSngl selectCheckSngl(@Param("checkFormNum") String checkFormNum);

    public List<CheckSnglSubTab> selectCheckSnglSubTab(@Param("checkFormNum") String checkFormNum);

    //����
    public List<CheckSngl> selectCheckSnglsList(@Param("checkFormNum") List<String> checkFormNum);

    //��ҳ��
    public List<CheckSnglMap> selectList(Map map);

    public int selectCount(Map map);

    //��ӡ
    public List<CheckSngl> selectListDaYin(Map map);

    //ͨ���ֿ⡢�̵����š����������롢ʧЧ�����ա�����Ϊ��ʱ�Ƿ��̵�
    //��ѯ�̵㵥�б�����������Ϣ��
    public List<CheckSngl> selectCheckSnglList(Map map);

    public List<CheckSngl> selectCheckSnglListZero(Map map);

    //���
    public int updateCSnglChk(List<CheckSngl> cList);

    public CheckSngl selectIsChkr(@Param("checkFormNum") String checkFormNum);//�鿴�Ƿ����

    public List<CheckSnglSubTab> selectCheckQty(@Param("checkFormNum") String checkFormNum);//��ѯʵ�����Ƿ�ȫ����д

    //����
    public int updateCSnglNoChk(List<CheckSngl> cList);

    public List<CheckPrftLoss> selectLossIsDel(@Param("srcFormNum") String srcFormNum);//�̵�������Ƿ��Ѿ�ɾ��

    //PDA �����̵����
    public List<CheckSngl> checkSnglList(@Param("whsEncd") List<String> whsEncd);

    //pda �����ӱ����
    public int updateCheckTab(List<CheckSnglSubTab> cSubTabList);

    public int updateCheckStatus(CheckSngl checkSngl);

    public List<CheckSnglSubTab> selectOrdNum();//��ѯ�̵��е��ӱ�

    //����֮������״̬  ����PC�ν��и���  ��֤����ͬ��
    public int updateStatus(List<CheckSngl> cList);

    public List<InvtyTab> selectCheckSnglGdsBitList(Map map);

    //�����̵㵥ɾ��������
    public Integer insertCheckSnglDl(List<String> checkFormNum);

    public Integer insertCheckSnglSubTabDl(List<String> checkFormNum);
}