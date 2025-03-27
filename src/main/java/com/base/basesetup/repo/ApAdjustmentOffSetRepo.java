package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ApAdjustmentOffSetVO;

@Repository
public interface ApAdjustmentOffSetRepo extends JpaRepository<ApAdjustmentOffSetVO, Long>{

	@Query(nativeQuery = true, value = "select * from apadjustmentoffset where orgid=?1")
	List<ApAdjustmentOffSetVO> getAllApAdjustmentOffSetByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from apadjustmentoffset where apadjustmentoffsetid=?1")
	List<ApAdjustmentOffSetVO> getApAdjustmentOffSetById(Long id);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getApAdjustmentOffSetDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getApAdjustmentOffSetByDocId(Long orgId, String finYear, String branchCode, String screenCode);

}
