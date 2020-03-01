package com.px.mis.account.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
/**
 * 
 * @author haokang_wu
 *@Date 2019年4月11日
 */
public class ExcelUtils{
	/**
	 * @param sheetName 工作表的名称
	 * @param excelHeader[] 表头名称
	 * @param ds_titles[] 字段名称
	 * @param excelList(list<Map>) 表格数据
	 * @param response
	 * @throws IOException
	 */
	public void fileUploadExcel(String sheetName,String[] excelHeader,String[] ds_titles,List<Map<String,String>> excelList,HttpServletResponse response) throws IOException{
		//获取通过工作表转换的输入流
		InputStream is=exportCalculateExcel(sheetName, excelHeader, ds_titles, excelList); //获取输入流
		BufferedOutputStream bos=null;//创建缓冲输出流，作用是为另一个输出流提供“缓冲功能”。
		try {
			/*response.setContentType(MIME)的作用是使客户端浏览器，
			区分不同种类的数据，并根据不同的MIME调用浏览器内不同的程序嵌入模块来处理相应的数据。*/
			//向客户端浏览器输出EXCEL格式的数据
			response.setContentType("application/msexcel");
			sheetName=URLEncoder.encode(sheetName,"utf-8");
			//设置EXCEL名称
			response.setHeader("Content-Disposition", "attachment;filename="+sheetName+".xls");
			//将数据返回到客户端
			bos=new BufferedOutputStream(response.getOutputStream());
			byte[] buff=new byte[1024];
			int bytesRead;
			//先把流数据存放至临时字节数据然后再将字节数组中内容写入到缓冲输出流
			while(-1!=(bytesRead=is.read(buff,0,buff.length))){
				bos.write(buff,0,bytesRead);
			}
		} catch (Exception e) {
			response.setContentType("text/html;chaset=utf-8");
			PrintWriter pw=response.getWriter();
			pw.println("<script>");
			pw.println("alert('生成文件失败');");
			pw.println("history.go(-1);");
			pw.println("</script>");
			pw.flush();
			pw.close();
		}finally{
			if(is!=null){
				is.close();
			}
			if(bos!=null){
				bos.close();
			}
		}
	}
	
	public InputStream exportCalculateExcel(String sheetName,String[] excelHeader,String[] ds_titles,List<Map<String,String>> excelList){
		HSSFWorkbook workBook=createCalculateWorkBook(sheetName, excelHeader, ds_titles, excelList);//获取处理后的工作表
		if(workBook==null){
			return null;
		}
		ByteArrayOutputStream arrayIo=new ByteArrayOutputStream();//创建字节数组流对象
		try {
			workBook.write(arrayIo);//把工作表写入字节数组流
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] by=arrayIo.toByteArray();//获取内存缓冲中的数据
		InputStream is=new ByteArrayInputStream(by);//将字节数组转化为输入流
		if(arrayIo!=null){
			try {
				arrayIo.close();//关闭流
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return is;
		
	}
	/**
	 * 
	 * @param sheetName 工作表的名称
	 * @param excelHeader 表头名称
	 * @param ds_titles 字段名称
	 * @param excelList 表格数据
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public HSSFWorkbook createCalculateWorkBook(String sheetName,String[] excelHeader,String[] ds_titles,List<Map<String,String>> excelList){
		HSSFWorkbook workBook=new HSSFWorkbook();//创建Excel文件(Workbook)
		HSSFSheet sheet=workBook.createSheet(sheetName);//创建工作表(Sheet)
		//加个工作表
		HSSFSheet sheet1=workBook.createSheet("加表");//创建工作表(Sheet)
		HSSFCellStyle rowHeadStyle=getrowHeadStyle_BOLD(workBook);//获取表头样式
		//设置行
		HSSFRow row=sheet.createRow((short)0);//代表第一行
		//设置列
		for(int i=0;i<excelHeader.length;i++){
			row.createCell((short)i);//代表第一行第n列
		}
		
		for(int i=0;i<excelHeader.length;i++){
			//给第一行的每一列加表头样式
			sheet.getRow(0).getCell((short)i).setCellStyle(rowHeadStyle);
		}
		
		for(int i=0;i<excelHeader.length;i++){
			//设置每一列的宽度
			sheet.setColumnWidth((short)i, 7000);
		}
		//getSheetAt 的参数应该是索引， 引用第几个sheet；
		//workBook.getSheetAt(0)代表获取第一个工作表，getRow(0)：工作表中的第一行
		//设置第一个工作表中第一行的行高
		workBook.getSheetAt(0).getRow(0).setHeight((short)300);//行高
		//设置第一个工作表中第一行的每一列的列名
		for(int i=0;i<excelHeader.length;i++){
			workBook.getSheetAt(0).getRow(0).getCell((short)i).setCellValue(excelHeader[i]);
		}
		
		for(int i=0;i<excelList.size();i++){
			Map<String,String> map=excelList.get(i);
			HSSFCellStyle row1Style=getColCellStyle(workBook);//获取表结构样式
			//设置行(i+1代表从第二行开始写入数据)
			HSSFRow row1=sheet.createRow((short)(i+1));
			//设置列
			for(int j=0;j<excelHeader.length;j++){
				row1.createCell((short)j);
			}
			for(int x=0;x<excelHeader.length;x++){
				//给每一行的每一列添加表结构样式
				sheet.getRow((i+1)).getCell((short)x).setCellStyle(row1Style);
			}		
			workBook.getSheetAt(0).getRow(i+1).setHeight((short)400);//行高
			for(int t=0;t<ds_titles.length;t++){
				workBook.getSheetAt(0).getRow(i+1).getCell((short)t).setCellValue(map.get(ds_titles[t]));
			}
		}
		return workBook;
		
	}
	
	//设置表头信息
	public HSSFCellStyle getrowHeadStyle_BOLD(HSSFWorkbook workBook){
		HSSFCellStyle styleBody=workBook.createCellStyle();
		 // 列头样式
		styleBody.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//设置图案样式
		styleBody.setFillForegroundColor(HSSFColor.PALE_BLUE.index);//背景色为叶片蓝
		styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		styleBody.setTopBorderColor(HSSFColor.BLACK.index);//上边框颜色为黑色
		styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
		styleBody.setBottomBorderColor(HSSFColor.BLACK.index);//下边框颜色为黑色
		styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		styleBody.setLeftBorderColor(HSSFColor.BLACK.index);//左边框颜色为黑色
		styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		styleBody.setRightBorderColor(HSSFColor.BLACK.index);//右边框颜色为黑色	
		styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		styleBody.setWrapText(true);//自动换行
		//生成字体样式
		HSSFFont f=workBook.createFont();
		f.setFontHeightInPoints((short)11);//设置字号
		f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体加粗
		//把字体应用到当前样式
		styleBody.setFont(f);
		return styleBody;
	}
	//设置表结构
	public HSSFCellStyle getColCellStyle(HSSFWorkbook workBook){
		HSSFCellStyle styleBody=workBook.createCellStyle();
		//设置边框
		styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框
		styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//生成字体样式
		HSSFFont f=workBook.createFont();
		f.setFontHeight((short)200);// 设置字体高度（大小）
		f.setFontName("宋体");//设置字体名称
		//把字体应用到当前样式
		styleBody.setFont(f);
		styleBody.setWrapText(true);//自动换行
		return styleBody;
		
	}
}
