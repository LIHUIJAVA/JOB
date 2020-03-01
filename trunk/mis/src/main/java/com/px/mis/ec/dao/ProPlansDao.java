package com.px.mis.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.ProPlans;

public interface ProPlansDao {

    public void insert(List<ProPlans> proPlansList);

    public void delete(String proPlanId);

    public List<ProPlans> select(String proPlanId);

    public ProPlans selectByNo(Long no);

    /** ��� */
    public ProPlans selectMoneyORDERLIMIT(@Param("proPlanId") String proPlanId, @Param("money") String money);

    /** ���� */
    public ProPlans selectNumberORDERLIMIT(@Param("proPlanId") String proPlanId, @Param("number") String number);

    /** ������������� */
    public ProPlans selectORDERLIMIT(@Param("proPlanId") String proPlanId);

}
