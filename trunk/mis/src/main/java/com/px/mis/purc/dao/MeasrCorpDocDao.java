package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.MeasrCorpDoc;

public interface MeasrCorpDocDao {
	
    int deleteMeasrCorpDocByMeasrCorpId(String measrCorpId);
    
    int deleteMeasrCorpDocList(List<String> measrCorpId);

    int insertMeasrCorpDoc(MeasrCorpDoc measrCorpDoc);

    MeasrCorpDoc selectMeasrCorpDocByMeasrCorpId(String measrCorpId);

    int updateMeasrCorpDocByMeasrCorpId(MeasrCorpDoc measrCorpDoc);
    
    int updateMeasrCorpDocByMeasrCorpId(List<MeasrCorpDoc> measrCorpDoc);
    
    List<MeasrCorpDoc> selectMeasrCorpDocList(Map map);
    
    int selectMeasrCorpDocCount(Map map);
    
    List<MeasrCorpDoc> printingMeasrCorpDocList();
}