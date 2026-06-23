package com.base.basesetup.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.InvoiceNewVO;
import com.base.basesetup.entity.TaxInvoiceProductLineVO;


public interface TaxInvoiceProductLineRepo extends JpaRepository<TaxInvoiceProductLineVO, Long> {

	List<TaxInvoiceProductLineVO> findByInvoiceNewVO(InvoiceNewVO taxInvoiceVO);

}
