package com.px.mis.purc.entity;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.whs.entity.MovBitTab;
/*存货档案*/
public class InvtyDoc {
    private String invtyClsEncd;//存货分类编码
    private String invtyEncd;//存货编码
    private String invtyNm;//存货名称
    private String spcModel;//规格型号
    private String invtyCd;//存货代码
    private String provrId;//供应商编号
    private String measrCorpId;//计量单位编号
    private String vol;//体积
    private BigDecimal weight;//重量
    private BigDecimal longs;//长
    private BigDecimal wide;//宽
    private BigDecimal hght;//高
    private BigDecimal bxRule;//箱规
    private String baoZhiQiEarWarn;//保质期预警天数
    private String baoZhiQiDt;//保质期天数
    private String placeOfOrigin;//产地
    private String rgstBrand;//注册商标
    private String cretNum;//合格证号
    private String makeLicsNum;//生产许可证
    private BigDecimal traySize;//托架尺寸
    private String quantity;//数量/拖
    private String valtnMode;//计价方式
    private BigDecimal iptaxRate;//进项税率
    private BigDecimal optaxRate;//销项税率
    private BigDecimal highestPurPrice;//最高进价
    private BigDecimal loSellPrc;//最低售价
    private BigDecimal ltstCost;//最新成本
    private BigDecimal refCost;//参考成本
    private BigDecimal refSellPrc;//参考售价
    private Integer isNtSell;//是否销售
    private Integer isNtPurs;//是否采购
    private Integer isDomSale;//是否内销
    private Integer pto;//PTO
    private Integer isQuaGuaPer;//是否保质期管理
    private Integer allowBomMain;//允许BOM主件
    private Integer allowBomMinor;//允许BOM子件
    private Integer isNtBarCdMgmt;//是否条形码管理
    private String crspdBarCd;//对应条形码
    private String projDocInd;//项目档案标识
    private String setupPers;//创建人
    private String setupDt;//创建日期
    private String mdfr;//修改人
    private String modiDt;//修改日期
    private String memo;//备注
    private String measrCorpNm;//计量单位名称
    private InvtyCls invtyCls;//存货分类档案
    private MeasrCorpDoc measrCorpDoc;//计量单位档案
    //临近日期状态
    private String Stat;//临近、过期
    //移位实体
    private List<MovBitTab> movBitList;
    
    private Integer shdTaxLabour;//是否应税劳务
    
    private Integer isNtDiscnt;//是否折扣
    
    private String invtyClsNm; //存货分类名称

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