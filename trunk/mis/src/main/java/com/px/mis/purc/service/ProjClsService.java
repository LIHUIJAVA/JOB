package com.px.mis.purc.service;


import java.util.Map;

import com.px.mis.purc.entity.ProjCls;


	public interface ProjClsService {

		String insertProjCls(ProjCls projCls);
		
		String updateProjCls(ProjCls projCls);
		
	//	String deleteProjClsByOrdrNum(Integer ordrNum);

		String selectProjClsByOrdrNum(Integer ordrNum);

		String selectProjClsByProjEncd(String projEncd);

		String queryList(Map map);

		String delProjCls(String ordrNum);
	}

