package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.BudgetSalesPurchaseItemVO;

public interface BudgetSalesPurchaseItemRepo extends JpaRepository<BudgetSalesPurchaseItemVO, Long> {

	@Query(nativeQuery = true, value = "select * from budgetsalespurchaseitem where orgid=?1 and clientcode=?2 and year=?3 and type=?4 and month=?5")
	List<BudgetSalesPurchaseItemVO> getClientBudgetSalesPurchaseItemDtls(Long orgId, String clientCode, String year,
			String type,String month);

	@Query(nativeQuery = true,value = "select description,month,qty,value from budgetsalespurchaseitem where orgid=?1 and year=?2 and clientcode=?3 and type=?4")
	Set<Object[]> getBudgetItemDetails(Long orgId, String finYear, String clientCode, String type);

}
