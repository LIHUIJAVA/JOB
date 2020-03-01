package com.px.mis.purc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInv;
import com.px.mis.purc.entity.PayApplForm;
import com.px.mis.purc.entity.PayApplFormSub;

public interface PayApplFormService {

	public String addPayApplForm(String userId, PayApplForm payApplForm, List<PayApplFormSub> payApplFormSubList,
			String loginTime);

	public String editPayApplForm(PayApplForm payApplForm, List<PayApplFormSub> payApplFormSubList);

	public String deleteEntrsAgnAdj(String payApplId);

	public String queryPayApplForm(String payApplId);

	public String queryPayApplFormList(Map map);

	Map<String, Object> updatePayApplFormIsNtChk(PayApplForm payApplForm) throws Exception;

	String queryPayApplFormListOrderBy(Map map);

	public String printPayApplFormList(Map map);

	//ÍÆËÍµ½U8
	public String pushToU8(String ids) throws IOException;


}
