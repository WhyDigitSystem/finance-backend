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
	
	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getGeneralJournalDocId(Long orgId, String finYear, String branchCode, String screenCode);

	
	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from multipledocidgendetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getGeneralJournalByDocId(Long orgId, String finYear, String branchCode, String screenCode);

	@Query(nativeQuery = true, value = "select accountgroupname from groupledger where orgid=?1 and type='account'  and  active=1 and category not in('TAX','PAYABLE A/C','RECEIVABLE A/C')")
	Set<Object[]> findAccountNameFromGroup( Long orgId);
	
	@Query(nativeQuery = true, value = "select  distinct(category),case when category ='RECEIVABLE A/C'  then 'RECEIVABLE'  when category = 'PAYABLE A/C' then 'PAYABLE' else 'OTHERS' end as accountname from groupledger a0 \r\n"
			+ "where  a0.Active=1 and a0.category not in ('TAX','')and a0.category is not null and a0.orgid=?1")
	Set<Object[]> findAccountNameFromGroupLedgerGeneral(Long orgId);

	@Query(nativeQuery = true, value = "select a1.partyshortname, a1.partycode from partymaster a1 where a1.active=1 and a1.orgid=?1 and  a1.accounttype =?2 order by a1.partyname")
	Set<Object[]> findSubLedgerNameFromPartyMaster(Long orgId,String accountName);


}
