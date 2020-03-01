package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.PresentRange;
import com.px.mis.ec.entity.PresentRangeList;

public interface PresentRangeListService {

    ObjectNode insertPresentRange(PresentRange presentRange, List<PresentRangeList> cList);

    ObjectNode deletePresentRangeListById(String presentRangeEncd);

    String selectPresentRangeListById(String presentRangeEncd);

    String selectPresentRangeListList(Map map);

    ObjectNode updatePresentRangeListById(PresentRange presentRange, List<PresentRangeList> cList);

}
