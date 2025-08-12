package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.CostDebitNoteVO;
import com.base.basesetup.entity.TdsCostDebitNoteVO;
@Repository
public interface TdsCostDebitNoteRepo extends JpaRepository<TdsCostDebitNoteVO, Long>{

	List<TdsCostDebitNoteVO> findByCostDebitNoteVO(CostDebitNoteVO costDebitNoteVO);

}
