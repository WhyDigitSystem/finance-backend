package com.base.basesetup.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.entity.Document;
import com.base.basesetup.repo.DocumentRepository;

@Service
public class DocumentService {
	
	private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document saveDocument(MultipartFile file) throws IOException {
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        document.setPdfBlob(file.getBytes());

        return documentRepository.save(document);
    }
    
    // New method to save BLOB as PDF file
    public void saveBlobAsPdfFile(Long id, String outputFilePath) throws IOException {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        
        if (optionalDocument.isPresent()) {
            Document document = optionalDocument.get();
            
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(document.getPdfBlob());
                System.out.println("PDF file saved to " + outputFilePath);
            }
        } else {
            throw new IOException("Document with ID " + id + " not found.");
        }
    }

}
