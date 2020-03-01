package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.ProActivity;

public interface ProActivityDao {

    public void insert(ProActivity proActivity);

    public void update(ProActivity proActivity);

    public void delete(String proActId);

    public ProActivity select(String proActId);

    public List<ProActivity> selectList(Map map);

    public int selectCount(Map map);

    public int updateAudit(List<ProActivity> proActivity);

    /**
     * 关联查询 只能有一个不为null
     * @param proActId 活动
     * @param proPlanId 方案
     * @param presentRangeEncd 赠品范围
     * 
     */
    public List<String> selectpro(@Param("proActId") String proActId, @Param("proPlanId") String proPlanId,
            @Param("presentRangeEncd") String presentRangeEncd);
    
    public List<ProActivity> selectStorePayTime(@Param("nowTime") String nowTime,@Param("takeStore") String takeStore);
    public ProActivity selectStorePayTimeORDERLIMIT(@Param("nowTime") String nowTime,@Param("takeStore") String storeId,@Param("invtyEncd") String invtyEncd);

}
