package com.dzg.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Excel导出工具类
 * 需要POI包（3.15）
 * 完全可以自定义export方法，根据实际情况
 * 
 * @author DZG
 * @since V1.0 2016年11月30日
 */
public class ExcelExportUtil {

	private static Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);
	
	public ModelAndView excelExport(HttpServletRequest request, HttpServletResponse response) {
		try {
			//String params = request.getParameter("params");//获取前端传来的参数，可以去获取值
			String[] headers = new String[] { "XX", "XX" };//有多少个表头放几个
			List<Map<String, Object>> dataList = new ArrayList<>();
			// TODO dataList是需要导出Excel的数据源 类型List<Map<String, Object>>
			// dataList中的Map 为{1:Obj,2:Obj}格式，key为递增数字
			String fileName = "ExcelFile_" + String.valueOf(System.currentTimeMillis()).substring(4, 13);
			export(response, "ExcelData", headers, dataList, fileName);
		} catch (Exception e) {
			logger.error("excelExport err",e);
			return null;
		}
		return null;
	}
	
	/**
	 * 导出方法一
	 * 生成OutputStream导出方法
	 * @param response
	 * @param sheetName
	 * @param headers
	 * @param dateList
	 * 
	 * @since 2016年11月30日
	 */
	public void export(HttpServletResponse response, String sheetName, String[] headers,
			List<Map<String, Object>> dataList,String fileName) {
		fileName = fileName + ".xls";
		String header = "attachment; filename=\"" + fileName + "\"";
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", header);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			export(sheetName, headers, dataList, out, "", true, (short) 1);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("export excel file error！", e);
		}
	}
	
	/**
	 * 导出方法二
	 * 生成一个资源链接给前端
	 * 
	 * @since 2016年11月30日
	 */
	public String exportExcel(Map<String, Object> paramTime, String id, String selectedItems,
			Map<String, Object> itemList, HttpServletRequest request) {
		String fileName = "XX.xlsx";
		String WebPath = "/statics/excels/??/";
		try {
			//获取需要导出的数据
			List<?> list = new ArrayList<>();
			if(!(list.size() > 0)) return null;
			//生成Excel
			String path = request.getServletContext().getRealPath("");
			File f = new File(path + WebPath + fileName);
			if(!f.exists()){
	            try {
	                f.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
			OutputStream out = new FileOutputStream(path + WebPath + fileName);
			// TODO 导出数据到out中 workbook.write(out);
			out.close();
			
		} catch (Exception e) {
			logger.error("导出性能监控Excel发生问题，",e);
			return null;
		}
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + WebPath + fileName; 

		return basePath;	
	}

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[]/blob(图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyyy-MM-dd"
	 * @param flag
	 *            true/false:字段的值为null时是否导出显示
	 * @param pos
	 *            距离第一个cell的偏移个数
	 */
	public static void export(String title, String[] headers, List<Map<String, Object>> dataset, OutputStream out,
			String pattern, boolean flag, short pos) {
		if (null == headers || headers.length <= 0 || null == out) {
			logger.warn("no headers or OutputStream is null");
			return;
		}
		if (null == dataset) {
			logger.info("no dataset");
			dataset = new ArrayList<>();
		}
		if (pattern.isEmpty()) {// TODO 这里还需要进行正则表达式的验证
			pattern = "yyyy-MM-dd";
		}
		HSSFWorkbook workbook = new HSSFWorkbook();// 声明一个工作薄
		try {
			int sheetNum=1;//工作薄sheet编号  
		    int bodyRowCount=1;//正文内容行号  
		    int currentRowCount=1;//当前的行号  
		    int perPageNum = 50000;//每个工作薄显示50000条数据  
		    
			HSSFSheet sheet = workbook.createSheet(title+sheetNum);// 生成一个表格
			sheet.setDefaultColumnWidth((short) 15);// 设置表格默认列宽度为15个字节
			HSSFCellStyle headerStyle = workbook.createCellStyle();// 生成一个样式
			// 设置表头样式
			headerStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			HSSFFont headerFont = workbook.createFont();// 生成一个字体
			headerFont.setColor(HSSFColor.VIOLET.index);
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			headerStyle.setFont(headerFont);// 把字体应用到当前的样式
			// 设置内容样式
			HSSFCellStyle contentStyle = workbook.createCellStyle();// 生成并设置另一个样式
			contentStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			contentStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			contentStyle.setBorderBottom(BorderStyle.THIN);
			contentStyle.setBorderLeft(BorderStyle.THIN);
			contentStyle.setBorderRight(BorderStyle.THIN);
			contentStyle.setBorderTop(BorderStyle.THIN);
			contentStyle.setAlignment(HorizontalAlignment.CENTER);
			contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			HSSFFont contentFont = workbook.createFont();// 生成另一个字体
			contentFont.setBold(false);
			contentStyle.setFont(contentFont);// 把字体应用到当前的样式
			HSSFRow row = sheet.createRow(0);
			// 产生表格标题行
			for (short i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(headerStyle);
				HSSFRichTextString text = new HSSFRichTextString(headers[i]);
				cell.setCellValue(text);
			}
			// 遍历集合数据，产生数据行
			for (int i = 0; i < dataset.size(); i++) {
				if(currentRowCount % perPageNum == 0){//每个工作薄显示50000条数据  
		            sheet=null;  
		            sheetNum++;//工作薄编号递增1  
		            sheet = workbook.createSheet(title+sheetNum);//创建一个新的工作薄  
		            bodyRowCount = 0;//正文内容行号置位为0  
		        }  
				Map<String, Object> dataMap = dataset.get(i);
				row = sheet.createRow(bodyRowCount);
				Set<String> keySet = dataMap.keySet();
				Object value;
				int index = 0;
				for (String key : keySet) {
					value = dataMap.get(key);
					HSSFRichTextString richString = new HSSFRichTextString(
							(null == value ? "" : JSONObject.toJSONString(value)));
					HSSFCell cell = row.createCell(index);
					cell.setCellStyle(contentStyle);
					cell.setCellValue(richString);
					index++;
					value = null;
				}
		        bodyRowCount++;//正文内容行号递增1  
		        currentRowCount++;//当前行号递增1  
			}
			workbook.write(out);
		} catch (IOException e) {
			logger.error("export excel error:", e);
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
				logger.error("close workbook error!", e);
			}
		}
	}
	

}
