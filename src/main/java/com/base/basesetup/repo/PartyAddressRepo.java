package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.PartyAddressVO;
import com.base.basesetup.entity.PartyMasterVO;

@Repository
public interface PartyAddressRepo extends JpaRepository<PartyAddressVO, Long>{

	List<PartyAddressVO> findByPartyMasterVO(PartyMasterVO partyMasterVO);

}
