package com.example.DocumentManager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document, Long>{

	@Query("select new Document(d.id,d.name,d.size) from Document d order by d.uploadTime desc")
	List<Document> findAll();
}
