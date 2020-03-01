package com.px.mis.purc.entity;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.whs.entity.MovBitTab;
/*�������*/
public class InvtyDoc {
    private String invtyClsEncd;//����������
    private String invtyEncd;//�������
    private String invtyNm;//�������
    private String spcModel;//����ͺ�
    private String invtyCd;//�������
    private String provrId;//��Ӧ�̱��
    private String measrCorpId;//������λ���
    private String vol;//���
    private BigDecimal weight;//����
    private BigDecimal longs;//��
    private BigDecimal wide;//��
    private BigDecimal hght;//��
    private BigDecimal bxRule;//���
    private String baoZhiQiEarWarn;//������Ԥ������
    private String baoZhiQiDt;//����������
    private String placeOfOrigin;//����
    private String rgstBrand;//ע���̱�
    private String cretNum;//�ϸ�֤��
    private String makeLicsNum;//�������֤
    private BigDecimal traySize;//�мܳߴ�
    private String quantity;//����/��
    private String valtnMode;//�Ƽ۷�ʽ
    private BigDecimal iptaxRate;//����˰��
    private BigDecimal optaxRate;//����˰��
    private BigDecimal highestPurPrice;//��߽���
    private BigDecimal loSellPrc;//����ۼ�
    private BigDecimal ltstCost;//���³ɱ�
    private BigDecimal refCost;//�ο��ɱ�
    private BigDecimal refSellPrc;//�ο��ۼ�
    private Integer isNtSell;//�Ƿ�����
    private Integer isNtPurs;//�Ƿ�ɹ�
    private Integer isDomSale;//�Ƿ�����
    private Integer pto;//PTO
    private Integer isQuaGuaPer;//�Ƿ����ڹ���
    private Integer allowBomMain;//����BOM����
    private Integer allowBomMinor;//����BOM�Ӽ�
    private Integer isNtBarCdMgmt;//�Ƿ����������
    private String crspdBarCd;//��Ӧ������
    private String projDocInd;//��Ŀ������ʶ
    private String setupPers;//������
    private String setupDt;//��������
    private String mdfr;//�޸���
    private String modiDt;//�޸�����
    private String memo;//��ע
    private String measrCorpNm;//������λ����
    private InvtyCls invtyCls;//������൵��
    private MeasrCorpDoc measrCorpDoc;//������λ����
    //�ٽ�����״̬
    private String Stat;//�ٽ�������
    //��λʵ��
    private List<MovBitTab> movBitList;
    
    private Integer shdTaxLabour;//�Ƿ�Ӧ˰����
    
    private Integer isNtDiscnt;//�Ƿ��ۿ�
    
    private String invtyClsNm; //�����������

    public String getInvtyClsNm() {
		return invtyClsNm;
	}

	public void setInvtyClsNm(String invtyClsNm) {
		this.invtyClsNm = invtyClsNm;
	}

	public Integer getIsNtDiscnt() {
		return isNtDiscnt;
	}

	public void setIsNtDiscnt(Integer isNtDiscnt) {
		this.isNtDiscnt = isNtDiscnt;
	}

	public Integer getShdTaxLabour() {
		return shdTaxLabour;
	}

	public void setShdTaxLabour(Integer shdTaxLabour) {
		this.shdTaxLabour = shdTaxLabour;
	}

	public List<MovBitTab> getMovBitList() {
		return movBitList;
	}

	public void setMovBitList(List<MovBitTab> movBitList) {
		this.movBitList = movBitList;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public String getStat() {
		return Stat;
	}

	public void setStat(String stat) {
		Stat = stat;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

	public InvtyCls getInvtyCls() {
		return invtyCls;
	}

	public void setInvtyCls(InvtyCls invtyCls) {
		this.invtyCls = invtyCls;
	}

	public MeasrCorpDoc getMeasrCorpDoc() {
		return measrCorpDoc;
	}

	public void setMeasrCorpDoc(MeasrCorpDoc measrCorpDoc) {
		this.measrCorpDoc = measrCorpDoc;
	}

	public String getInvtyClsEncd() {
        return invtyClsEncd;
    }

    public void setInvtyClsEncd(String invtyClsEncd) {
        this.invtyClsEncd = invtyClsEncd == null ? null : invtyClsEncd.trim();
    }

    public String getInvtyEncd() {
        return invtyEncd;
    }

    public void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd == null ? null : invtyEncd.trim();
    }

    public String getInvtyNm() {
        return invtyNm;
    }

    public void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm == null ? null : invtyNm.trim();
    }

    public String getSpcModel() {
        return spcModel;
    }

    public void setSpcModel(String spcModel) {
        this.spcModel = spcModel == null ? null : spcModel.trim();
    }

    public String getInvtyCd() {
        return invtyCd;
    }

    public void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd == null ? null : invtyCd.trim();
    }

    public String getProvrId() {
        return provrId;
    }

    public void setProvrId(String provrId) {
        this.provrId = provrId == null ? null : provrId.trim();
    }



    public String getMeasrCorpId() {
		return measrCorpId;
	}

	public void setMeasrCorpId(String measrCorpId) {
		this.measrCorpId = measrCorpId;
	}

	public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol == null ? null : vol.trim();
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getLongs() {
        return longs;
    }

    public void setLongs(BigDecimal longs) {
        this.longs = longs;
    }

    public BigDecimal getWide() {
        return wide;
    }

    public void setWide(BigDecimal wide) {
        this.wide = wide;
    }

    public BigDecimal getHght() {
        return hght;
    }

    public void setHght(BigDecimal hght) {
        this.hght = hght;
    }

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public String getBaoZhiQiEarWarn() {
        return baoZhiQiEarWarn;
    }

    public void setBaoZhiQiEarWarn(String baoZhiQiEarWarn) {
        this.baoZhiQiEarWarn = baoZhiQiEarWarn == null ? null : baoZhiQiEarWarn.trim();
    }

    public String getBaoZhiQiDt() {
        return baoZhiQiDt;
    }

    public void setBaoZhiQiDt(String baoZhiQiDt) {
        this.baoZhiQiDt = baoZhiQiDt == null ? null : baoZhiQiDt.trim();
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin == null ? null : placeOfOrigin.trim();
    }

    public String getRgstBrand() {
        return rgstBrand;
    }

    public void setRgstBrand(String rgstBrand) {
        this.rgstBrand = rgstBrand == null ? null : rgstBrand.trim();
    }

    public String getCretNum() {
        return cretNum;
    }

    public void setCretNum(String cretNum) {
        this.cretNum = cretNum == null ? null : cretNum.trim();
    }

    public String getMakeLicsNum() {
        return makeLicsNum;
    }

    public void setMakeLicsNum(String makeLicsNum) {
        this.makeLicsNum = makeLicsNum == null ? null : makeLicsNum.trim();
    }

    public BigDecimal getTraySize() {
        return traySize;
    }

    public void setTraySize(BigDecimal traySize) {
        this.traySize = traySize;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity == null ? null : quantity.trim();
    }

    public String getValtnMode() {
        return valtnMode;
    }

    public void setValtnMode(String valtnMode) {
        this.valtnMode = valtnMode == null ? null : valtnMode.trim();
    }

    public BigDecimal getIptaxRate() {
        return iptaxRate;
    }

    public void setIptaxRate(BigDecimal iptaxRate) {
        this.iptaxRate = iptaxRate;
    }

    public BigDecimal getOptaxRate() {
        return optaxRate;
    }

    public void setOptaxRate(BigDecimal optaxRate) {
        this.optaxRate = optaxRate;
    }

    public BigDecimal getHighestPurPrice() {
        return highestPurPrice;
    }

    public void setHighestPurPrice(BigDecimal highestPurPrice) {
        this.highestPurPrice = highestPurPrice;
    }

    public BigDecimal getLoSellPrc() {
        return loSellPrc;
    }

    public void setLoSellPrc(BigDecimal loSellPrc) {
        this.loSellPrc = loSellPrc;
    }

    public BigDecimal getLtstCost() {
        return ltstCost;
    }

    public void setLtstCost(BigDecimal ltstCost) {
        this.ltstCost = ltstCost;
    }

    public BigDecimal getRefCost() {
        return refCost;
    }

    public void setRefCost(BigDecimal refCost) {
        this.refCost = refCost;
    }

    public BigDecimal getRefSellPrc() {
        return refSellPrc;
    }

    public void setRefSellPrc(BigDecimal refSellPrc) {
        this.refSellPrc = refSellPrc;
    }

    public Integer getIsNtSell() {
        return isNtSell;
    }

    public void setIsNtSell(Integer isNtSell) {
        this.isNtSell = isNtSell;
    }

    public Integer getIsNtPurs() {
        return isNtPurs;
    }

    public void setIsNtPurs(Integer isNtPurs) {
        this.isNtPurs = isNtPurs;
    }

    public Integer getIsDomSale() {
        return isDomSale;
    }

    public void setIsDomSale(Integer isDomSale) {
        this.isDomSale = isDomSale;
    }

    public Integer getPto() {
        return pto;
    }

    public void setPto(Integer pto) {
        this.pto = pto;
    }

    public Integer getIsQuaGuaPer() {
        return isQuaGuaPer;
    }

    public void setIsQuaGuaPer(Integer isQuaGuaPer) {
        this.isQuaGuaPer = isQuaGuaPer;
    }

    public Integer getAllowBomMain() {
        return allowBomMain;
    }

    public void setAllowBomMain(Integer allowBomMain) {
        this.allowBomMain = allowBomMain;
    }

    public Integer getAllowBomMinor() {
        return allowBomMinor;
    }

    public void setAllowBomMinor(Integer allowBomMinor) {
        this.allowBomMinor = allowBomMinor;
    }

    public Integer getIsNtBarCdMgmt() {
        return isNtBarCdMgmt;
    }

    public void setIsNtBarCdMgmt(Integer isNtBarCdMgmt) {
        this.isNtBarCdMgmt = isNtBarCdMgmt;
    }

    public String getCrspdBarCd() {
        return crspdBarCd;
    }

    public void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd == null ? null : crspdBarCd.trim();
    }

    public String getProjDocInd() {
        return projDocInd;
    }

    public void setProjDocInd(String projDocInd) {
        this.projDocInd = projDocInd == null ? null : projDocInd.trim();
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    }

    public String getSetupDt() {
        return setupDt;
    }

    public void setSetupDt(String setupDt) {
        this.setupDt = setupDt;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr == null ? null : mdfr.trim();
    }

    public String getModiDt() {
        return modiDt;
    }

    public void setModiDt(String modiDt) {
        this.modiDt = modiDt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}