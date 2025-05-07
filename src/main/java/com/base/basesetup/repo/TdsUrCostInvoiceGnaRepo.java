package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.TdsUrCostInvoiceGnaVO;
import com.base.basesetup.entity.UrCostInvoiceGnaVO;

@Repository
public interface TdsUrCostInvoiceGnaRepo extends JpaRepository<TdsUrCostInvoiceGnaVO, Long> {

	List<TdsUrCostInvoiceGnaVO> findByUrCostInvoiceGnaVO(UrCostInvoiceGnaVO urCostInvoiceGnaVO);

}
