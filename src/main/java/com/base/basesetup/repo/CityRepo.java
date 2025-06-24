package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.base.basesetup.entity.CityVO;

public interface CityRepo extends JpaRepository<CityVO, Long> {
	@Query("select a from CityVO a where a.orgId=?1")
	List<CityVO> findAll(Long orgid);

	@Query("select a from CityVO a where a.orgId=?1 and a.state=?2")
	List<CityVO> findAll(Long orgid, String state);

//	boolean existsByCityCodeAndCityNameAndOrgId(String cityCode, String cityName, Long orgId);

	boolean existsByCityCodeAndOrgId(String cityCode, Long orgId);

	boolean existsByCityNameAndOrgId(String cityName, Long orgId);
	
	List<CityVO> findAllByStateAndCountryAndOrgId(String state, String country, Long orgId);
}
