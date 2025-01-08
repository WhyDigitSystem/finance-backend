package com.base.basesetup.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.MultipleDocIdGenerationVO;

@Repository
public interface MultipleDocIdGenerationRepo extends JpaRepository<MultipleDocIdGenerationVO, Long>{

	@Query(nativeQuery = true,value = "SELECT \r\n"
			+ "    b.screenname, \r\n"
			+ "    b.screencode, \r\n"
			+ "    b.doccode,\r\n"
			+ "    ?4 AS finyear,\r\n"
			+ "    ?2 AS branch,\r\n"
			+ "    ?3 AS branchcode,\r\n"
			+ "    ?5 AS finyearidentifier,\r\n"
			+ "    CONCAT(?3, ?5, ?6, b.doccode) AS prefixfield\r\n"
			+ "FROM (\r\n"
			+ "    SELECT doccode, screencode, screenname \r\n"
			+ "    FROM documenttype  \r\n"
			+ "    WHERE orgid = ?1\r\n"
			+ ") b\r\n"
			+ "LEFT JOIN multipledocidgendetails md \r\n"
			+ "    ON md.orgid = ?1 \r\n"
			+ "    AND md.branch = ?2 \r\n"
			+ "    AND md.finyear = ?4 \r\n"
			+ "    AND md.screenname = b.screenname \r\n"
			+ "    AND md.screencode = b.screencode\r\n"
			+ "WHERE md.doccode IS NULL  \r\n"
			+ "")
	Set<Object[]> getPendingMultipleDocIdGeneration(Long orgId, String branch, String branchCode, String finYear,
			String finYearIdentifier,String docCode);

	@Query(nativeQuery = true, value = "select * from multipledocidgeneration where orgid=?1")
	List<MultipleDocIdGenerationVO> findMultipleDocIdGenerationByOrgId(Long orgId);

}
