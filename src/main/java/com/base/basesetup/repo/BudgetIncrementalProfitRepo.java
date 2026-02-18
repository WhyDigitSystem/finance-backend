package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.BudgetIncrementalProfitVO;

public interface BudgetIncrementalProfitRepo extends JpaRepository<BudgetIncrementalProfitVO, Long> {

	@Query(nativeQuery = true,value = "select * from budgetincrementalprofit where orgid=?1 and clientcode=?2 and year=?3 and subgroup=?4")
	List<BudgetIncrementalProfitVO> getClientBudgetDls(Long org, String clientcode, String yr,String subGroup);

}
