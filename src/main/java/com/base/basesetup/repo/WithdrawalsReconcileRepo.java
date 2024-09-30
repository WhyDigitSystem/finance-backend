package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.WithdrawalsReconcileVO;

@Repository
public interface WithdrawalsReconcileRepo extends JpaRepository<WithdrawalsReconcileVO, Long> {

}
