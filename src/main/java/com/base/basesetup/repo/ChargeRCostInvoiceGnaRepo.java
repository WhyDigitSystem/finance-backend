package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ChargeRCostInvoiceGnaVO;
import com.base.basesetup.entity.RCostInvoiceGnaVO;

@Repository
public interface ChargeRCostInvoiceGnaRepo extends JpaRepository<ChargeRCostInvoiceGnaVO, Long>{


	List<ChargeRCostInvoiceGnaVO> findByrCostInvoiceGnaVO(RCostInvoiceGnaVO rCostInvoiceGnaVO);

//	List<ChargeRCostInvoiceGnaVO> findByRCostInvoiceGnaVO(RCostInvoiceGnaVO rCostInvoiceGnaVO);



}
