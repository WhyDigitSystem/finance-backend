package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.RetrievalManifestProviderDetailsVO;
import com.base.basesetup.entity.RetrievalManifestProviderVO;
@Repository
public interface RetrievalManifestProviderDetailsRepo extends JpaRepository<RetrievalManifestProviderDetailsVO, Long>{

	List<RetrievalManifestProviderDetailsVO> findByRetrievalManifestProviderVO(
			RetrievalManifestProviderVO retrievalManifestProviderVO);

}
