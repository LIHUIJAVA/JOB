package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.VouchSupvnTab;

public interface VouchSupvnTabDao {
	
	int insertVouchSupvnTab(VouchSupvnTab vouchSupvnTab);
	
	int updateVouchSupvnTabByOrdrNum(VouchSupvnTab vouchSupvnTab);
	
	int deleteVouchSupvnTabByOrdrNum(Integer ordrNum);
	
	VouchSupvnTab selectVouchSupvnTabByOrdrNum(Integer ordrNum);

    List<VouchSupvnTab> selectVouchSupvnTabList(Map map);
    
    int selectVouchSupvnTabCount();
}
