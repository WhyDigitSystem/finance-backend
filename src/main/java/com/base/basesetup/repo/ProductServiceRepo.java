package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ProductServiceVO;

@Repository
public interface ProductServiceRepo extends JpaRepository<ProductServiceVO, Long> {

	@Query(nativeQuery = true,value="select * from productservice where productserviceid=?1")
	ProductServiceVO getProductServiceById(Long id);

	@Query(nativeQuery = true,value="select * from productservice where orgid=?1")
	List<ProductServiceVO> getProductServiceByOrgId(Long orgId);

	boolean existsByCodeAndOrgId(String code, Long orgId);

	boolean existsByDescriptionAndOrgId(String description, Long orgId);

}
