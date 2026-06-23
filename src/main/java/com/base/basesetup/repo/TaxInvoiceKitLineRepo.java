package com.base.basesetup.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.InvoiceNewVO;
import com.base.basesetup.entity.TaxInvoiceKitLineVO;


public interface TaxInvoiceKitLineRepo extends JpaRepository<TaxInvoiceKitLineVO, Long> {

	List<TaxInvoiceKitLineVO> findByInvoiceNewVO(InvoiceNewVO taxInvoiceVO);

}
