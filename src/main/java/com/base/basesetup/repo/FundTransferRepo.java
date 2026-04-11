package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.FundTransferVO;

@Repository
public interface FundTransferRepo extends JpaRepository<FundTransferVO, Long> {

	@Query(nativeQuery = true, value = "select * from fundtransfer where orgid=?1")
	List<FundTransferVO> getAllFundTransferByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from fundtransfer where fundtransferid=?1")
	List<FundTransferVO> getAllFundTransferById(Long id); 

	@Query(nativeQuery = true, value = "select * from fundtransfer where active =?1")
	List<FundTransferVO> findFundTransferByActive();

//	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getFundTranferDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "select * from fundtransfer a where a.docid=?2 and orgid=?1",nativeQuery =true)
	FundTransferVO findAllFundTransferByDocId(Long orgId, String docId);	
	
	@Query(value = "SELECT " + "CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid, " + "CASE "
			+ "   WHEN CURDATE() BETWEEN f.startdate AND f.enddate " + "   THEN CURDATE() " + "   ELSE f.enddate "
			+ "END AS docdate " + "FROM documenttypemappingdetails d " + "JOIN financialyear f "
			+ "ON d.finyear = f.finyear AND d.orgid = f.orgid " + "WHERE d.orgid = ?1 " + "AND d.finyear = ?2 "
			+ "AND d.branchcode = ?3 " + "AND d.screencode = ?4", nativeQuery = true)
	List<Object[]> getFundTranferDocId(Long orgId, String finYear, String branchCode, String screenCode);


} 