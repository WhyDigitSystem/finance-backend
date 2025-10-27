package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.PyLoansOutStandingVO;

public interface PyLoansOutStandingRepo extends JpaRepository<PyLoansOutStandingVO, Long> {

	@Query(nativeQuery = true,value = "select * from pyloanoutstanding where orgid=?1 and clientcode=?2 and year=?3")
	List<PyLoansOutStandingVO> getClientBudgetDls(Long org, String clientcode, String yr);

	List<PyLoansOutStandingVO> findByOrgIdAndYearAndClientCode(Long org, String clientcode, String yr);

}
