package br.com.softbank.file.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.stereotype.Component;

@Component
public class SftpIntegration {
	
	@Value("sftp_host")
	private String host;
	@Value("sftp_port")
	private String port;
	@Value("sftp_user")
	private String user;
	@Value("sftp_pass_word")
	private String passWord;

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
