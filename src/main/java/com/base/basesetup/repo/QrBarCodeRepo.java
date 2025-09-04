package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QrBarCodeVO;

@Repository
public interface QrBarCodeRepo extends JpaRepository<QrBarCodeVO, Long> {

	@Query(nativeQuery = true, value = "select * from qrbarcode where orgid=?1 and finyear=?2 and branchcode=?3")
	List<QrBarCodeVO> getAllQrBarCodeByOrgId(Long orgId, String finyear, String branchCode);

	@Query(nativeQuery = true, value = "select * from qrbarcode where qrbarcodeid=?1")
	QrBarCodeVO getQrBarCodeById(Long id);

	@Query(nativeQuery = true, value = "SELECT partno, partdescription,batchno,ROW_NUMBER() OVER (ORDER BY partdescription, partno) AS id FROM qrbarexcelupload WHERE entryno = ?1 AND (entryno) NOT IN (SELECT entryno FROM qrbarcode) GROUP BY partno, partdescription,batchno")
	Set<Object[]> findFillGridFromQrBarExcelUpload(String entryNo);

	@Query(nativeQuery = true, value = "select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2 and branchcode=?3 and screencode=?4")
	String getQrBarCodeDocId(Long orgId, String finYear, String branchCode, String screenCode);

}
