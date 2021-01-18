package br.com.softbank.file.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.softbank.file.enuns.ResourceEnum;
import br.com.softbank.file.service.FileService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/v1/files")
@Api(tags = "Recurso de Files")
public class FileController {

	@Autowired
	private FileService fileService;
	
	@PostMapping
	public ResponseEntity<Void> upload(@RequestHeader String  Authorization, @RequestParam ResourceEnum resource, MultipartFile file) {
		fileService.upload(resource, file);
		return ResponseEntity.noContent().build();
	}	
}
