package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CityVO;
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
	

//	@Query(value = "select w.warehouseLocation,w.warehouseId  from WarehouseVO w where w.orgId=?1")
//	Set<Object[]> getWarehouseLocationByOrgID(Long orgId);
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
