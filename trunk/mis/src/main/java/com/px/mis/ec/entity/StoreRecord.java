package com.px.mis.ec.entity;

public class StoreRecord {
	
    private String storeId;//���̱��
    private String storeName;//��������
    private String ecStoreId;//ƽ̨���̱��
    private String ecId;//����ƽ̨���
    private String ecName;//����ƽ̨����
    private String noAuditId;//������Ա��
    private String noAuditName;//�����������
    private String salesType;//��������
    private String brokId;//Ӷ��۵���
    private String brokName;//Ӷ��۵�����
    private String deliverMode;//����ģʽ
    private String safeInv;//��ȫ���
    private String clearingForm;//���㷽ʽ
    private String respDep;//������
    private String respPerson;//������
    private String sellerId;//��һ�Ա��
    private String alipayNo;//֧�����˺�
    private String mobile;//��ϵ�ֻ�
    private String phone;//��ϵ�绰
    private String linkman;//��ϵ��
    private String email;//�����ַ
    private String business;//ҵ������
    private String defaultRefWhs;//Ĭ���˻���
    private String memo;//��ע
    private String customerId;//��Ӧ�ͻ�����
    private String whsName;//chagnkunamgzi
    private String userName;//������mingzhi
    private String customerName;//��Ӧ�ͻ�����
    private String deptName;//��Ӧ��������
    private String personName;//����������


    
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