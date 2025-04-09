package com.base.basesetup.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QuotationVO;

@Repository
public interface QuotationRepo extends JpaRepository<QuotationVO, Long>{

	@Query(nativeQuery =true,value ="select * from quotation where orgid=?1")
	List<Map<String, Object>> findQutationByOrgId(Long orgId);

}
