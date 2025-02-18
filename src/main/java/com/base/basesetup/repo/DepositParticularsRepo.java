package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.BankingDepositVO;
import com.base.basesetup.entity.DepositParticularsVO;

@Repository
public interface DepositParticularsRepo extends JpaRepository<DepositParticularsVO, Long> {

	List<DepositParticularsVO> findByBankingDepositVO(BankingDepositVO bankingDepositVO);

}
