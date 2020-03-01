package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.whs.entity.MergePickSngl;
import com.px.mis.whs.entity.PickSnglSubTab;
import com.px.mis.whs.entity.PickSnglTab;
import com.px.mis.whs.entity.SellSnglWhs;

//�����
public interface PickSnglMapper {

    //�޸����۶������Ƿ�����ʶ
    public int updateSellSngl(@Param("list") List<String> sellSngl, @Param("ispick") String ispick);

    public int selectSellSngl(@Param("list") List<String> sellSngl, @Param("ispick") String ispick);

    public List<String> selectSellSnglTab(String sellSngl);

    //��ѯ���۵�
    public List<SellSngl> selectSellById(@Param("sellId") List<String> sellSnglId);

    public List<SellSngl> selectDistinctId(@Param("sellId") List<String> sellSnglId);

    // �������ɼ����
    public List<SellSnglWhs> selectSellWhsIsPick(Map whs);

    public int selectSellWhsIsPickCount(Map whs);

    public List<PickSnglSubTab> selectDistinctWhs(List<String> sellSngl);

    public int updateSellSnglWhs(@Param("list") List<String> sellSngl, @Param("number") String Number);


    //��ѯ��������Ƿ��Ѵ���
    public PickSnglTab selectPick(@Param("pickSnglNum") String pickSnglNum);

    //��Ӽ����
    public int insertPickSngl(PickSnglTab pickTab);

    public int insertPickSnglSubTab(List<PickSnglSubTab> pickList);

    //��������б���ʾ
    public List<PickSnglTab> selectAllPickSngl(Map map);

    public int selectAllPickCount(Map map);

    //ͨ�����������ݺŲ�ѯ�ӱ���Ϣ
    public List<PickSnglSubTab> selectPSubTabById(@Param("pickSnglNum") String pickSnglNum);

    //�������-���б���ʾ(��Ҫ��)
    public List<PickSnglTab> selectList(Map map);

    public int selectCount(Map map);

    //�ϲ������
    public List<PickSnglSubTab> selectPSubTab(@Param("pickSnglNum") String pickSnglNum,
                                              @Param("whsEncd") String whsEncd);

    public int insertmergePick(List<MergePickSngl> mPickList);

    //ɾ���ϲ������
    public int updateSellSnglZero(SellSngl sellSngl);

    public int deleteMerPickSngl(String pickSnglNum);//ɾ���ϲ������(���ݺ�)

    public int deletePickSnglTab(PickSnglSubTab pSnglSubTab);//ɾ�������(���ݺ� �ֿ���� ������� ����)

    public int selectPSubTabByIdCount(@Param("pickSnglNum") String pickSnglNum);//selectPSubTabById ��ѯ�ӱ��л��Ƿ��������ݺţ�1�� �������ɾ���ӱ�����2�� ���û����ɾ���ӱ�+����

    public int deletePickSngl(String pickSnglNum);

    public PickSnglSubTab selectSellId(PickSnglSubTab pSubTab);//ͨ��������ӱ��ֶβ�ѯ���۵���

    //PDA  ��ʾ���м�����б�-
    public List<PickSnglTab> selectAllMerge(List<String> list);

    public List<MergePickSngl> selectAllById(@Param("pickSnglNum") String pickSnglNum);

    public int updatePTab(PickSnglTab pSnglTab);

    //PDA �ش��ӿ�  �����ɱ�ʶ  ������ʱ��
    public int updatePTabPda(PickSnglTab pSnglTab);

    public int updateJHTabPda(MergePickSngl mergePickSngl);

    public int deleteMergePick(String pickSnglNum);//�����������ɾ������ϲ�������

    //=======================zds create====================================
    public int deletePickSnglTabBySellSnglId(String sellSnglId);//�������۵���ɾ�������

    public int updateTabPda(MergePickSngl mergePickSngl);//pda������������

    public int selectPickSnglSubTabCount(MergePickSngl mergePickSngl);//pda������������δ�������


    /**
     * ���ݼ�����Ų�ѯ���ⵥ
     */
    List<SellOutWhsSub> selectPickSnglOutTabList(String pickSnglNum);


    //��Ӽ����
    public Integer insertPickSnglDl(List<String> list);

    public Integer insertPickSnglSubTabDl(List<String> list);
}
