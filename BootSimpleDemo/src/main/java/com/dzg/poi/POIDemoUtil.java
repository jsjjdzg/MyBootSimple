package com.dzg.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class POIDemoUtil {

	private static Logger logger = LoggerFactory.getLogger(POIDemoUtil.class);
	private static String filePath1 = "C:\\Users\\DZG\\Desktop\\Test1.docx";
	private static String filePath2 = "C:\\Users\\DZG\\Desktop\\Test2.docx";
	private static String filePath3 = "C:\\Users\\DZG\\Desktop\\Test3.docx";
	private static String filePath4 = "C:\\Users\\DZG\\Desktop\\Test4.docx";
	private static String filePath5 = "C:\\Users\\DZG\\Desktop\\Test5.docx";
	//private static String filePath6 = "C:\\Users\\DZG\\Desktop\\Test6.docx";
	private static String filePathDesk = "C:\\Users\\DZG\\Desktop";

	@Test
	public void main() throws Exception {
		createSimpleTableDemo1();
		insertImageDemo();
		createSimpleTableDemo2();
		createFormatTableDemo();
		createSimpleTableDemo3();
	}

	public void export(HttpServletResponse response, String fileName) {
		fileName = fileName + ".docx";
		String header = "attachment; filename=\"" + fileName + "\"";
		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", header);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			// TODO 导出Word的方法
			export(response, fileName, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("export excel file error！", e);
		}
	}

	
	public void BetterHeaderFooterExample () {
        XWPFDocument doc = new XWPFDocument();

        XWPFParagraph p = doc.createParagraph();

        XWPFRun r = p.createRun();
        r.setText("Some Text");
        r.setBold(true);
        r = p.createRun();
        r.setText("Goodbye");

//        // create header/footer functions insert an empty paragraph
//        XWPFHeader head = doc.createHeader(HeaderFooterType.DEFAULT);
//        head.createParagraph().createRun().setText("header");
//        
//        XWPFFooter foot = doc.createFooter(HeaderFooterType.DEFAULT);
//        foot.createParagraph().createRun().setText("footer");
        
        try {
            OutputStream os = new FileOutputStream(new File("header2.docx"));
            doc.write(os);
            doc.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	
	
	/**
	 * 创建简单表格Demo1
	 * 
	 * @since 2016年11月30日
	 */
	public void createSimpleTableDemo1() throws IOException {
		@SuppressWarnings("resource")
		XWPFDocument doc = new XWPFDocument();
		XWPFTable table = doc.createTable(3, 3);

		table.getRow(1).getCell(1).setText("EXAMPLE OF TABLE");
		XWPFParagraph p1 = table.getRow(0).getCell(0).getParagraphs().get(0);

		XWPFRun r1 = p1.createRun();
		r1.setBold(true);
		r1.setText("The quick brown fox");
		r1.setItalic(true);
		r1.setFontFamily("Courier");
		r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
		r1.setTextPosition(100);

		table.getRow(2).getCell(2).setText("only text");

		FileOutputStream out = new FileOutputStream(filePath1);
		doc.write(out);
		out.close();
	}

	/**
	 * 创建简单带图表格（BUG）
	 * 
	 * @since 2016年11月30日
	 */
	public void insertImageDemo() throws Exception {
		@SuppressWarnings("resource")
		XWPFDocument doc = new XWPFDocument();
		XWPFParagraph p = doc.createParagraph();

		String imgFile = filePathDesk + "\\ttt.png";
		XWPFRun r = p.createRun();

		int format = XWPFDocument.PICTURE_TYPE_JPEG;
		r.setText(imgFile);
		r.addBreak();
		r.addPicture(new FileInputStream(imgFile), format, imgFile, 200, 200); // 200x200 pixels
		r.addBreak(BreakType.PAGE);

		FileOutputStream out = new FileOutputStream(filePath2);
		doc.write(out);
		out.close();

		System.out.println("Process Completed Successfully");
	}

	/**
	 * 创建简单表格Demo2
	 * 
	 * @since 2016年11月30日
	 */
	public void createSimpleTableDemo2() throws Exception {
		@SuppressWarnings("resource")
		XWPFDocument document = new XWPFDocument();

		// New 2x2 table
		XWPFTable tableOne = document.createTable();
		XWPFTableRow tableOneRowOne = tableOne.getRow(0);
		tableOneRowOne.getCell(0).setText("Hello");
		tableOneRowOne.addNewTableCell().setText("World");

		XWPFTableRow tableOneRowTwo = tableOne.createRow();
		tableOneRowTwo.getCell(0).setText("This is");
		tableOneRowTwo.getCell(1).setText("a table");

		// Add a break between the tables
		document.createParagraph().createRun().addBreak();

		// New 3x3 table
		XWPFTable tableTwo = document.createTable();
		XWPFTableRow tableTwoRowOne = tableTwo.getRow(0);
		tableTwoRowOne.getCell(0).setText("col one, row one");
		tableTwoRowOne.addNewTableCell().setText("col two, row one");
		tableTwoRowOne.addNewTableCell().setText("col three, row one");

		XWPFTableRow tableTwoRowTwo = tableTwo.createRow();
		tableTwoRowTwo.getCell(0).setText("col one, row two");
		tableTwoRowTwo.getCell(1).setText("col two, row two");
		tableTwoRowTwo.getCell(2).setText("col three, row two");

		XWPFTableRow tableTwoRowThree = tableTwo.createRow();
		tableTwoRowThree.getCell(0).setText("col one, row three");
		tableTwoRowThree.getCell(1).setText("col two, row three");
		tableTwoRowThree.getCell(2).setText("col three, row three");

		FileOutputStream outStream = new FileOutputStream(filePath3);
		document.write(outStream);
		outStream.close();
	}

	/**
	 * 创建简单格式化表格Demo
	 * 
	 * @since 2016年11月30日
	 */
	public void createFormatTableDemo() throws Exception {
		@SuppressWarnings("resource")
		// Create a new document from scratch
		XWPFDocument doc = new XWPFDocument();

		// create paragraph
		XWPFParagraph para = doc.createParagraph();

		// create a run to contain the content
		XWPFRun rh = para.createRun();

		// Format as desired
		rh.setFontSize(15);
		rh.setFontFamily("Verdana");
		rh.setText("This is the formatted Text");
		rh.setColor("fff000");
		para.setAlignment(ParagraphAlignment.RIGHT);

		// write the file
		FileOutputStream out = new FileOutputStream(filePath4);
		doc.write(out);
		out.close();

		System.out.println("Process Completed Successfully");
	}
	
	/**
	 * 创建简单表格Demo3
	 * 
	 * @since 2016年11月30日
	 */
	public void createSimpleTableDemo3() throws Exception {
		@SuppressWarnings("resource")
		XWPFDocument document1 = new XWPFDocument(new FileInputStream(new File(filePath4)));

	    List<IBodyElement> bodyElements = document1.getBodyElements();
	    for(int i = 0; i < bodyElements.size(); i++) {
	        IBodyElement element = bodyElements.get(i);

	        if(element.getElementType() == BodyElementType.PARAGRAPH) {
	            document1.removeBodyElement(i);
	            break;
	        }
	    }

	    document1.write(new FileOutputStream(new File(filePath5)));
	}

	public void export(HttpServletResponse response, String fileName, OutputStream out) {

	}
}
