package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.RetrievalManifestProviderVO;


@Repository
public interface RetrievalManifestProviderRepo extends JpaRepository<RetrievalManifestProviderVO, Long>{

	boolean existsByOrgIdAndTransactionNo(Long orgId, String transactionNo);

	@Query(nativeQuery = true,value = "select a.* from rim a where a.orgid=?1 and a.transactionno not in(\r\n"
			+ "select docid from binretrieval where orgid=?1 group by docid)")
	List<RetrievalManifestProviderVO> findPendingBinRetrievalDetails(Long orgId);
	
//	@Query(value = 
//			  "SELECT a.*\r\n"
//			  + "FROM rim a\r\n"
//			  + "WHERE a.orgid = ?2\r\n"
//			  + "  AND a.finyear = ?4\r\n"
//			  + "  AND 'RIM' = ?1\r\n"
//			  + "  AND (\r\n"
//			  + "    (\r\n"
//			  + "      ?3 IS NOT NULL\r\n"
//			  + "      AND ?5 IS NOT NULL\r\n"
//			  + "      AND ?6 IS NOT NULL\r\n"
//			  + "      AND a.sender = ?3\r\n"
//			  + "      AND a.transactiondate BETWEEN ?6 AND ?5\r\n"
//			  + "    )\r\n"
//			  + "    OR (\r\n"
//			  + "      ?3 IS NULL OR ?5 IS NULL OR ?6 IS NULL\r\n"
//			  + "    )\r\n"
//			  + "  )\r\n"
//			  + "",
//			  nativeQuery = true)
//List<RetrievalManifestProviderVO> findRIMReports(String type, Long orgId, String sender, String finYear, String toDate, String fromDate);

//	@Query(value = "SELECT \r\n" + "  m.*, \r\n" + "  ?1 AS type\r\n" + "FROM \r\n" + "  mim m\r\n" + "WHERE \r\n"
//			+ "  m.orgid = ?2\r\n" + "  AND m.finyear = ?4\r\n" + "  AND (\r\n" + "    (\r\n"
//			+ "      ?3 IS NOT NULL \r\n" + "      AND ?5 IS NOT NULL \r\n" + "      AND ?6 IS NOT NULL\r\n"
//			+ "      AND m.receiver = ?3\r\n" + "      AND m.transactiondate BETWEEN ?6 AND ?5\r\n" + "    )\r\n"
//			+ "    OR (\r\n" + "      ?3 IS NULL OR ?5 IS NULL OR ?6 IS NULL\r\n" + "    )\r\n"
//			+ "  )", nativeQuery = true)
//	List<IssueManifestProviderVO> findMIMReports(String type, Long orgId, String customerName, String finYear,
//			String toDate, String fromDate);
	
	@Query(value = 
			  "SELECT *\r\n"
			  + "FROM finance_aip.rim a\r\n"
			  + "WHERE ?1 = 'RIM'\r\n"
			  + "  AND a.orgid = ?2\r\n"
			  + "  AND (a.sender = ?3 OR ?3 = 'ALL')\r\n"
			  + "  AND a.finyear = ?4\r\n"
			  + "  AND (?5 IS NULL OR a.transactiondate >= ?5)\r\n"
			  + "  AND (?6 IS NULL OR a.transactiondate <= ?6)",
			  nativeQuery = true)
List<RetrievalManifestProviderVO> findRIMReports(String type, Long orgId, String sender, String finYear, String toDate, String fromDate);

}