package com.px.mis.ec.entity;

public class StoreRecord {
	
    private String storeId;//店铺编号
    private String storeName;//店铺名称
    private String ecStoreId;//平台店铺编号
    private String ecId;//电商平台编号
    private String ecName;//电商平台名称
    private String noAuditId;//免审策略编号
    private String noAuditName;//免审策略名称
    private String salesType;//销售类型
    private String brokId;//佣金扣点编号
    private String brokName;//佣金扣点名称
    private String deliverMode;//发货模式
    private String safeInv;//安全库存
    private String clearingForm;//结算方式
    private String respDep;//负责部门
    private String respPerson;//负责人
    private String sellerId;//买家会员好
    private String alipayNo;//支付宝账号
    private String mobile;//联系手机
    private String phone;//联系电话
    private String linkman;//联系人
    private String email;//邮箱地址
    private String business;//业务类型
    private String defaultRefWhs;//默认退货仓
    private String memo;//备注
    private String customerId;//对应客户编码
    private String whsName;//chagnkunamgzi
    private String userName;//负责人mingzhi
    private String customerName;//对应客户名称
    private String deptName;//对应部门名称
    private String personName;//负责人姓名


    
    public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
    public String getWhsName() {
		return whsName;
	}
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }
    
    public String getEcStoreId() {
		return ecStoreId;
	}

	public void setEcStoreId(String ecStoreId) {
		this.ecStoreId = ecStoreId;
	}

	public String getEcId() {
        return ecId;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId == null ? null : ecId.trim();
    }

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName == null ? null : ecName.trim();
    }

    public String getNoAuditId() {
        return noAuditId;
    }

    public void setNoAuditId(String noAuditId) {
        this.noAuditId = noAuditId == null ? null : noAuditId.trim();
    }

    public String getNoAuditName() {
        return noAuditName;
    }

    public void setNoAuditName(String noAuditName) {
        this.noAuditName = noAuditName == null ? null : noAuditName.trim();
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType == null ? null : salesType.trim();
    }

    public String getBrokId() {
        return brokId;
    }

    public void setBrokId(String brokId) {
        this.brokId = brokId == null ? null : brokId.trim();
    }

    public String getBrokName() {
        return brokName;
    }

    public void setBrokName(String brokName) {
        this.brokName = brokName == null ? null : brokName.trim();
    }

    public String getDeliverMode() {
        return deliverMode;
    }

    public void setDeliverMode(String deliverMode) {
        this.deliverMode = deliverMode == null ? null : deliverMode.trim();
    }

    public String getSafeInv() {
        return safeInv;
    }

    public void setSafeInv(String safeInv) {
        this.safeInv = safeInv == null ? null : safeInv.trim();
    }

    public String getClearingForm() {
        return clearingForm;
    }

    public void setClearingForm(String clearingForm) {
        this.clearingForm = clearingForm == null ? null : clearingForm.trim();
    }

    public String getRespDep() {
        return respDep;
    }

    public void setRespDep(String respDep) {
        this.respDep = respDep == null ? null : respDep.trim();
    }

    public String getRespPerson() {
        return respPerson;
    }

    public void setRespPerson(String respPerson) {
        this.respPerson = respPerson == null ? null : respPerson.trim();
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId == null ? null : sellerId.trim();
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo == null ? null : alipayNo.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business == null ? null : business.trim();
    }

    public String getDefaultRefWhs() {
        return defaultRefWhs;
    }

    public void setDefaultRefWhs(String defaultRefWhs) {
        this.defaultRefWhs = defaultRefWhs == null ? null : defaultRefWhs.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}