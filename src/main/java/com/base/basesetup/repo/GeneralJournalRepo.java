package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.GeneralJournalVO;

public interface GeneralJournalRepo extends JpaRepository<GeneralJournalVO, Long> {

	@Query(nativeQuery = true, value = "select * from generaljournal where orgid=?1")
	List<GeneralJournalVO> getAllGeneralJournalByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from generaljournal where generaljournalid=?1")
	List<GeneralJournalVO> getGeneralJournalById(Long id);

	@Query(nativeQuery = true, value = "select * from generaljournal where active=1")
	List<GeneralJournalVO> findGeneralJournalByActive();
	
//	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
//	String getGeneralJournalDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(value = "SELECT " + "CONCAT(d.prefixfield, LPAD(d.lastno, 5, '0')) AS docid, " + "CASE "
			+ "   WHEN CURDATE() BETWEEN f.startdate AND f.enddate " + "   THEN CURDATE() " + "   ELSE f.enddate "
			+ "END AS docdate " + "FROM documenttypemappingdetails d " + "JOIN financialyear f "
			+ "ON d.finyear = f.finyear AND d.orgid = f.orgid " + "WHERE d.orgid = ?1 " + "AND d.finyear = ?2 "
			+ "AND d.branchcode = ?3 " + "AND d.screencode = ?4", nativeQuery = true)
	List<Object[]> getGeneralJournalDocId(Long orgId, String finYear, String branchCode, String screenCode);

	
	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getGeneralJournalByDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "select accountgroupname from groupledger where orgid=?1 and type='account'  and  active=1 and category not in('TAX','PAYABLE A/C','RECEIVABLE A/C')")
	Set<Object[]> findAccountNameFromGroup( Long orgId);
	
	@Query(nativeQuery = true, value = "SELECT \r\n"
			+ "    accountcode,\r\n"
			+ "    accountgroupname AS accountname\r\n"
			+ "FROM \r\n"
			+ "    groupledger a0\r\n"
			+ "WHERE \r\n"
			+ "    a0.Active = 1\r\n"
			+ "    AND a0.category NOT IN ('BANK', 'CASH', 'PAYABLE A/C', 'RECEIVABLE A/C', 'TAX')\r\n"
			+ "    AND a0.type = 'ACCOUNT'\r\n"
			+ "    AND a0.category IS NOT NULL\r\n"
			+ "    AND a0.orgid = ?1")
	Set<Object[]> findAccountNameFromGroupLedgerGeneral(Long orgId);

	@Query(nativeQuery = true, value = "select a1.partyshortname, a1.partycode from partymaster a1 where a1.active=1 and a1.orgid=?1 and  a1.accounttype =?2 order by a1.partyname")
	Set<Object[]> findSubLedgerNameFromPartyMaster(Long orgId,String accountName);


}
