package com.base.basesetup.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.DeclarationAndNotesVO;

@Repository
public interface declarationAndNotesRepo extends JpaRepository<DeclarationAndNotesVO, Long>{

}
