package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.TaxInvoiceAnnexureVO;
import com.base.basesetup.entity.TaxInvoiceVO;
@Repository
public interface TaxInvoiceAnnexureRepo extends JpaRepository<TaxInvoiceAnnexureVO, Long>{

	List<TaxInvoiceAnnexureVO> findByTaxInvoiceVO(TaxInvoiceVO taxInvoiceVO);

}
