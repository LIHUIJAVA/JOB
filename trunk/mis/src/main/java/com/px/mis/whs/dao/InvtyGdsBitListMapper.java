package com.px.mis.whs.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.whs.entity.MovBitTab;

public interface InvtyGdsBitListMapper {
    // ��/
    public MovBitTab insertInvtyGdsBitList(MovBitTab record);

    // list��
    public int insertInvtyGdsBitLists(List<MovBitTab> record);

    // ��/
    public List<MovBitTab> selectInvtyGdsBitList(MovBitTab record);

    // ����
    public List<MovBitTab> selectInvtyGdsBitListf(String orderNum);

    // ����sum���Ų�
    public List<MovBitTab> selectInvtyGdsBitListOrderNum(String orderNum);

    // ����sum��
    public MovBitTab selectInvtyGdsBitSum(MovBitTab record);

    // ���� �� �ӵ�
    public List<MovBitTab> selectInvtyGdsBitListNum(@Param("list") List<String> list, @Param("orderNum") String orderNum);

    // ��/
    public int updateInvtyGdsBitList(MovBitTab record);

    // �� list
    public int updateInvtyGdsBitLists(List<MovBitTab> record);

    // ɾ/
    public int deleteInvtyGdsBitList(List<String> orderNum);

    // ��ѭ����1��12��12��213 serial
    public int deleteInvtyGdsBitSerial(List<String> serial);

    // ��ѭ����1��12��12��213 id
    public int deleteInvtyGdsBitId(List<String> id);

    // ����
    public int updateIntoinvtyGdsBitList(MovBitTab record);

    // ����
    public int updateOutInvtyGdsBitList(MovBitTab record);

}
