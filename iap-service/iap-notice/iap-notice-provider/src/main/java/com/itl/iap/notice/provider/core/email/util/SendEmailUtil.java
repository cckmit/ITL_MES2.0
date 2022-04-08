package com.itl.iap.notice.provider.core.email.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮件发送工具类
 *
 * @author 曾慧任
 * @date  2020/06/29
 * @since jdk1.8
 */
public class SendEmailUtil extends Authenticator {
	private static byte[] ZIP_HEADER_1 = new byte[] { 80, 75, 3, 4 };
	private static byte[] ZIP_HEADER_2 = new byte[] { 80, 75, 5, 6 };
	static {
		System.setProperty("mail.mime.splitlongparameters", "false");
		System.setProperty("mail.mime.charset", "UTF-8");
	}

	public static Session initProperties(String account, String password, String protocol, String host, String port, boolean enableSsl) {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", protocol);
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		MailSSLSocketFactory mailSslSocketFactory = null;
		try {
			mailSslSocketFactory = new MailSSLSocketFactory();
			mailSslSocketFactory.setTrustAllHosts(true);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		properties.put("mail.smtp.enable", "true");

		if(enableSsl){
			properties.put("mail.smtp.ssl.socketFactory", mailSslSocketFactory);
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.socketFactory.fallback", "false");
			properties.put("mail.smtp.socketFactory.port", port);
		}

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account, password);
			}
		});
		session.setDebug(true);
		return session;
	}

//	public static void send(Session session, String subject, String content, Address[] TOAddress, String[] fileSrc,
//							String account, String password, String[] fileName, Address[] CCAddress) throws Exception {
//		MimeMessage mimeMessage = new MimeMessage(session);
//		mimeMessage.setFrom(new InternetAddress(account));// 发件人,可以设置发件人的别名
//
//		//邮件接收人
//        mimeMessage.addRecipients(Message.RecipientType.TO ,TOAddress);
//        //抄送地址
//        mimeMessage.addRecipients(Message.RecipientType.CC ,CCAddress);
//		mimeMessage.setSubject(subject);
//		mimeMessage.setSentDate(new Date());
//		Multipart mainParth = new MimeMultipart();
//		BodyPart html = new MimeBodyPart();
//		html.setContent(content, "text/html; charset=UTF-8");
//		mainParth.addBodyPart(html);
//		MimeBodyPart bodyPart = new MimeBodyPart();
//		if (fileSrc != null && fileSrc.length > 0) {
//			URL url;
//			for (int i = 0; i < fileSrc.length; i++) {
//				String devAgreement = "http";
//				if (fileSrc[i].startsWith(devAgreement)) {
//					bodyPart = new MimeBodyPart();
//					url = new URL(fileSrc[i]);
//					DataSource dataSource = new URLDataSource(url);
//					DataHandler dataHandler = new DataHandler(dataSource);
//					bodyPart.setDataHandler(dataHandler);
//					mainParth.addBodyPart(bodyPart);
//				}
//			}
//		}
//		mimeMessage.setContent(mainParth);
//		Transport.send(mimeMessage);
//	}



	private void fileError(File file) {
		throw new RuntimeException("文件路径不正确");
	}

	/**
	 * 判断文件是否为一个压缩文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isArchiveFile(File file) {

		if (file == null) {
			return false;
		}

		if (file.isDirectory()) {
			return false;
		}

		boolean isArchive = false;
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			byte[] buffer = new byte[4];
			int length = input.read(buffer, 0, 4);
			if (length == 4) {
				isArchive = (Arrays.equals(ZIP_HEADER_1, buffer)) || (Arrays.equals(ZIP_HEADER_2, buffer));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}

		return isArchive;
	}

	public static boolean isMailAddr(String mailString) {
		String pattenString = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		Pattern pattern = Pattern.compile(pattenString);
		Matcher matcher = pattern.matcher(mailString);
		return matcher.matches();
	}

}
