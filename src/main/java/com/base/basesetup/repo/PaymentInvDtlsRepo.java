package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.PaymentInvDtlsVO;
import com.base.basesetup.entity.PaymentVO;

@Repository
public interface PaymentInvDtlsRepo extends JpaRepository<PaymentInvDtlsVO, Long>{

	List<PaymentInvDtlsVO> findByPaymentVO(PaymentVO paymentVO);


}
