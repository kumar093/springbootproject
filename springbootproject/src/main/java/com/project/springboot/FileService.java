package com.project.springboot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	private static String UPLOAD_PATH = "upload-dir";
	
	private final FileRepository repository;
	private final ResourceLoader resourceloader;
	
	@Autowired
	public FileService(FileRepository Repository,ResourceLoader loader){
		this.repository = Repository;
		this.resourceloader = loader;
	}
	
	public Page<File> findFile(Pageable pageable){
		
		return repository.findAll(pageable);
		
	}
	
	public Resource findOneFile(String filename){
		
		return resourceloader.getResource("file:" + UPLOAD_PATH + "/" + filename);
	}
	
	public void createFile(MultipartFile file)throws IOException{
		
		if(!file.isEmpty()){
			Files.copy(file.getInputStream(),Paths.get(UPLOAD_PATH, file.getOriginalFilename()));
			repository.save(new File(file.getOriginalFilename()));
		}
	}
		
	 public void deleteFile(String filename)throws IOException{
		 
		 final File byname = repository.FindByName(filename);
		 repository.delete(byname);
		 Files.deleteIfExists(Paths.get(UPLOAD_PATH, filename));
		
	}

}
