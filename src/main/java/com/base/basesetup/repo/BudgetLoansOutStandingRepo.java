package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.BudgetLoansOutStandingVO;

public interface BudgetLoansOutStandingRepo extends JpaRepository<BudgetLoansOutStandingVO, Long> {

	@Query(nativeQuery = true,value = "select * from budgetloanoutstanding where orgid=?1 and clientcode=?2 and year=?3")
	List<BudgetLoansOutStandingVO> getClientBudgetDls(Long org, String clientcode, String yr);

	List<BudgetLoansOutStandingVO> findByOrgIdAndYearAndClientCode(Long orgId, String year, String clientCode);

}
