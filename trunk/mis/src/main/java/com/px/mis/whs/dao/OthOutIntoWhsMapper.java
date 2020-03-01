package com.px.mis.whs.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.MovBitTab;
import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsMap;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;

//��������ⵥ
public interface OthOutIntoWhsMapper {
    /**
     * ��ѯδ���˵���������ⵥ
     */
    List<OthOutIntoWhs> selectOthOutIntoWhsIsInvty(Map map);

    /**
     * ���������ڳ�����ⵥ
     */
    Integer selectInvtyTabTermInvty(String loginTime);

    /**
     * �������ڳ�����ⵥ
     */
    Integer insertInvtyTabTermInvty(String loginTime);

    /**
     * ɾ�����ܳ�����ⵥ
     */
    Integer deleteInvtyTabTerm(String loginTime);

    /**
     * ��ѯ�ڳ�ʱ�����ⵥ��¼�Ƿ��м�¼
     */
    Integer selectInvtyTabTermInvtyCount(String loginTime);

    /**
     * ɾ���ڳ�����ⵥ
     */
    Integer deleteInvtyTabTermInvty(String loginTime);

    // �������������
    public int insertOthOutIntoWhs(OthOutIntoWhs record);

    public int insertOthOutIntoWhsSubTab(List<OthOutIntoWhsSubTab> record);// �����ӱ�

    public int exInsertOthOutIntoWhs(OthOutIntoWhs record);

    public int exInsertOthOutIntoWhsSubTab(List<OthOutIntoWhsSubTab> record);// �����ӱ�
    // �޸����������

    public int updateOthOutIntoWhs(OthOutIntoWhs record);

    public int updateBat(List<OthOutIntoWhsSubTab> oList);// ���ع�������
    // ɾ����������� ��ɾ

    public int deleteOthOutIntoWhsSubTab(String formNum); // ��

    public int deleteAllOthOutIntoWhs(List<String> formNum);// ����

    // �򵥲� ���������
    public OthOutIntoWhs selectOthOutIntoWhs(@Param("formNum") String formNum);

    public List<OthOutIntoWhsSubTab> selectOthOutIntoWhsSubTabList(@Param("formNum") String formNum);

    // ���ӱ�
    public OthOutIntoWhs selectOthOutIntoWhsSubTab(@Param("formNum") String formNum);

    // ����
    public List<OthOutIntoWhs> selectOthOutIntoWhsList(@Param("formNum") List<String> formNum);

    // ��ҳ��
    public List<OthOutIntoWhsMap> selectList(Map map);

    public int selectCount(Map map);

    // ��ӡ
    public List<OthOutIntoWhs> selectListDaYin(Map map);

    // ���
    public int updateOutInWhsChk(List<OthOutIntoWhs> cList);

    public OthOutIntoWhs selectIsChk(@Param("formNum") String formNum);// ��ѯ�Ƿ��������״̬
    // ����

    public int updateOutInWhsNoChk(List<OthOutIntoWhs> cList);

    // PDA ��ⵥ
    // (��ѯ) ���
    public List<OthOutIntoWhs> selectOINChkr(List<String> whsEncd);
    // �ش����� ָ����λ ��������Ҫ�������ӿ����� �޸ĳ���ⵥ�����״̬[������--�������]��InvtyNum��

    // PDA ���ⵥ
    // ��ѯ ����
    public List<OthOutIntoWhs> selectOutChkr(List<String> whsEncd);

    // ������ⵥ���õĽӿ���һ���� ������Ҫ������ٿ����� �޸ĳ���ⵥ�ĳ���״̬[������--�������]��
    // �޸ļ���״̬
    public int updateOutInWhsBookEntry(List<OthOutIntoWhs> cList);

    // ��ҳ
    List<OthOutIntoWhs> selectOthOutIntoWhsStream(Map map);

    int countSelectOthOutIntoWhsStream(Map map);

    // ����Դ����
    public OthOutIntoWhs selectOthOutIntoWhsSrc(@Param("srcFormNum") String srcFormNum,
                                                @Param("outIntoWhsTypId") String outIntoWhsTypId);

    /**
     * ��Ų��ӱ� ���������
     *
     * @param ordrNum ���
     * @return OthOutIntoWhsSubTab ��ϸ��
     */
    public OthOutIntoWhsSubTab selectOthByOrdrNumKey(@Param("ordrNum") Long ordrNum);


    //		 �������������ɾ��������
    public Integer insertOthOutIntoWhsDl(List<String> formNum);

    public Integer insertOthOutIntoWhsSubTabDl(List<String> formNum);

    List<MovBitTab> selectallInvtyGdsBitListInto(String formNum);


}