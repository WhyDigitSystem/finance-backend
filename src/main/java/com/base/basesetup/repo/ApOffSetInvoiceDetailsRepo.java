package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ApAdjustmentOffSetVO;
import com.base.basesetup.entity.ApOffSetInvoiceDetailsVO;

@Repository
public interface ApOffSetInvoiceDetailsRepo extends JpaRepository<ApOffSetInvoiceDetailsVO, Long>{

	List<ApOffSetInvoiceDetailsVO> findByApAdjustmentOffSetVO(ApAdjustmentOffSetVO apAdjustmentOffSetVO);

}
