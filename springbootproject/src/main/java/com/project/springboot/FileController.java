package com.project.springboot;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {
	
	public static final String BASE_PATH = "/images";
	public static final String FILENAME = "{filename:.+}";
			
	
	private final FileService fileservice;
	
	@Autowired
	public FileController(FileService service){
		
		this.fileservice = service;
	}
	
	@RequestMapping("/")
	public String index(Model model, Pageable pageable){
		
		final Page<File> page = fileservice.findFile(pageable);
		model.addAttribute("page", page);
		return "index";
		
	}

	@RequestMapping(method = RequestMethod.GET, value = BASE_PATH + "/" + FILENAME + "/raw")
	@ResponseBody
	public ResponseEntity<?> oneFile(@PathVariable String filename) {

		try{
		Resource file = fileservice.findOneFile(filename);
		return  ResponseEntity.ok()
		            .contentLength(file.contentLength())
		            .contentType(MediaType.IMAGE_JPEG)
		            .body(new InputStreamResource(file.getInputStream()));	                          
		
	}catch(IOException e){
		
		return ResponseEntity.badRequest().body("couldn't find filename" + filename + e.getMessage());
	}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = BASE_PATH)
	@ResponseBody
	public String createFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectattributes){
           
		try{
			fileservice.createFile(file);
			redirectattributes.addFlashAttribute("flash.message", "succsessfully uplaoded" + file.getOriginalFilename());
		}catch(IOException e){
			redirectattributes.addFlashAttribute("flash.message", "failed to uplaoded" + file.getOriginalFilename() + e.getMessage());
		}
		return "redirect:/";
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = BASE_PATH + "/" + FILENAME)
	@ResponseBody
	public ResponseEntity<?> deleteFile(@PathVariable String filename) {

		try{
		        fileservice.deleteFile(filename);
		        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("successfully deleted" + filename);
		
	}catch(IOException e){
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed to delete" + filename + e.getMessage());
	}
	}
	
	
}
