package com.base.basesetup.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.AdjustmentJournalDTO;
import com.base.basesetup.dto.ArApAdjustmentOffSetDTO;
import com.base.basesetup.dto.BankingDepositDTO;
import com.base.basesetup.dto.BankingWithdrawalDTO;
import com.base.basesetup.dto.BrsOpeningDTO;
import com.base.basesetup.dto.ChartCostCenterDTO;
import com.base.basesetup.dto.ContraVoucherDTO;
import com.base.basesetup.dto.DailyMonthlyExRatesDTO;
import com.base.basesetup.dto.DebitNoteDTO;
import com.base.basesetup.dto.FundTransferDTO;
import com.base.basesetup.dto.GeneralJournalDTO;
import com.base.basesetup.dto.GlOpeningBalanceDTO;
import com.base.basesetup.dto.GstSalesVoucherDTO;
import com.base.basesetup.dto.PaymentReversalDTO;
import com.base.basesetup.dto.PaymentVoucherDTO;
import com.base.basesetup.dto.ReceiptReversalDTO;
import com.base.basesetup.dto.ReconcileBankDTO;
import com.base.basesetup.dto.ReconcileCashDTO;
import com.base.basesetup.dto.ReconcileCorpBankDTO;
import com.base.basesetup.dto.TmsJobCardDTO;
import com.base.basesetup.entity.AdjustmentJournalVO;
import com.base.basesetup.entity.ArApAdjustmentOffSetVO;
import com.base.basesetup.entity.BankingDepositVO;
import com.base.basesetup.entity.BankingWithdrawalVO;
import com.base.basesetup.entity.BrsExcelUploadVO;
import com.base.basesetup.entity.BrsOpeningVO;
import com.base.basesetup.entity.ChartCostCenterVO;
import com.base.basesetup.entity.ContraVoucherVO;
import com.base.basesetup.entity.DailyMonthlyExRatesVO;
import com.base.basesetup.entity.DebitNoteVO;
import com.base.basesetup.entity.FundTransferVO;
import com.base.basesetup.entity.GeneralJournalVO;
import com.base.basesetup.entity.GlOpeningBalanceVO;
import com.base.basesetup.entity.GstSalesVoucherVO;
import com.base.basesetup.entity.JobCardVO;
import com.base.basesetup.entity.PaymentReversalVO;
import com.base.basesetup.entity.PaymentVoucherVO;
import com.base.basesetup.entity.ReceiptReversalVO;
import com.base.basesetup.entity.ReconcileBankVO;
import com.base.basesetup.entity.ReconcileCashVO;
import com.base.basesetup.entity.ReconcileCorpBankVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface TransactionService {

//	DailyMonthlyExRatesVO
	List<DailyMonthlyExRatesVO> getAllDailyMonthlyExRatesByOrgId(Long orgId);

	DailyMonthlyExRatesVO updateCreateDailyMonthlyExRates(@Valid DailyMonthlyExRatesDTO irnCreditDTO)
			throws ApplicationException;

	List<DailyMonthlyExRatesVO> getAllDailyMonthlyExRatesById(Long id);

	List<DailyMonthlyExRatesVO> getDailyMonthlyExRatesByActive();

//	BrsOpening
	List<BrsOpeningVO> getAllBrsOpeningByOrgId(Long orgId);

	Map<String, Object> updateCreateBrsOpening(@Valid BrsOpeningDTO brsOpeningDTO) throws ApplicationException;

	List<BrsOpeningVO> getAllBrsOpeningById(Long id);

	List<BrsOpeningVO> getBrsOpeningByActive();

	List<Map<String, Object>> getBranchForBrsOpening(Long orgId);

	void ExcelUploadForBrs(MultipartFile[] files, Long orgId, String createdBy, String branch, String branchCode)
			throws ApplicationException, EncryptedDocumentException, IOException;

	List<BrsExcelUploadVO> getAllBrsExcelByOrgId(Long orgId);

	int getTotalRows();

	int getSuccessfulUploads();

//	ChartCostCenter
	List<ChartCostCenterVO> getAllChartCostCenterByOrgId(Long orgId);

	List<Map<String, Object>> updateCreateChartCostCenterList(@Valid List<ChartCostCenterDTO> chartCostCenterDTOList)
			throws ApplicationException;


	
	List<ChartCostCenterVO> getChartCostCenterById(Long id);

	List<ChartCostCenterVO> getChartCostCenterByActive();

//	FundTransfer
	List<FundTransferVO> getAllFundTransferByOrgId(Long orgId);

	Map<String, Object> updateCreateFundTransfer(@Valid FundTransferDTO fundTransferDTO) throws ApplicationException;

	List<FundTransferVO> getAllFundTransferById(Long id);

	List<FundTransferVO> getFundTransferByActive();

// GeneralJournal
	List<GeneralJournalVO> getAllGeneralJournalByOrgId(Long orgId);

	Map<String, Object> updateCreateGeneralJournal(@Valid GeneralJournalDTO generalJournalDTO)
			throws ApplicationException;

	List<GeneralJournalVO> getGeneralJournalById(Long id);

	List<GeneralJournalVO> getGeneralJournalByActive();
	
	List<Map<String, Object>> getAccountNameFromGroup( Long orgId);

// DebitNote
	List<DebitNoteVO> getAllDebitNoteByOrgId(Long orgId);

	DebitNoteVO updateCreateDebitNote(@Valid DebitNoteDTO debitNoteDTO) throws ApplicationException;

	List<DebitNoteVO> getAllDebitNoteById(Long id);

	List<DebitNoteVO> getDebitNoteByActive();

// GstSalesVoucher
	List<GstSalesVoucherVO> getAllGstSalesVoucherByOrgId(Long orgId);

	GstSalesVoucherVO updateCreateGstSalesVoucher(@Valid GstSalesVoucherDTO gstSalesVoucherDTO)
			throws ApplicationException;

	List<GstSalesVoucherVO> getAllGstSalesVoucherById(Long id);

	List<GstSalesVoucherVO> getGstSalesVoucherByActive();

// PaymentVoucher
	List<PaymentVoucherVO> getAllPaymentVoucherByOrgId(Long orgId);

	Map<String, Object> updateCreatePaymentVoucher(@Valid PaymentVoucherDTO paymentVoucherDTO)
			throws ApplicationException;

	List<PaymentVoucherVO> getPaymentVoucherById(Long id);

	List<PaymentVoucherVO> getPaymentVoucherByActive();

	PaymentVoucherVO getpaymentVoucherByDocId(Long orgId, String docId);

	String getpaymentVoucherDocId(Long orgId, String finYear, String branch, String branchCode);

//	ReceiptReversal
	List<ReceiptReversalVO> getAllReceiptReversalByOrgId(Long orgId);

	ReceiptReversalVO updateCreateReceiptReversal(@Valid ReceiptReversalDTO receiptReversalDTO)
			throws ApplicationException;

	List<ReceiptReversalVO> getAllReceiptReversalById(Long id);

	List<ReceiptReversalVO> getReceiptReversalByActive();

//	PaymentReversal
	List<PaymentReversalVO> getAllPaymentReversalByOrgId(Long orgId);

	PaymentReversalVO updateCreatePaymentReversal(@Valid PaymentReversalDTO paymentReversalDTO)
			throws ApplicationException;

	List<PaymentReversalVO> getAllPaymentReversalById(Long id);

	List<PaymentReversalVO> getPaymentReversalByActive();

//	ArApAdjustmentOffSet
	List<ArApAdjustmentOffSetVO> getAllArApAdjustmentOffSetByOrgId(Long orgId);

	Map<String, Object> updateCreateArApAdjustmentOffSet(@Valid ArApAdjustmentOffSetDTO arApAdjustmentOffSetDTO)
			throws ApplicationException;

	List<ArApAdjustmentOffSetVO> getAllArApAdjustmentOffSetById(Long id);
	
	String getArApAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode);

	// GlOpeningBalance
	List<GlOpeningBalanceVO> getAllGlOpeningBalanceByOrgId(Long orgId);

	Map<String, Object> updateCreateGlOpeningBalance(@Valid GlOpeningBalanceDTO glOpeningBalanceDTO)
			throws ApplicationException;

	List<GlOpeningBalanceVO> getAllGlOpeningBalanceById(Long id);

	List<GlOpeningBalanceVO> getGlOpeningBalanceByActive();

	String getGlOpeningBalanceDocId(Long orgId, String finYear, String branch, String branchCode);

	GlOpeningBalanceVO getGlOpeningBalanceByDocId(Long orgId, String docId);

	// ReconciliationSummary

//	List<ReconciliationSummaryVO> getAllReconciliationSummaryById(Long id);
//
//	List<ReconciliationSummaryVO> getAllReconciliationSummaryByOrgId(Long orgId);
//
//	ReconciliationSummaryVO updateCreateReconciliationSummary(@Valid ReconciliationSummaryDTO reconciliationSummaryDTO);

	// ReconcileBank
	List<ReconcileBankVO> getAllReconcileBankByOrgId(Long orgId);

	Map<String, Object> updateCreateReconcileBank(@Valid ReconcileBankDTO reconcileBankDTO) throws ApplicationException;

	List<ReconcileBankVO> getAllReconcileBankById(Long id);

	String getReconcileBankDocId(Long orgId, String finYear, String branch, String branchCode);

	String getGeneralJournalDocId(Long orgId, String finYear, String branch, String branchCode);

	List<Map<String, Object>> getBankNameForGroupLedger(Long orgId);

	// ReconcileCorpBank
	List<ReconcileCorpBankVO> getAllReconcileCorpBankByOrgId(Long orgId);

	Map<String, Object> updateCreateReconcileCorpBank(@Valid ReconcileCorpBankDTO reconcileCorpBankDTO)
			throws ApplicationException;

	List<ReconcileCorpBankVO> getAllReconcileCorpBankById(Long id);

	String getReconcileCorpBankDocId(Long orgId, String finYear, String branch, String branchCode);


	// ReconcileCash

	List<ReconcileCashVO> getAllReconcileCashByOrgId(Long orgId);

	Map<String, Object> updateCreateReconcileCash(@Valid ReconcileCashDTO reconcileCashDTO) throws ApplicationException;

	List<ReconcileCashVO> getAllReconcileCashById(Long id);

	FundTransferVO getFundTranferByDocId(Long orgId, String docId);

	String getFundTranferDocId(Long orgId, String finYear, String branch, String branchCode);

	String getReconcileCashDocId(Long orgId, String finYear, String branch, String branchCode);

	String getChartCostCenterDocId(Long orgId, String finYear, String branch, String branchCode);


	/// TMS-TT-JobCard

	List<JobCardVO> getAllJobCardByOrgId(Long orgId);

	Map<String, Object> updateCreateJobCard(@Valid TmsJobCardDTO tmsJobCardDTO) throws ApplicationException;

	List<JobCardVO> getAllJobCardById(Long id);

	List<Map<String, Object>> getSalesPersonFromPartyMaster(Long orgId, String partyName);

	List<Map<String, Object>> getAllCustomersFromPartyMaster(Long orgId);

	String getJobCardDocId(Long orgId, String finYear, String branch, String branchCode);

	// AdjustmentJournal

	List<AdjustmentJournalVO> getAllAdjustmentJournalByOrgId(Long orgId);

	List<AdjustmentJournalVO> getAdjustmentJournalById(Long id);

	Map<String, Object> updateCreateAdjustmentJournal(@Valid AdjustmentJournalDTO adjustmentJournalDTO)
			throws ApplicationException;

	String getAdjustmentJournalDocId(Long orgId, String finYear, String branch, String branchCode);

	//BankingDeposit
	List<BankingDepositVO> getAllBankingDepositByOrgId(Long orgId);

	List<BankingDepositVO> getBankingDepositById(Long id);

	Map<String, Object> updateCreateBankingDeposit(@Valid BankingDepositDTO bankingDepositDTO)
			throws ApplicationException;

	String getBankingDepositDocId(Long orgId, String finYear, String branch, String branchCode);
 
	List<Map<String, Object>> getBankNameFromGroupforBankingDeposit(Long orgId);

	//BankingWithdrawal
	List<BankingWithdrawalVO> getAllBankingWithdrawalByOrgId(Long orgId);

	List<BankingWithdrawalVO> getBankingWithdrawalById(Long id);

	Map<String, Object> updateCreateBankingWithdrawal(@Valid BankingWithdrawalDTO bankingWithdrawalDTO) throws ApplicationException;

	String getBankingWithdrawalDocId(Long orgId, String finYear, String branch, String branchCode);

	List<Map<String, Object>> getCurrencyAndExrates(Long orgId);

	//contraVoucher
	
	List<ContraVoucherVO> getAllContraVoucherByOrgId(Long orgId);

	List<ContraVoucherVO> getContraVoucherById(Long id);

	String getContraVoucherDocId(Long orgId, String finYear, String branch, String branchCode);

	Map<String, Object> updateCreateContraVoucher(@Valid ContraVoucherDTO contraVoucherDTO) throws ApplicationException;

	List<Map<String, Object>> getAccountNamefromGroupLedgerforCV(Long orgId);


	
}