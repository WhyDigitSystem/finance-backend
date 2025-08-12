package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.IRNResponseVO;

public interface IRNResponseRepo extends JpaRepository<IRNResponseVO, Long> {

	IRNResponseVO findByDocid(String docId);

}
