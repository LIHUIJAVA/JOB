package com.px.mis.whs.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//�ֿ⵵��
public class WhsDoc {
    //
//	private String whsEncd;// �ֿ����
//
//	private Long ordrNum;// ���
//
//	private String whsNm;// �ֿ�����
//
//	private String deptEncd;// ���ű���
//
//	private String whsAddr;// �ֿ��ַ
//
//	private String tel;// �绰
//
//	private String princ;// ������
//
//	private String valtnMode;// �Ƽ۷�ʽ
//
//	private String crspdBarCd;// ��Ӧ������
//
//	private Integer isNtPrgrGdsBitMgmt;// �Ƿ���л�λ����
//
//	private String whsAttr;// �ֿ�����
//
//	private String sellAvalQtyCtrlMode;// ���ۿ��������Ʒ�ʽ
//
//	private String invtyAvalQtyCtrlMode;// �����������Ʒ�ʽ
//
//	private String memo;// ��ע
//
//	private Integer isNtShop;// �Ƿ��ŵ�
//
//	private String stpUseDt;// ͣ������
//
//	private String prov;// ʡ/ֱϽ��
//
//	private String cty;// ��
//
//	private String cnty;// ��
//
//	private Integer dumyWhs;// �����(�Ƿ��������)
//
//	private String setupPers;// ������
//
//	private String setupTm;// ����ʱ��
//
//	private String mdfr;// �޸���
//
//	private String modiTm;// �޸�ʱ��
//
//	private String deptName;// ��������
//
//	private String realWhs;// ���
    @JsonProperty("�ֿ����")
    private String whsEncd;// �ֿ����
    //	@JsonProperty("���")
    @JsonIgnore
    private Long ordrNum;// ���
    @JsonProperty("�ֿ�����")
    private String whsNm;// �ֿ�����
    @JsonProperty("���ű���")
    private String deptEncd;// ���ű���
    @JsonProperty("��������")
    private String deptName;// ��������
    @JsonProperty("�ֿ��ַ")
    private String whsAddr;// �ֿ��ַ
    @JsonProperty("�绰")
    private String tel;// �绰
    @JsonProperty("������")
    private String princ;// ������
    @JsonProperty("�Ƽ۷�ʽ")
    private String valtnMode;// �Ƽ۷�ʽ
    @JsonProperty("��Ӧ������")
    private String crspdBarCd;// ��Ӧ������
    @JsonProperty("�Ƿ��λ����")
    private Integer isNtPrgrGdsBitMgmt;// �Ƿ���л�λ����
    @JsonProperty("�ֿ�����")
    private String whsAttr;// �ֿ�����
    @JsonProperty("���ۿ��������Ʒ�ʽ")
    private String sellAvalQtyCtrlMode;// ���ۿ��������Ʒ�ʽ
    @JsonProperty("�����������Ʒ�ʽ")
    private String invtyAvalQtyCtrlMode;// �����������Ʒ�ʽ
    @JsonProperty("��ע")
    private String memo;// ��ע
    @JsonProperty("�Ƿ��ŵ�")
    private Integer isNtShop;// �Ƿ��ŵ�
    @JsonProperty("ͣ������")
    private String stpUseDt;// ͣ������
    @JsonProperty("ʡ/ֱϽ��")
    private String prov;// ʡ/ֱϽ��
    @JsonProperty("��")
    private String cty;// ��
    @JsonProperty("����")
    private String cnty;// ��
    @JsonProperty("�Ƿ������")
    private Integer dumyWhs;// �����(�Ƿ��������)
    @JsonProperty("������")
    private String setupPers;// ������
    @JsonProperty("����ʱ��")
    private String setupTm;// ����ʱ��
    @JsonProperty("�޸���")
    private String mdfr;// �޸���
    @JsonProperty("�޸�ʱ��")
    private String modiTm;// �޸�ʱ��
    @JsonProperty("ʵ��ֿ�")
    private String realWhs;// ���
    @JsonIgnore
    private List<MovBitTab> movBitList;

    public String getRealWhs() {
        return realWhs;
    }

    public void setRealWhs(String realWhs) {
        this.realWhs = realWhs;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }


    public List<MovBitTab> getMovBitList() {
        return movBitList;
    }

    public void setMovBitList(List<MovBitTab> movBitList) {
        this.movBitList = movBitList;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getDeptEncd() {
        return deptEncd;
    }

    public void setDeptEncd(String deptEncd) {
        this.deptEncd = deptEncd;
    }

    public String getWhsAddr() {
        return whsAddr;
    }

    public void setWhsAddr(String whsAddr) {
        this.whsAddr = whsAddr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPrinc() {
        return princ;
    }

    public void setPrinc(String princ) {
        this.princ = princ;
    }

    public String getValtnMode() {
        return valtnMode;
    }

    public void setValtnMode(String valtnMode) {
        this.valtnMode = valtnMode;
    }

    public String getCrspdBarCd() {
        return crspdBarCd;
    }

    public void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public Integer getIsNtPrgrGdsBitMgmt() {
        return isNtPrgrGdsBitMgmt;
    }

    public void setIsNtPrgrGdsBitMgmt(Integer isNtPrgrGdsBitMgmt) {
        this.isNtPrgrGdsBitMgmt = isNtPrgrGdsBitMgmt;
    }

    public String getWhsAttr() {
        return whsAttr;
    }

    public void setWhsAttr(String whsAttr) {
        this.whsAttr = whsAttr;
    }

    public String getSellAvalQtyCtrlMode() {
        return sellAvalQtyCtrlMode;
    }

    public void setSellAvalQtyCtrlMode(String sellAvalQtyCtrlMode) {
        this.sellAvalQtyCtrlMode = sellAvalQtyCtrlMode;
    }

    public String getInvtyAvalQtyCtrlMode() {
        return invtyAvalQtyCtrlMode;
    }

    public void setInvtyAvalQtyCtrlMode(String invtyAvalQtyCtrlMode) {
        this.invtyAvalQtyCtrlMode = invtyAvalQtyCtrlMode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsNtShop() {
        return isNtShop;
    }

    public void setIsNtShop(Integer isNtShop) {
        this.isNtShop = isNtShop;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCty() {
        return cty;
    }

    public void setCty(String cty) {
        this.cty = cty;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public Integer getDumyWhs() {
        return dumyWhs;
    }

    public void setDumyWhs(Integer dumyWhs) {
        this.dumyWhs = dumyWhs;
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getStpUseDt() {
        return stpUseDt;
    }

    public void setStpUseDt(String stpUseDt) {
        this.stpUseDt = stpUseDt;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

}