package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QuotationVO;

@Repository
public interface QuotationRepo extends JpaRepository<QuotationVO, Long>{

	@Query(nativeQuery =true,value ="select * from quotation where orgid=?1")
	List<QuotationVO> findQutationByOrgId(Long orgId);

	boolean existsByOrgIdAndQuotationNo(Long orgId, String quotationNo);

}
