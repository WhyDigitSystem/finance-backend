package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.BankDetailsVO;
import com.base.basesetup.entity.CompanyVO;

@Repository
public interface BankDetailsRepo extends JpaRepository<BankDetailsVO, Long> {


	List<BankDetailsVO> findByCompanyVO(CompanyVO companyVO);

}
