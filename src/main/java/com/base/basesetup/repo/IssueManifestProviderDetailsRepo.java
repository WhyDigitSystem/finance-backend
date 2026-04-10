package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.IssueManifestProviderDetailsVO;
import com.base.basesetup.entity.IssueManifestProviderVO;

@Repository
public interface IssueManifestProviderDetailsRepo extends JpaRepository<IssueManifestProviderDetailsVO, Long>{

	List<IssueManifestProviderDetailsVO> findByIssueManifestProviderVO(IssueManifestProviderVO issueManifestProviderVO);

}
