package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.InvoiceResponseVO;

public interface InvoiceResponseRepo extends JpaRepository<InvoiceResponseVO, Long> {

}
