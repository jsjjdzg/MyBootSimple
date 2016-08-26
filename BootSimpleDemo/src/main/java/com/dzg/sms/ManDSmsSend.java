package com.dzg.sms;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;



public class ManDSmsSend{
	
	private static Logger LogUtil = Logger.getLogger(ManDSmsSend.class);
	/**
	 * 发送短信
	 * @param mobile 手机号码，","分隔
	 * @param content 内容
	 */
	
	public static String SendSms(String mobile, String content){
		String sn="SDK-BJR-010-00010";
		String pwd="a3-(63c-";
		Client client = null;
		try {
			client = new Client(sn,pwd);
			
			//短信发送	
			content   =   java.net.URLEncoder.encode(content+"【丁先生】",  "utf-8");    
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String result_mt = client.mdgxsend(mobile, content, "1", "", "", "");	
		LogUtil.info("调用短信通道成功！返回值+"+result_mt+",mobile="+mobile+",content="+content);
		return result_mt;
	}
	
	@SuppressWarnings("unused")
	private static String request(String url, NameValuePair[] params) {
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestBody(params);
		int statusCode = 0;
		try {
			statusCode = client.executeMethod(postMethod);
		} catch (HttpException e) {
		} catch (IOException e) {
		}
		try {
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				return result;
			} else {
			}
		} catch (IOException e) {
		}
		postMethod.releaseConnection();
		return result;
	}
	
	public static void main(String[] args) {
		SendSms("17186387930","今天干嘛啊？");
		//SendSms("15665192553","今天干嘛啊？");
	}
}
