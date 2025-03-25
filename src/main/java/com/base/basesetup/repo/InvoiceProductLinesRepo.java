package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.InvoiceProductLinesVO;
import com.base.basesetup.entity.InvoiceVO;

@Repository
public interface InvoiceProductLinesRepo extends JpaRepository<InvoiceProductLinesVO, Long>{

	List<InvoiceProductLinesVO> findByInvoiceVO(InvoiceVO invoiceVO);

}
