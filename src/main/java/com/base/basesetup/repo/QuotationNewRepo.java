package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QuotationNewVO;

@Repository
public interface QuotationNewRepo extends JpaRepository<QuotationNewVO, Long>{

	@Query(nativeQuery =true,value ="select * from quotationnew where orgid=?1")
	List<QuotationNewVO> getQuotationByorgId(Long orgId);

}
