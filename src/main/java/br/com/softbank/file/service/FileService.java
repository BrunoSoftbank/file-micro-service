package br.com.softbank.file.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
	
	public void download(ResourceEnum resource,  HttpServletResponse response) throws Exception {
		String nomeArquivo = "Modelo ".concat(resource.getDescricao().concat(".xlsx"));
	
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(nomeArquivo);
			sheet.setDefaultColumnWidth(30);

			CellStyle style = workbook.createCellStyle();
			org.apache.poi.ss.usermodel.Font font = workbook.createFont();
			font.setFontName("Arial");
			font.setBold(true);
			style.setFont(font);

			if(resource.equals(ResourceEnum.exames)) {
				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("Nome");
				header.getCell(0).setCellStyle(style);
				header.createCell(1).setCellValue("Tipo Id");
				header.getCell(1).setCellStyle(style);
					
			} else {
				Row header = sheet.createRow(0);
				header.createCell(0).setCellValue("Nome");
				header.getCell(0).setCellStyle(style);
				header.createCell(1).setCellValue("Cidade");
				header.getCell(1).setCellStyle(style);
				header.createCell(2).setCellValue("Bairro");
				header.getCell(2).setCellStyle(style);
				header.createCell(3).setCellValue("Rua");
				header.getCell(3).setCellStyle(style);
				header.createCell(4).setCellValue("Número");
				header.getCell(4).setCellStyle(style);
			}
			
			response.setHeader("content-disposition", "attachment; filename=" + nomeArquivo);
			response.setContentType("application/xlsx");

			workbook.write(response.getOutputStream());

			response.flushBuffer();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
}
