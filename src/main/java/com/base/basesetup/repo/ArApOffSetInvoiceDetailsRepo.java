package com.base.basesetup.repo;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ArApAdjustmentOffSetVO;
import com.base.basesetup.entity.ArApOffSetInvoiceDetailsVO;
@Repository
public interface ArApOffSetInvoiceDetailsRepo extends JpaRepository<ArApOffSetInvoiceDetailsVO, Long>{

	List<ArApOffSetInvoiceDetailsVO> findByArApAdjustmentOffSetVO(ArApAdjustmentOffSetVO arApAdjustmentOffSetVO);



}
