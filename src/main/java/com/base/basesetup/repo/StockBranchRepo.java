package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.StockBranchVO;

public interface StockBranchRepo extends JpaRepository<StockBranchVO, Long> {

	@Query(value = "select a from StockBranchVO a where a.orgId=?1")
	List<StockBranchVO> getAllStockBranchByOrgId(Long orgId);

	@Query(value = "select a from StockBranchVO a where a.id=?1")
	StockBranchVO getStockBranchById(Long id);
	
//	@Query(nativeQuery = true,value="select b.* from warehouse a, availablekit b where a.stockbranch=b.stockbranch and a.warehouseid=?1 and b.kitcode=?2")
//	Set<Object[]> findAvalKitQty(Long warehouseId, String kitname);
//
//	@Query(nativeQuery = true,value="select b.* from availablekit b where  b.stockbranch=?1 and b.kitcode=?2")
//	Set<Object[]> findAvalKitQtyByBranch(String branch, String kitname);

	boolean existsByBranchAndOrgId(String branch, Long orgId);

	@Query(value = "select a from StockBranchVO a where orgId=?1 and a.active=true")
	List<StockBranchVO> findActiveBranchByOrgId(Long orgId);

	boolean existsBybranchCodeAndOrgId(String branchCode, Long orgId);

}
