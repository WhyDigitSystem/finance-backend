package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.ToleranceMasterVO;

public interface ToleranceMasterRepo extends JpaRepository<ToleranceMasterVO, Long> {

}
