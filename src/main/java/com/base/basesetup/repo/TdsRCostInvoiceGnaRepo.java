package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.RCostInvoiceGnaVO;
import com.base.basesetup.entity.TdsRCostInvoiceGnaVO;

@Repository
public interface TdsRCostInvoiceGnaRepo extends JpaRepository<TdsRCostInvoiceGnaVO, Long>{

	List<TdsRCostInvoiceGnaVO> findByrCostInvoiceGnaVO(RCostInvoiceGnaVO rCostInvoiceGnaVO);

//	List<TdsRCostInvoiceGnaVO> findByRCostInvoiceGnaVO(RCostInvoiceGnaVO rCostInvoiceGnaVO);



}
