package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.PySalesPurchaseItemVO;

public interface PySalesPurchaseItemRepo extends JpaRepository<PySalesPurchaseItemVO, Long> {

	@Query(nativeQuery = true, value = "select * from pysalespurchaseitem where orgid=?1 and clientcode=?2 and year=?3 and type=?4 and month=?5")
	List<PySalesPurchaseItemVO> getClientPySalesPurchaseItemDtls(Long orgId, String clientCode, String year,
			String type, String month);

	@Query(nativeQuery = true,value = "select description,month,qty,value from pysalespurchaseitem where orgid=?1 and year=?2 and clientcode=?3 and type=?4")
	Set<Object[]> getPyItemDetails(Long orgId, String finYear, String clientCode, String type);

}
