package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.ArapDetailsVO;

public interface ArapDetailsRepo extends JpaRepository<ArapDetailsVO, Long> {

	@Query(nativeQuery = true, value = "select * from arapdetails where orgid=?1")
	List<ArapDetailsVO> getAllArapDetailsByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * form arapdetails where arapdetailsid=?1")
	List<ArapDetailsVO> getAllArapDetailsById(Long id);

	@Query(nativeQuery = true, value = "select * from arapdetails where active=1")
	List<ArapDetailsVO> findArapDetailsByActive();

	@Query(value = "select a from ArapDetailsVO a where a.id=?1")
	String getArapDetailsDocId(Long orgId, String finYear, String branchCode, String screenCode);
	
	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	ArapDetailsVO findArapDetailsByDocId(Long orgId, String docId);

	List<ArapDetailsVO> findByRefNo(String refNo);

}
