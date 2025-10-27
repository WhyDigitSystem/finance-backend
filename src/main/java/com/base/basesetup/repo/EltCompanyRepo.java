package com.base.basesetup.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.EltCompanyVO;

@Repository
public interface EltCompanyRepo extends JpaRepository<EltCompanyVO, Long>{

	boolean existsByCompanyCodeAndId(String companyCode, Long id);

	boolean existsByCompanyNameAndId(String companyName, Long id);

	boolean existsByEmailAndId(String email, Long id);

	boolean existsByPhoneAndId(String phone, Long id);

	boolean existsByWebSiteAndId(String webSite, Long id);

	@Query(value ="SELECT * FROM eltcompany where eltcompanyid=?1",nativeQuery =true)
	Optional<EltCompanyVO> findEltCompanyById(Long id);


}
