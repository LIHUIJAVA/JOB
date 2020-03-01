package com.px.mis.ec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.ProPlans;

public interface ProPlansDao {

    public void insert(List<ProPlans> proPlansList);

    public void delete(String proPlanId);

    public List<ProPlans> select(String proPlanId);

    public ProPlans selectByNo(Long no);

    /** 金额 */
    public ProPlans selectMoneyORDERLIMIT(@Param("proPlanId") String proPlanId, @Param("money") String money);

    /** 数量 */
    public ProPlans selectNumberORDERLIMIT(@Param("proPlanId") String proPlanId, @Param("number") String number);

    /** 无数量金额限制 */
    public ProPlans selectORDERLIMIT(@Param("proPlanId") String proPlanId);

}
