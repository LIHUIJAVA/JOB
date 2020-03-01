package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.MthTermBgnTab;

public interface MthTermBgnTabDao {
	
	int insertMthTermBgnTab(MthTermBgnTab mthTermBgnTab);
	
	int updateMthTermBgnTabByOrdrNum(MthTermBgnTab mthTermBgnTab);
	
	int deleteMthTermBgnTabByOrdrNum(Integer ordrNum);
	
	MthTermBgnTab selectMthTermBgnTabByOrdrNum(Integer ordrNum);

    List<MthTermBgnTab> selectMthTermBgnTabList(Map map);
    
    int selectMthTermBgnTabCount();
    //ÆÚ³õ½á´æ
	List<MthTermBgnTab> selectMthTermInitList(Map map);
	
	int countSelectMthTermInit(Map map);
}
