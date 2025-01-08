package com.base.basesetup.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ArApAdjustmentOffSetVO;


@Repository
public interface ArApAdjustmentOffSetRepo extends JpaRepository<ArApAdjustmentOffSetVO, Long>{

	@Query(nativeQuery = true,value = "select * from arapadjustmentoffset where orgid=?1")
	List<ArApAdjustmentOffSetVO> getAllArApAdjustmentOffSetByOrgId(Long orgId);

	@Query(nativeQuery = true,value = "select * from arapadjustmentoffset where arapadjustmentoffsetid=?1")
	List<ArApAdjustmentOffSetVO> getAllArApAdjustmentOffSetById(Long id);


	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getArApAdjustmentOffSetDocId(Long orgId, String finYear, String branchCode, String screenCode);

}
