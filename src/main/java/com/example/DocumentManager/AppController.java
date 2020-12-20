package com.example.DocumentManager;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {
	static int i=0;

	@Autowired
	private DocumentRepository repo;
	@GetMapping("/")
	public String viewHomePage(Model model) {
		List<Document> listdocuments=repo.findAll();
		model.addAttribute("listdocuments",listdocuments);
		return "home";
	}
	
	@PostMapping("/upload")
	
	public String uploadFile(@RequestParam("document") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException  {
		
		String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Document document=new Document();
		document.setId(i++);
		document.setName(fileName);
		document.setContent(multipartFile.getBytes());
		document.setSize(multipartFile.getSize());
		document.setUploadTime(new Date());
		
		repo.save(document);
		ra.addFlashAttribute("message", "File Uploaded Successfully");
		return "redirect:/";
	}
	
	@GetMapping("/download")
	public void downloadFile(@Param("id") long id,HttpServletResponse response) throws Exception {
		Optional<Document> result=repo.findById(id);
		if(!result.isPresent()) {
			throw new Exception ("Could not find id "+id);
		}
		Document document=result.get();
		response.setContentType("application/octet-stream");
		String headerkey="Content-Disposition";
		String headervalue="attachment; filename"+document.getName();
		
		response.setHeader(headerkey, headervalue);
		ServletOutputStream oout=response.getOutputStream();
		oout.write(document.getContent());
		oout.close();
	}
}
