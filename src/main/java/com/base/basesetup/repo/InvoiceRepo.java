package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.InvoiceVO;

@Repository
public interface InvoiceRepo extends JpaRepository<InvoiceVO, Long>{

	boolean existsByOrgIdAndPoNumber(Long orgId, String poNumber);

	@Query(nativeQuery =true,value ="select * from invoice where orgid=?1")
	List<InvoiceVO> findAllByOrgId(Long orgId);

}
