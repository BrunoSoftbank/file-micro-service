package br.com.softbank.file.integration;

import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.stereotype.Component;

@Component
public class SftpIntegration {
	
	private String host = "localhost";
	private String port = "2222";
	private String user = "admin";
	private String passWord = "admin123";

	public DefaultSftpSessionFactory factory() {
		DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
		factory.setHost(host);
		factory.setPort(Integer.valueOf(port));
		factory.setUser(user);
		factory.setPassword(passWord);
		factory.setAllowUnknownKeys(true);		
		return factory;
	}
}
