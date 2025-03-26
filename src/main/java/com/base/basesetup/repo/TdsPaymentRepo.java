package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.basesetup.entity.PaymentVO;
import com.base.basesetup.entity.TdsPaymentVO;

public interface TdsPaymentRepo extends JpaRepository<TdsPaymentVO, Long> {

	List<TdsPaymentVO> findByPaymentVO(PaymentVO paymentVO);

}
