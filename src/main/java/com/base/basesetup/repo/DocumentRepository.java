package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
