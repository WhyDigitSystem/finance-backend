package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.CustomerVO;

public interface CustomerRepo extends JpaRepository<CustomerVO, Long> {

	@Query(value = "select a.customer,b.client from customer a,client b where a.customerid=b.customerid and a.orgid =?1 group by a.customer,b.client;",nativeQuery = true)
	Set<Object[]> findAllCustomerAndClientByOrgId(Long orgid);

	@Query(value = "SELECT a from CustomerVO a where a.orgId=?1 ")
	List<CustomerVO> findAll(Long orgid);

	@Query(nativeQuery = true,value ="select a.customer from customer a ,clientbranch b,userclientaccess c,users d where a.customerid=b.customerid  and a.customer=c.customer and d.userid=c.usersid and a.orgid=?1 and d.username=?2  and b.branchcode=?3 group by a.customer")
	Set<Object[]> findAllAccessCustomerByUserName(Long orgid, String userName, String branchcode);

	boolean existsByCustomerNameAndOrgId(String customerName, Long orgId);

	boolean existsByCustomerShortNameAndOrgId(String customerShortName, Long orgId);

	boolean existsByPanNoAndOrgId(String panNo, Long orgId);
	
	


}
