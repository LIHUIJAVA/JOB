package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.GoodRecord;

public interface GoodRecordDao  {

	public void insert(GoodRecord goodRecord);
	
	public void update(GoodRecord goodRecord);
	
	public void delete(@Param("list")List<String> ids);
	
	public GoodRecord select(String ecGoodId);
	
	public GoodRecord selectBySku(String goodSku);
	
	public GoodRecord selectById(Integer id);
	
	public List<GoodRecord> selectList(Map map);
	
	public List<Map> exportList(Map map);
	
	public Integer selectByEcGoodIdAndSku(GoodRecord goodEcGoodIdAndSku);
	public Integer selectByEcGoodIdAnd(GoodRecord goodEcGoodIdAndSku);

	public int selectCount(Map map);
	
	public void download(List<GoodRecord> goodRecordList);
	
	public GoodRecord selectBySkuAndEcGoodId(@Param("goodSku") String goodSku,@Param("ecGoodId") String ecGoodId);
	
	public void insertList(List<GoodRecord> list);
	/**
	 * 根据sku和平台商品编码查询店铺商品档案
	 * @param goodSku
	 * @param ecGoodId
	 * @return GoodRecord
	 */
	public GoodRecord selectGoodRecordBySkuAndEcGoodId(@Param("goodSku")String goodSku,@Param("ecGoodId")String ecGoodId);
	
}
