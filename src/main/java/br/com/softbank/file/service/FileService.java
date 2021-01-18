package br.com.softbank.file.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.softbank.file.enuns.ErrosDefaultEnum;
import br.com.softbank.file.enuns.ResourceEnum;
import br.com.softbank.file.exception.MediaTypeNotSupportedException;
import br.com.softbank.file.integration.SftpIntegration;

@Service
public class FileService {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Autowired
	private SftpIntegration sftpIntegration;

	public void upload(ResourceEnum resource, MultipartFile file) {
		SftpSession session = null;
		
		if(!file.getOriginalFilename().endsWith(".xlsx") &&  !file.getOriginalFilename().endsWith(".XLSX")) {
			throw new MediaTypeNotSupportedException(ErrosDefaultEnum.TIPO_MIDIA_NAO_SUPORTADO.getDescricao());
		}
		
		try {
			session = sftpIntegration.factory().getSession();
			
			StringBuilder sb = new StringBuilder("upload/");
			sb.append(resource.getDescricao());
			sb.append("-");
			sb.append(LocalDate.now().format(FORMATTER));
			sb.append(".xlsx");
			
			session.write(file.getInputStream(), sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(session != null) {
				session.close();
			}			
		}
	}
}
