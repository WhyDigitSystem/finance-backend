package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.BudgetSalesPurchaseVO;

public interface BudgetSalesPurchaseRepo extends JpaRepository<BudgetSalesPurchaseVO, Long>{

	@Query(nativeQuery = true, value = "select * from budgetsalespurchase where orgid=?1 and clientcode=?2 and year=?3 and type=?4 and month=?5")
	List<BudgetSalesPurchaseVO> getClientBudgetSalesPurchaseDtls(Long orgId, String clientCode, String year,
			String type,String month);

	@Query(nativeQuery = true,value = "select description,month,amount,natureofaccount from budgetsalespurchase where orgid=?1 and year=?2 and clientcode=?3 and type=?4")
	Set<Object[]> getBudgetDetails(Long orgId, String finYear, String clientCode, String type);

}
