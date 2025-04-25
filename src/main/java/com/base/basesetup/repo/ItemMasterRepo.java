package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ItemMasterVO;

@Repository
public interface ItemMasterRepo extends JpaRepository<ItemMasterVO, Long> {

	
	@Query(nativeQuery = true, value = "select * from itemmaster where orgid=?1 and branchcode=?2")
	List<ItemMasterVO> getAllItemMasterByOrgId(Long orgId, String branchCode);

	@Query(nativeQuery = true, value = "select * from itemmaster where itemmasterid=?1")
	List<ItemMasterVO> getAllItemMasterById(Long id);

	@Query(nativeQuery = true, value = "select * from itemmaster where active=1")
	List<ItemMasterVO> getAllItemMasterByActive();

	boolean existsByDupChk(String dupChk);
}
