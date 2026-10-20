package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QuotationDetailsNewVO;
import com.base.basesetup.entity.QuotationNewVO;

@Repository
public interface QuotationDetailsNewRepo extends JpaRepository<QuotationDetailsNewVO, Long>{

	List<QuotationDetailsNewVO> findByQuotationNewVO(QuotationNewVO quotationVO);

}
