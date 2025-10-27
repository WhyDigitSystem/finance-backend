package com.base.basesetup.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.MonthlyProcessVO;

public interface MonthlyProcessRepo extends JpaRepository<MonthlyProcessVO, Long> {

	

	List<MonthlyProcessVO> findByOrgIdAndClientCodeAndMainGroupAndSubGroupCodeAndYear(Long orgId, String clientCode,
			String mainGroup, String subGroupCode, String finYear);

	

	@Query(value = "select a from MonthlyProcessVO a where a.orgId=?1 and a.clientCode=?2 and a.year=?3 and a.month=?4 and a.mainGroup=?5 and a.subGroupCode=?6")
	MonthlyProcessVO getOrgIdAndClientCodeAndYearAndMonthAndMainGroupAndSubGroupCode(Long orgId, String clientCode,
			String year, String month, String mainGroup, String subGroupCode);

	Optional<MonthlyProcessVO> findByOrgIdAndClientCodeAndYearAndMonthAndMainGroupAndSubGroup(Long orgId,
			String clientCode, String year, String month, String mainGroup, String subGroup);


	@Query(value = "select a from MonthlyProcessVO a where a.orgId=?1 and a.clientCode=?2 and a.year=?3 and a.month=?4 and a.mainGroup=?5 and a.subGroup=?6")
	MonthlyProcessVO getOrgIdAndClientCodeAndYearAndMonthAndMainGroupAndSubGroup(Long orgId, String clientCode,
			String year, String month, String mainGroup, String subGroup);


}
