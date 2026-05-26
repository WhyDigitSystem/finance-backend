package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.RegisterCostInvoiceGnaVO;
import com.base.basesetup.entity.TdsRegisterCostInvoiceGnaVO;

@Repository
public interface TdsRegisterCostInvoiceGnaRepo extends JpaRepository<TdsRegisterCostInvoiceGnaVO, Long> {

	List<TdsRegisterCostInvoiceGnaVO> findByRegisterCostInvoiceGnaVO(RegisterCostInvoiceGnaVO registerCostInvoiceGnaVO);

}
