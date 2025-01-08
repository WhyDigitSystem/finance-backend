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
	List<JobCardVO> getAllJobCardById(Long id);

	@Query(nativeQuery = true, value = "select  a1.salesperson from partymaster a,partysalespersontagging a1 where active=1 and  a.partymasterid=a1.partymasterid\r\n"
			+ "	and a.orgid=?1  and a.partyname=?2 group by a1.salesperson")
	Set<Object[]> findBySalesPreson(Long orgId,String partyName);
	
	@Query(nativeQuery = true,value="select partyname from partymaster where orgid=?1 and active=1 group by partyname")
	Set<Object[]> findAllCustomers(Long orgId);

	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getJobCardDocId(Long orgId, String finYear, String branchCode, String screenCode);


}
