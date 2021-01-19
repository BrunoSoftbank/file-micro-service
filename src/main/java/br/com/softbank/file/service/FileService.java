package br.com.softbank.file.service;

import java.io.FileInputStream;
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

		if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".XLSX")) {
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
			if (session != null) {
				session.close();
			}
		}
	}

	public void download(ResourceEnum resource, HttpServletResponse response) throws Exception {
		String arquivo = "src/main/resources/modelo/arquivos/".concat(resource.getDescricao().concat(".xlsx"));

		Workbook workbook = new XSSFWorkbook(new FileInputStream(arquivo));

		response.setHeader("content-disposition", "attachment; filename=" + resource.getDescricao().concat(".xlsx"));
		response.setContentType("application/xlsx");

		workbook.write(response.getOutputStream());

		response.flushBuffer();
		response.getOutputStream().flush();
		response.getOutputStream().close();

	}
}
