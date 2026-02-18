package com.base.basesetup.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QrBarExcelUploadVO;

@Repository
public interface QrBarExcelUploadRepo extends JpaRepository<QrBarExcelUploadVO, Long> {


}
