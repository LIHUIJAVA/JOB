package com.px.mis.account.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.AcctItmDoc;

public class AcctltmDocTree {

	public static List<Map> getTree(List<AcctItmDoc> acctItmDocList) throws IOException {
		return getSonList(null, acctItmDocList);
	}

	private static Map getObjList(AcctItmDoc acctItmDoc, List list) throws IOException {
		Map map = new HashMap();
		map.put("subjId", acctItmDoc.getSubjId());
		map.put("subjNm", acctItmDoc.getSubjNm());
		map.put("subjTyp", acctItmDoc.getSubjTyp());
		map.put("subjCharc", acctItmDoc.getSubjCharc());
		map.put("encdLvlSub", acctItmDoc.getEncdLvlSub());
		map.put("subjMnem", acctItmDoc.getSubjMnem());
		map.put("isNtIndvRecoAccti", acctItmDoc.getIsNtIndvRecoAccti());
		map.put("isNtCustRecoAccti", acctItmDoc.getIsNtCustRecoAccti());
		map.put("isNtProvrRecoAccti", acctItmDoc.getIsNtProvrRecoAccti());
		map.put("isNtDeptAccti", acctItmDoc.getIsNtDeptAccti());
		map.put("isNtEndLvl", acctItmDoc.getIsNtEndLvl());
		map.put("isNtCashSubj", acctItmDoc.getIsNtCashSubj());
		map.put("isNtBankSubj", acctItmDoc.getIsNtBankSubj());
		map.put("isNtComnCashflowQtySubj", acctItmDoc.getIsNtComnCashflowQtySubj());
		map.put("isNtBkat", acctItmDoc.getIsNtBkat());
		map.put("isNtDayBookEntry", acctItmDoc.getIsNtDayBookEntry());
		map.put("memo", acctItmDoc.getMemo());
		map.put("pId", acctItmDoc.getpId());
		map.put("balDrct", acctItmDoc.getBalDrct());

		if (list != null && list.size() != 0) {
			map.put("children", list);
		}
		return map;
	}

	private static List<Map> getSonList(AcctItmDoc acctItmDoc, List<AcctItmDoc> acctItmDocList) throws IOException {

		List<Map> mList = new ArrayList();
		if (acctItmDoc == null) {
			for (AcctItmDoc sonAcctItmDoc : acctItmDocList) {
				mList.add(getObjList(sonAcctItmDoc, getSonList(sonAcctItmDoc, acctItmDocList)));
			}
		} else {
			for (AcctItmDoc sonAcctItmDoc : acctItmDocList) {
				if (sonAcctItmDoc.getEncdLvlSub() != null && acctItmDoc.getpId() != null
						&& sonAcctItmDoc.getpId() != null) {
					if (sonAcctItmDoc.getEncdLvlSub() - 1 == acctItmDoc.getEncdLvlSub()
							&& sonAcctItmDoc.getpId().equals(acctItmDoc.getSubjId())) {
						List<Map> mapList = new ArrayList<Map>();
						if (sonAcctItmDoc.getEncdLvlSub() < 3) {
							mList.add(getObjList(sonAcctItmDoc, getSonList(sonAcctItmDoc, acctItmDocList)));
						} else {
							mList.add(getObjList(sonAcctItmDoc, null));
						}
					}
				}

			}
		}
		return mList;
	}
}
