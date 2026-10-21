package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.InvoiceNewVO;

@Repository
public interface InvoiceNewRepo extends JpaRepository<InvoiceNewVO, Long>{
	
	List<InvoiceNewVO> findAllByOrgId(Long orgId);

	boolean existsByOrgIdAndInvoiceNo(Long orgId, String invoiceNo);

}
