package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.OffSetVO;

public interface OffSetRepo extends JpaRepository<OffSetVO, Long> {

	@Query(value = "select a from OffSetVO a where a.orgId=?1 and a.finYear=?2 and a.branchCode=?3 order by a.docId desc ")
	List<OffSetVO> findByOrgIdAndFinYearAndBranchCode(Long orgId, String finYear, String branchCode);

	String getOffSetDocId(Long orgId, String finYear, String branchCode, String screenCode);

	

}
