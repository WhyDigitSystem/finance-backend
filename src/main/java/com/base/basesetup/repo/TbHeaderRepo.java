package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.TbHeaderVO;

@Repository
public interface TbHeaderRepo extends JpaRepository<TbHeaderVO, Long>{

	@Query(nativeQuery = true,value="select concat(prefixfield,lpad(lastno,5,0)) AS docid from documenttypemappingdetails where orgid=?1 and finyear=?2  and screencode=?3 and  clientcode=?4")
	String getTBDocId(Long orgId, String finYear, String screenCode,String clientCode);

	@Query(nativeQuery = true,value = "select a.* from tbheader a where a.orgid=?1 and a.finyear=?2 and a.client=?3 order by a.docid desc")
	List<TbHeaderVO> getAllTrialBalance(Long orgId, String finYear, String client);

}
