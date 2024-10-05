package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ReceiptReceivableVO;
@Repository
public interface ReceiptReceivableRepo extends JpaRepository<ReceiptReceivableVO, Long> {

	@Query(nativeQuery = true, value = "select * from receiptreceivable where orgid=?1")
	List<ReceiptReceivableVO> getAllReceiptReceivableByOrgId(Long orgId);

	@Query(nativeQuery = true, value = "select * from receiptreceivable where receiptreceivableid=?1")
	List<ReceiptReceivableVO> getAllReceiptReceivableById(Long id);

	@Query(nativeQuery = true, value = "select * from receiptreceivable where active=1")
	List<ReceiptReceivableVO> findReceiptReceivablesByActive();
}
