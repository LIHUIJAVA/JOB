package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.CannibSngl;
import com.px.mis.whs.entity.CannibSnglMap;
import com.px.mis.whs.entity.CannibSnglSubTab;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.OthOutIntoWhs;

//��������
public interface CannibSnglMapper {

    //1.��ӵ�����
    public int insertCSngl(CannibSngl record);

    public int insertCSnglSubTab(List<CannibSnglSubTab> record);//�����ӱ�

    //Ex��ӵ�����
    public int exInsertCSngl(CannibSngl record);

    public int exInsertCSnglSubTab(List<CannibSnglSubTab> record);//�����ӱ�

    //2.�޸ĵ�����
    public int updateCSngl(CannibSngl record);

    //3.ɾ��������   ��ɾ
    public int deleteCSnglSubTab(String formNum);

    public int deleteAllCSngl(List<String> formNum);

    //4.�򵥲�  ������
    public CannibSngl selectCSngl(@Param("formNum") String formNum);

    public List<CannibSnglSubTab> selectCSnglSubTabList(@Param("formNum") String formNum);

    //����
    public List<CannibSngl> selectCSnglList(@Param("formNum") List<String> formNum);

    //��ҳ��
    public List<CannibSnglMap> selectList(Map map);

    public int selectCount(Map map);

    //���������
    public int updateCSnglChk(List<CannibSngl> cList);

    public CannibSngl selectCanSnglChk(@Param("formNum") String formNum);//��ѯ�Ƿ������

    //����������
    public int updateCSnglNoChk(List<CannibSngl> cList);

    //�鿴�Ƿ������������
    public List<OthOutIntoWhs> selectOthIsDelete(@Param("srcFromNum") String srcFromNum);

    //PDA ��ⵥ  �ѱ���δ��˵ĵ���(��ѯ)
    public List<CannibSngl> selectCSnglChkr();

    public int updateCSnglSubTab(CannibSnglSubTab cSubTab);
    //PDA ���ⵥ  ����������е���������б�չʾ

    //��ӡ
    public List<CannibSngl> selectListDaYin(Map map);

    //��ѯ������ִ���  ������
    public List<InvtyTab> selectInvty(Map map);

    //------------------------------
    //����
//    public int updateAInvtyTab(List<InvtyTab> invtyTab);//����������(����)  ת���ֿ�
//
//    public int updateAInvtyTabJia(List<InvtyTab> invtyTab);//����������(����)  ת��ֿ�
//
//    //���
//    public int updateInvtyTab(List<InvtyTab> invtyTab);//�ִ�������(����)
//
//    public int updateInvtyTabJia(List<InvtyTab> invtyTab);//�ִ�������(�ӷ�)

    //����
//    public int updateInvtyTabNo(List<InvtyTab> invtyTab);//�ִ�������(����)
//
//    public int updateInvtyTabJiaNo(List<InvtyTab> invtyTab);//�ִ�������(�ӷ�)
//
//    public int updateAInvtyTabNo(List<InvtyTab> invtyTab);//����������(����)  ת��ֿ�
//
//    public int updateAInvtyTabJiaNo(List<InvtyTab> invtyTab);//����������(�ӷ�)  ת���ֿ�

    //��ѯ����
//    InvtyTab selectInvtyTabByTermOut(InvtyTab invtyTab);//ת���ֿ�
//
//    InvtyTab selectInvtyTabByTermIn(InvtyTab invtyTab);//ת��ֿ�

    //��ӵ�����ɾ��������
    public Integer insertCSnglDl(List<String> formNum);

    public Integer insertCSnglSubTabDl(List<String> formNum);

}