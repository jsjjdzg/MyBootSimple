package com.dzg.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

/**
 * 
 * WordPoi通用方法
 * 
 * @author DZG
 * @since V1.0 2016年11月29日
 */
public class WordPoiUtil {

	static String filePath = "C:/Users/DZG/Desktop/t1.docx";
	static String filePath2 = "C:/Users/DZG/Desktop/t4.docx";

	private static XWPFParagraph[] pars;

	public static void main(String[] args) throws IOException {
		XWPFDocument doc = new XWPFDocument();

		XWPFParagraph p = doc.createParagraph();

		XWPFRun r = p.createRun();
		r.setText("Some Text");
		r.setBold(true);
		r = p.createRun();
		r.setText("Goodbye");

		CTP ctP = CTP.Factory.newInstance();
		CTText t = ctP.addNewR().addNewT();
		t.setStringValue("header");
		pars = new XWPFParagraph[1];
		p = new XWPFParagraph(ctP, doc);
		pars[0] = p;

		XWPFHeaderFooterPolicy hfPolicy = doc.createHeaderFooterPolicy();
		hfPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, pars);

		ctP = CTP.Factory.newInstance();
		t = ctP.addNewR().addNewT();
		t.setStringValue("My Footer");
		pars[0] = new XWPFParagraph(ctP, doc);
		hfPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, pars);

		try {
			OutputStream os = new FileOutputStream(new File(filePath2));
			doc.write(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
