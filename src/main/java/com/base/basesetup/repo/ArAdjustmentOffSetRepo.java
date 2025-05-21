package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ArAdjustmentOffSetVO;

@Repository
public interface ArAdjustmentOffSetRepo extends JpaRepository<ArAdjustmentOffSetVO, Long>{

	@Query(nativeQuery = true, value = "select * from aradjustmentoffset where orgid=?1")
	List<ArAdjustmentOffSetVO> getAllArAdjustmentOffSetByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from aradjustmentoffset where aradjustmentoffsetid=?1")
	List<ArAdjustmentOffSetVO> getArAdjustmentOffSetById(Long id);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getArAdjustmentOffSetByDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getArAdjustmentOffSetDocId(Long orgId, String finYear, String branchCode, String screenCode);

	ArAdjustmentOffSetVO findByOrgIdAndIdAndDocId(Long orgId, Long id, String docId);

}
