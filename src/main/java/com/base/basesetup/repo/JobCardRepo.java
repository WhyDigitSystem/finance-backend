package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.JobCardVO;

@Repository
public interface JobCardRepo extends JpaRepository<JobCardVO, Long> {

	@Query(nativeQuery = true, value = "select * from jobcard where orgid=?1")
	List<JobCardVO> getAllJobCardByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from jobcard where jobcardid=?1")
	JobCardVO getAllJobCardById(Long id);

	@Query(nativeQuery = true, value = "select  a1.salesperson from partymaster a,partysalespersontagging a1 where active=1 and  a.partymasterid=a1.partymasterid\r\n"
			+ "	and a.orgid=?1  and a.partyname=?2 group by a1.salesperson")
	Set<Object[]> findBySalesPreson(Long orgId, String partyName);

	@Query(nativeQuery = true, value = "select partyname from partymaster where orgid=?1 and active=1 group by partyname")
	Set<Object[]> findAllCustomers(Long orgId);

	@Query(nativeQuery = true, value = "select a.totalinvamountlc from taxinvoice a,jobcard b where a.orgid=?1 and\r\n"
			+ "a.partyname=?2  and a.joborderno=b.jobno  order by a.totalinvamountlc")
	Set<Object[]> getIncomeByTaxInvoice(Long orgId, String customerName);

	@Query(nativeQuery = true, value = "select a.netbillcurramt from costinvoice a,jobcard b ,chargercostinvoice a1 where a.orgid=?1  and \r\n"
			+ "a1.costinvoiceid=a.costinvoiceid\r\n"
			+ "and a1.jobno=b.jobno  order by a.netbillcurramt")
	Set<Object[]> getExponesByCostInvoice(Long orgId);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getJobCardDocId(Long orgId, String finYear, String branchCode, String screenCode);

}
