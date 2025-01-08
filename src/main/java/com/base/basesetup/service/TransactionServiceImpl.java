package com.base.basesetup.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.AccountParticularsDTO;
import com.base.basesetup.dto.AdjustmentJournalDTO;
import com.base.basesetup.dto.ArApAdjustmentOffSetDTO;
import com.base.basesetup.dto.ArApOffSetInvoiceDetailsDTO;
import com.base.basesetup.dto.BankingDepositDTO;
import com.base.basesetup.dto.BankingWithdrawalDTO;
import com.base.basesetup.dto.BrsOpeningDTO;
import com.base.basesetup.dto.ChargerDebitNoteDTO;
import com.base.basesetup.dto.ChartCostCenterDTO;
import com.base.basesetup.dto.ContraVoucherDTO;
import com.base.basesetup.dto.ContraVoucherParticularsDTO;
import com.base.basesetup.dto.CostCenterTmsJobCardDTO;
import com.base.basesetup.dto.DailyMonthlyExRatesDTO;
import com.base.basesetup.dto.DailyMonthlyExRatesDtlDTO;
import com.base.basesetup.dto.DebitNoteDTO;
import com.base.basesetup.dto.DepositParticularsDTO;
import com.base.basesetup.dto.FundTransferDTO;
import com.base.basesetup.dto.GeneralJournalDTO;
import com.base.basesetup.dto.GlOpeningBalanceDTO;
import com.base.basesetup.dto.GstDebitNoteDTO;
import com.base.basesetup.dto.GstSalesVoucherDTO;
import com.base.basesetup.dto.ParticularsDebitNoteDTO;
import com.base.basesetup.dto.ParticularsGlOpeningBalanceDTO;
import com.base.basesetup.dto.ParticularsGstVoucherDTO;
import com.base.basesetup.dto.ParticularsJournalDTO;
import com.base.basesetup.dto.ParticularsPaymentVoucherDTO;
import com.base.basesetup.dto.ParticularsReconcileCorpBankDTO;
import com.base.basesetup.dto.ParticularsReconcileDTO;
import com.base.basesetup.dto.PaymentInvoiceDTO;
import com.base.basesetup.dto.PaymentOtherAccountDTO;
import com.base.basesetup.dto.PaymentReversalDTO;
import com.base.basesetup.dto.PaymentVoucherDTO;
import com.base.basesetup.dto.ReceiptInvoiceDTO;
import com.base.basesetup.dto.ReceiptOtherAccountDTO;
import com.base.basesetup.dto.ReceiptReversalDTO;
import com.base.basesetup.dto.ReconcileBankDTO;
import com.base.basesetup.dto.ReconcileCashDTO;
import com.base.basesetup.dto.ReconcileCorpBankDTO;
import com.base.basesetup.dto.TmsJobCardDTO;
import com.base.basesetup.dto.WithdrawalParticularsDTO;
import com.base.basesetup.entity.AccountParticularsVO;
import com.base.basesetup.entity.AccountsDetailsVO;
import com.base.basesetup.entity.AccountsVO;
import com.base.basesetup.entity.AdjustmentJournalVO;
import com.base.basesetup.entity.ArApAdjustmentOffSetVO;
import com.base.basesetup.entity.ArApOffSetInvoiceDetailsVO;
import com.base.basesetup.entity.BankingDepositVO;
import com.base.basesetup.entity.BankingWithdrawalVO;
import com.base.basesetup.entity.BrsExcelUploadVO;
import com.base.basesetup.entity.BrsOpeningVO;
import com.base.basesetup.entity.ChargerDebitNoteVO;
import com.base.basesetup.entity.ChartCostCenterVO;
import com.base.basesetup.entity.ContraVoucherParticularsVO;
import com.base.basesetup.entity.ContraVoucherVO;
import com.base.basesetup.entity.CostCenterJobCardVO;
import com.base.basesetup.entity.DailyMonthlyExRatesDtlVO;
import com.base.basesetup.entity.DailyMonthlyExRatesVO;
import com.base.basesetup.entity.DebitNoteVO;
import com.base.basesetup.entity.DepositParticularsVO;
import com.base.basesetup.entity.DocumentTypeMappingDetailsVO;
import com.base.basesetup.entity.FundTransferVO;
import com.base.basesetup.entity.GeneralJournalVO;
import com.base.basesetup.entity.GlOpeningBalanceVO;
import com.base.basesetup.entity.GroupLedgerVO;
import com.base.basesetup.entity.GstDebitNoteVO;
import com.base.basesetup.entity.GstSalesVoucherVO;
import com.base.basesetup.entity.JobCardVO;
import com.base.basesetup.entity.MultipleDocIdGenerationDetailsVO;
import com.base.basesetup.entity.ParticularsDebitNoteVO;
import com.base.basesetup.entity.ParticularsGlOpeningBalanceVO;
import com.base.basesetup.entity.ParticularsGstVoucherVO;
import com.base.basesetup.entity.ParticularsJournalVO;
import com.base.basesetup.entity.ParticularsPaymentVoucherVO;
import com.base.basesetup.entity.ParticularsReconcileCorpBankVO;
import com.base.basesetup.entity.ParticularsReconcileVO;
import com.base.basesetup.entity.PaymentInvoiceVO;
import com.base.basesetup.entity.PaymentOtherAccountVO;
import com.base.basesetup.entity.PaymentReversalVO;
import com.base.basesetup.entity.PaymentVoucherVO;
import com.base.basesetup.entity.ReceiptInvoiceVO;
import com.base.basesetup.entity.ReceiptOtherAccountVO;
import com.base.basesetup.entity.ReceiptReversalVO;
import com.base.basesetup.entity.ReconcileBankVO;
import com.base.basesetup.entity.ReconcileCashVO;
import com.base.basesetup.entity.ReconcileCorpBankVO;
import com.base.basesetup.entity.WithdrawalParticularsVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.AccountParticularsRepo;
import com.base.basesetup.repo.AccountsDetailsRepo;
import com.base.basesetup.repo.AccountsRepo;
import com.base.basesetup.repo.AdjustmentJournalRepo;
import com.base.basesetup.repo.ArApAdjustmentOffSetRepo;
import com.base.basesetup.repo.ArApOffSetInvoiceDetailsRepo;
import com.base.basesetup.repo.ArapAdjustmentsRepo;
import com.base.basesetup.repo.ArapDetailsRepo;
import com.base.basesetup.repo.BankingDepositRepo;
import com.base.basesetup.repo.BankingWithdrawalRepo;
import com.base.basesetup.repo.BranchRepo;
import com.base.basesetup.repo.BrsExcelUploadRepo;
import com.base.basesetup.repo.BrsOpeningRepo;
import com.base.basesetup.repo.ChargerCostInvoiceRepo;
import com.base.basesetup.repo.ChargerDebitNoteRepo;
import com.base.basesetup.repo.ChartCostCenterRepo;
import com.base.basesetup.repo.ContraVoucherParticularsRepo;
import com.base.basesetup.repo.ContraVoucherRepo;
import com.base.basesetup.repo.CostCenterJobCardRepo;
import com.base.basesetup.repo.CostInvoiceRepo;
import com.base.basesetup.repo.DailyMonthlyExRatesDtlRepo;
import com.base.basesetup.repo.DailyMonthlyExRatesRepo;
import com.base.basesetup.repo.DebitNoteRepo;
import com.base.basesetup.repo.DepositParticularsRepo;
import com.base.basesetup.repo.DocumentTypeMappingDetailsRepo;
import com.base.basesetup.repo.FundTransferRepo;
import com.base.basesetup.repo.GeneralJournalRepo;
import com.base.basesetup.repo.GlOpeningBalanceRepo;
import com.base.basesetup.repo.GroupLedgerRepo;
import com.base.basesetup.repo.GstDebitNoteRepo;
import com.base.basesetup.repo.GstSalesVoucherRepo;
import com.base.basesetup.repo.IrnCreditNoteDetailsRepo;
import com.base.basesetup.repo.IrnCreditNoteGstRepo;
import com.base.basesetup.repo.IrnCreditNoteRepo;
import com.base.basesetup.repo.JobCardRepo;
import com.base.basesetup.repo.MultipleDocIdGenerationDetailsRepo;
import com.base.basesetup.repo.ParticularsDebitNoteRepo;
import com.base.basesetup.repo.ParticularsGlOpeningBalanceRepo;
import com.base.basesetup.repo.ParticularsGstVoucherRepo;
import com.base.basesetup.repo.ParticularsJournalRepo;
import com.base.basesetup.repo.ParticularsPaymentVoucherRepo;
import com.base.basesetup.repo.ParticularsReconcileCorpBankRepo;
import com.base.basesetup.repo.ParticularsReconcileRepo;
import com.base.basesetup.repo.PaymentInvoiceRepo;
import com.base.basesetup.repo.PaymentOtherAccountRepo;
import com.base.basesetup.repo.PaymentReversalRepo;
import com.base.basesetup.repo.PaymentVoucherRepo;
import com.base.basesetup.repo.ReceiptInvoiceRepo;
import com.base.basesetup.repo.ReceiptOtherAccountRepo;
import com.base.basesetup.repo.ReceiptReversalRepo;
import com.base.basesetup.repo.ReconcileBankRepo;
import com.base.basesetup.repo.ReconcileCashRepo;
import com.base.basesetup.repo.ReconcileCorpBankRepo;
import com.base.basesetup.repo.TaxInvoiceDetailsRepo;
import com.base.basesetup.repo.TaxInvoiceGstRepo;
import com.base.basesetup.repo.TaxInvoiceRepo;
import com.base.basesetup.repo.TdsCostInvoiceRepo;
import com.base.basesetup.repo.WithdrawalParticularsRepo;

@Service
public class TransactionServiceImpl implements TransactionService {
	public static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);
	@Autowired
	TaxInvoiceRepo taxInvoiceRepo;

	@Autowired
	TaxInvoiceDetailsRepo taxInvoiceDetailsRepo;

	@Autowired
	TaxInvoiceGstRepo gstTaxInvoiceRepo;

	@Autowired
	IrnCreditNoteRepo irnCreditRepo;

	@Autowired
	ReconcileBankRepo reconcileBankRepo;

	@Autowired
	ParticularsReconcileRepo particularsReconcileRepo;

	@Autowired
	ParticularsReconcileCorpBankRepo particularsReconcileCorpBankRepo;

	@Autowired
	ReconcileCorpBankRepo reconcileCorpBankRepo;

	@Autowired
	IrnCreditNoteDetailsRepo irnCreditChargesRepo;

	@Autowired
	GroupLedgerRepo groupLedgerRepo;

	@Autowired
	AmountInWordsConverterService amountInWordsConverterService;

	@Autowired
	IrnCreditNoteGstRepo irnCreditGstRepo;

	@Autowired
	DailyMonthlyExRatesRepo dailyMonthlyExRatesRepo;

	@Autowired
	DailyMonthlyExRatesDtlRepo dailyMonthlyExRatesDtlRepo;

	@Autowired
	BrsOpeningRepo brsOpeningRepo;

	@Autowired
	ChartCostCenterRepo chartCostCenterRepo;

	@Autowired
	FundTransferRepo fundTransferRepo;

	@Autowired
	GeneralJournalRepo generalJournalRepo;

	@Autowired
	ParticularsJournalRepo particularsJournalRepo;

	@Autowired
	CostInvoiceRepo costInvoiceRepo;

	@Autowired
	TdsCostInvoiceRepo tdsCostInvoiceRepo;

	@Autowired
	ChargerCostInvoiceRepo chargerCostInvoiceRepo;

	@Autowired
	DebitNoteRepo debitNoteRepo;

	@Autowired
	ChargerDebitNoteRepo chargerDebitNoteRepo;

	@Autowired
	ParticularsDebitNoteRepo particularsDebitNoteRepo;

	@Autowired
	GstDebitNoteRepo gstDebitNoteRepo;

	@Autowired
	GstSalesVoucherRepo gstSalesVoucherRepo;

	@Autowired
	ParticularsGstVoucherRepo particularsGstVoucherRepo;

	@Autowired
	PaymentVoucherRepo paymentVoucherRepo;

	@Autowired
	ParticularsPaymentVoucherRepo particularsPaymentVoucherRepo;

	@Autowired
	ArapDetailsRepo arapDetailsRepo;

	@Autowired
	ArapAdjustmentsRepo arapAdjustmentsRepo;

	@Autowired
	ReceiptReversalRepo receiptReversalRepo;

	@Autowired
	ReceiptInvoiceRepo receiptInvoiceRepo;

	@Autowired
	ReceiptOtherAccountRepo receiptOtherAccountRepo;

	@Autowired
	PaymentReversalRepo paymentReversalRepo;

	@Autowired
	PaymentInvoiceRepo paymentInvoiceRepo;

	@Autowired
	PaymentOtherAccountRepo paymentOtherAccountRepo;

	@Autowired
	ArApAdjustmentOffSetRepo arApAdjustmentOffSetRepo;

	@Autowired
	ArApOffSetInvoiceDetailsRepo arApOffSetInvoiceDetailsRepo;

	@Autowired
	BranchRepo branchRepo;

	@Autowired
	GlOpeningBalanceRepo glOpeningBalanceRepo;

	@Autowired
	ParticularsGlOpeningBalanceRepo particularsGlOpeningBalanceRepo;

	@Autowired
	BrsExcelUploadRepo brsExcelUploadRepo;

	@Autowired
	JobCardRepo tmsJobCardRepo;

	@Autowired
	CostCenterJobCardRepo costCenterTmsJobCardRepo;

//	@Autowired
//	ReconciliationSummaryRepo reconciliationSummaryRepo;

	@Autowired
	ReconcileCashRepo reconcileCashRepo;

	@Autowired
	AdjustmentJournalRepo adjustmentJournalRepo;

	@Autowired
	DocumentTypeMappingDetailsRepo documentTypeMappingDetailsRepo;

	@Autowired
	ParticularsPaymentVoucherRepo ParticularsPaymentVoucherRepo;

	@Autowired
	AccountParticularsRepo accountParticularsRepo;

	@Autowired
	BankingDepositRepo bankingDepositRepo;

	@Autowired
	DepositParticularsRepo depositParticularsRepo;

	@Autowired
	BankingWithdrawalRepo bankingWithdrawalRepo;

	@Autowired
	WithdrawalParticularsRepo withdrawalParticularsRepo;

	@Autowired
	ContraVoucherRepo contraVoucherRepo;

	@Autowired
	ContraVoucherParticularsRepo contraVoucherParticularsRepo;

	@Autowired
	AccountsRepo accountsRepo;

	@Autowired
	AccountsDetailsRepo accountsDetailsRepo;

	@Autowired
	MultipleDocIdGenerationDetailsRepo multipleDocIdGenerationDetailsRepo;

	// DailyMonthlyExRates
	@Override
	public List<DailyMonthlyExRatesVO> getAllDailyMonthlyExRatesById(Long id) {
		List<DailyMonthlyExRatesVO> dailyMonthlyExRatesVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received DailyMonthlyExRates BY Id : {}", id);
			dailyMonthlyExRatesVO = dailyMonthlyExRatesRepo.findDailyMonthlyExRatesById(id);
		} else {
			LOGGER.info("Successfully Received DailyMonthlyExRates For All Id.");
			dailyMonthlyExRatesVO = dailyMonthlyExRatesRepo.findAll();
		}
		return dailyMonthlyExRatesVO;
	}

	@Override
	public List<DailyMonthlyExRatesVO> getAllDailyMonthlyExRatesByOrgId(Long orgId) {
		List<DailyMonthlyExRatesVO> dailyMonthlyExRatesVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received DailyMonthlyExRates BY OrgId : {}", orgId);
			dailyMonthlyExRatesVO = dailyMonthlyExRatesRepo.findDailyMonthlyExRatesByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received DailyMonthlyExRates For All OrgId.");
			dailyMonthlyExRatesVO = dailyMonthlyExRatesRepo.findAll();
		}
		return dailyMonthlyExRatesVO;
	}

	@Override
	public DailyMonthlyExRatesVO updateCreateDailyMonthlyExRates(@Valid DailyMonthlyExRatesDTO dailyMonthlyExRatesDTO)
			throws ApplicationException {
		DailyMonthlyExRatesVO dailyMonthlyExRatesVO = new DailyMonthlyExRatesVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(dailyMonthlyExRatesDTO.getId())) {
			isUpdate = true;
			dailyMonthlyExRatesVO = dailyMonthlyExRatesRepo.findById(dailyMonthlyExRatesDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid DailyMonthlyExRates details"));
			dailyMonthlyExRatesVO.setUpdatedBy(dailyMonthlyExRatesDTO.getCreatedBy());
		} else {
			dailyMonthlyExRatesVO.setUpdatedBy(dailyMonthlyExRatesDTO.getCreatedBy());
			dailyMonthlyExRatesVO.setCreatedBy(dailyMonthlyExRatesDTO.getCreatedBy());
		}

		List<DailyMonthlyExRatesDtlVO> dailyMonthlyExRatesDtlVOs = new ArrayList<>();
		if (dailyMonthlyExRatesDTO.getDailyMonthlyExRatesDtlDTO() != null) {
			for (DailyMonthlyExRatesDtlDTO dailyMonthlyExRatesDtlDTO : dailyMonthlyExRatesDTO
					.getDailyMonthlyExRatesDtlDTO()) {
				DailyMonthlyExRatesDtlVO dailyMonthlyExRatesDtlVO;
				if (dailyMonthlyExRatesDtlDTO.getId() != null
						&& ObjectUtils.isNotEmpty(dailyMonthlyExRatesDtlDTO.getId())) {
					dailyMonthlyExRatesDtlVO = dailyMonthlyExRatesDtlRepo.findById(dailyMonthlyExRatesDtlDTO.getId())
							.orElse(new DailyMonthlyExRatesDtlVO());
				} else {
					dailyMonthlyExRatesDtlVO = new DailyMonthlyExRatesDtlVO();
				}
				dailyMonthlyExRatesDtlVO.setBuyingExrate(dailyMonthlyExRatesDtlDTO.getBuyingExrate());
				dailyMonthlyExRatesDtlVO.setCurrency(dailyMonthlyExRatesDtlDTO.getCurrency());
				dailyMonthlyExRatesDtlVO.setCurrencyDescripition(dailyMonthlyExRatesDtlDTO.getCurrencyDescripition());
				dailyMonthlyExRatesDtlVO.setSellingExRate(dailyMonthlyExRatesDtlDTO.getSellingExRate());
				dailyMonthlyExRatesDtlVO.setDailyMonthlyExRatesVO(dailyMonthlyExRatesVO);
				dailyMonthlyExRatesDtlVOs.add(dailyMonthlyExRatesDtlVO);
			}
		}
		dailyMonthlyExRatesVO.setDailyMonthlyExRatesDtlVO(dailyMonthlyExRatesDtlVOs); // Assuming you have this setter
		getDailyMonthlyExRatesVOFromDailyMonthlyExRatesDTO(dailyMonthlyExRatesDTO, dailyMonthlyExRatesVO);
		return dailyMonthlyExRatesRepo.save(dailyMonthlyExRatesVO);
	}

	private void getDailyMonthlyExRatesVOFromDailyMonthlyExRatesDTO(
			@Valid DailyMonthlyExRatesDTO dailyMonthlyExRatesDTO, DailyMonthlyExRatesVO dailyMonthlyExRatesVO) {
		dailyMonthlyExRatesVO.setDate(dailyMonthlyExRatesDTO.getDate());
		LocalDate date = LocalDate.parse(dailyMonthlyExRatesDTO.getMonth());

		// Define a formatter to convert to "MMMM yyyy" format (e.g., March 2024)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

		// Format the date and set it to the VO
		String formattedDate = date.format(formatter);
		dailyMonthlyExRatesVO.setMonth(formattedDate);
		dailyMonthlyExRatesVO.setActive(dailyMonthlyExRatesDTO.isActive());
		dailyMonthlyExRatesVO.setOrgId(dailyMonthlyExRatesDTO.getOrgId());
	}

	@Override
	public List<DailyMonthlyExRatesVO> getDailyMonthlyExRatesByActive() {
		return dailyMonthlyExRatesRepo.findDailyMonthlyExRatesByActive();

	}
	// BrsOpening

	@Override
	public List<BrsOpeningVO> getAllBrsOpeningByOrgId(Long orgId) {
		List<BrsOpeningVO> brsOpeningVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  BrsOpening BY OrgId : {}", orgId);
			brsOpeningVO = brsOpeningRepo.getAllBrsOpeningByOrgId(orgId);
		}
		return brsOpeningVO;
	}

	@Override
	public List<BrsOpeningVO> getAllBrsOpeningById(Long id) {
		List<BrsOpeningVO> brsOpeningVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received BrsOpening BY Id : {}", id);
			brsOpeningVO = brsOpeningRepo.getAllBrsOpeningById(id);
		}
		return brsOpeningVO;
	}

	@Override
	public Map<String, Object> updateCreateBrsOpening(@Valid BrsOpeningDTO brsOpeningDTO) throws ApplicationException {
		String screenCode = "BO";
		BrsOpeningVO brsOpeningVO = new BrsOpeningVO();
		String message;
		if (ObjectUtils.isNotEmpty(brsOpeningDTO.getId())) {
			brsOpeningVO = brsOpeningRepo.findById(brsOpeningDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid BrsOpening details"));
			createUpdateBrsOpeningVOByBrsOpeningDTO(brsOpeningDTO, brsOpeningVO);
			message = "Brs Opening Updated Successfully";
			brsOpeningVO.setUpdatedBy(brsOpeningDTO.getCreatedBy());
		} else {
			brsOpeningVO.setUpdatedBy(brsOpeningDTO.getCreatedBy());
			brsOpeningVO.setCreatedBy(brsOpeningDTO.getCreatedBy());
			createUpdateBrsOpeningVOByBrsOpeningDTO(brsOpeningDTO, brsOpeningVO);
			message = "BRS Opening Created Successfully";
		}

		brsOpeningRepo.save(brsOpeningVO);
		Map<String, Object> response = new HashMap<>();
		response.put("brsOpeningVO", brsOpeningVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateBrsOpeningVOByBrsOpeningDTO(@Valid BrsOpeningDTO brsOpeningDTO,
			BrsOpeningVO brsOpeningVO) {
		brsOpeningVO.setBillNo(brsOpeningDTO.getBillNo());
		brsOpeningVO.setBillDate(brsOpeningDTO.getBillDate());
		brsOpeningVO.setChqNo(brsOpeningDTO.getChqNo());
		brsOpeningVO.setChqDate(brsOpeningDTO.getChqDate());
		brsOpeningVO.setBank(brsOpeningDTO.getBank());
		brsOpeningVO.setCurrency(brsOpeningDTO.getCurrency());
		brsOpeningVO.setExRate(brsOpeningDTO.getExRate());
		brsOpeningVO.setReceiptAmount(brsOpeningDTO.getReceiptAmount());
		brsOpeningVO.setPaymentAmount(brsOpeningDTO.getPaymentAmount());
		brsOpeningVO.setReconcile(brsOpeningDTO.isReconcile());
		brsOpeningVO.setOrgId(brsOpeningDTO.getOrgId());
		brsOpeningVO.setBranch(brsOpeningDTO.getBranch());
		brsOpeningVO.setBranchCode(brsOpeningDTO.getBranchCode());
		brsOpeningVO.setActive(brsOpeningDTO.isActive());
		brsOpeningVO.setCancel(brsOpeningDTO.isCancel());
		brsOpeningVO.setCancelRemarks(brsOpeningDTO.getCancelRemarks());
		brsOpeningVO.setFinYear(brsOpeningDTO.getFinYear());
	}

	@Override
	public List<BrsOpeningVO> getBrsOpeningByActive() {
		return brsOpeningRepo.findBrsOpeningByActive();
	}

	@Override
	public List<Map<String, Object>> getBranchForBrsOpening(Long orgId) {
		Set<Object[]> result = branchRepo.findBranchForBrsOpening(orgId);
		return getBranch(result);
	}

	private List<Map<String, Object>> getBranch(Set<Object[]> result) {
		List<Map<String, Object>> details = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> object = new HashMap<>();
			object.put("branch", fs[0] != null ? fs[0].toString() : "");
		}
		return details;
	}

	// BRS file upload
	private int totalRows = 0; // Instance variable to keep track of total rows
	private int successfulUploads = 0; // Instance variable to keep track of successful uploads

	private final DataFormatter dataFormatter = new DataFormatter();

	@Transactional
	public void ExcelUploadForBrs(MultipartFile[] files, Long orgId, String createdBy, String branch, String branchCode)
			throws ApplicationException {
		List<BrsExcelUploadVO> brsExcelUploadVOsToSave = new ArrayList<>();
		totalRows = 0;
		successfulUploads = 0;

		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				throw new ApplicationException(
						"The supplied file '" + file.getOriginalFilename() + "' is empty (zero bytes long).");
			}

			try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
				Sheet sheet = workbook.getSheetAt(0);
				List<String> errorMessages = new ArrayList<>();
				System.out.println("Processing file: " + file.getOriginalFilename());

				Row headerRow = sheet.getRow(0);
				if (!isHeaderValid(headerRow)) {
					throw new ApplicationException("Invalid Excel format in file '" + file.getOriginalFilename()
							+ "'. Expected headers are: Type, From location, From location type, Location pick, partno, partdesc, sku, Grn No, GRN date, Batch No, Exp date, Entry no, From Status, To Status");
				}

				for (Row row : sheet) {
					if (row.getRowNum() == 0 || isRowEmpty(row)) {
						continue;
					}

					totalRows++;
					System.out.println("Validating row: " + (row.getRowNum() + 1));

					try {
						String billNo = getStringCellValue(row.getCell(0));
						LocalDate billDate = parseDate(row.getCell(1));
						String chqNo = getStringCellValue(row.getCell(2));
						LocalDate chqDate = parseDate(row.getCell(3));
						String bank = getStringCellValue(row.getCell(4));
						String currency = getStringCellValue(row.getCell(5));
						String exRate = getStringCellValue(row.getCell(6));
						BigDecimal receiptAmount = getBigDecimalCellValue(row.getCell(7));
						BigDecimal paymentAmount = getBigDecimalCellValue(row.getCell(8));
						boolean reconcile = getBooleanCellValue(row.getCell(9));

						// Create and populate SrsExcelUploadVO object
						BrsExcelUploadVO brsExcelUploadVO = new BrsExcelUploadVO();
						brsExcelUploadVO.setBillNo(billNo);
						brsExcelUploadVO.setBillDate(billDate);
						brsExcelUploadVO.setChqNo(chqNo);
						brsExcelUploadVO.setChqDate(chqDate);
						brsExcelUploadVO.setBank(bank);
						brsExcelUploadVO.setCurrency(currency);
						brsExcelUploadVO.setExRate(exRate);
						brsExcelUploadVO.setReceiptAmount(receiptAmount);
						brsExcelUploadVO.setPaymentAmount(paymentAmount);
						brsExcelUploadVO.setReconcile(reconcile);
						brsExcelUploadVO.setOrgId(orgId);
						brsExcelUploadVO.setBranch(branch);
						brsExcelUploadVO.setBranchCode(branchCode);
						brsExcelUploadVO.setCreatedBy(createdBy);
						brsExcelUploadVO.setUpdatedBy(createdBy);
						brsExcelUploadVO.setActive(true);
						brsExcelUploadVO.setCancel(false);
						brsExcelUploadVO.setCancelRemarks("");

						brsExcelUploadVOsToSave.add(brsExcelUploadVO);
						successfulUploads++;
					} catch (Exception e) {
						// Optionally handle specific row processing exceptions here
					}
				}

				brsExcelUploadRepo.saveAll(brsExcelUploadVOsToSave);
			} catch (IOException e) {
				throw new ApplicationException(
						"Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
			}
		}
	}

	private boolean getBooleanCellValue(Cell cell) {
		if (cell == null) {
			return false; // Return false if the cell is null
		}

		switch (cell.getCellType()) {
		case BOOLEAN:
			return cell.getBooleanCellValue(); // If the cell type is BOOLEAN, return its value
		case STRING:
			// For string values, treat "true" (case-insensitive) as true, anything else as
			// false
			return "true".equalsIgnoreCase(cell.getStringCellValue().trim());
		case NUMERIC:
			// If numeric and non-zero, treat as true (e.g., 1 = true, 0 = false)
			return cell.getNumericCellValue() != 0;
		default:
			throw new RuntimeException("Cell type is not BOOLEAN, STRING, or NUMERIC.");
		}
	}

	private BigDecimal getBigDecimalCellValue(Cell cell) {
		if (cell == null) {
			return BigDecimal.ZERO; // Return 0 if the cell is null
		}

		switch (cell.getCellType()) {
		case NUMERIC:
			return BigDecimal.valueOf(cell.getNumericCellValue()); // Convert numeric value to BigDecimal
		case STRING:
			try {
				return new BigDecimal(cell.getStringCellValue().trim()); // Convert string value to BigDecimal
			} catch (NumberFormatException e) {
				throw new RuntimeException("Invalid number format for BigDecimal: " + cell.getStringCellValue());
			}
		default:
			throw new RuntimeException("Cell type is not numeric or string convertible to BigDecimal.");
		}
	}

	private LocalDate parseDate(Cell cell) {
		if (cell == null) {
			return null;
		}

		try {
			if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
				return cell.getLocalDateTimeCellValue().toLocalDate();
			} else if (cell.getCellType() == CellType.STRING) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Adjusted to dd/MM/yyyy
				return LocalDate.parse(cell.getStringCellValue(), formatter);
			}
		} catch (Exception e) {
			System.err.println("Date parsing error for cell value: " + getStringCellValue(cell));
		}
		return null;
	}

	private String getStringCellValue(Cell cell) {
		if (cell == null) {
			return ""; // Return empty string if cell is null
		}

		// Use DataFormatter to get the cell value as a string
		return dataFormatter.formatCellValue(cell);
	}

	private boolean isRowEmpty(Row row) {
		for (Cell cell : row) {
			if (cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}

	private boolean isHeaderValid(Row headerRow) throws ApplicationException {
		if (headerRow == null) {
			return false;
		}
		List<String> expectedHeaders = Arrays.asList("Bill No", "Bill Date", "Cheque No", "Cheque Date", "Bank",
				"Currency", "Ex Rate", "Receipt Amount", "Payment Amount", "Reconcile");

		List<String> actualHeaders = new ArrayList<>();
		for (Cell cell : headerRow) {
			actualHeaders.add(getStringCellValue(cell).trim());
		}

		if (!expectedHeaders.equals(actualHeaders)) {
			String errorDetails = "Expected headers: " + expectedHeaders.toString() + ", Found headers: "
					+ actualHeaders.toString();
			throw new ApplicationException("Invalid Excel format. " + errorDetails);
		}
		return true;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public int getSuccessfulUploads() {
		return successfulUploads;
	}

	@Override
	public List<BrsExcelUploadVO> getAllBrsExcelByOrgId(Long orgId) {
		List<BrsExcelUploadVO> brsExcelUploadVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received BrsExcel BY Id : {}", orgId);
			brsExcelUploadVO = brsExcelUploadRepo.findBrsExcelByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received BrsExcel For All orgId.");
			brsExcelUploadVO = brsExcelUploadRepo.findAll();
		}
		return brsExcelUploadVO;
	}

	// ChartCostCenter
	@Override
	public List<ChartCostCenterVO> getAllChartCostCenterByOrgId(Long orgId) {
		List<ChartCostCenterVO> chartCostCenterVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received ChartCostCenter BY OrgId : {}", orgId);
			chartCostCenterVO = chartCostCenterRepo.getAllChartCostCenterByOrgId(orgId);
		}
		return chartCostCenterVO;
	}

	@Override
	public List<ChartCostCenterVO> getChartCostCenterById(Long id) {
		List<ChartCostCenterVO> chartCostCenterVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ChartCostCenter BY Id : {}", id);
			chartCostCenterVO = chartCostCenterRepo.getChartCostCenterById(id);
		}
		return chartCostCenterVO;
	}

	@Override
	public List<ChartCostCenterVO> getChartCostCenterByActive() {
		return chartCostCenterRepo.findChartCostCenterByActive();
	}

	@Override
	public String getChartCostCenterDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "CCC";
		String result = chartCostCenterRepo.getChartCostCenterDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<Map<String, Object>> updateCreateChartCostCenterList(
			@Valid List<ChartCostCenterDTO> chartCostCenterDTOList) throws ApplicationException {
		List<Map<String, Object>> responseList = new ArrayList<>();
		String screenCode = "CCC";

		for (ChartCostCenterDTO chartCostCenterDTO : chartCostCenterDTOList) {
			ChartCostCenterVO chartCostCenterVO;
			String message;

			if (ObjectUtils.isNotEmpty(chartCostCenterDTO.getId())) {
				// Update existing record
				chartCostCenterVO = chartCostCenterRepo.findById(chartCostCenterDTO.getId())
						.orElseThrow(() -> new ApplicationException(
								"ChartCostCenter Not Found with id: " + chartCostCenterDTO.getId()));
				chartCostCenterVO.setUpdatedBy(chartCostCenterDTO.getCreatedBy());
				getChartCostCenterVOFromChartCostCenterDTO(chartCostCenterDTO, chartCostCenterVO);
				message = "ChartCostCenter Updation Successfully";
			} else {
				// Create new record
				chartCostCenterVO = new ChartCostCenterVO();
				chartCostCenterVO.setCreatedBy(chartCostCenterDTO.getCreatedBy());
				chartCostCenterVO.setUpdatedBy(chartCostCenterDTO.getCreatedBy());
				getChartCostCenterVOFromChartCostCenterDTO(chartCostCenterDTO, chartCostCenterVO);
				message = "ChartCostCenter Creation Successfully";
			}

			chartCostCenterRepo.save(chartCostCenterVO);

			Map<String, Object> response = new HashMap<>();
			response.put("chartCostCenterVO", chartCostCenterVO);
			response.put("message", message);
			responseList.add(response);
		}

		return responseList;
	}

	private void getChartCostCenterVOFromChartCostCenterDTO(@Valid ChartCostCenterDTO chartCostCenterDTO,
			ChartCostCenterVO chartCostCenterVO) {
		chartCostCenterVO.setCostCenterCode(chartCostCenterDTO.getCostCenterCode());
		chartCostCenterVO.setCostCenterName(chartCostCenterDTO.getCostCenterName());
		chartCostCenterVO.setCredit(chartCostCenterDTO.getCredit());
		chartCostCenterVO.setDebit(chartCostCenterDTO.getDebit());
		chartCostCenterVO.setOrgId(chartCostCenterDTO.getOrgId());
		chartCostCenterVO.setActive(chartCostCenterDTO.isActive());
		chartCostCenterVO.setCancel(chartCostCenterDTO.isCancel());
		chartCostCenterVO.setCancelRemarks(chartCostCenterDTO.getCancelRemarks());
		chartCostCenterVO.setBranch(chartCostCenterDTO.getBranch());
		chartCostCenterVO.setBranchCode(chartCostCenterDTO.getBranchCode());
		chartCostCenterVO.setFinYear(chartCostCenterDTO.getFinYear());
	}

	// FundTransfer

	@Override
	public List<FundTransferVO> getAllFundTransferByOrgId(Long orgId) {
		List<FundTransferVO> fundTransferVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  FundTransfer BY OrgId : {}", orgId);
			fundTransferVO = fundTransferRepo.getAllFundTransferByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received  FundTransfer For All OrgId.");
			fundTransferVO = fundTransferRepo.findAll();
		}
		return fundTransferVO;
	}

	@Override
	public List<FundTransferVO> getAllFundTransferById(Long id) {
		List<FundTransferVO> fundTransferVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received FundTransfer BY Id : {}", id);
			fundTransferVO = fundTransferRepo.getAllFundTransferById(id);
		} else {
			LOGGER.info("Successfully Received FundTransfer For All Id.");
			fundTransferVO = fundTransferRepo.findAll();
		}
		return fundTransferVO;
	}

	@Override
	public Map<String, Object> updateCreateFundTransfer(@Valid FundTransferDTO fundTransferDTO)
			throws ApplicationException {
		FundTransferVO fundTransferVO = new FundTransferVO();
		String message = null;
		String screenCode = "FT";

		if (fundTransferDTO.getCorpAccount().equalsIgnoreCase(fundTransferDTO.getTransferTo())) {

			String errorMessage = String.format("CorpAccount And TransferAccount Is Same,Try Different Bank",
					fundTransferDTO.getCorpAccount());

			throw new ApplicationException(errorMessage);
		}

		if (ObjectUtils.isEmpty(fundTransferDTO.getId())) {

			fundTransferVO = new FundTransferVO();

			// GETDOCID API
			String docId = fundTransferRepo.getFundTranferDocId(fundTransferDTO.getOrgId(),
					fundTransferDTO.getFinYear(), fundTransferDTO.getBranchCode(), screenCode);
			fundTransferVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(fundTransferDTO.getOrgId(),
							fundTransferDTO.getFinYear(), fundTransferDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			fundTransferVO.setCreatedBy(fundTransferDTO.getCreatedBy());
			fundTransferVO.setUpdatedBy(fundTransferDTO.getCreatedBy());

			message = "FundTransfer Creation Successfully";

		}

		else {
			fundTransferVO = fundTransferRepo.findById(fundTransferDTO.getId()).orElseThrow(
					() -> new ApplicationException("Fund Transfer Not Found with id: " + fundTransferDTO.getId()));
			fundTransferVO.setUpdatedBy(fundTransferDTO.getCreatedBy());

			message = "FundTransfer Updation Successfully";
		}

		getFundTransferVOFromFundTransferDTO(fundTransferDTO, fundTransferVO);
		fundTransferRepo.save(fundTransferVO);

		Map<String, Object> response = new HashMap<>();
		response.put("fundTransferVO", fundTransferVO);
		response.put("message", message);
		return response;
	}

	private void getFundTransferVOFromFundTransferDTO(@Valid FundTransferDTO fundTransferDTO,
			FundTransferVO fundTransferVO) {
		fundTransferVO.setBranch(fundTransferDTO.getBranch());
		fundTransferVO.setCurrency(fundTransferDTO.getCurrency());
		fundTransferVO.setExRate(fundTransferDTO.getExRate());
		fundTransferVO.setAmount(fundTransferDTO.getAmount());
		fundTransferVO.setOrgId(fundTransferDTO.getOrgId());
		fundTransferVO.setCreatedBy(fundTransferDTO.getCreatedBy());
		fundTransferVO.setCancelRemarks(fundTransferDTO.getCancelRemarks());
		fundTransferVO.setBranchCode(fundTransferDTO.getBranchCode());
		fundTransferVO.setFinYear(fundTransferDTO.getFinYear());
		fundTransferVO.setIpNo(fundTransferDTO.getIpNo());
		fundTransferVO.setLatitude(fundTransferDTO.getLatitude());
		fundTransferVO.setMode(fundTransferDTO.getMode());
		fundTransferVO.setDocNo(fundTransferDTO.getDocNo());
		fundTransferVO.setCorpAccount(fundTransferDTO.getCorpAccount());
		fundTransferVO.setTransferTo(fundTransferDTO.getTransferTo());
		fundTransferVO.setBranchAcc(fundTransferDTO.getBranchAcc());
		fundTransferVO.setAmtBase(fundTransferDTO.getAmtBase());
		fundTransferVO.setNarration(fundTransferDTO.getNarration());

	}

	@Override
	public List<FundTransferVO> getFundTransferByActive() {
		return fundTransferRepo.findFundTransferByActive();
	}

	// GeneralJournal

	@Override
	public List<GeneralJournalVO> getAllGeneralJournalByOrgId(Long orgId) {
		List<GeneralJournalVO> generalJournalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  GeneralJournal BY OrgId : {}", orgId);
			generalJournalVO = generalJournalRepo.getAllGeneralJournalByOrgId(orgId);
		}
		return generalJournalVO;
	}

	@Override
	public List<GeneralJournalVO> getGeneralJournalById(Long id) {
		List<GeneralJournalVO> generalJournalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received GeneralJournal BY Id : {}", id);
			generalJournalVO = generalJournalRepo.getGeneralJournalById(id);
		}
		return generalJournalVO;
	}

	@Override
	public Map<String, Object> updateCreateGeneralJournal(@Valid GeneralJournalDTO generalJournalDTO)
			throws ApplicationException {
		String sourceScreenCode = "GJ";
		String screenCode = "AC";
		GeneralJournalVO generalJournalVO = new GeneralJournalVO();
		String message;

		if (ObjectUtils.isNotEmpty(generalJournalDTO.getId())) {
			generalJournalVO = generalJournalRepo.findById(generalJournalDTO.getId())
					.orElseThrow(() -> new ApplicationException("General Journal not found"));

			generalJournalVO.setUpdatedBy(generalJournalDTO.getCreatedBy());
			createUpdateJournalVOByGeneralJournalDTO(generalJournalDTO, generalJournalVO);
			message = "General Journal Updated Successfully";

		} else {

//			// GETDOCID API
			String accountsDocId = accountsRepo.getApproveDocId(generalJournalDTO.getOrgId(),
					generalJournalDTO.getFinYear(), generalJournalDTO.getBranchCode(), sourceScreenCode, screenCode);
			generalJournalVO.setDocId(accountsDocId);

			// GETDOCID LASTNO +1
			MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(generalJournalDTO.getOrgId(),
							generalJournalDTO.getFinYear(), generalJournalDTO.getBranchCode(), sourceScreenCode,
							screenCode);
			multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);

			generalJournalVO.setCreatedBy(generalJournalDTO.getCreatedBy());
			generalJournalVO.setUpdatedBy(generalJournalDTO.getCreatedBy());
			createUpdateJournalVOByGeneralJournalDTO(generalJournalDTO, generalJournalVO);
			message = "General Journal Created Successfully";
		}
		GeneralJournalVO savedGeneralJournalVO = generalJournalRepo.save(generalJournalVO);
		if (savedGeneralJournalVO.getStatus().equalsIgnoreCase("SUBMIT")) {
			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(savedGeneralJournalVO.getDocId());
			accountsVO.setSourceScreen(savedGeneralJournalVO.getScreenName());
			accountsVO.setSourceScreenCode(savedGeneralJournalVO.getScreenCode());
			accountsVO.setSourceId(savedGeneralJournalVO.getId());
			accountsVO.setCreatedBy(savedGeneralJournalVO.getCreatedBy());
			accountsVO.setModifiedBy(savedGeneralJournalVO.getUpdatedBy());
			accountsVO.setOrgId(savedGeneralJournalVO.getOrgId());
			accountsVO.setBranch(savedGeneralJournalVO.getBranch());
			accountsVO.setBranchCode(savedGeneralJournalVO.getBranchCode());
			accountsVO.setModifiedon(savedGeneralJournalVO.getCommonDate().getModifiedon().toUpperCase());
			accountsVO.setCreatedon(savedGeneralJournalVO.getCommonDate().getModifiedon().toUpperCase());
			accountsVO.setRefNo(savedGeneralJournalVO.getDocId());
			accountsVO.setRefDate(savedGeneralJournalVO.getDocDate());
			accountsVO.setCurrency(savedGeneralJournalVO.getCurrency());
			accountsVO.setExRate(savedGeneralJournalVO.getExRate());
			accountsVO.setRemarks(savedGeneralJournalVO.getRemarks());
			accountsVO.setFinYear(savedGeneralJournalVO.getFinYear());

			accountsVO.setTotalDebitAmount(savedGeneralJournalVO.getTotalDebitAmount());
			accountsVO.setTotalCreditAmount(savedGeneralJournalVO.getTotalCreditAmount());
			accountsVO.setAmountInWords(
					amountInWordsConverterService.convert(savedGeneralJournalVO.getTotalDebitAmount().longValue()));

			// Create AccountsDetailsVO list and populate it
			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();
			List<ParticularsJournalVO> particularsJournalVOLists = savedGeneralJournalVO.getParticularsJournalVO();
			for (ParticularsJournalVO particularsJournalVO : particularsJournalVOLists) {
				GroupLedgerVO groupLedgerVO = groupLedgerRepo
						.findByAccountGroupName(particularsJournalVO.getAccountsName());
				AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
				accountsDetailsVO.setACategory(groupLedgerVO.getCategory());
				accountsDetailsVO.setArapFlag(false);
				accountsDetailsVO.setSubledgerName("None");
				accountsDetailsVO.setSubLedgerCode("None");
				accountsDetailsVO.setAccountName(groupLedgerVO.getAccountGroupName());
				accountsDetailsVO.setACurrency(savedGeneralJournalVO.getCurrency());
				accountsDetailsVO.setArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setNArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setAExRate(savedGeneralJournalVO.getExRate());
				accountsDetailsVO.setGstflag(3);

				if (!particularsJournalVO.getDebitAmount().equals(BigDecimal.ZERO)) {
					accountsDetailsVO.setNDebitAmount(particularsJournalVO.getDebitAmount());
					accountsDetailsVO.setDebitAmount(particularsJournalVO.getDebitAmount());
					accountsDetailsVO.setBDebitAmount(particularsJournalVO.getDebitAmount());
					accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
					accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
					accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
				} else {
					accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
					accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
					accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
					accountsDetailsVO.setNCreditAmount(particularsJournalVO.getCreditAmount());
					accountsDetailsVO.setCreditAmount(particularsJournalVO.getCreditAmount());
					accountsDetailsVO.setBCrAmount(particularsJournalVO.getCreditAmount());
				}
				accountsDetailsVO.setAccountsVO(accountsVO);
				accountsDetailsVOs.add(accountsDetailsVO);
			}
			accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
			accountsRepo.save(accountsVO);
		}
		Map<String, Object> response = new HashMap<>();
		response.put("generalJournalVO", generalJournalVO);
		response.put("message", message);
		return response;

	}

	private void createUpdateJournalVOByGeneralJournalDTO(@Valid GeneralJournalDTO generalJournalDTO,
			GeneralJournalVO generalJournalVO) throws ApplicationException {
		generalJournalVO.setVoucherSubType(generalJournalDTO.getVoucherSubType());
		generalJournalVO.setRemarks(generalJournalDTO.getRemarks().toUpperCase());
		generalJournalVO.setCurrency(generalJournalDTO.getCurrency());
		generalJournalVO.setExRate(generalJournalDTO.getExRate());

		generalJournalVO.setRefNo(generalJournalDTO.getRefNo().toUpperCase());
		generalJournalVO.setRefDate(generalJournalDTO.getRefDate());
		generalJournalVO.setOrgId(generalJournalDTO.getOrgId());
		generalJournalVO.setBranch(generalJournalDTO.getBranch());
		generalJournalVO.setBranchCode(generalJournalDTO.getBranchCode());
		generalJournalVO.setFinYear(generalJournalDTO.getFinYear());
		generalJournalVO.setStatus(generalJournalDTO.getStatus());

		if (ObjectUtils.isNotEmpty(generalJournalDTO.getId())) {
			List<ParticularsJournalVO> particularsJournalVOList = particularsJournalRepo
					.findByGeneralJournalVO(generalJournalVO);
			particularsJournalRepo.deleteAll(particularsJournalVOList);
		}

		BigDecimal totalDebitAmount = BigDecimal.ZERO;
		BigDecimal totalCreditAmount = BigDecimal.ZERO;
		List<ParticularsJournalVO> particularsJournalVOs = new ArrayList<>();
		for (ParticularsJournalDTO particularsJournalDTO : generalJournalDTO.getParticularsJournalDTO()) {
			ParticularsJournalVO particularsJournalVO = new ParticularsJournalVO();

			particularsJournalVO.setAccountsName(particularsJournalDTO.getAccountsName());
			particularsJournalVO.setSubledgerName(particularsJournalDTO.getSubledgerName());
			particularsJournalVO.setSubLedgerCode(particularsJournalDTO.getSubLedgerCode());
			if (particularsJournalDTO.getDebitAmount() != null
					&& particularsJournalDTO.getDebitAmount().compareTo(BigDecimal.ZERO) != 0) {
				particularsJournalVO.setDebitAmount(particularsJournalDTO.getDebitAmount());
				particularsJournalVO.setCreditAmount(BigDecimal.ZERO);
			} else {
				particularsJournalVO.setCreditAmount(particularsJournalDTO.getCreditAmount());
				particularsJournalVO.setDebitAmount(BigDecimal.ZERO);
			}
			totalCreditAmount = totalCreditAmount.add(particularsJournalVO.getCreditAmount());
			totalDebitAmount = totalDebitAmount.add(particularsJournalVO.getDebitAmount());
			particularsJournalVO.setNarration(particularsJournalDTO.getNarration());
			particularsJournalVO.setGeneralJournalVO(generalJournalVO);
			particularsJournalVOs.add(particularsJournalVO);
		}
		if (totalCreditAmount.equals(totalDebitAmount)) {
			generalJournalVO.setTotalCreditAmount(totalCreditAmount);
			generalJournalVO.setTotalDebitAmount(totalDebitAmount);
		} else {
			throw new ApplicationException("Total Debit Amount and Total Credit Amount Should be Equal");
		}
		generalJournalVO.setParticularsJournalVO(particularsJournalVOs);

	}

	@Override
	public List<GeneralJournalVO> getGeneralJournalByActive() {
		return generalJournalRepo.findGeneralJournalByActive();

	}

	@Override
	public String getGeneralJournalDocId(Long orgId, String finYear, String branch, String branchCode) {
		String sourceScreenCode = "GJ";
		String screenCode = "AC";
		String accountsDocId = accountsRepo.getApproveDocId(orgId, finYear, branchCode, sourceScreenCode, screenCode);
		return accountsDocId;
	}

	@Override
	@Transactional
	public List<Map<String, Object>> getAccountNameFromGroup(Long orgId) {

		Set<Object[]> result = generalJournalRepo.findAccountNameFromGroup(orgId);
		return getAccountNameFromGroup(result);
	}

	private List<Map<String, Object>> getAccountNameFromGroup(Set<Object[]> result) {
		List<Map<String, Object>> details1 = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> part = new HashMap<>();
			part.put("accountName", fs[0] != null ? fs[0].toString() : "");
			details1.add(part);
		}
		return details1;
	}

	// DebitNote

	@Override
	public List<DebitNoteVO> getAllDebitNoteByOrgId(Long orgId) {
		List<DebitNoteVO> debitNoteVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received DebitNote BY OrgId : {}", orgId);
			debitNoteVO = debitNoteRepo.getAllDebitNoteByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received DebitNote For All OrgId.");
			debitNoteVO = debitNoteRepo.findAll();
		}
		return debitNoteVO;
	}

	@Override
	public List<DebitNoteVO> getAllDebitNoteById(Long id) {
		List<DebitNoteVO> debitNoteVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received DebitNote BY Id : {}", id);
			debitNoteVO = debitNoteRepo.getAllDebitNoteById(id);
		} else {
			LOGGER.info("Successfully Received DebitNote For All Id.");
			debitNoteVO = debitNoteRepo.findAll();
		}
		return debitNoteVO;
	}

	@Override
	public DebitNoteVO updateCreateDebitNote(@Valid DebitNoteDTO debitNoteDTO) throws ApplicationException {
		DebitNoteVO debitNoteVO = new DebitNoteVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(debitNoteDTO.getId())) {
			isUpdate = true;
			debitNoteVO = debitNoteRepo.findById(debitNoteDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid DebitNote details"));
			debitNoteVO.setUpdatedBy(debitNoteDTO.getCreatedBy());
		} else {
			debitNoteVO.setUpdatedBy(debitNoteDTO.getCreatedBy());
			debitNoteVO.setCreatedBy(debitNoteDTO.getCreatedBy());
		}

		List<ChargerDebitNoteVO> chargerDebitNoteVOs = new ArrayList<>();
		if (debitNoteDTO.getChargerDebitNoteDTO() != null) {
			for (ChargerDebitNoteDTO chargerDebitNoteDTO : debitNoteDTO.getChargerDebitNoteDTO()) {
				ChargerDebitNoteVO chargerDebitNoteVO;
				if (chargerDebitNoteDTO.getId() != null && ObjectUtils.isNotEmpty(chargerDebitNoteDTO.getId())) {
					chargerDebitNoteVO = chargerDebitNoteRepo.findById(chargerDebitNoteDTO.getId())
							.orElse(new ChargerDebitNoteVO());
				} else {
					chargerDebitNoteVO = new ChargerDebitNoteVO();
				}
				chargerDebitNoteVO.setGChargeCode(chargerDebitNoteDTO.getGChargeCode());
				chargerDebitNoteVO.setGsac(chargerDebitNoteDTO.getGsac());
				chargerDebitNoteVO.setChargeName(chargerDebitNoteDTO.getChargeName());
				chargerDebitNoteVO.setApplyOn(chargerDebitNoteDTO.getApplyOn());
				chargerDebitNoteVO.setTax(chargerDebitNoteDTO.getTax());
				chargerDebitNoteVO.setCurrency(chargerDebitNoteDTO.getCurrency());
				chargerDebitNoteVO.setExRate(chargerDebitNoteDTO.getExRate());
				chargerDebitNoteVO.setRate(chargerDebitNoteDTO.getRate());
				chargerDebitNoteVO.setExempted(chargerDebitNoteDTO.getExempted());
				chargerDebitNoteVO.setFcAmount(chargerDebitNoteDTO.getFcAmount());
				chargerDebitNoteVO.setLcAmount(chargerDebitNoteDTO.getLcAmount());
				chargerDebitNoteVO.setTaxablePercentage(chargerDebitNoteDTO.getTaxablePercentage());
				chargerDebitNoteVO.setTlcAmount(chargerDebitNoteDTO.getTlcAmount());
				chargerDebitNoteVO.setBillAmount(chargerDebitNoteDTO.getBillAmount());
				chargerDebitNoteVO.setGstPercentage(chargerDebitNoteDTO.getGstPercentage());
				chargerDebitNoteVO.setGst(chargerDebitNoteDTO.getGst());
				chargerDebitNoteVO.setDebitNoteVO(debitNoteVO);
				chargerDebitNoteVOs.add(chargerDebitNoteVO);
			}
		}

		List<GstDebitNoteVO> gstDebitNoteVOs = new ArrayList<>();
		if (debitNoteDTO.getGstDebitNoteDTO() != null) {
			for (GstDebitNoteDTO gstDebitNoteDTO : debitNoteDTO.getGstDebitNoteDTO()) {
				GstDebitNoteVO gstDebitNoteVO;
				if (gstDebitNoteDTO.getId() != null & ObjectUtils.isNotEmpty(gstDebitNoteDTO.getId())) {
					gstDebitNoteVO = gstDebitNoteRepo.findById(gstDebitNoteDTO.getId()).orElse(new GstDebitNoteVO());
				} else {
					gstDebitNoteVO = new GstDebitNoteVO();
				}
				gstDebitNoteVO.setChargeAccount(gstDebitNoteDTO.getChargeAccount());
				gstDebitNoteVO.setSubLedgerCode(gstDebitNoteDTO.getSubLedgerCode());
				gstDebitNoteVO.setDbBillAmount(gstDebitNoteDTO.getDbBillAmount());
				gstDebitNoteVO.setCrBillAmount(gstDebitNoteDTO.getCrBillAmount());
				gstDebitNoteVO.setDblcAmt(gstDebitNoteDTO.getDblcAmt());
				gstDebitNoteVO.setCrlcamt(gstDebitNoteDTO.getCrlcamt());
				gstDebitNoteVO.setDebitNoteVO(debitNoteVO);
				gstDebitNoteVOs.add(gstDebitNoteVO);
			}
		}

		List<ParticularsDebitNoteVO> particularsDebitNoteVOs = new ArrayList<>();
		if (debitNoteDTO.getParticularsDebitNoteDTO() != null) {
			for (ParticularsDebitNoteDTO particularsDebitNoteDTO : debitNoteDTO.getParticularsDebitNoteDTO()) {
				ParticularsDebitNoteVO particularsDebitNoteVO;
				if (particularsDebitNoteDTO.getId() != null & ObjectUtils.isEmpty(particularsDebitNoteDTO.getId())) {
					particularsDebitNoteVO = particularsDebitNoteRepo.findById(particularsDebitNoteDTO.getId())
							.orElse(new ParticularsDebitNoteVO());
				} else {
					particularsDebitNoteVO = new ParticularsDebitNoteVO();
				}
				particularsDebitNoteVO.setTds(particularsDebitNoteDTO.getTds());
				particularsDebitNoteVO.setTdsPercent(particularsDebitNoteDTO.getTdsPercent());
				particularsDebitNoteVO.setSection(particularsDebitNoteDTO.getSection());
				particularsDebitNoteVO.setTotalTdsAmount(particularsDebitNoteDTO.getTotalTdsAmount());
				particularsDebitNoteVO.setDebitNoteVO(debitNoteVO);
				particularsDebitNoteVOs.add(particularsDebitNoteVO);
			}
		}

		getDebitNoteVOFromDebitNoteDTO(debitNoteDTO, debitNoteVO);
		debitNoteVO.setChargerDebitNoteVO(chargerDebitNoteVOs);
		debitNoteVO.setParticularsDebitNoteVO(particularsDebitNoteVOs);
		debitNoteVO.setGstDebitNoteVO(gstDebitNoteVOs);
		return debitNoteRepo.save(debitNoteVO);
	}

	private void getDebitNoteVOFromDebitNoteDTO(@Valid DebitNoteDTO debitNoteDTO, DebitNoteVO debitNoteVO) {
		// // Finyr
		// int finyr = taxInvoiceRepo.findFinyr();
		// // DocId
		// String taxInvoice = "AI" + finyr + taxInvoiceRepo.findDocId();
		// taxInvoiceVO.setDocId(taxInvoice);
		// taxInvoiceRepo.nextSeq();
		// // InvoiceNo
		// String invoiceNo = "AI" + finyr + "INV" + taxInvoiceRepo.findInvoiceNo();
		// taxInvoiceVO.setInvoiceNo(invoiceNo);
		// taxInvoiceRepo.nextSeqInvoice();
		debitNoteVO.setDocNo(debitNoteDTO.getDocNo());
		debitNoteVO.setSubType(debitNoteDTO.getSubType());
		debitNoteVO.setProduct(debitNoteDTO.getProduct());
		debitNoteVO.setDocDate(debitNoteDTO.getDocDate());
		debitNoteVO.setPartyType(debitNoteDTO.getPartyType());
		debitNoteVO.setPartyCode(debitNoteDTO.getPartyCode());
		debitNoteVO.setPartyName(debitNoteDTO.getPartyName());
		debitNoteVO.setAddress(debitNoteDTO.getAddress());
		debitNoteVO.setOtherInfo(debitNoteDTO.getOtherInfo());
		debitNoteVO.setStatus(debitNoteDTO.getStatus());
		debitNoteVO.setOriginBill(debitNoteDTO.getOriginBill());
		debitNoteVO.setVchNo(debitNoteDTO.getVchNo());
		debitNoteVO.setVchDate(debitNoteDTO.getVchDate());
		debitNoteVO.setCreditDays(debitNoteDTO.getCreditDays());
		debitNoteVO.setSupplierRefNo(debitNoteDTO.getSupplierRefNo());
		debitNoteVO.setDate(debitNoteDTO.getDate());
		debitNoteVO.setDueDate(debitNoteDTO.getDueDate());
		debitNoteVO.setExRate(debitNoteDTO.getExRate());
		debitNoteVO.setTaxExempt(debitNoteDTO.isTaxExempt());
		debitNoteVO.setCurrency(debitNoteDTO.getCurrency());
		debitNoteVO.setRemarks(debitNoteDTO.getRemarks());
		debitNoteVO.setShipperRefNo(debitNoteDTO.getShipperRefNo());
		debitNoteVO.setGstType(debitNoteDTO.getGstType());
		debitNoteVO.setMode(debitNoteDTO.getMode());
		debitNoteVO.setOrgId(debitNoteDTO.getOrgId());
		debitNoteVO.setActive(debitNoteDTO.isActive());
		debitNoteVO.setBillCurrTotChargeAmount(debitNoteDTO.getBillCurrTotChargeAmount());
		debitNoteVO.setBillCurrTotGrossAmount(debitNoteDTO.getBillCurrTotGrossAmount());
		debitNoteVO.setBillCurrNetAmount(debitNoteDTO.getBillCurrNetAmount());
		debitNoteVO.setAmountInWords(debitNoteDTO.getAmountInWords());
		debitNoteVO.setRoundOff(debitNoteDTO.getRoundOff());
		debitNoteVO.setLctotChargeAmount(debitNoteDTO.getLctotChargeAmount());
		debitNoteVO.setLctotGrossAmount(debitNoteDTO.getLctotGrossAmount());
		debitNoteVO.setLcNetAmount(debitNoteDTO.getLcNetAmount());
	}

	@Override
	public List<DebitNoteVO> getDebitNoteByActive() {
		return debitNoteRepo.findDebitNoteByActive();
	}

	// GstSalesVoucher

	@Override
	public List<GstSalesVoucherVO> getAllGstSalesVoucherByOrgId(Long orgId) {
		List<GstSalesVoucherVO> gstSalesVoucherVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received GstSalesVoucher BY OrgId : {}", orgId);
			gstSalesVoucherVO = gstSalesVoucherRepo.getAllGstSalesVoucherByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received GstSalesVoucher For All OrgId.");
			gstSalesVoucherVO = gstSalesVoucherRepo.findAll();
		}
		return gstSalesVoucherVO;
	}

	@Override
	public List<GstSalesVoucherVO> getAllGstSalesVoucherById(Long id) {
		List<GstSalesVoucherVO> gstSalesVoucherVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received GstSalesVoucher BY Id : {}", id);
			gstSalesVoucherVO = gstSalesVoucherRepo.getAllGstSalesVoucherById(id);
		} else {
			LOGGER.info("Successfully Received GstSalesVoucher For All Id.");
			gstSalesVoucherVO = gstSalesVoucherRepo.findAll();
		}
		return gstSalesVoucherVO;
	}

	@Override
	public GstSalesVoucherVO updateCreateGstSalesVoucher(@Valid GstSalesVoucherDTO gstSalesVoucherDTO)
			throws ApplicationException {
		GstSalesVoucherVO gstSalesVoucherVO = new GstSalesVoucherVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(gstSalesVoucherDTO.getId())) {
			isUpdate = true;
			gstSalesVoucherVO = gstSalesVoucherRepo.findById(gstSalesVoucherDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid GstSalesVoucher details"));
			gstSalesVoucherVO.setUpdatedBy(gstSalesVoucherDTO.getCreatedBy());
		} else {
			gstSalesVoucherVO.setUpdatedBy(gstSalesVoucherDTO.getCreatedBy());
			gstSalesVoucherVO.setCreatedBy(gstSalesVoucherDTO.getCreatedBy());
		}

		List<ParticularsGstVoucherVO> particularsGstVoucherVOs = new ArrayList<>();
		if (gstSalesVoucherDTO.getParticularsGstVoucherDTO() != null) {
			for (ParticularsGstVoucherDTO particularsGstVoucherDTO : gstSalesVoucherDTO.getParticularsGstVoucherDTO()) {
				ParticularsGstVoucherVO particularsGstVoucherVO;
				if (particularsGstVoucherDTO.getId() != null & ObjectUtils.isEmpty(particularsGstVoucherDTO.getId())) {
					particularsGstVoucherVO = particularsGstVoucherRepo.findById(particularsGstVoucherDTO.getId())
							.orElse(new ParticularsGstVoucherVO());
				} else {
					particularsGstVoucherVO = new ParticularsGstVoucherVO();
				}
				particularsGstVoucherVO.setAccountName(particularsGstVoucherDTO.getAccountName());
				particularsGstVoucherVO.setSubLedgerCode(particularsGstVoucherDTO.getSubLedgerCode());
				particularsGstVoucherVO.setSubLedgerName(particularsGstVoucherDTO.getSubLedgerName());
				particularsGstVoucherVO.setDebit(particularsGstVoucherDTO.getDebit());
				particularsGstVoucherVO.setBaseDbAmount(particularsGstVoucherDTO.getBaseDbAmount());
				particularsGstVoucherVO.setCredit(particularsGstVoucherDTO.getCredit());
				particularsGstVoucherVO.setBaseCrAmount(particularsGstVoucherDTO.getBaseCrAmount());
				particularsGstVoucherVO.setNarration(particularsGstVoucherDTO.getNarration());
				particularsGstVoucherVO.setGstSalesVoucherVO(gstSalesVoucherVO);
				particularsGstVoucherVOs.add(particularsGstVoucherVO);
			}
		}

		getGstSalesVoucherVOFromGstSalesVoucherDTO(gstSalesVoucherDTO, gstSalesVoucherVO);
		gstSalesVoucherVO.setParticularsGstVoucherVO(particularsGstVoucherVOs);
		return gstSalesVoucherRepo.save(gstSalesVoucherVO);
	}

	private void getGstSalesVoucherVOFromGstSalesVoucherDTO(@Valid GstSalesVoucherDTO gstSalesVoucherDTO,
			GstSalesVoucherVO gstSalesVoucherVO) {
		// Finyr
		int finyr = gstSalesVoucherRepo.findFinyr();
		// DocId
		String salesVoucher = "SV" + finyr + gstSalesVoucherRepo.findDocId();
		gstSalesVoucherVO.setDocId(salesVoucher);
		gstSalesVoucherRepo.nextSeq();
		// InvoiceNo
		gstSalesVoucherVO.setDocId(gstSalesVoucherDTO.getDocId());
		gstSalesVoucherVO.setReferenceNo(gstSalesVoucherDTO.getReferenceNo());
		gstSalesVoucherVO.setCurrency(gstSalesVoucherDTO.getCurrency());
		gstSalesVoucherVO.setDocDate(gstSalesVoucherDTO.getDocDate());
		gstSalesVoucherVO.setReferenceDate(gstSalesVoucherDTO.getReferenceDate());
		gstSalesVoucherVO.setExRate(gstSalesVoucherDTO.getExRate());
		gstSalesVoucherVO.setRemarks(gstSalesVoucherDTO.getRemarks());
		gstSalesVoucherVO.setTotalDebitAmount(gstSalesVoucherDTO.getTotalDebitAmount());
		gstSalesVoucherVO.setTotalCreditAmount(gstSalesVoucherDTO.getTotalCreditAmount());
		gstSalesVoucherVO.setStTaxAmount(gstSalesVoucherDTO.getStTaxAmount());
		gstSalesVoucherVO.setBasAmount(gstSalesVoucherDTO.getBasAmount());
		gstSalesVoucherVO.setBssAmount(gstSalesVoucherDTO.getBssAmount());
		gstSalesVoucherVO.setChaAmount(gstSalesVoucherDTO.getChaAmount());
	}

	@Override
	public List<GstSalesVoucherVO> getGstSalesVoucherByActive() {
		return gstSalesVoucherRepo.findGstSalesVoucherByActive();
	}

	// PaymentVoucher

	@Override
	public List<PaymentVoucherVO> getAllPaymentVoucherByOrgId(Long orgId) {
		List<PaymentVoucherVO> paymentVoucherVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received PaymentVoucher BY OrgId : {}", orgId);
			paymentVoucherVO = paymentVoucherRepo.getAllPaymentVoucherByOrgId(orgId);
		}
		return paymentVoucherVO;
	}

	@Override
	public List<PaymentVoucherVO> getPaymentVoucherById(Long id) {
		List<PaymentVoucherVO> paymentVoucherVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received PaymentVoucher BY Id : {}", id);
			paymentVoucherVO = paymentVoucherRepo.getPaymentVoucherById(id);
		}
		return paymentVoucherVO;
	}

	@Override
	public Map<String, Object> updateCreatePaymentVoucher(@Valid PaymentVoucherDTO paymentVoucherDTO)
			throws ApplicationException {
		PaymentVoucherVO paymentVoucherVO;
		String message = null;
		String screenCode = "PV";

		if (ObjectUtils.isEmpty(paymentVoucherDTO.getId())) {
			paymentVoucherVO = new PaymentVoucherVO();

			// GETDOCID API
			String docId = paymentVoucherRepo.getpaymentVoucherDocId(paymentVoucherDTO.getOrgId(),
					paymentVoucherDTO.getFinyear(), paymentVoucherDTO.getBranchCode(), screenCode);
			paymentVoucherVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(paymentVoucherDTO.getOrgId(),
							paymentVoucherDTO.getFinyear(), paymentVoucherDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			paymentVoucherVO.setCreatedBy(paymentVoucherDTO.getCreatedBy());

			paymentVoucherVO.setUpdatedBy(paymentVoucherDTO.getCreatedBy());

			paymentVoucherVO.setUpdatedBy(paymentVoucherDTO.getCreatedBy());
//			paymentVoucherVO.setCreatedBy(paymentVoucherDTO.getCreatedBy());

		} else {
			paymentVoucherVO = paymentVoucherRepo.findById(paymentVoucherDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid PaymentVoucher details"));
			paymentVoucherVO.setUpdatedBy(paymentVoucherDTO.getCreatedBy());

			paymentVoucherVO.setUpdatedBy(paymentVoucherDTO.getCreatedBy());
		}

		getPaymentVoucherVOFromPaymentVoucherDTO(paymentVoucherDTO, paymentVoucherVO);
		paymentVoucherRepo.save(paymentVoucherVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("paymentVoucherVO", paymentVoucherVO);
		return response;
	}

	private PaymentVoucherVO getPaymentVoucherVOFromPaymentVoucherDTO(@Valid PaymentVoucherDTO paymentVoucherDTO,
			PaymentVoucherVO paymentVoucherVO) {
		paymentVoucherVO.setVehicleSubType(paymentVoucherDTO.getVehicleSubType());
		paymentVoucherVO.setReferenceNo(paymentVoucherDTO.getReferenceNo());
		paymentVoucherVO.setCurrency(paymentVoucherDTO.getCurrency());
		paymentVoucherVO.setReferenceDate(paymentVoucherDTO.getReferenceDate());

		paymentVoucherVO.setExRate(paymentVoucherDTO.getExRate());
		paymentVoucherVO.setChequeNo(paymentVoucherDTO.getChequeNo());
		paymentVoucherVO.setChequeDate(paymentVoucherDTO.getChequeDate());
		paymentVoucherVO.setChequeBank(paymentVoucherDTO.getChequeBank());
		paymentVoucherVO.setOrgId(paymentVoucherDTO.getOrgId());
		paymentVoucherVO.setActive(paymentVoucherDTO.isActive());
		paymentVoucherVO.setCancel(paymentVoucherDTO.isCancel());
		paymentVoucherVO.setBranch(paymentVoucherDTO.getBranch());
		paymentVoucherVO.setBranchCode(paymentVoucherDTO.getBranchCode());
		paymentVoucherVO.setCancelRemarks(paymentVoucherDTO.getCancelRemarks());
		paymentVoucherVO.setIpNo(paymentVoucherDTO.getIpNo());
		paymentVoucherVO.setLatitude(paymentVoucherDTO.getLatitude());
		paymentVoucherVO.setRemarks(paymentVoucherDTO.getRemarks().toUpperCase());
		paymentVoucherVO.setTotalDebitAmount(paymentVoucherDTO.getTotalDebitAmount());
		paymentVoucherVO.setTotalCreditAmount(paymentVoucherDTO.getTotalCreditAmount());
		paymentVoucherVO.setRemarks(paymentVoucherDTO.getRemarks());

		if (paymentVoucherDTO.getId() != null) {
			List<ParticularsPaymentVoucherVO> particularsPaymentVoucherVOs = ParticularsPaymentVoucherRepo
					.findByPaymentVoucherVO(paymentVoucherVO);
			ParticularsPaymentVoucherRepo.deleteAll(particularsPaymentVoucherVOs);
		}

		List<ParticularsPaymentVoucherVO> particularsPaymentVoucherVOs = new ArrayList<>();
		for (ParticularsPaymentVoucherDTO particularsPaymentVoucherDTO : paymentVoucherDTO
				.getParticularsPaymentVoucherDTO()) {
			ParticularsPaymentVoucherVO particularsPaymentVoucherVO = new ParticularsPaymentVoucherVO();

			particularsPaymentVoucherVO.setAccountName(particularsPaymentVoucherDTO.getAccountName());
			particularsPaymentVoucherVO.setSubLedgerCode(particularsPaymentVoucherDTO.getSubLedgerCode());
			particularsPaymentVoucherVO.setSubLedgerName(particularsPaymentVoucherDTO.getSubLedgerName());
			particularsPaymentVoucherVO.setDebit(particularsPaymentVoucherDTO.getDebit());
			particularsPaymentVoucherVO.setCredit(particularsPaymentVoucherDTO.getCredit());
			particularsPaymentVoucherVO.setNarration(particularsPaymentVoucherDTO.getNarration());
			particularsPaymentVoucherVO.setPaymentVoucherVO(paymentVoucherVO);

			particularsPaymentVoucherVOs.add(particularsPaymentVoucherVO);
		}
		paymentVoucherVO.setParticularsPaymentVoucherVO(particularsPaymentVoucherVOs);

		return paymentVoucherVO;

	}

	@Override
	public List<PaymentVoucherVO> getPaymentVoucherByActive() {
		return paymentVoucherRepo.findPaymentVoucherByActive();
	}

	@Override
	public List<Map<String, Object>> getCurrencyAndExrates(Long orgId) {
		Set<Object[]> currency = paymentVoucherRepo.getCurrencyAndExrateDetails(orgId);
		return getCurrency(currency);
	}

	private List<Map<String, Object>> getCurrency(Set<Object[]> currency) {
		List<Map<String, Object>> List1 = new ArrayList<>();
		for (Object[] ch : currency) {
			Map<String, Object> map = new HashMap<>();
			map.put("currency", ch[0] != null ? ch[0].toString() : ""); // Empty string if null
			map.put("currencyDescription", ch[1] != null ? ch[1].toString() : "");
			map.put("buyingExRate", ch[2] != null ? ch[2].toString() : "");
			map.put("sellingExRate", ch[3] != null ? ch[3].toString() : "");
			List1.add(map);
		}
		return List1;
	}

	// ReceiptReversal

	@Override
	public List<ReceiptReversalVO> getAllReceiptReversalByOrgId(Long orgId) {
		List<ReceiptReversalVO> receiptReversalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  ReceiptReversal BY OrgId : {}", orgId);
			receiptReversalVO = receiptReversalRepo.getAllReceiptReversalByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received  ReceiptReversal For All OrgId.");
			receiptReversalVO = receiptReversalRepo.findAll();
		}
		return receiptReversalVO;
	}

	@Override
	public List<ReceiptReversalVO> getAllReceiptReversalById(Long id) {
		List<ReceiptReversalVO> receiptReversalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ReceiptReversal BY Id : {}", id);
			receiptReversalVO = receiptReversalRepo.getAllReceiptReversalById(id);
		} else {
			LOGGER.info("Successfully Received ReceiptReversal For All Id.");
			receiptReversalVO = receiptReversalRepo.findAll();
		}
		return receiptReversalVO;
	}

	@Override
	public ReceiptReversalVO updateCreateReceiptReversal(@Valid ReceiptReversalDTO receiptReversalDTO)
			throws ApplicationException {
		ReceiptReversalVO receiptReversalVO = new ReceiptReversalVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(receiptReversalDTO.getId())) {
			isUpdate = true;
			receiptReversalVO = receiptReversalRepo.findById(receiptReversalDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid ReceiptReversal details"));
			receiptReversalVO.setUpdatedBy(receiptReversalDTO.getCreatedBy());
		} else {
			receiptReversalVO.setUpdatedBy(receiptReversalDTO.getCreatedBy());
			receiptReversalVO.setCreatedBy(receiptReversalDTO.getCreatedBy());
		}

		List<ReceiptInvoiceVO> receiptInvoiceVOs = new ArrayList<>();
		if (receiptReversalDTO.getReceiptInvoiceDTO() != null) {
			for (ReceiptInvoiceDTO receiptInvoiceDTO : receiptReversalDTO.getReceiptInvoiceDTO()) {
				ReceiptInvoiceVO receiptInvoiceVO;
				if (receiptInvoiceDTO.getId() != null && ObjectUtils.isNotEmpty(receiptInvoiceDTO.getId())) {
					receiptInvoiceVO = receiptInvoiceRepo.findById(receiptInvoiceDTO.getId())
							.orElse(new ReceiptInvoiceVO());
				} else {
					receiptInvoiceVO = new ReceiptInvoiceVO();
				}
				receiptInvoiceVO.setInvoiceNo(receiptInvoiceDTO.getInvoiceNo());
				receiptInvoiceVO.setInvoiceDt(receiptInvoiceDTO.getInvoiceDt());
				receiptInvoiceVO.setRefNo(receiptInvoiceDTO.getRefNo());
				receiptInvoiceVO.setRefDate(receiptInvoiceDTO.getRefDate());
				receiptInvoiceVO.setMasterRef(receiptInvoiceDTO.getMasterRef());
				receiptInvoiceVO.setHouseRef(receiptInvoiceDTO.getHouseRef());
				receiptInvoiceVO.setCurr(receiptInvoiceDTO.getCurr());
				receiptInvoiceVO.setExRate(receiptInvoiceDTO.getExRate());
				receiptInvoiceVO.setAmount(receiptInvoiceDTO.getAmount());
				receiptInvoiceVO.setChargableAmount(receiptInvoiceDTO.getChargableAmount());
				receiptInvoiceVO.setOutStanding(receiptInvoiceDTO.getOutStanding());
				receiptInvoiceVO.setSettled(receiptInvoiceDTO.getSettled());
				receiptInvoiceVO.setRecExRate(receiptInvoiceDTO.getRecExRate());
				receiptInvoiceVO.setTxnSettled(receiptInvoiceDTO.getTxnSettled());
				receiptInvoiceVO.setGainOrLoss(receiptInvoiceDTO.getGainOrLoss());
				receiptInvoiceVO.setReceiptReversalVO(receiptReversalVO);
				receiptInvoiceVOs.add(receiptInvoiceVO);
			}
		}

		List<ReceiptOtherAccountVO> receiptOtherAccountVOs = new ArrayList<>();
		if (receiptReversalDTO.getReceiptOtherAccountDTO() != null) {
			for (ReceiptOtherAccountDTO receiptOtherAccountDTO : receiptReversalDTO.getReceiptOtherAccountDTO()) {
				ReceiptOtherAccountVO receiptOtherAccountVO;
				if (receiptOtherAccountDTO.getId() != null & ObjectUtils.isNotEmpty(receiptOtherAccountDTO.getId())) {
					receiptOtherAccountVO = receiptOtherAccountRepo.findById(receiptOtherAccountDTO.getId())
							.orElse(new ReceiptOtherAccountVO());
				} else {
					receiptOtherAccountVO = new ReceiptOtherAccountVO();
				}
				receiptOtherAccountVO.setAccountName(receiptOtherAccountDTO.getAccountName());
				receiptOtherAccountVO.setSubLedgerName(receiptOtherAccountDTO.getSubLedgerName());
				receiptOtherAccountVO.setCredit(receiptOtherAccountDTO.getCredit());
				receiptOtherAccountVO.setRemarks(receiptOtherAccountDTO.getRemarks());
				receiptOtherAccountVO.setReceiptReversalVO(receiptReversalVO);
				receiptOtherAccountVOs.add(receiptOtherAccountVO);
			}
		}

		getReceiptReversalVOFromReceiptReversalDTO(receiptReversalDTO, receiptReversalVO);
		receiptReversalVO.setReceiptOtherAccountVO(receiptOtherAccountVOs);
		receiptReversalVO.setReceiptInvoiceVO(receiptInvoiceVOs);
		return receiptReversalRepo.save(receiptReversalVO);
	}

	private void getReceiptReversalVOFromReceiptReversalDTO(@Valid ReceiptReversalDTO receiptReversalDTO,
			ReceiptReversalVO receiptReversalVO) {
		// // Finyr
		// int finyr = taxInvoiceRepo.findFinyr();
		// // DocId
		// String taxInvoice = "AI" + finyr + taxInvoiceRepo.findDocId();
		// taxInvoiceVO.setDocId(taxInvoice);
		// taxInvoiceRepo.nextSeq();bbbbb
		// // InvoiceNo
		// String invoiceNo = "AI" + finyr + "INV" + taxInvoiceRepo.findInvoiceNo();
		// taxInvoiceVO.setInvoiceNo(invoiceNo);
		// taxInvoiceRepo.nextSeqInvoice();
		receiptReversalVO.setDocId(receiptReversalDTO.getDocId());
		receiptReversalVO.setDocDate(receiptReversalDTO.getDocDate());
		receiptReversalVO.setOriginBill(receiptReversalDTO.getOriginBill());
		receiptReversalVO.setOriginDate(receiptReversalDTO.getOriginDate());
		receiptReversalVO.setType(receiptReversalDTO.getType());
		receiptReversalVO.setReceiptType(receiptReversalDTO.getReceiptType());
		receiptReversalVO.setCustomerName(receiptReversalDTO.getCustomerName());
		receiptReversalVO.setCustomerCode(receiptReversalDTO.getCustomerCode());
		receiptReversalVO.setBankCash(receiptReversalDTO.getBankCash());
		receiptReversalVO.setReceiptAmount(receiptReversalDTO.getReceiptAmount());
		receiptReversalVO.setCurrency(receiptReversalDTO.getCurrency());
		receiptReversalVO.setBankChargesAc(receiptReversalDTO.getBankChargesAc());
		receiptReversalVO.setBankCharges(receiptReversalDTO.getBankCharges());
		receiptReversalVO.setBcInCurrency(receiptReversalDTO.getBcInCurrency());
		receiptReversalVO.setTdsAmount(receiptReversalDTO.getTdsAmount());
		receiptReversalVO.setTdsInCurrency(receiptReversalDTO.getTdsInCurrency());
		receiptReversalVO.setChequeBank(receiptReversalDTO.getChequeBank());
		receiptReversalVO.setReceiptType2(receiptReversalDTO.getReceiptType2());
		receiptReversalVO.setChqNo(receiptReversalDTO.getChqNo());
		receiptReversalVO.setChqDt(receiptReversalDTO.getChqDt());
		receiptReversalVO.setReceivedFrom(receiptReversalDTO.getReceivedFrom());
		receiptReversalVO.setOffSetStatus(receiptReversalDTO.getOffSetStatus());
		receiptReversalVO.setOrgId(receiptReversalDTO.getOrgId());
		receiptReversalVO.setActive(receiptReversalDTO.isActive());
		receiptReversalVO.setFoxenGainOrLoss(receiptReversalDTO.getFoxenGainOrLoss());
		receiptReversalVO.setRoundOffAmount(receiptReversalDTO.getRoundOffAmount());
		receiptReversalVO.setTotalSettled(receiptReversalDTO.getTotalSettled());
		receiptReversalVO.setOtherAccNetAmt(receiptReversalDTO.getOtherAccNetAmt());
		receiptReversalVO.setOnAccount(receiptReversalDTO.getOnAccount());
		receiptReversalVO.setNarration(receiptReversalDTO.getNarration());
	}

	@Override
	public List<ReceiptReversalVO> getReceiptReversalByActive() {
		return receiptReversalRepo.findReceiptReversalByActive();
	}

	// PaymentReversal

	@Override
	public List<PaymentReversalVO> getAllPaymentReversalByOrgId(Long orgId) {
		List<PaymentReversalVO> paymentReversalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  PaymentReversal BY OrgId : {}", orgId);
			paymentReversalVO = paymentReversalRepo.getAllPaymentReversalByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received  PaymentReversal For All OrgId.");
			paymentReversalVO = paymentReversalRepo.findAll();
		}
		return paymentReversalVO;
	}

	@Override
	public List<PaymentReversalVO> getAllPaymentReversalById(Long id) {
		List<PaymentReversalVO> paymentReversalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received PaymentReversal BY Id : {}", id);
			paymentReversalVO = paymentReversalRepo.getAllPaymentReversalById(id);
		} else {
			LOGGER.info("Successfully Received PaymentReversal For All Id.");
			paymentReversalVO = paymentReversalRepo.findAll();
		}
		return paymentReversalVO;
	}

	@Override
	public PaymentReversalVO updateCreatePaymentReversal(@Valid PaymentReversalDTO paymentReversalDTO)
			throws ApplicationException {
		PaymentReversalVO paymentReversalVO = new PaymentReversalVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(paymentReversalDTO.getId())) {
			isUpdate = true;
			paymentReversalVO = paymentReversalRepo.findById(paymentReversalDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid PaymentReversal details"));
			paymentReversalVO.setUpdatedBy(paymentReversalDTO.getCreatedBy());
		} else {
			paymentReversalVO.setUpdatedBy(paymentReversalDTO.getCreatedBy());
			paymentReversalVO.setCreatedBy(paymentReversalDTO.getCreatedBy());
		}

		List<PaymentInvoiceVO> paymentInvoiceVOs = new ArrayList<>();
		if (paymentReversalDTO.getPaymentInvoiceDTO() != null) {
			for (PaymentInvoiceDTO paymentInvoiceDTO : paymentReversalDTO.getPaymentInvoiceDTO()) {
				PaymentInvoiceVO paymentInvoiceVO;
				if (paymentInvoiceDTO.getId() != null && ObjectUtils.isNotEmpty(paymentInvoiceDTO.getId())) {
					paymentInvoiceVO = paymentInvoiceRepo.findById(paymentInvoiceDTO.getId())
							.orElse(new PaymentInvoiceVO());
				} else {
					paymentInvoiceVO = new PaymentInvoiceVO();
				}
				paymentInvoiceVO.setInvoiceNumber(paymentInvoiceDTO.getInvoiceNumber());
				paymentInvoiceVO.setInvoiceDate(paymentInvoiceDTO.getInvoiceDate());
				paymentInvoiceVO.setRefNo(paymentInvoiceDTO.getRefNo());
				paymentInvoiceVO.setRefDate(paymentInvoiceDTO.getRefDate());
				paymentInvoiceVO.setSuppRefNo(paymentInvoiceDTO.getSuppRefNo());
				paymentInvoiceVO.setSuppRefDate(paymentInvoiceDTO.getSuppRefDate());
				paymentInvoiceVO.setCurr(paymentInvoiceDTO.getCurr());
				paymentInvoiceVO.setExRate(paymentInvoiceDTO.getExRate());
				paymentInvoiceVO.setAmount(paymentInvoiceDTO.getAmount());
				paymentInvoiceVO.setOutStanding(paymentInvoiceDTO.getOutStanding());
				paymentInvoiceVO.setSettled(paymentInvoiceDTO.getSettled());
				paymentInvoiceVO.setPayExRate(paymentInvoiceDTO.getPayExRate());
				paymentInvoiceVO.setTxnSettled(paymentInvoiceDTO.getTxnSettled());
				paymentInvoiceVO.setGainOrLoss(paymentInvoiceDTO.getGainOrLoss());
				paymentInvoiceVO.setRemarks(paymentInvoiceDTO.getRemarks());
				paymentInvoiceVO.setPaymentReversalVO(paymentReversalVO);
				paymentInvoiceVOs.add(paymentInvoiceVO);
			}
		}

		List<PaymentOtherAccountVO> paymentOtherAccountVOs = new ArrayList<>();
		if (paymentReversalDTO.getPaymentOtherAccountDTO() != null) {
			for (PaymentOtherAccountDTO paymentOtherAccountDTO : paymentReversalDTO.getPaymentOtherAccountDTO()) {
				PaymentOtherAccountVO paymentOtherAccountVO;
				if (paymentOtherAccountDTO.getId() != null & ObjectUtils.isNotEmpty(paymentOtherAccountDTO.getId())) {
					paymentOtherAccountVO = paymentOtherAccountRepo.findById(paymentOtherAccountDTO.getId())
							.orElse(new PaymentOtherAccountVO());
				} else {
					paymentOtherAccountVO = new PaymentOtherAccountVO();
				}
				paymentOtherAccountVO.setAccountName(paymentOtherAccountDTO.getAccountName());
				paymentOtherAccountVO.setSubLedgerName(paymentOtherAccountDTO.getSubLedgerName());
				paymentOtherAccountVO.setDebitAmount(paymentOtherAccountDTO.getDebitAmount());
				paymentOtherAccountVO.setRemarks(paymentOtherAccountDTO.getRemarks());
				paymentOtherAccountVO.setPaymentReversalVO(paymentReversalVO);
				paymentOtherAccountVOs.add(paymentOtherAccountVO);
			}
		}

		getPaymentReversalVOFromPaymentReversalDTO(paymentReversalDTO, paymentReversalVO);
		paymentReversalVO.setPaymentOtherAccountVO(paymentOtherAccountVOs);
		paymentReversalVO.setPaymentInvoiceVO(paymentInvoiceVOs);
		return paymentReversalRepo.save(paymentReversalVO);
	}

	private void getPaymentReversalVOFromPaymentReversalDTO(@Valid PaymentReversalDTO paymentReversalDTO,
			PaymentReversalVO paymentReversalVO) {
		// // Finyr
		// int finyr = taxInvoiceRepo.findFinyr();
		// // DocId
		// String taxInvoice = "AI" + finyr + taxInvoiceRepo.findDocId();
		// taxInvoiceVO.setDocId(taxInvoice);
		// taxInvoiceRepo.nextSeq();
		// // InvoiceNo
		// String invoiceNo = "AI" + finyr + "INV" + taxInvoiceRepo.findInvoiceNo();
		// taxInvoiceVO.setInvoiceNo(invoiceNo);
		// taxInvoiceRepo.nextSeqInvoice();
		paymentReversalVO.setDocId(paymentReversalDTO.getDocId());
		paymentReversalVO.setDocDate(paymentReversalDTO.getDocDate());
		paymentReversalVO.setOriginBill(paymentReversalDTO.getOriginBill());
		paymentReversalVO.setOrigin(paymentReversalDTO.getOrigin());
		paymentReversalVO.setType(paymentReversalDTO.getType());
		paymentReversalVO.setPartyCode(paymentReversalDTO.getPartyCode());
		paymentReversalVO.setPartyName(paymentReversalDTO.getPartyName());
		paymentReversalVO.setGstState(paymentReversalDTO.getGstState());
		paymentReversalVO.setGstIn(paymentReversalDTO.getGstIn());
		paymentReversalVO.setBankCash(paymentReversalDTO.getBankCash());
		paymentReversalVO.setPaymentAmount(paymentReversalDTO.getPaymentAmount());
		paymentReversalVO.setTdsAc(paymentReversalDTO.getTdsAc());
		paymentReversalVO.setSTaxAmount(paymentReversalDTO.getSTaxAmount());
		paymentReversalVO.setCurrency(paymentReversalDTO.getCurrency());
		paymentReversalVO.setBankChargesAc(paymentReversalDTO.getBankChargesAc());
		paymentReversalVO.setBankCharges(paymentReversalDTO.getBankCharges());
		paymentReversalVO.setBcInCurrency(paymentReversalDTO.getBcInCurrency());
		paymentReversalVO.setTdsAmount(paymentReversalDTO.getTdsAmount());
		paymentReversalVO.setPaymentTime(paymentReversalDTO.getPaymentTime());
		paymentReversalVO.setTaInCurrency(paymentReversalDTO.getTaInCurrency());
		paymentReversalVO.setChequeBank(paymentReversalDTO.getChequeBank());
		paymentReversalVO.setPayTo(paymentReversalDTO.getPayTo());
		paymentReversalVO.setChqDt(paymentReversalDTO.getChqDt());
		paymentReversalVO.setOffSetStatus(paymentReversalDTO.getOffSetStatus());
		paymentReversalVO.setOrgId(paymentReversalDTO.getOrgId());
		paymentReversalVO.setActive(paymentReversalDTO.isActive());
		paymentReversalVO.setFoxenGainOrLoss(paymentReversalDTO.getFoxenGainOrLoss());
		paymentReversalVO.setRoundOffAmount(paymentReversalDTO.getRoundOffAmount());
		paymentReversalVO.setTotalSettled(paymentReversalDTO.getTotalSettled());
		paymentReversalVO.setOtherAccNetAmt(paymentReversalDTO.getOtherAccNetAmt());
		paymentReversalVO.setOnAccount(paymentReversalDTO.getOnAccount());
		paymentReversalVO.setNarration(paymentReversalDTO.getNarration());
	}

	@Override
	public List<PaymentReversalVO> getPaymentReversalByActive() {
		return paymentReversalRepo.findPaymentReversalByActive();
	}

	// ArApAdjustmentOffSet

	@Override
	public List<ArApAdjustmentOffSetVO> getAllArApAdjustmentOffSetByOrgId(Long orgId) {
		List<ArApAdjustmentOffSetVO> arApAdjustmentOffSetVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received ArApAdjustmentOffSet BY OrgId : {}", orgId);
			arApAdjustmentOffSetVO = arApAdjustmentOffSetRepo.getAllArApAdjustmentOffSetByOrgId(orgId);
		}
		return arApAdjustmentOffSetVO;
	}

	@Override
	public List<ArApAdjustmentOffSetVO> getAllArApAdjustmentOffSetById(Long id) {
		List<ArApAdjustmentOffSetVO> arApAdjustmentOffSetVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ArApAdjustmentOffSet BY Id : {}", id);
			arApAdjustmentOffSetVO = arApAdjustmentOffSetRepo.getAllArApAdjustmentOffSetById(id);
		}
		return arApAdjustmentOffSetVO;
	}

	@Override
	public Map<String, Object> updateCreateArApAdjustmentOffSet(@Valid ArApAdjustmentOffSetDTO arApAdjustmentOffSetDTO)
			throws ApplicationException {
		String screenCode = "OFS";
		ArApAdjustmentOffSetVO arApAdjustmentOffSetVO = new ArApAdjustmentOffSetVO();
		String message;
		if (ObjectUtils.isNotEmpty(arApAdjustmentOffSetDTO.getId())) {
			arApAdjustmentOffSetVO = arApAdjustmentOffSetRepo.findById(arApAdjustmentOffSetDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid ArApAdjustmentOffSet details"));

			arApAdjustmentOffSetVO.setUpdatedBy(arApAdjustmentOffSetDTO.getCreatedBy());
			getArApAdjustmentOffSetVOFromArApAdjustmentOffSetDTO(arApAdjustmentOffSetDTO, arApAdjustmentOffSetVO);
			message = "ArApAdjustmentOffSet Updated Successfully";
		} else {
			// GETDOCID API
			String docId = arApAdjustmentOffSetRepo.getArApAdjustmentOffSetDocId(arApAdjustmentOffSetDTO.getOrgId(),
					arApAdjustmentOffSetDTO.getFinYear(), arApAdjustmentOffSetDTO.getBranchCode(), screenCode);
			arApAdjustmentOffSetVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(arApAdjustmentOffSetDTO.getOrgId(),
							arApAdjustmentOffSetDTO.getFinYear(), arApAdjustmentOffSetDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			arApAdjustmentOffSetVO.setCreatedBy(arApAdjustmentOffSetDTO.getCreatedBy());
			arApAdjustmentOffSetVO.setUpdatedBy(arApAdjustmentOffSetDTO.getCreatedBy());
			getArApAdjustmentOffSetVOFromArApAdjustmentOffSetDTO(arApAdjustmentOffSetDTO, arApAdjustmentOffSetVO);
			message = "ArApAdjustmentOffSet Created Successfully";
		}

		arApAdjustmentOffSetRepo.save(arApAdjustmentOffSetVO);
		Map<String, Object> response = new HashMap<>();
		response.put("arApAdjustmentOffSetVO", arApAdjustmentOffSetVO);
		response.put("message", message);
		return response;
	}

	private void getArApAdjustmentOffSetVOFromArApAdjustmentOffSetDTO(
			@Valid ArApAdjustmentOffSetDTO arApAdjustmentOffSetDTO, ArApAdjustmentOffSetVO arApAdjustmentOffSetVO) {
		arApAdjustmentOffSetVO.setSubLedgerType(arApAdjustmentOffSetDTO.getSubLedgerType());
		arApAdjustmentOffSetVO.setSubLedgerName(arApAdjustmentOffSetDTO.getSubLedgerName());
		arApAdjustmentOffSetVO.setSubLedgerCode(arApAdjustmentOffSetDTO.getSubLedgerCode());
		arApAdjustmentOffSetVO.setReceiptPaymentDocId(arApAdjustmentOffSetDTO.getReceiptPaymentDocId());
		arApAdjustmentOffSetVO.setCurrency(arApAdjustmentOffSetDTO.getCurrency());
		arApAdjustmentOffSetVO.setExRate(arApAdjustmentOffSetDTO.getExRate());
		arApAdjustmentOffSetVO.setAmount(arApAdjustmentOffSetDTO.getAmount());
		arApAdjustmentOffSetVO.setSupplierRefNo(arApAdjustmentOffSetDTO.getSupplierRefNo());
		arApAdjustmentOffSetVO.setForexGainOrLoss(arApAdjustmentOffSetDTO.getForexGainOrLoss());
		arApAdjustmentOffSetVO.setTotalSettled(arApAdjustmentOffSetDTO.getTotalSettled());
		arApAdjustmentOffSetVO.setRoundOffAmount(arApAdjustmentOffSetDTO.getRoundOffAmount());
		arApAdjustmentOffSetVO.setOnAccount(arApAdjustmentOffSetDTO.getOnAccount());
		arApAdjustmentOffSetVO.setOrgId(arApAdjustmentOffSetDTO.getOrgId());
		arApAdjustmentOffSetVO.setBranch(arApAdjustmentOffSetDTO.getBranch());
		arApAdjustmentOffSetVO.setBranchCode(arApAdjustmentOffSetDTO.getBranchCode());
		arApAdjustmentOffSetVO.setFinYear(arApAdjustmentOffSetDTO.getFinYear());
		arApAdjustmentOffSetVO.setActive(arApAdjustmentOffSetDTO.isActive());
		arApAdjustmentOffSetVO.setReceiptPaymentDocDate(arApAdjustmentOffSetDTO.getReceiptPaymentDocDate());
		arApAdjustmentOffSetVO.setNarration(arApAdjustmentOffSetDTO.getNarration());

		if (ObjectUtils.isNotEmpty(arApAdjustmentOffSetVO.getId())) {
			List<ArApOffSetInvoiceDetailsVO> arApOffSetInvoiceDetailsVO1 = arApOffSetInvoiceDetailsRepo
					.findByArApAdjustmentOffSetVO(arApAdjustmentOffSetVO);

			arApOffSetInvoiceDetailsRepo.deleteAll(arApOffSetInvoiceDetailsVO1);
		}

		List<ArApOffSetInvoiceDetailsVO> arApOffSetInvoiceDetailsVOs = new ArrayList<>();
		for (ArApOffSetInvoiceDetailsDTO arApOffSetInvoiceDetailsDTO : arApAdjustmentOffSetDTO
				.getArApOffSetInvoiceDetailsDTO()) {
			ArApOffSetInvoiceDetailsVO arApOffSetInvoiceDetailsVO = new ArApOffSetInvoiceDetailsVO();
			arApOffSetInvoiceDetailsVO.setInvoiceNo(arApOffSetInvoiceDetailsDTO.getInvoiceNo());
			arApOffSetInvoiceDetailsVO.setInvoiceDate(arApOffSetInvoiceDetailsDTO.getInvoiceDate());
			arApOffSetInvoiceDetailsVO.setRefNo(arApOffSetInvoiceDetailsDTO.getRefNo());
			arApOffSetInvoiceDetailsVO.setRefDate(arApOffSetInvoiceDetailsDTO.getRefDate());
			arApOffSetInvoiceDetailsVO.setCurr(arApOffSetInvoiceDetailsDTO.getCurr());
			arApOffSetInvoiceDetailsVO.setExRate(arApOffSetInvoiceDetailsDTO.getExRate());
			arApOffSetInvoiceDetailsVO.setSetExRate(arApOffSetInvoiceDetailsDTO.getSetExRate());
			arApOffSetInvoiceDetailsVO.setInvAmount(arApOffSetInvoiceDetailsDTO.getInvAmount());
			arApOffSetInvoiceDetailsVO.setOutStanding(arApOffSetInvoiceDetailsDTO.getOutStanding());
			arApOffSetInvoiceDetailsVO.setSettled(arApOffSetInvoiceDetailsDTO.getSettled());
			arApOffSetInvoiceDetailsVO.setTnxSettled(arApOffSetInvoiceDetailsDTO.getTnxSettled());
			arApOffSetInvoiceDetailsVO.setGainOrLoss(arApOffSetInvoiceDetailsDTO.getGainOrLoss());
			arApOffSetInvoiceDetailsVO.setRemarks(arApOffSetInvoiceDetailsDTO.getRemarks());
			arApOffSetInvoiceDetailsVO.setArApAdjustmentOffSetVO(arApAdjustmentOffSetVO);
			arApOffSetInvoiceDetailsVOs.add(arApOffSetInvoiceDetailsVO);

		}
		arApAdjustmentOffSetVO.setArApAdjustmentInvoiceDetailsVO(arApOffSetInvoiceDetailsVOs);

	}

	@Override
	public String getArApAdjustmentOffSetDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "OFS";
		String result = arApAdjustmentOffSetRepo.getArApAdjustmentOffSetDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	//// GlOpeningBalance

	@Override
	public List<GlOpeningBalanceVO> getAllGlOpeningBalanceByOrgId(Long orgId) {
		List<GlOpeningBalanceVO> glOpeningBalanceVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received GlOpeningBalance BY OrgId : {}", orgId);
			glOpeningBalanceVO = glOpeningBalanceRepo.getAllGlOpeningBalanceByOrgId(orgId);
		} else {
			LOGGER.info("Successfully Received  GlOpeningBalance For All OrgId.");
			glOpeningBalanceVO = glOpeningBalanceRepo.findAll();
		}
		return glOpeningBalanceVO;
	}

	@Override
	public List<GlOpeningBalanceVO> getAllGlOpeningBalanceById(Long id) {
		List<GlOpeningBalanceVO> glOpeningBalanceVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received GlOpeningBalance BY Id : {}", id);
			glOpeningBalanceVO = glOpeningBalanceRepo.getAllGlOpeningBalanceById(id);
		} else {
			LOGGER.info("Successfully Received GlOpeningBalance For All Id.");
			glOpeningBalanceVO = glOpeningBalanceRepo.findAll();
		}
		return glOpeningBalanceVO;
	}

	@Override
	public Map<String, Object> updateCreateGlOpeningBalance(@Valid GlOpeningBalanceDTO glOpeningBalanceDTO)
			throws ApplicationException {
		String screenCode = "GOB";
		GlOpeningBalanceVO glOpeningBalanceVO = new GlOpeningBalanceVO();
		String message;
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(glOpeningBalanceDTO.getId())) {
			isUpdate = true;
			glOpeningBalanceVO = glOpeningBalanceRepo.findById(glOpeningBalanceDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid GlOpeningBalance details"));
			glOpeningBalanceVO.setUpdatedBy(glOpeningBalanceDTO.getCreatedBy());
			getGlOpeningBalanceVOFromGlOpeningBalanceDTO(glOpeningBalanceDTO, glOpeningBalanceVO);
			message = "GlOpeningBalance Updated Successfully";
		} else {
			// GETDOCID API
			String docId = glOpeningBalanceRepo.getGlOpeningBalanceDocId(glOpeningBalanceDTO.getOrgId(),
					glOpeningBalanceDTO.getFinYear(), glOpeningBalanceDTO.getBranchCode(), screenCode);
			glOpeningBalanceVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(glOpeningBalanceDTO.getOrgId(),
							glOpeningBalanceDTO.getFinYear(), glOpeningBalanceDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			glOpeningBalanceVO.setUpdatedBy(glOpeningBalanceDTO.getCreatedBy());
			glOpeningBalanceVO.setCreatedBy(glOpeningBalanceDTO.getCreatedBy());
			message = "GlOpeningBalance Created Successfully";
		}
		getGlOpeningBalanceVOFromGlOpeningBalanceDTO(glOpeningBalanceDTO, glOpeningBalanceVO);
		glOpeningBalanceRepo.save(glOpeningBalanceVO);
		Map<String, Object> response = new HashMap<>();
		response.put("glOpeningBalanceVO", glOpeningBalanceVO);
		response.put("message", message);
		return response;
	}

	private void getGlOpeningBalanceVOFromGlOpeningBalanceDTO(@Valid GlOpeningBalanceDTO glOpeningBalanceDTO,
			GlOpeningBalanceVO glOpeningBalanceVO) throws ApplicationException {
		glOpeningBalanceVO.setBranch(glOpeningBalanceDTO.getBranch());
		glOpeningBalanceVO.setCurrency(glOpeningBalanceDTO.getCurrency());
		glOpeningBalanceVO.setExRate(glOpeningBalanceDTO.getExRate());
		glOpeningBalanceVO.setRefNo(glOpeningBalanceDTO.getRefNo());
		glOpeningBalanceVO.setRefDate(glOpeningBalanceDTO.getRefDate());
		glOpeningBalanceVO.setSuppRefNo(glOpeningBalanceDTO.getSuppRefNo());
		glOpeningBalanceVO.setSuppRefDate(glOpeningBalanceDTO.getSuppRefDate());
		glOpeningBalanceVO.setOrgId(glOpeningBalanceDTO.getOrgId());
		glOpeningBalanceVO.setActive(true);
		glOpeningBalanceVO.setBranchCode(glOpeningBalanceDTO.getBranchCode());
		glOpeningBalanceVO.setRemarks(glOpeningBalanceDTO.getRemarks());
		glOpeningBalanceVO.setFinYear(glOpeningBalanceDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(glOpeningBalanceVO.getId())) {
			List<ParticularsGlOpeningBalanceVO> particularsGlOpeningBalanceVO1 = particularsGlOpeningBalanceRepo
					.findByGlOpeningBalanceVO(glOpeningBalanceVO);
			particularsGlOpeningBalanceRepo.deleteAll(particularsGlOpeningBalanceVO1);
		}

		BigDecimal totalDebit = BigDecimal.ZERO;
		BigDecimal totalCredit = BigDecimal.ZERO;

		List<ParticularsGlOpeningBalanceVO> particularsGlOpeningBalanceVOs = new ArrayList<>();
		for (ParticularsGlOpeningBalanceDTO particularsGlOpeningBalanceDTO : glOpeningBalanceDTO
				.getParticularsGlOpeningBalanceDTO()) {
			ParticularsGlOpeningBalanceVO particularsGlOpeningBalanceVO = new ParticularsGlOpeningBalanceVO();
			particularsGlOpeningBalanceVO.setAccountName(particularsGlOpeningBalanceDTO.getAccountName());
			particularsGlOpeningBalanceVO.setSubLedgerCode(particularsGlOpeningBalanceDTO.getSubLedgerCode());
			particularsGlOpeningBalanceVO.setDebitAmount(particularsGlOpeningBalanceDTO.getDebitAmount());
			particularsGlOpeningBalanceVO.setCreditAmount(particularsGlOpeningBalanceDTO.getCreditAmount());
			particularsGlOpeningBalanceVO.setCreditBase(particularsGlOpeningBalanceDTO.getCreditBase());
			particularsGlOpeningBalanceVO.setDebitBase(particularsGlOpeningBalanceDTO.getDebitBase());
			particularsGlOpeningBalanceVO.setGlOpeningBalanceVO(glOpeningBalanceVO);

			totalDebit = totalDebit.add(particularsGlOpeningBalanceDTO.getDebitAmount());
			totalCredit = totalCredit.add(particularsGlOpeningBalanceDTO.getCreditAmount());
			particularsGlOpeningBalanceVOs.add(particularsGlOpeningBalanceVO);

		}
		if (totalDebit.compareTo(totalCredit) != 0) {
			throw new ApplicationException("Data Miss Matching!");
		}
		glOpeningBalanceVO.setTotalDebitAmount(totalDebit);
		glOpeningBalanceVO.setTotalCreditAmount(totalCredit);

		glOpeningBalanceVO.setParticularsGlOpeningBalanceVO(particularsGlOpeningBalanceVOs);
	}

	@Override
	public List<GlOpeningBalanceVO> getGlOpeningBalanceByActive() {
		return glOpeningBalanceRepo.findGlOpeningBalanceByActive();
	}

	@Override
	public String getGlOpeningBalanceDocId(Long orgId, String finYear, String branchCode, String branch) {
		String ScreenCode = "GOB";
		String result = glOpeningBalanceRepo.getGlOpeningBalanceDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public GlOpeningBalanceVO getGlOpeningBalanceByDocId(Long orgId, String docId) {

		return glOpeningBalanceRepo.findAllGlOpeningBalanceByDocId(orgId, docId);
	}

	/// ReconcileBank

	@Override
	public List<ReconcileBankVO> getAllReconcileBankByOrgId(Long orgId) {
		List<ReconcileBankVO> reconcileBankVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  ReconcileBank BY OrgId : {}", orgId);
			reconcileBankVO = reconcileBankRepo.getAllReconcileBankByOrgId(orgId);
		}
		return reconcileBankVO;
	}

	@Override
	public List<ReconcileBankVO> getAllReconcileBankById(Long id) {
		List<ReconcileBankVO> reconcileBankVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  ReconcileBank BY Id : {}", id);
			reconcileBankVO = reconcileBankRepo.getAllReconcileBankById(id);
		}
		return reconcileBankVO;
	}

	@Override
	public Map<String, Object> updateCreateReconcileBank(@Valid ReconcileBankDTO reconcileBankDTO)
			throws ApplicationException {
		String screenCode = "RB";
		ReconcileBankVO reconcileBankVO = new ReconcileBankVO();
		String message;

		if (ObjectUtils.isNotEmpty(reconcileBankDTO.getId())) {

			reconcileBankVO = reconcileBankRepo.findById(reconcileBankDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid ReconcileBank details"));
			reconcileBankVO.setUpdatedBy(reconcileBankDTO.getCreatedBy());
			message = "ReconcileBank Updated Successfully";
		} else {

			// GETDOCID API
			String docId = reconcileBankRepo.getReconcileBankDocId(reconcileBankDTO.getOrgId(),
					reconcileBankDTO.getFinYear(), reconcileBankDTO.getBranchCode(), screenCode);
			reconcileBankVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(reconcileBankDTO.getOrgId(),
							reconcileBankDTO.getFinYear(), reconcileBankDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			reconcileBankVO.setCreatedBy(reconcileBankDTO.getCreatedBy());
			reconcileBankVO.setUpdatedBy(reconcileBankDTO.getCreatedBy());
			message = "ReconcileBank Created Successfully";
		}

		getReconcileBankVOFromReconcileBankDTO(reconcileBankDTO, reconcileBankVO);
		reconcileBankRepo.save(reconcileBankVO);
		Map<String, Object> response = new HashMap<>();
		response.put("reconcileBankVO", reconcileBankVO);
		response.put("message", message);

		return response;
	}

	private void getReconcileBankVOFromReconcileBankDTO(@Valid ReconcileBankDTO reconcileBankDTO,
			ReconcileBankVO reconcileBankVO) throws ApplicationException {

		reconcileBankVO.setBankStmtDate(reconcileBankDTO.getBankStmtDate());
		reconcileBankVO.setBankAccount(reconcileBankDTO.getBankAccount());
		reconcileBankVO.setRemarks(reconcileBankDTO.getRemarks());
		reconcileBankVO.setOrgId(reconcileBankDTO.getOrgId());
		reconcileBankVO.setBranch(reconcileBankDTO.getBranch());
		reconcileBankVO.setBranchCode(reconcileBankDTO.getBranchCode());
		reconcileBankVO.setFinYear(reconcileBankDTO.getFinYear());
		reconcileBankVO.setActive(reconcileBankDTO.isActive());
		reconcileBankVO.setIpNo(reconcileBankDTO.getIpNo());
		reconcileBankVO.setLatitude(reconcileBankDTO.getLatitude());

		if (ObjectUtils.isNotEmpty(reconcileBankVO.getId())) {
			List<ParticularsReconcileVO> particularsReconcileVO1 = particularsReconcileRepo
					.findByReconcileBankVO(reconcileBankVO);
			particularsReconcileRepo.deleteAll(particularsReconcileVO1);
		}

		BigDecimal totalDeposit = BigDecimal.ZERO;
		BigDecimal totalWithdrawal = BigDecimal.ZERO;

		List<ParticularsReconcileVO> particularsReconcileVOs = new ArrayList<>();
		for (ParticularsReconcileDTO particularsReconcileDTO : reconcileBankDTO.getParticularsReconcileDTO()) {

			ParticularsReconcileVO particularsReconcileVO = new ParticularsReconcileVO();
			particularsReconcileVO.setVoucherNo(particularsReconcileDTO.getVoucherNo());
			particularsReconcileVO.setVoucherDate(particularsReconcileDTO.getVoucherDate());
			particularsReconcileVO.setChequeNo(particularsReconcileDTO.getChequeNo());
			particularsReconcileVO.setChequeDate(particularsReconcileDTO.getChequeDate());
			if (particularsReconcileDTO.getDeposit() != null
					&& particularsReconcileDTO.getDeposit().compareTo(BigDecimal.ZERO) != 0) {
				particularsReconcileVO.setDeposit(particularsReconcileDTO.getDeposit());
				particularsReconcileVO.setWithdrawal(BigDecimal.ZERO);
			} else {
				particularsReconcileVO.setWithdrawal(particularsReconcileDTO.getWithdrawal());
				particularsReconcileVO.setDeposit(BigDecimal.ZERO);
			}

			totalDeposit = totalDeposit.add(particularsReconcileVO.getDeposit());
			totalWithdrawal = totalWithdrawal.add(particularsReconcileVO.getWithdrawal());
			particularsReconcileVO.setBankRef(particularsReconcileDTO.getBankRef());
			particularsReconcileVO.setReconcileBankVO(reconcileBankVO);
			particularsReconcileVOs.add(particularsReconcileVO);
		}
		if (totalDeposit.equals(totalWithdrawal)) {
			reconcileBankVO.setTotalDeposit(totalDeposit);
			reconcileBankVO.setTotalWithdrawal(totalWithdrawal);
		} else {
			throw new ApplicationException("Total Deposit Amount and Total Withdrawal Amount Should be Equal");
		}
		reconcileBankVO.setParticularsReconcileVO(particularsReconcileVOs);
	}

	@Override
	public String getReconcileBankDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "RB";
		String result = reconcileBankRepo.getReconcileBankDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public List<Map<String, Object>> getBankNameForGroupLedger(Long orgId) {
		Set<Object[]> getAccount = reconcileBankRepo.findByAccountNameForBank(orgId);
		return getAccountName(getAccount);
	}

	private List<Map<String, Object>> getAccountName(Set<Object[]> getAccount) {
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Object[] ch : getAccount) {
			Map<String, Object> map = new HashMap<>();
			map.put("accountgroupname", ch[0] != null ? ch[0].toString() : "");
			list1.add(map);
		}
		return list1;
	}

	// ReconcileCorpBank

	@Override
	public List<ReconcileCorpBankVO> getAllReconcileCorpBankByOrgId(Long orgId) {
		List<ReconcileCorpBankVO> reconcileCorpBankVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  ReconcileCorpBank BY OrgId : {}", orgId);
			reconcileCorpBankVO = reconcileCorpBankRepo.getAllReconcileCorpBankByOrgId(orgId);
		}
		return reconcileCorpBankVO;
	}

	@Override
	public List<ReconcileCorpBankVO> getAllReconcileCorpBankById(Long id) {
		List<ReconcileCorpBankVO> reconcileCorpBankVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  ReconcileCorpBank BY Id : {}", id);
			reconcileCorpBankVO = reconcileCorpBankRepo.getAllReconcileCorpBankById(id);
		}
		return reconcileCorpBankVO;
	}

	@Override
	public Map<String, Object> updateCreateReconcileCorpBank(@Valid ReconcileCorpBankDTO reconcileCorpBankDTO)
			throws ApplicationException {
		String screenCode = "RC";
		ReconcileCorpBankVO reconcileCorpBankVO = new ReconcileCorpBankVO();
		String message;
		// boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(reconcileCorpBankDTO.getId())) {
			boolean isUpdate = true;
			reconcileCorpBankVO = reconcileCorpBankRepo.findById(reconcileCorpBankDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid ReconcileCorpBank details"));
			reconcileCorpBankVO.setUpdatedBy(reconcileCorpBankDTO.getCreatedBy());
			message = "ReconcileCorpBank Updated Successfully";
		} else {
			String docId = reconcileCorpBankRepo.getReconcileCorpBankDocId(reconcileCorpBankDTO.getOrgId(),
					reconcileCorpBankDTO.getFinYear(), reconcileCorpBankDTO.getBranchCode(), screenCode);
			reconcileCorpBankVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(reconcileCorpBankDTO.getOrgId(),
							reconcileCorpBankDTO.getFinYear(), reconcileCorpBankDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			reconcileCorpBankVO.setUpdatedBy(reconcileCorpBankDTO.getCreatedBy());
			reconcileCorpBankVO.setCreatedBy(reconcileCorpBankDTO.getCreatedBy());
			message = "ReconcileCorpBank Created Successfully";
		}
		getReconcileCorpBankVOFromReconcileCorpBankDTO(reconcileCorpBankDTO, reconcileCorpBankVO);
		reconcileCorpBankRepo.save(reconcileCorpBankVO);
		Map<String, Object> response = new HashMap<>();
		response.put("reconcileCorpBankVO", reconcileCorpBankVO);
		response.put("message", message);
		return response;
	}

	private void getReconcileCorpBankVOFromReconcileCorpBankDTO(@Valid ReconcileCorpBankDTO reconcileCorpBankDTO,
			ReconcileCorpBankVO reconcileCorpBankVO) {
		// // Finyr
		// int finyr = taxInvoiceRepo.findFinyr();
		// // DocId
		// String taxInvoice = "AI" + finyr + taxInvoiceRepo.findDocId();
		// taxInvoiceVO.setDocId(taxInvoice);
		// taxInvoiceRepo.nextSeq();
		// // InvoiceNo
		// String invoiceNo = "AI" + finyr + "INV" + taxInvoiceRepo.findInvoiceNo();
		// taxInvoiceVO.setInvoiceNo(invoiceNo);
		// taxInvoiceRepo.nextSeqInvoice();

		reconcileCorpBankVO.setBankStmtDate(reconcileCorpBankDTO.getBankStmtDate());
		reconcileCorpBankVO.setBankAccount(reconcileCorpBankDTO.getBankAccount());
		reconcileCorpBankVO.setRemarks(reconcileCorpBankDTO.getRemarks());
		reconcileCorpBankVO.setOrgId(reconcileCorpBankDTO.getOrgId());
		reconcileCorpBankVO.setBranch(reconcileCorpBankDTO.getBranch());
		reconcileCorpBankVO.setBranchCode(reconcileCorpBankDTO.getBranchCode());
		reconcileCorpBankVO.setFinYear(reconcileCorpBankDTO.getFinYear());
		reconcileCorpBankVO.setActive(reconcileCorpBankDTO.isActive());
		reconcileCorpBankVO.setRemarks(reconcileCorpBankDTO.getRemarks());
		reconcileCorpBankVO.setIpNo(reconcileCorpBankDTO.getIpNo());
		reconcileCorpBankVO.setLatitude(reconcileCorpBankDTO.getLatitude());

		if (ObjectUtils.isNotEmpty(reconcileCorpBankVO.getId())) {
			List<ParticularsReconcileCorpBankVO> particularsReconcileCorpBankVO1 = particularsReconcileCorpBankRepo
					.findByReconcileCorpBankVO(reconcileCorpBankVO);

			particularsReconcileCorpBankRepo.deleteAll(particularsReconcileCorpBankVO1);
		}

		List<ParticularsReconcileCorpBankVO> particularsReconcileCorpBankVOs = new ArrayList<>();
		for (ParticularsReconcileCorpBankDTO particularsReconcileCorpBankDTO : reconcileCorpBankDTO
				.getParticularsReconcileCorpBankDTO()) {
			ParticularsReconcileCorpBankVO particularsReconcileCorpBankVO = new ParticularsReconcileCorpBankVO();

			particularsReconcileCorpBankVO.setVoucherNo(particularsReconcileCorpBankDTO.getVoucherNo());
			particularsReconcileCorpBankVO.setVoucherDate(particularsReconcileCorpBankDTO.getVoucherDate());
			particularsReconcileCorpBankVO.setChequeNo(particularsReconcileCorpBankDTO.getChequeNo());
			particularsReconcileCorpBankVO.setChequeDate(particularsReconcileCorpBankDTO.getChequeDate());
			particularsReconcileCorpBankVO.setDeposit(particularsReconcileCorpBankDTO.getDeposit());
			particularsReconcileCorpBankVO.setWithdrawal(particularsReconcileCorpBankDTO.getWithdrawal());
			particularsReconcileCorpBankVO.setBankRef(particularsReconcileCorpBankDTO.getBankRef());
			particularsReconcileCorpBankVO.setReconcileCorpBankVO(reconcileCorpBankVO);
			particularsReconcileCorpBankVOs.add(particularsReconcileCorpBankVO);
		}
		reconcileCorpBankVO.setParticularsReconcileCorpBankVO(particularsReconcileCorpBankVOs);

	}

	@Override
	public String getReconcileCorpBankDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "RC";
		String result = reconcileCorpBankRepo.getReconcileCorpBankDocId(orgId, finYear, branchCode, ScreenCode);
		return result;

	}

	// ReconcileCash

	@Override
	public List<ReconcileCashVO> getAllReconcileCashByOrgId(Long orgId) {
		List<ReconcileCashVO> reconcileCashVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received ReconcileCash BY OrgId : {}", orgId);
			reconcileCashVO = reconcileCashRepo.getAllReconcileCashByOrgId(orgId);
		}
		return reconcileCashVO;
	}

	@Override
	public List<ReconcileCashVO> getAllReconcileCashById(Long id) {
		List<ReconcileCashVO> reconcileCashVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received ReconcileCash BY Id : {}", id);
			reconcileCashVO = reconcileCashRepo.getAllReconcileCashById(id);
		}
		return reconcileCashVO;
	}

	@Override
	public Map<String, Object> updateCreateReconcileCash(@Valid ReconcileCashDTO reconcileCashDTO)
			throws ApplicationException {
		String screenCode = "RCH";
		ReconcileCashVO reconcileCashVO = new ReconcileCashVO();
		boolean isUpdate = false;
		String message;
		if (ObjectUtils.isNotEmpty(reconcileCashDTO.getId())) {
			isUpdate = true;
			reconcileCashVO = reconcileCashRepo.findById(reconcileCashDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid ReconcileCash details"));
			reconcileCashVO.setUpdatedBy(reconcileCashDTO.getCreatedBy());
			message = "ReconcileCash Updated Successfully";
		} else {
			String docId = reconcileCashRepo.getReconcileCashDocId(reconcileCashDTO.getOrgId(),
					reconcileCashDTO.getFinYear(), reconcileCashDTO.getBranchCode(), screenCode);
			reconcileCashVO.setDocId(docId);

			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(reconcileCashDTO.getOrgId(),
							reconcileCashDTO.getFinYear(), reconcileCashDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			reconcileCashVO.setUpdatedBy(reconcileCashDTO.getCreatedBy());
			reconcileCashVO.setCreatedBy(reconcileCashDTO.getCreatedBy());
			message = "ReconcileCash Created Successfully";
		}

		getReconcileCashVOFromReconcileCashDTO(reconcileCashDTO, reconcileCashVO);
		reconcileCashRepo.save(reconcileCashVO);
		Map<String, Object> response = new HashMap<>();
		response.put("reconcileCashVO", reconcileCashVO);
		response.put("message", message);
		return response;
	}

	private void getReconcileCashVOFromReconcileCashDTO(@Valid ReconcileCashDTO reconcileCashDTO,
			ReconcileCashVO reconcileCashVO) {
		reconcileCashVO.setCashAccount(reconcileCashDTO.getCashAccount());
		reconcileCashVO.setBalanceAsPerBooks(reconcileCashDTO.getBalanceAsPerBooks());
		reconcileCashVO.setDn1(reconcileCashDTO.getDn1());
		reconcileCashVO.setDn2(reconcileCashDTO.getDn2());
		reconcileCashVO.setDn3(reconcileCashDTO.getDn3());
		reconcileCashVO.setDn4(reconcileCashDTO.getDn4());
		reconcileCashVO.setDn5(reconcileCashDTO.getDn5());
		reconcileCashVO.setDn6(reconcileCashDTO.getDn6());
		reconcileCashVO.setDn7(reconcileCashDTO.getDn7());
		reconcileCashVO.setDn8(reconcileCashDTO.getDn8());
		reconcileCashVO.setRemarks(reconcileCashDTO.getRemarks());
		reconcileCashVO.setOrgId(reconcileCashDTO.getOrgId());
		reconcileCashVO.setCancel(false);
		reconcileCashVO.setBranch(reconcileCashDTO.getBranch());
		reconcileCashVO.setBranchCode(reconcileCashDTO.getBranchCode());
		reconcileCashVO.setActive(reconcileCashDTO.isActive());
		reconcileCashVO.setFinYear(reconcileCashDTO.getFinYear());
		reconcileCashVO.setIpNo(reconcileCashDTO.getIpNo());
		reconcileCashVO.setLatitude(reconcileCashDTO.getLatitude());

		// Map counts from DTO
		int[] counts = { reconcileCashDTO.getDn1(), reconcileCashDTO.getDn2(), reconcileCashDTO.getDn3(),
				reconcileCashDTO.getDn4(), reconcileCashDTO.getDn5(), reconcileCashDTO.getDn6(),
				reconcileCashDTO.getDn7(), reconcileCashDTO.getDn8() };
		BigDecimal[] amounts = { BigDecimal.valueOf(2000), BigDecimal.valueOf(500), BigDecimal.valueOf(200),
				BigDecimal.valueOf(100), BigDecimal.valueOf(50), BigDecimal.valueOf(20), BigDecimal.valueOf(10),
				BigDecimal.valueOf(1) };
		BigDecimal totalPhysicalAmount = calculateTotalPhysicalAmount(counts, amounts);
		reconcileCashVO.setTotalPhyAmount(totalPhysicalAmount);
		reconcileCashVO.setDifferenceAmount(totalPhysicalAmount);

		// Set individual dn amounts based on calculations
		reconcileCashVO.setDn1Amt(amounts[0].multiply(BigDecimal.valueOf(counts[0])).setScale(2, RoundingMode.HALF_UP));
		reconcileCashVO.setDn2Amt(amounts[1].multiply(BigDecimal.valueOf(counts[1])).setScale(2, RoundingMode.HALF_UP));
		reconcileCashVO.setDn3Amt(amounts[2].multiply(BigDecimal.valueOf(counts[2])).setScale(2, RoundingMode.HALF_UP));
		reconcileCashVO.setDn4Amt(amounts[3].multiply(BigDecimal.valueOf(counts[3])).setScale(2, RoundingMode.HALF_UP));
		reconcileCashVO.setDn5Amt(amounts[4].multiply(BigDecimal.valueOf(counts[4])).setScale(2, RoundingMode.HALF_UP));
		reconcileCashVO.setDn6Amt(amounts[5].multiply(BigDecimal.valueOf(counts[5])).setScale(2, RoundingMode.HALF_UP));
		reconcileCashVO.setDn7Amt(amounts[6].multiply(BigDecimal.valueOf(counts[6])).setScale(2, RoundingMode.HALF_UP));
		reconcileCashVO.setDn8Amt(amounts[7].multiply(BigDecimal.valueOf(counts[7])).setScale(2, RoundingMode.HALF_UP));
	}

	private BigDecimal calculateTotalPhysicalAmount(int[] counts, BigDecimal[] amounts) {
		BigDecimal total = BigDecimal.ZERO;

		for (int i = 0; i < counts.length; i++) {
			total = total.add(amounts[i].multiply(BigDecimal.valueOf(counts[i])));
		}

		return total.setScale(2, RoundingMode.HALF_UP);
	}

	@Override

	public FundTransferVO getFundTranferByDocId(Long orgId, String docId) {
		return fundTransferRepo.findAllFundTransferByDocId(orgId, docId);
	}

	@Override
	public String getReconcileCashDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "RCH";
		String result = reconcileCashRepo.getReconcileCashDocId(orgId, finYear, branchCode, ScreenCode);
		return result;

	}

	@Override
	public String getFundTranferDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "FT";
		String result = fundTransferRepo.getFundTranferDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public PaymentVoucherVO getpaymentVoucherByDocId(Long orgId, String docId) {
		return paymentVoucherRepo.findAllPaymentVoucherByDocId(orgId, docId);
	}

	@Override
	public String getpaymentVoucherDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "PV";
		String result = paymentVoucherRepo.getpaymentVoucherDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	// TMS-TT-JobCard

	@Override
	public List<JobCardVO> getAllJobCardByOrgId(Long orgId) {
		List<JobCardVO> tmsJobCardVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  TmsJobCard BY OrgId : {}", orgId);
			tmsJobCardVO = tmsJobCardRepo.getAllJobCardByOrgId(orgId);
		}
		return tmsJobCardVO;
	}

	@Override
	public List<JobCardVO> getAllJobCardById(Long id) {
		List<JobCardVO> tmsJobCardVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  TmsJobCard BY Id : {}", id);
			tmsJobCardVO = tmsJobCardRepo.getAllJobCardById(id);
		}
		return tmsJobCardVO;
	}

	@Override
	public Map<String, Object> updateCreateJobCard(@Valid TmsJobCardDTO tmsJobCardDTO) throws ApplicationException {
		JobCardVO tmsJobCardVO = new JobCardVO();
		String screenCode = "JC";
		String message;
		if (ObjectUtils.isNotEmpty(tmsJobCardDTO.getId())) {
			tmsJobCardVO = tmsJobCardRepo.findById(tmsJobCardDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid TmsJobCard details"));
			tmsJobCardVO.setUpdatedBy(tmsJobCardVO.getCreatedBy());
			getJobCardVOFromJobCardDTO(tmsJobCardDTO, tmsJobCardVO);
			message = "TmsJobCard Updated Successfully";
		} else {
			// GETDOCID API
			String docId = tmsJobCardRepo.getJobCardDocId(tmsJobCardDTO.getOrgId(), tmsJobCardDTO.getFinYear(),
					tmsJobCardDTO.getBranchCode(), screenCode);

			tmsJobCardVO.setJobNo(docId);

//			// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(tmsJobCardDTO.getOrgId(),
							tmsJobCardDTO.getFinYear(), tmsJobCardDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			tmsJobCardVO.setCreatedBy(tmsJobCardDTO.getCreatedBy());
			tmsJobCardVO.setUpdatedBy(tmsJobCardVO.getCreatedBy());
			getJobCardVOFromJobCardDTO(tmsJobCardDTO, tmsJobCardVO);
			message = "TmsJobCard Creation  Successfully !";
		}

		tmsJobCardRepo.save(tmsJobCardVO);
		Map<String, Object> response = new HashMap<>();
		response.put("tmsJobCardVO", tmsJobCardVO);
		response.put("message", message);
		return response;
	}

	private void getJobCardVOFromJobCardDTO(@Valid TmsJobCardDTO tmsJobCardDTO, JobCardVO tmsJobCardVO) {
		// tmsJobCardVO.setJobNo(tmsJobCardDTO.getJobNo());
		tmsJobCardVO.setCustomer(tmsJobCardDTO.getCustomer());
		tmsJobCardVO.setSalesCategory(tmsJobCardDTO.getSalesCategory());
		tmsJobCardVO.setSalesPerson(tmsJobCardDTO.getSalesPerson());
		tmsJobCardVO.setIncome(tmsJobCardDTO.getIncome());
		tmsJobCardVO.setExpense(tmsJobCardDTO.getExpense());
		tmsJobCardVO.setProfit(tmsJobCardDTO.getProfit());
		tmsJobCardVO.setRemarks(tmsJobCardDTO.getRemarks());
		tmsJobCardVO.setCreatedBy(tmsJobCardDTO.getCreatedBy());
		tmsJobCardVO.setOrgId(tmsJobCardDTO.getOrgId());
		tmsJobCardVO.setOperationClosed(tmsJobCardDTO.isOperationClosed());
		tmsJobCardVO.setFinanceClosed(tmsJobCardDTO.isFinanceClosed());
		tmsJobCardVO.setClosed(tmsJobCardDTO.isClosed());
		tmsJobCardVO.setClosedOn(tmsJobCardDTO.getClosedOn());
		tmsJobCardVO.setBranch(tmsJobCardDTO.getBranch());
		tmsJobCardVO.setBranchCode(tmsJobCardDTO.getBranchCode());
		tmsJobCardVO.setCancelRemarks(tmsJobCardDTO.getCancelRemarks());
		tmsJobCardVO.setActive(tmsJobCardDTO.isActive());
		tmsJobCardVO.setFinYear(tmsJobCardDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(tmsJobCardDTO.getId())) {
			List<CostCenterJobCardVO> costCenterTmsJobCardVO1 = costCenterTmsJobCardRepo.findByJobCardVO(tmsJobCardVO);
			costCenterTmsJobCardRepo.deleteAll(costCenterTmsJobCardVO1);
		}
		List<CostCenterJobCardVO> costCenterTmsJobCardVOs = new ArrayList<>();
		for (CostCenterTmsJobCardDTO costCenterTmsJobCardDTO : tmsJobCardDTO.getCostCenterTmsJobCardDTO()) {
			CostCenterJobCardVO costCenterTmsJobCardVO = new CostCenterJobCardVO();
			costCenterTmsJobCardVO.setAccountName(costCenterTmsJobCardDTO.getAccountName());
			costCenterTmsJobCardVO.setAmount(costCenterTmsJobCardDTO.getAmount());
			costCenterTmsJobCardVO.setJobCardVO(tmsJobCardVO);
			costCenterTmsJobCardVOs.add(costCenterTmsJobCardVO);
		}
		tmsJobCardVO.setCostCenterJobCardVO(costCenterTmsJobCardVOs);

	}

	@Override
	public String getJobCardDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "JC";
		String result = tmsJobCardRepo.getJobCardDocId(orgId, finYear, branchCode, ScreenCode);
		return result;

	}

	// AdjustmentJournal

	@Override
	public List<AdjustmentJournalVO> getAllAdjustmentJournalByOrgId(Long orgId) {
		List<AdjustmentJournalVO> adjustmentJournalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  AdjustmentJournal BY OrgId : {}", orgId);
			adjustmentJournalVO = adjustmentJournalRepo.getAllAdjustmentJournalByOrgId(orgId);
		}
		return adjustmentJournalVO;
	}

	@Override
	public List<AdjustmentJournalVO> getAdjustmentJournalById(Long id) {
		List<AdjustmentJournalVO> adjustmentJournalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received AdjustmentJournal BY Id : {}", id);
			adjustmentJournalVO = adjustmentJournalRepo.getAdjustmentJournalById(id);
		}
		return adjustmentJournalVO;
	}

	@Override
	public Map<String, Object> updateCreateAdjustmentJournal(@Valid AdjustmentJournalDTO adjustmentJournalDTO)
			throws ApplicationException {
		String sourceScreenCode = "AJ";
		String screenCode = "AC";
		AdjustmentJournalVO adjustmentJournalVO = new AdjustmentJournalVO();
		String message;

		if (ObjectUtils.isNotEmpty(adjustmentJournalDTO.getId())) {
			adjustmentJournalVO = adjustmentJournalRepo.findById(adjustmentJournalDTO.getId())
					.orElseThrow(() -> new ApplicationException("adjustmentJournal not found"));

			adjustmentJournalVO.setUpdatedBy(adjustmentJournalDTO.getCreatedBy());
			createUpdateAdjustmentJournalVOByAdjustmentJournalDTO(adjustmentJournalDTO, adjustmentJournalVO);
			message = "AdjustmentJournal Updated Successfully";
		} else {
			// GETDOCID API
			String accountsDocId = accountsRepo.getApproveDocId(adjustmentJournalDTO.getOrgId(),
					adjustmentJournalDTO.getFinYear(), adjustmentJournalDTO.getBranchCode(), sourceScreenCode,
					screenCode);
			adjustmentJournalVO.setDocId(accountsDocId);

//				// GETDOCID LASTNO +1
			MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(adjustmentJournalDTO.getOrgId(),
							adjustmentJournalDTO.getFinYear(), adjustmentJournalDTO.getBranchCode(), sourceScreenCode,
							screenCode);
			multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);

			adjustmentJournalVO.setCreatedBy(adjustmentJournalDTO.getCreatedBy());
			adjustmentJournalVO.setUpdatedBy(adjustmentJournalDTO.getCreatedBy());
			createUpdateAdjustmentJournalVOByAdjustmentJournalDTO(adjustmentJournalDTO, adjustmentJournalVO);
			message = "AdjustmentJournal Created Successfully";
		}

		AdjustmentJournalVO savedAdjustmentJournalVO = adjustmentJournalRepo.save(adjustmentJournalVO);
		if (savedAdjustmentJournalVO.getStatus().equalsIgnoreCase("SUBMIT")) {
			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(savedAdjustmentJournalVO.getDocId());
			accountsVO.setSourceScreen(savedAdjustmentJournalVO.getScreenName());
			accountsVO.setSourceScreenCode(savedAdjustmentJournalVO.getScreenCode());
			accountsVO.setSourceId(savedAdjustmentJournalVO.getId());
			accountsVO.setCreatedBy(savedAdjustmentJournalVO.getCreatedBy());
			accountsVO.setModifiedBy(savedAdjustmentJournalVO.getUpdatedBy());
			accountsVO.setOrgId(savedAdjustmentJournalVO.getOrgId());
			accountsVO.setBranch(savedAdjustmentJournalVO.getBranch());
			accountsVO.setBranchCode(savedAdjustmentJournalVO.getBranchCode());
			accountsVO.setModifiedon(savedAdjustmentJournalVO.getCommonDate().getModifiedon().toUpperCase());
			accountsVO.setCreatedon(savedAdjustmentJournalVO.getCommonDate().getModifiedon().toUpperCase());
			accountsVO.setRefNo(savedAdjustmentJournalVO.getDocId());
			accountsVO.setRefDate(savedAdjustmentJournalVO.getDocDate());
			accountsVO.setCurrency(savedAdjustmentJournalVO.getCurrency());
			accountsVO.setExRate(savedAdjustmentJournalVO.getExRate());
			accountsVO.setRemarks(savedAdjustmentJournalVO.getRemarks());
			accountsVO.setFinYear(savedAdjustmentJournalVO.getFinYear());
			accountsVO.setTotalDebitAmount(savedAdjustmentJournalVO.getTotalDebitAmount());
			accountsVO.setTotalCreditAmount(savedAdjustmentJournalVO.getTotalCreditAmount());
			accountsVO.setAmountInWords(
					amountInWordsConverterService.convert(savedAdjustmentJournalVO.getTotalDebitAmount().longValue()));

			// Create AccountsDetailsVO list and populate it
			List<AccountsDetailsVO> accountsDetailsVOs = new ArrayList<>();
			List<AccountParticularsVO> particularsJournalVOLists = savedAdjustmentJournalVO.getAccountParticularsVO();
			for (AccountParticularsVO particularsJournalVO : particularsJournalVOLists) {
				GroupLedgerVO groupLedgerVO = groupLedgerRepo
						.findByAccountGroupName(particularsJournalVO.getAccountsName());

				AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
//				if (!particularsJournalVO.getDebitAmount().equals(BigDecimal.ZERO)) {
				// DebitAmount entry
				accountsDetailsVO.setACategory(groupLedgerVO.getCategory());
				accountsDetailsVO.setArapFlag(false);
				accountsDetailsVO.setSubledgerName("None");
				accountsDetailsVO.setSubLedgerCode("None");
				accountsDetailsVO.setAccountName(groupLedgerVO.getAccountGroupName());
				accountsDetailsVO.setACurrency(savedAdjustmentJournalVO.getCurrency());
				accountsDetailsVO.setArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setNArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setAExRate(savedAdjustmentJournalVO.getExRate());
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVO.setNDebitAmount(particularsJournalVO.getDebitAmount());
				accountsDetailsVO.setDebitAmount(particularsJournalVO.getDebitAmount());
				accountsDetailsVO.setBDebitAmount(particularsJournalVO.getDebitAmount());
				accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
				accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
				accountsDetailsVO.setArapAmount(particularsJournalVO.getDebitAmount().multiply(BigDecimal.valueOf(-1)));
				accountsDetailsVO
						.setBArapAmount(particularsJournalVO.getDebitAmount().multiply(BigDecimal.valueOf(-1)));
				accountsDetailsVO
						.setNArapAmount(particularsJournalVO.getDebitAmount().multiply(BigDecimal.valueOf(-1)));
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVOs.add(accountsDetailsVO);

//				Credit Amount entry
				accountsDetailsVO.setACategory(groupLedgerVO.getCategory());
				accountsDetailsVO.setArapFlag(false);
				accountsDetailsVO.setSubledgerName("None");
				accountsDetailsVO.setSubLedgerCode("None");
				accountsDetailsVO.setAccountName(groupLedgerVO.getAccountGroupName());
				accountsDetailsVO.setACurrency(savedAdjustmentJournalVO.getCurrency());
				accountsDetailsVO.setArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setNArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setAExRate(savedAdjustmentJournalVO.getExRate());
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO.setNCreditAmount(particularsJournalVO.getCreditAmount());
				accountsDetailsVO.setCreditAmount(particularsJournalVO.getCreditAmount());
				accountsDetailsVO.setBCrAmount(particularsJournalVO.getCreditAmount());
				accountsDetailsVO.setArapAmount(particularsJournalVO.getCreditAmount());
				accountsDetailsVO.setBArapAmount(particularsJournalVO.getCreditAmount());
				accountsDetailsVO.setNArapAmount(particularsJournalVO.getCreditAmount());
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVO.setAccountsVO(accountsVO);
				accountsDetailsVOs.add(accountsDetailsVO);
			}
			accountsVO.setAccountsDetailsVO(accountsDetailsVOs);
			accountsRepo.save(accountsVO);
		}

//		adjustmentJournalRepo.save(adjustmentJournalVO);
		Map<String, Object> response = new HashMap<>();
		response.put("adjustmentJournalVO", adjustmentJournalVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateAdjustmentJournalVOByAdjustmentJournalDTO(@Valid AdjustmentJournalDTO adjustmentJournalDTO,
			AdjustmentJournalVO adjustmentJournalVO) throws ApplicationException {
		adjustmentJournalVO.setAdjustmentType(adjustmentJournalDTO.getAdjustmentType());
		adjustmentJournalVO.setRemarks(adjustmentJournalDTO.getRemarks().toUpperCase());
		adjustmentJournalVO.setCurrency(adjustmentJournalDTO.getCurrency());
		adjustmentJournalVO.setExRate(adjustmentJournalDTO.getExRate());

		adjustmentJournalVO.setRefNo(adjustmentJournalDTO.getRefNo().toUpperCase());
		adjustmentJournalVO.setRefDate(adjustmentJournalDTO.getRefDate());
		adjustmentJournalVO.setSuppRefNo(adjustmentJournalDTO.getSuppRefNo().toUpperCase());
		adjustmentJournalVO.setSuppRefDate(adjustmentJournalDTO.getSuppRefDate());
		adjustmentJournalVO.setOrgId(adjustmentJournalDTO.getOrgId());
		adjustmentJournalVO.setBranch(adjustmentJournalDTO.getBranch());
		adjustmentJournalVO.setBranchCode(adjustmentJournalDTO.getBranchCode());
		adjustmentJournalVO.setFinYear(adjustmentJournalDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(adjustmentJournalVO.getId())) {
			List<AccountParticularsVO> accountParticularsVOList = accountParticularsRepo
					.findByAdjustmentJournalVO(adjustmentJournalVO);
			accountParticularsRepo.deleteAll(accountParticularsVOList);
		}

		BigDecimal totalDebitAmount = BigDecimal.ZERO;
		BigDecimal totalCreditAmount = BigDecimal.ZERO;
		List<AccountParticularsVO> accountParticularsVOs = new ArrayList<>();
		for (AccountParticularsDTO accountParticularsDTO : adjustmentJournalDTO.getAccountParticularsDTO()) {
			AccountParticularsVO accountParticularsVO = new AccountParticularsVO();

			accountParticularsVO.setAccountsName(accountParticularsDTO.getAccountsName());
			accountParticularsVO.setSubledgerName(accountParticularsDTO.getSubledgerName());
			accountParticularsVO.setSubLedgerCode(accountParticularsDTO.getSubLedgerCode());
			if (accountParticularsDTO.getDebitAmount() != null
					&& accountParticularsDTO.getDebitAmount().compareTo(BigDecimal.ZERO) != 0) {
				accountParticularsVO.setDebitAmount(accountParticularsDTO.getDebitAmount());
				accountParticularsVO.setCreditAmount(BigDecimal.ZERO);
			} else {
				accountParticularsVO.setCreditAmount(accountParticularsDTO.getCreditAmount());
				accountParticularsVO.setDebitAmount(BigDecimal.ZERO);
			}
			totalCreditAmount = totalCreditAmount.add(accountParticularsVO.getCreditAmount());
			totalDebitAmount = totalDebitAmount.add(accountParticularsVO.getDebitAmount());

			if (accountParticularsDTO.getDebitBase() != null
					&& accountParticularsDTO.getDebitBase().compareTo(BigDecimal.ZERO) != 0) {
				accountParticularsVO.setDebitBase(accountParticularsDTO.getDebitBase());
				accountParticularsVO.setCreditBase(BigDecimal.ZERO);
			} else {
				accountParticularsVO.setCreditBase(accountParticularsDTO.getCreditBase());
				accountParticularsVO.setDebitBase(BigDecimal.ZERO);
			}

			accountParticularsVO.setAdjustmentJournalVO(adjustmentJournalVO);
			accountParticularsVOs.add(accountParticularsVO);
		}
		if (totalCreditAmount.equals(totalDebitAmount)) {
			adjustmentJournalVO.setTotalCreditAmount(totalCreditAmount);
			adjustmentJournalVO.setTotalDebitAmount(totalDebitAmount);
		} else {
			throw new ApplicationException("Total Debit Amount and Total Credit Amount Should be Equal");
		}
		adjustmentJournalVO.setAccountParticularsVO(accountParticularsVOs);

	}

	@Override
	public String getAdjustmentJournalDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "AJ";
		String result = adjustmentJournalRepo.getAdjustmentJournalByDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	// BankingDeposit

	@Override
	public List<BankingDepositVO> getAllBankingDepositByOrgId(Long orgId) {
		List<BankingDepositVO> bankingDepositVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  BankingDeposit BY OrgId : {}", orgId);
			bankingDepositVO = bankingDepositRepo.getAllBankingDepositByOrgId(orgId);
		}
		return bankingDepositVO;
	}

	@Override
	public List<BankingDepositVO> getBankingDepositById(Long id) {
		List<BankingDepositVO> bankingDepositVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  BankingDeposit BY Id : {}", id);
			bankingDepositVO = bankingDepositRepo.getBankingDepositById(id);
		}
		return bankingDepositVO;
	}

	@Override
	public Map<String, Object> updateCreateBankingDeposit(@Valid BankingDepositDTO bankingDepositDTO)
			throws ApplicationException {
		String screenCode = "BD";
		BankingDepositVO bankingDepositVO = new BankingDepositVO();
		String message;
		if (ObjectUtils.isNotEmpty(bankingDepositDTO.getId())) {
			bankingDepositVO = bankingDepositRepo.findById(bankingDepositDTO.getId())
					.orElseThrow(() -> new ApplicationException("BankingDeposit not found"));

			bankingDepositVO.setUpdatedBy(bankingDepositDTO.getCreatedBy());
			createUpdateBankingDepositVOByBankingDepositDTO(bankingDepositDTO, bankingDepositVO);
			message = "BankingDeposit Updated Successfully";
		} else {
			// GETDOCID API
			String docId = bankingDepositRepo.getBankingDepositDocId(bankingDepositDTO.getOrgId(),
					bankingDepositDTO.getFinYear(), bankingDepositDTO.getBranchCode(), screenCode);

			bankingDepositVO.setDocId(docId);

//						// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(bankingDepositDTO.getOrgId(),
							bankingDepositDTO.getFinYear(), bankingDepositDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			bankingDepositVO.setCreatedBy(bankingDepositDTO.getCreatedBy());
			bankingDepositVO.setUpdatedBy(bankingDepositDTO.getCreatedBy());
			createUpdateBankingDepositVOByBankingDepositDTO(bankingDepositDTO, bankingDepositVO);
			message = "BankingDeposit Created Successfully";
		}

		bankingDepositRepo.save(bankingDepositVO);
		Map<String, Object> response = new HashMap<>();
		response.put("bankingDepositVO", bankingDepositVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateBankingDepositVOByBankingDepositDTO(@Valid BankingDepositDTO bankingDepositDTO,
			BankingDepositVO bankingDepositVO) throws ApplicationException {
		bankingDepositVO.setDepositMode(bankingDepositDTO.getDepositMode());
		bankingDepositVO.setReceivedFrom(bankingDepositDTO.getReceivedFrom().toUpperCase());
		bankingDepositVO.setChequeNo(bankingDepositDTO.getChequeNo());
		bankingDepositVO.setChequeDate(bankingDepositDTO.getChequeDate());
		bankingDepositVO.setChequeBank(bankingDepositDTO.getChequeBank());
		bankingDepositVO.setBankAccount(bankingDepositDTO.getBankAccount());
		bankingDepositVO.setCurrency(bankingDepositDTO.getCurrency());
		bankingDepositVO.setExchangeRate(bankingDepositDTO.getExchangeRate());
		bankingDepositVO.setDepositAmount(bankingDepositDTO.getDepositAmount());

		bankingDepositVO.setRemarks(bankingDepositDTO.getRemarks());
		bankingDepositVO.setOrgId(bankingDepositDTO.getOrgId());
		bankingDepositVO.setBranch(bankingDepositDTO.getBranch());
		bankingDepositVO.setBranchCode(bankingDepositDTO.getBranchCode());
		bankingDepositVO.setFinYear(bankingDepositDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(bankingDepositVO.getId())) {
			List<DepositParticularsVO> depositParticularsVOList = depositParticularsRepo
					.findByBankingDepositVO(bankingDepositVO);
			depositParticularsRepo.deleteAll(depositParticularsVOList);
		}

		BigDecimal totalDebitAmount = BigDecimal.ZERO;
		BigDecimal totalCreditAmount = BigDecimal.ZERO;
		List<DepositParticularsVO> depositParticularsVOs = new ArrayList<>();
		for (DepositParticularsDTO depositParticularsDTO : bankingDepositDTO.getDepositParticularsDTO()) {
			DepositParticularsVO depositParticularsVO = new DepositParticularsVO();

			depositParticularsVO.setAccountsName(depositParticularsDTO.getAccountsName());
			depositParticularsVO.setNarration(depositParticularsDTO.getNarration());
			if (depositParticularsDTO.getDebit() != null
					&& depositParticularsDTO.getDebit().compareTo(BigDecimal.ZERO) != 0) {
				depositParticularsVO.setDebit(depositParticularsDTO.getDebit());
				depositParticularsVO.setCredit(BigDecimal.ZERO);
			} else {
				depositParticularsVO.setCredit(depositParticularsDTO.getCredit());
				depositParticularsVO.setDebit(BigDecimal.ZERO);
			}
			totalCreditAmount = totalCreditAmount.add(depositParticularsVO.getCredit());
			totalDebitAmount = totalDebitAmount.add(depositParticularsVO.getDebit());

			depositParticularsVO.setBankingDepositVO(bankingDepositVO);
			depositParticularsVOs.add(depositParticularsVO);
		}
		if (totalCreditAmount.equals(totalDebitAmount)) {
			bankingDepositVO.setTotalCreditAmount(totalCreditAmount);
			bankingDepositVO.setTotalDebitAmount(totalDebitAmount);
			bankingDepositVO.setTotalAmount(bankingDepositDTO.getDepositAmount());

		} else {
			throw new ApplicationException("Total Debit Amount and Total Credit Amount Should be Equal");
		}
		bankingDepositVO.setDepositparticularsVO(depositParticularsVOs);

	}

	@Override
	public String getBankingDepositDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "BD";
		String result = bankingDepositRepo.getBankingDepositByDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	@Transactional
	public List<Map<String, Object>> getBankNameFromGroupforBankingDeposit(Long orgId) {

		Set<Object[]> result = bankingDepositRepo.findBankNameFromGroupforBankingDeposit(orgId);
		return getBankNameFromGroupforBankingDeposit(result);
	}

	private List<Map<String, Object>> getBankNameFromGroupforBankingDeposit(Set<Object[]> result) {
		List<Map<String, Object>> details1 = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> part = new HashMap<>();
			part.put("bankName", fs[0] != null ? fs[0].toString() : "");
			details1.add(part);
		}
		return details1;
	}

	// BANKINGWITHDRAWAL

	@Override
	public List<BankingWithdrawalVO> getAllBankingWithdrawalByOrgId(Long orgId) {
		List<BankingWithdrawalVO> bankingWithdrawalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  BankingWithdrawal BY OrgId : {}", orgId);
			bankingWithdrawalVO = bankingWithdrawalRepo.getAllBankingWithdrawalByOrgId(orgId);
		}
		return bankingWithdrawalVO;
	}

	@Override
	public List<BankingWithdrawalVO> getBankingWithdrawalById(Long id) {
		List<BankingWithdrawalVO> bankingWithdrawalVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  BankingWithdrawal BY Id : {}", id);
			bankingWithdrawalVO = bankingWithdrawalRepo.getBankingWithdrawalById(id);
		}
		return bankingWithdrawalVO;
	}

	@Override
	public String getBankingWithdrawalDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "BW";
		String result = bankingWithdrawalRepo.getBankingWithdrawalDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public Map<String, Object> updateCreateBankingWithdrawal(@Valid BankingWithdrawalDTO bankingWithdrawalDTO)
			throws ApplicationException {
		String sourceScreenCode = "BW";
		String screenCode = "AC";
		BankingWithdrawalVO bankingWithdrawalVO = new BankingWithdrawalVO();
		String message;

		if (ObjectUtils.isNotEmpty(bankingWithdrawalDTO.getId())) {
			bankingWithdrawalVO = bankingWithdrawalRepo.findById(bankingWithdrawalDTO.getId())
					.orElseThrow(() -> new ApplicationException("BankingWithdrawal not found"));

			bankingWithdrawalVO.setUpdatedBy(bankingWithdrawalDTO.getCreatedBy());
			createUpdateBankingWithdrawalVOByBankingWithdrawalDTO(bankingWithdrawalDTO, bankingWithdrawalVO);
			message = "BankingWithdrawal Updated Successfully";
		} else {
			// GETDOCID API

//			// GETDOCID API
			String bankingDocId = accountsRepo.getApproveDocId(bankingWithdrawalDTO.getOrgId(),
					bankingWithdrawalDTO.getFinYear(), bankingWithdrawalDTO.getBranchCode(), sourceScreenCode,
					screenCode);
			bankingWithdrawalVO.setDocId(bankingDocId);

			// GETDOCID LASTNO +1
			MultipleDocIdGenerationDetailsVO multipleDocIdGenerationDetailsVO = multipleDocIdGenerationDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndSourceScreenCodeAndScreenCode(bankingWithdrawalDTO.getOrgId(),
							bankingWithdrawalDTO.getFinYear(), bankingWithdrawalDTO.getBranchCode(), sourceScreenCode,
							screenCode);
			multipleDocIdGenerationDetailsVO.setLastno(multipleDocIdGenerationDetailsVO.getLastno() + 1);
			multipleDocIdGenerationDetailsRepo.save(multipleDocIdGenerationDetailsVO);

			bankingWithdrawalVO.setCreatedBy(bankingWithdrawalDTO.getCreatedBy());
			bankingWithdrawalVO.setUpdatedBy(bankingWithdrawalDTO.getCreatedBy());
			createUpdateBankingWithdrawalVOByBankingWithdrawalDTO(bankingWithdrawalDTO, bankingWithdrawalVO);
			message = "Banking Withdrawal Created Successfully";
		}

		BankingWithdrawalVO savedBankingWithdrawalVO = bankingWithdrawalRepo.save(bankingWithdrawalVO);
		if (savedBankingWithdrawalVO.getStatus().equalsIgnoreCase("SUBMIT")) {
			AccountsVO accountsVO = new AccountsVO();
			accountsVO.setDocId(savedBankingWithdrawalVO.getDocId());
			accountsVO.setSourceScreen(savedBankingWithdrawalVO.getScreenName());
			accountsVO.setSourceScreenCode(savedBankingWithdrawalVO.getScreenCode());
			accountsVO.setSourceId(savedBankingWithdrawalVO.getId());
			accountsVO.setCreatedBy(savedBankingWithdrawalVO.getCreatedBy());
			accountsVO.setModifiedBy(savedBankingWithdrawalVO.getUpdatedBy());
			accountsVO.setOrgId(savedBankingWithdrawalVO.getOrgId());
			accountsVO.setBranch(savedBankingWithdrawalVO.getBranch());
			accountsVO.setBranchCode(savedBankingWithdrawalVO.getBranchCode());
			accountsVO.setModifiedon(savedBankingWithdrawalVO.getCommonDate().getModifiedon().toUpperCase());
			accountsVO.setCreatedon(savedBankingWithdrawalVO.getCommonDate().getModifiedon().toUpperCase());
			accountsVO.setRefNo(savedBankingWithdrawalVO.getDocId());
			accountsVO.setRefDate(savedBankingWithdrawalVO.getDocDate());
			accountsVO.setCurrency(savedBankingWithdrawalVO.getCurrency());
			accountsVO.setExRate(savedBankingWithdrawalVO.getExchangeRate());
			accountsVO.setRemarks(savedBankingWithdrawalVO.getRemarks());
			accountsVO.setFinYear(savedBankingWithdrawalVO.getFinYear());
			accountsVO.setTotalDebitAmount(savedBankingWithdrawalVO.getTotalDebitAmount());
			accountsVO.setTotalCreditAmount(savedBankingWithdrawalVO.getTotalCreditAmount());
			accountsVO.setAmountInWords(
					amountInWordsConverterService.convert(savedBankingWithdrawalVO.getTotalDebitAmount().longValue()));

			// Create AccountsDetailsVO list and populate it
			List<AccountsDetailsVO> withdrawlDetailsVOs = new ArrayList<>();
			List<WithdrawalParticularsVO> withdrawlDetailsVOLists = savedBankingWithdrawalVO
					.getWithdrawalParticularsVO();
			for (WithdrawalParticularsVO particularsJournalVO : withdrawlDetailsVOLists) {
				GroupLedgerVO groupLedgerVO = groupLedgerRepo
						.findByAccountGroupName(particularsJournalVO.getAccountsName());

				AccountsDetailsVO accountsDetailsVO = new AccountsDetailsVO();
//				if (!particularsJournalVO.getDebitAmount().equals(BigDecimal.ZERO)) {
				// DebitAmount entry
				accountsDetailsVO.setACategory(groupLedgerVO.getCategory());
				accountsDetailsVO.setArapFlag(false);
				accountsDetailsVO.setSubledgerName("None");
				accountsDetailsVO.setSubLedgerCode("None");
				accountsDetailsVO.setAccountName(groupLedgerVO.getAccountGroupName());
				accountsDetailsVO.setACurrency(savedBankingWithdrawalVO.getCurrency());
				accountsDetailsVO.setAExRate(savedBankingWithdrawalVO.getExchangeRate());
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVO.setNDebitAmount(particularsJournalVO.getDebit());
				accountsDetailsVO.setDebitAmount(particularsJournalVO.getDebit());
				accountsDetailsVO.setBDebitAmount(particularsJournalVO.getDebit());
				accountsDetailsVO.setNCreditAmount(BigDecimal.ZERO);
				accountsDetailsVO.setCreditAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBCrAmount(BigDecimal.ZERO);
				accountsDetailsVO.setArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setNArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setGstflag(3);
				withdrawlDetailsVOs.add(accountsDetailsVO);

//				Credit Amount entry
				accountsDetailsVO.setACategory(groupLedgerVO.getCategory());
				accountsDetailsVO.setArapFlag(false);
				accountsDetailsVO.setSubledgerName("None");
				accountsDetailsVO.setSubLedgerCode("None");
				accountsDetailsVO.setAccountName(groupLedgerVO.getAccountGroupName());
				accountsDetailsVO.setACurrency(savedBankingWithdrawalVO.getCurrency());
				accountsDetailsVO.setAExRate(savedBankingWithdrawalVO.getExchangeRate());
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVO.setNDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO.setDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBDebitAmount(BigDecimal.ZERO);
				accountsDetailsVO.setNCreditAmount(particularsJournalVO.getCredit());
				accountsDetailsVO.setCreditAmount(particularsJournalVO.getCredit());
				accountsDetailsVO.setBCrAmount(particularsJournalVO.getCredit());
				accountsDetailsVO.setArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setBArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setNArapAmount(BigDecimal.ZERO);
				accountsDetailsVO.setGstflag(3);
				accountsDetailsVO.setAccountsVO(accountsVO);
				withdrawlDetailsVOs.add(accountsDetailsVO);
			}
			accountsVO.setAccountsDetailsVO(withdrawlDetailsVOs);
			accountsRepo.save(accountsVO);
		}

//		bankingWithdrawalRepo.save(bankingWithdrawalVO);
		Map<String, Object> response = new HashMap<>();
		response.put("bankingWithdrawalVO", bankingWithdrawalVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateBankingWithdrawalVOByBankingWithdrawalDTO(@Valid BankingWithdrawalDTO bankingWithdrawalDTO,
			BankingWithdrawalVO bankingWithdrawalVO) throws ApplicationException {
		bankingWithdrawalVO.setWithdrawalMode(bankingWithdrawalDTO.getWithdrawalMode());
		bankingWithdrawalVO.setPayTo(bankingWithdrawalDTO.getPayTo().toUpperCase());
		bankingWithdrawalVO.setChequeNo(bankingWithdrawalDTO.getChequeNo());
		bankingWithdrawalVO.setChequeDate(bankingWithdrawalDTO.getChequeDate());
		bankingWithdrawalVO.setChequeBank(bankingWithdrawalDTO.getChequeBank());
		bankingWithdrawalVO.setBankAccount(bankingWithdrawalDTO.getBankAccount());
		bankingWithdrawalVO.setCurrency(bankingWithdrawalDTO.getCurrency());
		bankingWithdrawalVO.setExchangeRate(bankingWithdrawalDTO.getExchangeRate());
		bankingWithdrawalVO.setWithdrawalAmount(bankingWithdrawalDTO.getWithdrawalAmount());

		bankingWithdrawalVO.setRemarks(bankingWithdrawalDTO.getRemarks());
		bankingWithdrawalVO.setOrgId(bankingWithdrawalDTO.getOrgId());
		bankingWithdrawalVO.setBranch(bankingWithdrawalDTO.getBranch());
		bankingWithdrawalVO.setBranchCode(bankingWithdrawalDTO.getBranchCode());
		bankingWithdrawalVO.setFinYear(bankingWithdrawalDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(bankingWithdrawalVO.getId())) {
			List<WithdrawalParticularsVO> withdrawalParticularsVOList = withdrawalParticularsRepo
					.findByBankingWithdrawalVO(bankingWithdrawalVO);
			withdrawalParticularsRepo.deleteAll(withdrawalParticularsVOList);
		}

		BigDecimal totalDebitAmount = BigDecimal.ZERO;
		BigDecimal totalCreditAmount = BigDecimal.ZERO;

		List<WithdrawalParticularsVO> withdrawalParticularsVOs = new ArrayList<>();
		for (WithdrawalParticularsDTO withdrawalParticularsDTO : bankingWithdrawalDTO.getWithdrawalParticularsDTO()) {
			WithdrawalParticularsVO withdrawalParticularsVO = new WithdrawalParticularsVO();

			withdrawalParticularsVO.setAccountsName(withdrawalParticularsDTO.getAccountsName());
			withdrawalParticularsVO.setNarration(withdrawalParticularsDTO.getNarration());
			if (withdrawalParticularsDTO.getDebit() != null
					&& withdrawalParticularsDTO.getDebit().compareTo(BigDecimal.ZERO) != 0) {
				withdrawalParticularsVO.setDebit(withdrawalParticularsDTO.getDebit());
				withdrawalParticularsVO.setCredit(BigDecimal.ZERO);
			} else {
				withdrawalParticularsVO.setCredit(withdrawalParticularsDTO.getCredit());
				withdrawalParticularsVO.setDebit(BigDecimal.ZERO);
			}
			totalCreditAmount = totalCreditAmount.add(withdrawalParticularsVO.getCredit());
			totalDebitAmount = totalDebitAmount.add(withdrawalParticularsVO.getDebit());

			withdrawalParticularsVO.setBankingWithdrawalVO(bankingWithdrawalVO);
			withdrawalParticularsVOs.add(withdrawalParticularsVO);
		}
		if (totalCreditAmount.equals(totalDebitAmount)) {
			bankingWithdrawalVO.setTotalCreditAmount(totalCreditAmount);
			bankingWithdrawalVO.setTotalDebitAmount(totalDebitAmount);
			bankingWithdrawalVO.setTotalAmount(bankingWithdrawalDTO.getWithdrawalAmount());

		} else {
			throw new ApplicationException("Total Debit Amount and Total Credit Amount Should be Equal");
		}
		bankingWithdrawalVO.setWithdrawalParticularsVO(withdrawalParticularsVOs);

	}

	@Override
	public List<Map<String, Object>> getSalesPersonFromPartyMaster(Long orgId, String partyName) {
		Set<Object[]> getSalePerson = tmsJobCardRepo.findBySalesPreson(orgId, partyName);
		return getSalePersons(getSalePerson);
	}

	private List<Map<String, Object>> getSalePersons(Set<Object[]> getSalePerson) {
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Object[] ch : getSalePerson) {
			Map<String, Object> map = new HashMap<>();
			map.put("salesperson", ch[0] != null ? ch[0].toString() : "");
			list1.add(map);
		}
		return list1;
	}

	@Override
	public List<Map<String, Object>> getAllCustomersFromPartyMaster(Long orgId) {
		Set<Object[]> getCustomer = tmsJobCardRepo.findAllCustomers(orgId);
		return getCustomers(getCustomer);
	}

	private List<Map<String, Object>> getCustomers(Set<Object[]> getCustomer) {
		List<Map<String, Object>> list1 = new ArrayList<>();
		for (Object[] ch : getCustomer) {
			Map<String, Object> map = new HashMap<>();
			map.put("partyname", ch[0] != null ? ch[0].toString() : "");
			list1.add(map);
		}
		return list1;
	}

	// ContraVoucher

	@Override
	public List<ContraVoucherVO> getAllContraVoucherByOrgId(Long orgId) {
		List<ContraVoucherVO> contraVoucherVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(orgId)) {
			LOGGER.info("Successfully Received  ContraVoucher BY OrgId : {}", orgId);
			contraVoucherVO = contraVoucherRepo.getAllContraVoucherByOrgId(orgId);
		}
		return contraVoucherVO;
	}

	@Override
	public List<ContraVoucherVO> getContraVoucherById(Long id) {
		List<ContraVoucherVO> contraVoucherVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  ContraVoucher BY Id : {}", id);
			contraVoucherVO = contraVoucherRepo.getAllContraVoucherById(id);
		}
		return contraVoucherVO;
	}

	@Override
	public String getContraVoucherDocId(Long orgId, String finYear, String branch, String branchCode) {
		String ScreenCode = "CV";
		String result = contraVoucherRepo.getContraVoucherDocId(orgId, finYear, branchCode, ScreenCode);
		return result;
	}

	@Override
	public Map<String, Object> updateCreateContraVoucher(@Valid ContraVoucherDTO contraVoucherDTO)
			throws ApplicationException {
		String screenCode = "CV";
		ContraVoucherVO contraVoucherVO = new ContraVoucherVO();
		String message;
		if (ObjectUtils.isNotEmpty(contraVoucherDTO.getId())) {
			contraVoucherVO = contraVoucherRepo.findById(contraVoucherDTO.getId())
					.orElseThrow(() -> new ApplicationException("ContraVoucher not found"));

			contraVoucherVO.setUpdatedBy(contraVoucherDTO.getCreatedBy());
			createUpdateContraVoucherVOByContraVoucherDTO(contraVoucherDTO, contraVoucherVO);
			message = "ContraVoucher Updated Successfully";
		} else {
			// GETDOCID API
			String docId = contraVoucherRepo.getContraVoucherByDocId(contraVoucherDTO.getOrgId(),
					contraVoucherDTO.getFinYear(), contraVoucherDTO.getBranchCode(), screenCode);

			contraVoucherVO.setDocId(docId);

//						// GETDOCID LASTNO +1
			DocumentTypeMappingDetailsVO documentTypeMappingDetailsVO = documentTypeMappingDetailsRepo
					.findByOrgIdAndFinYearAndBranchCodeAndScreenCode(contraVoucherDTO.getOrgId(),
							contraVoucherDTO.getFinYear(), contraVoucherDTO.getBranchCode(), screenCode);
			documentTypeMappingDetailsVO.setLastno(documentTypeMappingDetailsVO.getLastno() + 1);
			documentTypeMappingDetailsRepo.save(documentTypeMappingDetailsVO);

			contraVoucherVO.setCreatedBy(contraVoucherDTO.getCreatedBy());
			contraVoucherVO.setUpdatedBy(contraVoucherDTO.getCreatedBy());
			createUpdateContraVoucherVOByContraVoucherDTO(contraVoucherDTO, contraVoucherVO);
			message = "ContraVoucher Created Successfully";
		}

		contraVoucherRepo.save(contraVoucherVO);
		Map<String, Object> response = new HashMap<>();
		response.put("contraVoucherVO", contraVoucherVO);
		response.put("message", message);
		return response;
	}

	private void createUpdateContraVoucherVOByContraVoucherDTO(@Valid ContraVoucherDTO contraVoucherDTO,
			ContraVoucherVO contraVoucherVO) throws ApplicationException {
		contraVoucherVO.setReferenceNo(contraVoucherDTO.getReferenceNo());
		contraVoucherVO.setReferenceDate(contraVoucherDTO.getReferenceDate());
		contraVoucherVO.setChequeNo(contraVoucherDTO.getChequeNo());
		contraVoucherVO.setChequeDate(contraVoucherDTO.getChequeDate());
		contraVoucherVO.setCurrency(contraVoucherDTO.getCurrency());
		contraVoucherVO.setExRate(contraVoucherDTO.getExRate());

		contraVoucherVO.setRemarks(contraVoucherDTO.getRemarks());
		contraVoucherVO.setOrgId(contraVoucherDTO.getOrgId());
		contraVoucherVO.setBranch(contraVoucherDTO.getBranch());
		contraVoucherVO.setBranchCode(contraVoucherDTO.getBranchCode());
		contraVoucherVO.setFinYear(contraVoucherDTO.getFinYear());

		if (ObjectUtils.isNotEmpty(contraVoucherVO.getId())) {
			List<ContraVoucherParticularsVO> ContraVoucherParticularsVOList = contraVoucherParticularsRepo
					.findByContraVoucherVO(contraVoucherVO);
			contraVoucherParticularsRepo.deleteAll(ContraVoucherParticularsVOList);
		}

		BigDecimal totalDebitAmount = BigDecimal.ZERO;
		BigDecimal totalCreditAmount = BigDecimal.ZERO;

		List<ContraVoucherParticularsVO> contraVoucherParticularsVOs = new ArrayList<>();
		for (ContraVoucherParticularsDTO contraVoucherParticularsDTO : contraVoucherDTO
				.getContraVoucherParticularsDTO()) {
			ContraVoucherParticularsVO contraVoucherParticularsVO = new ContraVoucherParticularsVO();

			contraVoucherParticularsVO.setAccountsName(contraVoucherParticularsDTO.getAccountsName());
			contraVoucherParticularsVO.setNarration(contraVoucherParticularsDTO.getNarration());
			contraVoucherParticularsVO.setSubLedgerCode(contraVoucherParticularsDTO.getSubLedgerCode());
			contraVoucherParticularsVO.setSubledgerName(contraVoucherParticularsDTO.getSubledgerName());

			if (contraVoucherParticularsDTO.getDebit() != null
					&& contraVoucherParticularsDTO.getDebit().compareTo(BigDecimal.ZERO) != 0) {
				contraVoucherParticularsVO.setDebit(contraVoucherParticularsDTO.getDebit());
				contraVoucherParticularsVO.setCredit(BigDecimal.ZERO);
			} else {
				contraVoucherParticularsVO.setCredit(contraVoucherParticularsDTO.getCredit());
				contraVoucherParticularsVO.setDebit(BigDecimal.ZERO);
			}
			totalCreditAmount = totalCreditAmount.add(contraVoucherParticularsVO.getCredit());
			totalDebitAmount = totalDebitAmount.add(contraVoucherParticularsVO.getDebit());

			contraVoucherParticularsVO.setContraVoucherVO(contraVoucherVO);
			contraVoucherParticularsVOs.add(contraVoucherParticularsVO);
		}
		if (totalCreditAmount.equals(totalDebitAmount)) {
			contraVoucherVO.setTotalCreditAmount(totalCreditAmount);
			contraVoucherVO.setTotalDebitAmount(totalDebitAmount);

		} else {
			throw new ApplicationException("Total Debit Amount and Total Credit Amount Should be Equal");
		}
		contraVoucherVO.setContraVoucherParticularsVO(contraVoucherParticularsVOs);

	}

	@Override
	@Transactional
	public List<Map<String, Object>> getAccountNamefromGroupLedgerforCV(Long orgId) {

		Set<Object[]> result = contraVoucherRepo.findAccountNamefromGroupLedgerforCV(orgId);
		return getAccountNamefromGroupLedgerforCV(result);
	}

	private List<Map<String, Object>> getAccountNamefromGroupLedgerforCV(Set<Object[]> result) {
		List<Map<String, Object>> details1 = new ArrayList<>();
		for (Object[] fs : result) {
			Map<String, Object> part = new HashMap<>();
			part.put("accountName", fs[0] != null ? fs[0].toString() : "");
			part.put("accountCode", fs[1] != null ? fs[1].toString() : "");
			part.put("category", fs[2] != null ? fs[2].toString() : "");
			part.put("currency", fs[3] != null ? fs[3].toString() : "");

			details1.add(part);
		}
		return details1;
	}

}
