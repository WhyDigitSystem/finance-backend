package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QrBarCodeDetailsVO;
import com.base.basesetup.entity.QrBarCodeVO;

@Repository
public interface QrBarCodeDetailsRepo extends JpaRepository<QrBarCodeDetailsVO, Long> {

	List<QrBarCodeDetailsVO> findByQrBarCodeVO(QrBarCodeVO qrBarCodeVO);

}
