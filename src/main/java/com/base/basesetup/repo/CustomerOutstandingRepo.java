package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CustomerOutstandingVO;

@Repository
public interface CustomerOutstandingRepo extends JpaRepository<CustomerOutstandingVO, String>{

	@Query(value = "SELECT subledgercode, subledgername, creditlimit AS creditLimit,totaldue AS totalDue, FLOOR(outpercentage) AS outPercentage from customeroutstanding WHERE outpercentage <= 80 ", nativeQuery = true)
	List<CustomerOutstandingVO> findCustomersExceeding80Percent();


}
