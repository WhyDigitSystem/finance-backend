package com.base.basesetup.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.SampleExcelUploadVO;
@Repository
public interface SampleExcelUploadRepo extends JpaRepository<SampleExcelUploadVO, Long> {

	@Query(nativeQuery = true,value = "select*from sampleexcelupload where sampleexceluploadid=?1")
	List<SampleExcelUploadVO> findSampleExcelUploadById(Long id);

}
