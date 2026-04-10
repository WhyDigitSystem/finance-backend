package com.base.basesetup.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.base.basesetup.entity.FinancialYearVO;

public interface FinancialYearRepo extends JpaRepository<FinancialYearVO, Long> {
	@Query(nativeQuery = true, value = "select * from finyear where company=?1")
	List<FinancialYearVO> findFinyearByCompany(String company);

	@Query(value = "select * from financialyear where orgid=?1", nativeQuery = true)
	List<FinancialYearVO> findFinancialYearByOrgId(Long orgId);

	@Query(value = "select a from FinancialYearVO a where a.orgId=?1 and a.active=true and a.closed=false")
	List<FinancialYearVO> findAllActiveFinYear(Long orgId);

	boolean existsByFinYearAndOrgId(int finYear, Long orgId);

	boolean existsByFinYearIdentifierAndOrgId(String finYearIdentifier, Long orgId);

	boolean existsByFinYearIdAndOrgId(Long finYearId, Long orgId);

	FinancialYearVO findByOrgIdAndFinYear(Long orgId, int finyear);

	FinancialYearVO findByOrgIdAndFinYearIdentifierAndYearType(Long orgId, String finyear, String yearType);

	FinancialYearVO findByOrgIdAndFinYearAndYearType(Long orgId, int preYear, String yearType);

	@Query(nativeQuery = true, value = "select b.finyearidentifier from clientcompany a,financialyear b where a.orgid=b.orgid and a.clientcode=?2 and a.orgid=?1 and a.clientyear=b.yeartype\r\n"
			+ "order by b.finyearidentifier desc")
	Set<Object[]> getClientFinYear(Long orgId, String clientCode);

//	@Query("SELECT f FROM FinancialYearVO f " + "WHERE :today BETWEEN f.startDate AND f.endDate "
//			+ "AND f.orgId = :orgId")
//	FinancialYearVO findCurrentYearByDate(@Param("orgId") Long orgId, @Param("today") LocalDate today);
//
//	@Query("SELECT f FROM FinancialYearVO f WHERE f.orgId = :orgId AND f.finYear = :finYear")
//	FinancialYearVO findByOrgIdAndFinyear(@Param("orgId") Long orgId, @Param("finYear") String finYear);

}