package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ChargesUrCostInvoiceGnaVO;
import com.base.basesetup.entity.UrCostInvoiceGnaVO;

@Repository
public interface ChargesUrCostInvoiceGnaRepo extends JpaRepository<ChargesUrCostInvoiceGnaVO, Long> {

	List<ChargesUrCostInvoiceGnaVO> findByUrCostInvoiceGnaVO(UrCostInvoiceGnaVO urCostInvoiceGnaVO);

}
