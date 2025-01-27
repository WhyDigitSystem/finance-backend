package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.ChargerCostDebitNoteVO;
import com.base.basesetup.entity.CostDebitNoteVO;
@Repository
public interface ChargesCostDebitNoteRepo extends JpaRepository<ChargerCostDebitNoteVO, Long>{

	List<ChargerCostDebitNoteVO> findByCostDebitNoteVO(CostDebitNoteVO costDebitNoteVO);

}
