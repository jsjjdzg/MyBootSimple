package com.bocloud.emaas.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;

public class FileOperation {

	private Logger logger = LoggerFactory.getLogger(FileOperation.class);

	/**
	 * 鍒涘缓鏂囦欢
	 * 
	 * @param pathname
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public File createFile(String pathname, String filename) throws Exception {
		File file = null;
		try {
			File catalog = new File(pathname);
			if (!catalog.exists()) {
				catalog.mkdirs();
			}
			file = new File(catalog, filename);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			logger.error("create file fail:", e);
		}
		return file;
	}

	/**
	 * 鏂囦欢杩藉姞鍐呭
	 * 
	 * @param content
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public void appendWrite(String content, File fileName) throws Exception {
		try {
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName,true)));
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void coverWrite(String content, File fileName) throws Exception{
		try {
			FileOutputStream fw = new FileOutputStream(fileName,true);
			fw.write(content.getBytes());
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 鍒涘缓妯℃澘
	 * 
	 * @param opentsdbIp
	 *            opentsdb鍦板潃
	 * @param alertName
	 *            鍛婅鍚嶇О
	 * @param ips
	 *            涓绘満IP鍦板潃
	 * @param metric
	 *            閲囬泦椤瑰悕绉�
	 * @param nameEn
	 *            绾у埆椤瑰悕绉�
	 * @param value
	 *            闃堝�煎ぇ灏�
	 * @return
	 */
	public String createTemplate(String alertName, String ips, String metric, String nameEn, Long value) {
		StringBuffer buffer = new StringBuffer();
		// cpu妯℃澘
		buffer.append("\r\n");
		buffer.append("alert ").append(alertName + " {");
		buffer.append("\r\n");
		JSONArray array = JSONArray.parseArray(ips);
		JSONArray alerts = new JSONArray();
		for(int i=0;i<array.size();i++){
			String ip = array.getString(i);
			ip = JSONTools.isJSONObj(ip).getString("ip");
			String alert = "$"+ip.substring(ip.length()-3,ip.length())+"_"+alertName;
			alerts.add(alert);
			buffer.append("    " +alert+ " =");
			buffer.append("avg(q("+"\"sum:rate:" + metric + "{host=" + ip + "}\",");
			buffer.append("\"1h\","+"\"\""+"))");
			buffer.append("\r\n");
		}
		buffer.append("    "+nameEn +" = ");
		for(int i=0;i<alerts.size();i++){
			if(i<alerts.size()-1){
				buffer.append("("+alerts.getString(i) + " > " + value+")"+" || ");
				continue;
			}
			buffer.append("("+alerts.getString(i) + " > " + value+")");
		}
		buffer.append("\r\n");
		buffer.append("}");
		return buffer.toString();
	}
}