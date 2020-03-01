package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.PresentRange;
import com.px.mis.ec.entity.PresentRangeList;

public interface PresentRangeListDao {

    int insertPresentRange(PresentRange presentRangeList);

    int updatePresentRangeById(PresentRange presentRangeList);

    int deletePresentRangeById(String presentRangeEncd);

    PresentRange selectPresentRangeById(String presentRangeEncd);

//	PresentRangeList selectPresentRangeListByPresentRangeEncd(String presentRangeEncd);

//	List<PresentRangeList> selectPresentRangeListByProPlansNo(Long proPlansNo);

    List<PresentRange> selectPresentRangesd(Map<?, ?> map);

    int selectPresentRangeCount(Map<?, ?> map);

    
    
    
    int deleteByPrimaryKey(Long id);
    int deleteByPresentRangeList( String  presentRangeEncd);

    int insert(PresentRangeList record);
    int insertPresentRangeList(List<PresentRangeList>  list);

//    int insertSelective(PresentRangeList record);

    PresentRangeList selectByPrimaryKey(Long id);
    List<PresentRangeList> selectByPresentRangeList(String presentRangeEncd);

//    int updateByPrimaryKeySelective(PresentRangeList record);

    int updateByPrimaryKey(PresentRangeList record);
}
