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
 *@Date 2019��4��11��
 */
public class ExcelUtils{
	/**
	 * @param sheetName �����������
	 * @param excelHeader[] ��ͷ����
	 * @param ds_titles[] �ֶ�����
	 * @param excelList(list<Map>) �������
	 * @param response
	 * @throws IOException
	 */
	public void fileUploadExcel(String sheetName,String[] excelHeader,String[] ds_titles,List<Map<String,String>> excelList,HttpServletResponse response) throws IOException{
		//��ȡͨ��������ת����������
		InputStream is=exportCalculateExcel(sheetName, excelHeader, ds_titles, excelList); //��ȡ������
		BufferedOutputStream bos=null;//���������������������Ϊ��һ��������ṩ�����幦�ܡ���
		try {
			/*response.setContentType(MIME)��������ʹ�ͻ����������
			���ֲ�ͬ��������ݣ������ݲ�ͬ��MIME����������ڲ�ͬ�ĳ���Ƕ��ģ����������Ӧ�����ݡ�*/
			//��ͻ�����������EXCEL��ʽ������
			response.setContentType("application/msexcel");
			sheetName=URLEncoder.encode(sheetName,"utf-8");
			//����EXCEL����
			response.setHeader("Content-Disposition", "attachment;filename="+sheetName+".xls");
			//�����ݷ��ص��ͻ���
			bos=new BufferedOutputStream(response.getOutputStream());
			byte[] buff=new byte[1024];
			int bytesRead;
			//�Ȱ������ݴ������ʱ�ֽ�����Ȼ���ٽ��ֽ�����������д�뵽���������
			while(-1!=(bytesRead=is.read(buff,0,buff.length))){
				bos.write(buff,0,bytesRead);
			}
		} catch (Exception e) {
			response.setContentType("text/html;chaset=utf-8");
			PrintWriter pw=response.getWriter();
			pw.println("<script>");
			pw.println("alert('�����ļ�ʧ��');");
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
		HSSFWorkbook workBook=createCalculateWorkBook(sheetName, excelHeader, ds_titles, excelList);//��ȡ�����Ĺ�����
		if(workBook==null){
			return null;
		}
		ByteArrayOutputStream arrayIo=new ByteArrayOutputStream();//�����ֽ�����������
		try {
			workBook.write(arrayIo);//�ѹ�����д���ֽ�������
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] by=arrayIo.toByteArray();//��ȡ�ڴ滺���е�����
		InputStream is=new ByteArrayInputStream(by);//���ֽ�����ת��Ϊ������
		if(arrayIo!=null){
			try {
				arrayIo.close();//�ر���
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return is;
		
	}
	/**
	 * 
	 * @param sheetName �����������
	 * @param excelHeader ��ͷ����
	 * @param ds_titles �ֶ�����
	 * @param excelList �������
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public HSSFWorkbook createCalculateWorkBook(String sheetName,String[] excelHeader,String[] ds_titles,List<Map<String,String>> excelList){
		HSSFWorkbook workBook=new HSSFWorkbook();//����Excel�ļ�(Workbook)
		HSSFSheet sheet=workBook.createSheet(sheetName);//����������(Sheet)
		//�Ӹ�������
		HSSFSheet sheet1=workBook.createSheet("�ӱ�");//����������(Sheet)
		HSSFCellStyle rowHeadStyle=getrowHeadStyle_BOLD(workBook);//��ȡ��ͷ��ʽ
		//������
		HSSFRow row=sheet.createRow((short)0);//�����һ��
		//������
		for(int i=0;i<excelHeader.length;i++){
			row.createCell((short)i);//�����һ�е�n��
		}
		
		for(int i=0;i<excelHeader.length;i++){
			//����һ�е�ÿһ�мӱ�ͷ��ʽ
			sheet.getRow(0).getCell((short)i).setCellStyle(rowHeadStyle);
		}
		
		for(int i=0;i<excelHeader.length;i++){
			//����ÿһ�еĿ��
			sheet.setColumnWidth((short)i, 7000);
		}
		//getSheetAt �Ĳ���Ӧ���������� ���õڼ���sheet��
		//workBook.getSheetAt(0)�����ȡ��һ��������getRow(0)���������еĵ�һ��
		//���õ�һ���������е�һ�е��и�
		workBook.getSheetAt(0).getRow(0).setHeight((short)300);//�и�
		//���õ�һ���������е�һ�е�ÿһ�е�����
		for(int i=0;i<excelHeader.length;i++){
			workBook.getSheetAt(0).getRow(0).getCell((short)i).setCellValue(excelHeader[i]);
		}
		
		for(int i=0;i<excelList.size();i++){
			Map<String,String> map=excelList.get(i);
			HSSFCellStyle row1Style=getColCellStyle(workBook);//��ȡ��ṹ��ʽ
			//������(i+1����ӵڶ��п�ʼд������)
			HSSFRow row1=sheet.createRow((short)(i+1));
			//������
			for(int j=0;j<excelHeader.length;j++){
				row1.createCell((short)j);
			}
			for(int x=0;x<excelHeader.length;x++){
				//��ÿһ�е�ÿһ����ӱ�ṹ��ʽ
				sheet.getRow((i+1)).getCell((short)x).setCellStyle(row1Style);
			}		
			workBook.getSheetAt(0).getRow(i+1).setHeight((short)400);//�и�
			for(int t=0;t<ds_titles.length;t++){
				workBook.getSheetAt(0).getRow(i+1).getCell((short)t).setCellValue(map.get(ds_titles[t]));
			}
		}
		return workBook;
		
	}
	
	//���ñ�ͷ��Ϣ
	public HSSFCellStyle getrowHeadStyle_BOLD(HSSFWorkbook workBook){
		HSSFCellStyle styleBody=workBook.createCellStyle();
		 // ��ͷ��ʽ
		styleBody.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//����ͼ����ʽ
		styleBody.setFillForegroundColor(HSSFColor.PALE_BLUE.index);//����ɫΪҶƬ��
		styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);//�ϱ߿�
		styleBody.setTopBorderColor(HSSFColor.BLACK.index);//�ϱ߿���ɫΪ��ɫ
		styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);//�±߿�
		styleBody.setBottomBorderColor(HSSFColor.BLACK.index);//�±߿���ɫΪ��ɫ
		styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);//��߿�
		styleBody.setLeftBorderColor(HSSFColor.BLACK.index);//��߿���ɫΪ��ɫ
		styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);//�ұ߿�
		styleBody.setRightBorderColor(HSSFColor.BLACK.index);//�ұ߿���ɫΪ��ɫ	
		styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);//ˮƽ����
		styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//��ֱ����
		styleBody.setWrapText(true);//�Զ�����
		//����������ʽ
		HSSFFont f=workBook.createFont();
		f.setFontHeightInPoints((short)11);//�����ֺ�
		f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//����Ӵ�
		//������Ӧ�õ���ǰ��ʽ
		styleBody.setFont(f);
		return styleBody;
	}
	//���ñ�ṹ
	public HSSFCellStyle getColCellStyle(HSSFWorkbook workBook){
		HSSFCellStyle styleBody=workBook.createCellStyle();
		//���ñ߿�
		styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);//�±߿�
		styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);//��߿�
		styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);//�ұ߿�
		styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);//�ϱ߿�
		styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);//ˮƽ����
		styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//��ֱ����
		//����������ʽ
		HSSFFont f=workBook.createFont();
		f.setFontHeight((short)200);// ��������߶ȣ���С��
		f.setFontName("����");//������������
		//������Ӧ�õ���ǰ��ʽ
		styleBody.setFont(f);
		styleBody.setWrapText(true);//�Զ�����
		return styleBody;
		
	}
}
