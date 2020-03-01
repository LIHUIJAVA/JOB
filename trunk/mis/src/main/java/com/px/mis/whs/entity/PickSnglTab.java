package com.px.mis.whs.entity;

import java.util.List;

//拣货主表
public class PickSnglTab {

    private String pickSnglNum;//拣货单号
    private String pickNum;//合并单中的拣货单号

    private String pickSnglTm;//拣货日期

    private Integer isFinshPick;//拣货完成标识

    private String finshPickTm;//拣货完成时间

    private Integer merge;//合并标识

    private Integer split;//拆分标识

    private String pickPers;//拣货人
    private String pickPersId;//拣货人编码
    private String setupPers;//创建人

    private String setupTm;//创建时间

    private List<PickSnglSubTab> pList;
    //拣货单合并子
    private List<MergePickSngl> mergePickSnglList;

    public final List<MergePickSngl> getMergePickSnglList() {
        return mergePickSnglList;
    }

    public final void setMergePickSnglList(List<MergePickSngl> mergePickSnglList) {
        this.mergePickSnglList = mergePickSnglList;
    }

    public String getPickPersId() {
        return pickPersId;
    }

    public void setPickPersId(String pickPersId) {
        this.pickPersId = pickPersId;
    }

    public String getPickNum() {
        return pickNum;
    }

    public void setPickNum(String pickNum) {
        this.pickNum = pickNum;
    }

    public List<PickSnglSubTab> getpList() {
        return pList;
    }

    public void setpList(List<PickSnglSubTab> pList) {
        this.pList = pList;
    }

    public String getPickSnglNum() {
        return pickSnglNum;
    }

    public void setPickSnglNum(String pickSnglNum) {
        this.pickSnglNum = pickSnglNum;
    }

    public String getPickSnglTm() {
        return pickSnglTm;
    }

    public void setPickSnglTm(String pickSnglTm) {
        this.pickSnglTm = pickSnglTm;
    }

    public Integer getIsFinshPick() {
        return isFinshPick;
    }

    public void setIsFinshPick(Integer isFinshPick) {
        this.isFinshPick = isFinshPick;
    }

    public Integer getMerge() {
        return merge;
    }

    public void setMerge(Integer merge) {
        this.merge = merge;
    }

    public Integer getSplit() {
        return split;
    }

    public void setSplit(Integer split) {
        this.split = split;
    }

    public String getPickPers() {
        return pickPers;
    }

    public void setPickPers(String pickPers) {
        this.pickPers = pickPers;
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public String getFinshPickTm() {
        return finshPickTm;
    }

    public void setFinshPickTm(String finshPickTm) {
        this.finshPickTm = finshPickTm;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }


}
