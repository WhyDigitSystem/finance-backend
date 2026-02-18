package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.StockBranchVO;
import com.base.basesetup.entity.WarehouseVO;

@Repository
public interface WarehouseRepo extends JpaRepository<WarehouseVO, Long> {

	@Query(value = "select a from WarehouseVO a where a.orgId=?1 ")
	List<WarehouseVO> getAllWarehouseByOrgId(Long orgId);

	@Query(value = "select a from WarehouseVO a where a.id=?1")
	WarehouseVO getWarehouseById(Long id);

//	boolean existsByLocationNameAndLocationUnitAndOrgId(String locationName, String locationUnit, Long orgId);

	boolean existsByNameAndOrgId(String concatName, Long orgId);
	
	@Query(value = "select a from StockBranchVO a where a.orgId=?1 and a.active=true")
	List<StockBranchVO> getStockBranchName(Long orgId);

	boolean existsByLocationUnitAndOrgId(String locationUnit, Long orgId);
	

	@Query(value = "select name,address,gst from warehouse where orgid=?1 and active=1 and cancel=0\r\n"
			+ "union \r\n"
			+ "select p.partyname,concat(p1.addressline1,',',p1.addressline2,',',p1.addressline3) address,p.gstin from partymaster p join partyaddress p1 on p.partymasterid=p1.partymasterid\r\n"
			+ "where p.orgid=?1 and p.partytype='CUSTOMER'  and p.active=1 and p.cancel=0",nativeQuery = true)
	Set<Object[]> getAllWarehouseNames(Long orgId);
//
//boolean existsByLocationNameAndUnitAndOrgId(String locationName, String unit, Long orgId);
//
//boolean existsByWarehouseLocationAndOrgId(String whlocation, Long orgId);
//
//@Query(value = "select a from WarehouseVO a where a.orgId=?1")
//List<WarehouseVO> findAllWarehouse(Long orgId);
//
//@Query(value = "select a from WarehouseVO a where a.orgId=?1 and a.active=true")
//List<WarehouseVO> findAllActiveWarehouse(Long orgId);
//
//@Query(value = "select a.whlocation,a.kitcode,a.avalqty from availablekit1 a where whlocation=?1 and kitcode=?2",nativeQuery = true)
//Set<Object[]> getAvalkitqtyByWarehouse(String warehouse, String kitName);
//
//boolean existsByWarehouseLocationAndCodeAndOrgId(String location, String code, Long orgId);
//
//boolean existsByCodeAndOrgId(String code, Long orgId);
//
//@Query(value = "SELECT a.whlocation FROM flow a, users b \n"
//		+ "WHERE FIND_IN_SET(a.flowid, b.access_flow_id) > 0 and b.user_id=?1 and b.org_id=?2 group by whlocation",nativeQuery = true)
//Set<Object[]> findByorginWareHouse(Long userId, Long orgId);
}
