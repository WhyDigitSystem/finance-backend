package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ArAdjustmentOffSetVO;
import com.base.basesetup.entity.ArOffSetInvoiceDetailsVO;

@Repository
public interface ArOffSetInvoiceDetailsRepo extends JpaRepository<ArOffSetInvoiceDetailsVO, Long>{

	List<ArOffSetInvoiceDetailsVO> findByArAdjustmentOffSetVO(ArAdjustmentOffSetVO arAdjustmentOffSetVO);

}
