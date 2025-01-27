package com.base.basesetup.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.AccountDTO;
import com.base.basesetup.dto.BranchDTO;
import com.base.basesetup.dto.ChargeTypeRequestDTO;
import com.base.basesetup.dto.ChequeBookDTO;
import com.base.basesetup.dto.CostCenterDTO;
import com.base.basesetup.dto.EmployeeDTO;
import com.base.basesetup.dto.GroupLedgerDTO;
import com.base.basesetup.dto.ListOfValuesDTO;
import com.base.basesetup.dto.PartyMasterDTO;
import com.base.basesetup.dto.SacCodeDTO;
import com.base.basesetup.dto.SetTaxRateDTO;
import com.base.basesetup.dto.SubLedgerAccountDTO;
import com.base.basesetup.dto.TaxMasterDTO;
import com.base.basesetup.dto.TcsMasterDTO;
import com.base.basesetup.dto.TdsMasterDTO;
import com.base.basesetup.entity.AccountVO;
import com.base.basesetup.entity.BranchVO;
import com.base.basesetup.entity.ChargeTypeRequestVO;
import com.base.basesetup.entity.ChequeBookVO;
import com.base.basesetup.entity.CostCenterVO;
import com.base.basesetup.entity.EmployeeVO;
import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.ListOfValuesVO;
import com.base.basesetup.entity.PartyMasterVO;
import com.base.basesetup.entity.SacCodeVO;
import com.base.basesetup.entity.SetTaxRateVO;
import com.base.basesetup.entity.SubLedgerAccountVO;
import com.base.basesetup.entity.TaxMasterVO;
import com.base.basesetup.entity.TcsMasterVO;
import com.base.basesetup.entity.TdsMasterVO;
import com.base.basesetup.exception.ApplicationException;

import io.jsonwebtoken.io.IOException;

@Service
public interface MasterService {

	// Branch

	List<BranchVO> getAllBranch(Long orgid);

	Optional<BranchVO> getBranchById(Long branchid);

	Map<String, Object> createUpdateBranch(BranchDTO branchDTO) throws Exception;

	void deleteBranch(Long branchid);

	// employee

	List<EmployeeVO> getAllEmployee();

	List<EmployeeVO> getAllEmployeeByOrgId(Long orgId);

	Optional<EmployeeVO> getEmployeeById(Long employeeid);

	Map<String, Object> createEmployee(EmployeeDTO employeeDTO) throws ApplicationException;

	void deleteEmployee(Long employeeid);
	
	List<Map<String, Object>> getDepartmentNameForEmployee(Long orgId);

	List<Map<String, Object>> getDesignationNameForEmployee(Long orgId);

//	SetTaxRateVO
	List<SetTaxRateVO> getAllSetTaxRateByOrgId(Long orgId);

	List<SetTaxRateVO> getAllSetTaxRateById(Long id);

	SetTaxRateVO updateCreateSetTaxRate(@Valid SetTaxRateDTO setTaxRateDTO) throws Exception;

	List<SetTaxRateVO> getSetTaxRateByActive();

//	TaxMasterVO
	TaxMasterVO updateCreateTaxMaster(TaxMasterDTO taxMasterDTO) throws ApplicationException;

	List<TaxMasterVO> getAllTaxMasterByOrgId(Long orgId);

	List<TaxMasterVO> getAllTaxMasterById(Long id);

	List<TaxMasterVO> getTaxMasterByActive();

//	TcsMasterVO 
	List<TcsMasterVO> getAllTcsMasterByOrgId(Long orgId);

	List<TcsMasterVO> getAllTcsMasterById(Long id);

	TcsMasterVO updateCreateTcsMaster(@Valid TcsMasterDTO tcsMasterDTO) throws ApplicationException;

	List<TcsMasterVO> getTcsMasterByActive();

//	TdsMasterVO
	List<TdsMasterVO> getAllTdsMasterByOrgId(Long orgId);

	List<TdsMasterVO> getAllTdsMasterById(Long id);

	TdsMasterVO updateCreateTdsMaster(@Valid TdsMasterDTO tdsMasterDTO) throws ApplicationException;

	List<TdsMasterVO> getTdsMasterByActive();

//	AccountVO
	List<AccountVO> getAllAccountByOrgId(Long orgId);

	AccountVO updateCreateAccount(@Valid AccountDTO accountDTO) throws ApplicationException;

	List<AccountVO> getAllAccountById(Long id);

	List<AccountVO> getAccountByActive();

//	GroupLedgerVO
	List<GroupLedgerVO> getAllGroupLedgerById(Long id);

	List<GroupLedgerVO> getAllGroupLedgerByOrgId(Long orgId);

	List<Map<String, Object>> getGroupName(Long orgId);

	GroupLedgerVO updateCreateGroupLedger(@Valid GroupLedgerDTO groupLedgerDTO) throws ApplicationException;

	List<GroupLedgerVO> getGroupLedgerByActive();

//	SacCode
	List<SacCodeVO> getAllSacCodeById(Long id);

	List<SacCodeVO> getAllSacCodeByOrgId(Long orgId);

	List<SacCodeVO> getAllActiveSacCodeByOrgId(Long orgId);

	SacCodeVO updateCreateSacCode(@Valid SacCodeDTO sacCodeDTO) throws ApplicationException;

//	List<SacCodeVO> getSacCodeByActive();

//	SubLedgerAccount
	List<SubLedgerAccountVO> getAllSubLedgerAccountByOrgId(Long orgId);

	SubLedgerAccountVO updateCreateSubLedgerAccount(@Valid SubLedgerAccountDTO subLedgerAccountDTO)
			throws ApplicationException;

	List<SubLedgerAccountVO> getAllSubLedgerAccountById(Long id);

	List<SubLedgerAccountVO> getSubLedgerAccountByActive();

//	CostCenterVO
	List<CostCenterVO> getAllCostCenterByOrgId(Long orgId);

	CostCenterVO updateCreateCostCenter(@Valid CostCenterDTO costCenterDTO) throws ApplicationException;

	List<CostCenterVO> getAllCostCenterById(Long id);

	List<CostCenterVO> getCostCenterByActive();

//	ChequeBook
	List<ChequeBookVO> getAllChequeBookByOrgId(Long orgId);

	ChequeBookVO updateCreateChequeBook(@Valid ChequeBookDTO chequeBookDTO) throws ApplicationException;

	List<ChequeBookVO> getAllChequeBookById(Long id);

	List<ChequeBookVO> getChequeBookByActive();

//	ChargeTypeRequest
	List<ChargeTypeRequestVO> getAllChargeTypeRequestByOrgId(Long orgId);

	List<Map<String, Object>> getChargeType(Long orgId);

	ChargeTypeRequestVO updateCreateChargeTypeRequest(@Valid ChargeTypeRequestDTO chargeTypeRequestDTO)
			throws ApplicationException;

	List<ChargeTypeRequestVO> getAllChargeTypeRequestById(Long id);

	List<ChargeTypeRequestVO> getChargeTypeRequestByActive();

	List<Map<String, Object>> getSalesAccountFromGroup(Long orgId);

	List<Map<String, Object>> getPaymentAccountFromGroup(Long orgId);
//	ListOfValues

	List<ListOfValuesVO> getListOfValuesById(Long id);

	List<ListOfValuesVO> getListOfValuesByOrgId(Long orgid);

	ListOfValuesVO updateCreateListOfValues(@Valid ListOfValuesDTO listOfValuesDTO) throws ApplicationException;

	// PartyMaster

	List<PartyMasterVO> getPartyMasterByOrgId(Long orgid);

	List<PartyMasterVO> getPartyMasterById(Long id);

	PartyMasterVO updateCreatePartyMaster(@Valid PartyMasterDTO partyMasterDTO) throws ApplicationException;

	String getPartyMasterDocId(Long orgId, String finYear, String branch, String branchCode);
	
	void excelUploadForGroupLedger(MultipartFile[] files, String createdBy, Long orgId)
			throws ApplicationException, EncryptedDocumentException, IOException, java.io.IOException;
	int getTotalRows();
	int getSuccessfulUploads();

	void excelUploadForChargeCode(MultipartFile[] files, String createdBy, Long orgId)
			throws EncryptedDocumentException, ApplicationException, java.io.IOException;



}
