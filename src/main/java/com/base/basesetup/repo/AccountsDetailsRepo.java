package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;

public interface AccountsDetailsRepo extends JpaRepository<AccountsDetailsVO, Long> {

	AccountsDetailsVO findByAccountsVOAndGstflag(AccountsVO savedAccountsVO, int gstflag);

}
