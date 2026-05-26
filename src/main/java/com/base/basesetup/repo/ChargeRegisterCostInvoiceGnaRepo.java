package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ChargeRegisterCostInvoiceGnaVO;
import com.base.basesetup.entity.RegisterCostInvoiceGnaVO;

@Repository
public interface ChargeRegisterCostInvoiceGnaRepo extends JpaRepository<ChargeRegisterCostInvoiceGnaVO, Long> {

	List<ChargeRegisterCostInvoiceGnaVO> findByRegisterCostInvoiceGnaVO(
			RegisterCostInvoiceGnaVO registerCostInvoiceGnaVO);

}
