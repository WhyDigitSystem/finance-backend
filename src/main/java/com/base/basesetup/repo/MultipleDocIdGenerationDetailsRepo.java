package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;

@Repository
public interface MultipleDocIdGenerationDetailsRepo extends JpaRepository<MultipleDocIdGenerationDetailsVO, Long> {

	@Query(nativeQuery = true, value = "select * from multipledocidgendetails where orgid=?1 and finyear=?2 and branchcode=?3 and sourcescreencode=?4 and screencode=?5")
	MultipleDocIdGenerationDetailsVO findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(Long orgId,
			String finYear, String branchCode, String sourceScreenCode, String screenCode);

}
