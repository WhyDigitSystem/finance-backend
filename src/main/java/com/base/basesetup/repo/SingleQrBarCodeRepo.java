package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.SingleQrBarCodeVO;

@Repository
public interface SingleQrBarCodeRepo extends JpaRepository<SingleQrBarCodeVO, Long>{

	@Query(nativeQuery = true, value = "select * from singleqrbarcode where orgid=?1")
	List<SingleQrBarCodeVO> findAllSingleQrBarCode(Long oegId);

	@Query(nativeQuery = true, value = "select * from singleqrbarcode where singleqrbarcodeid=?1")
	SingleQrBarCodeVO findSingleQrBarCodeById(Long id);

	boolean existsByQrBarCodeValueAndOrgId(String qrBarCodeValue, Long orgId);
	
}
