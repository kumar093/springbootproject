package com.project.springboot;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends PagingAndSortingRepository<File,Long> {
	
	public File FindByName(String name);

}
