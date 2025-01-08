package com.base.basesetup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.CityDTO;
import com.base.basesetup.dto.CompanyDTO;
import com.base.basesetup.dto.CountryDTO;
import com.base.basesetup.dto.CurrencyDTO;
import com.base.basesetup.dto.FinScreenDTO;
import com.base.basesetup.dto.FinancialYearDTO;
import com.base.basesetup.dto.RegionDTO;
import com.base.basesetup.dto.Role;
import com.base.basesetup.dto.ScreenNamesDTO;
import com.base.basesetup.dto.StateDTO;
import com.base.basesetup.entity.CityVO;
import com.base.basesetup.entity.CompanyVO;
import com.base.basesetup.entity.CountryVO;
import com.base.basesetup.entity.CurrencyVO;
import com.base.basesetup.entity.EmployeeVO;
import com.base.basesetup.entity.FinancialYearVO;
import com.base.basesetup.entity.RegionVO;
import com.base.basesetup.entity.ScreenNamesVO;
import com.base.basesetup.entity.StateVO;
import com.base.basesetup.entity.UserVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.CityRepo;
import com.base.basesetup.repo.CompanyRepo;
import com.base.basesetup.repo.CountryRepo;
import com.base.basesetup.repo.CurrencyRepo;
import com.base.basesetup.repo.EmployeeRepo;
import com.base.basesetup.repo.FinScreenRepo;
import com.base.basesetup.repo.FinancialYearRepo;
import com.base.basesetup.repo.RegionRepo;
import com.base.basesetup.repo.ResponsibilitiesRepo;
import com.base.basesetup.repo.RoleRepo;
import com.base.basesetup.repo.ScreenNamesRepo;
import com.base.basesetup.repo.StateRepo;
import com.base.basesetup.repo.UserRepo;
import com.base.basesetup.util.CryptoUtils;

@Service
public class CommonMasterServiceImpl implements CommonMasterService {

	public static final Logger LOGGER = LoggerFactory.getLogger(CommonMasterServiceImpl.class);

	@Autowired
	CountryRepo countryRepo;

	@Autowired
	CurrencyRepo currencyRepo;

	@Autowired
	StateRepo stateRepo;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	CityRepo cityRepo;

	@Autowired
	RegionRepo regionRepo;

	@Autowired
	CompanyRepo companyRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	FinancialYearRepo financialYearRepo;

	@Autowired
	RoleRepo roleRepo;

	@Autowired
	ResponsibilitiesRepo responsibilitiesRepo;

	@Autowired
	FinScreenRepo finScreenRepo;

	@Autowired
	ScreenNamesRepo screenNamesRepo;

	// Company

	@Override
	public List<CompanyVO> getAllCompany() {
		return companyRepo.findAll();
	}

	@Override
	public List<CompanyVO> getCompanyById(Long companyid) {
		return companyRepo.findByCompany(companyid);
	}

	@Override
	@Transactional
	public CompanyVO createCompany(CompanyDTO companyDTO) throws Exception {

		if (companyRepo.existsByCompanyCodeAndCompanyNameAndEmployeeCodeAndEmailAndPhoneAndId(
				companyDTO.getCompanyCode(), companyDTO.getCompanyName(), companyDTO.getEmployeeCode(),
				companyDTO.getEmail(), companyDTO.getPhone(), companyDTO.getId())) {

			String errorMessage = String.format(
					"The CompanyCode : %s And CompanyName : %s And EmployeeCode : %s And Email : %s And PhoneNumber : %s Already Exists"
							+ "This Organization",
					companyDTO.getCompanyCode(), companyDTO.getCompanyName(), companyDTO.getEmployeeCode(),
					companyDTO.getEmail(), companyDTO.getPhone());
			throw new ApplicationException(errorMessage);
		}

		if (companyRepo.existsByCompanyCodeAndId(companyDTO.getCompanyCode(), companyDTO.getId())) {

			String errorMessage = String.format("The CompanyCode : %s Already Exists This Organization",
					companyDTO.getCompanyCode());
			throw new ApplicationException(errorMessage);
		}

		if (companyRepo.existsByCompanyNameAndId(companyDTO.getCompanyName(), companyDTO.getId())) {
			String errorMessage = String.format("The CompanyName : %s Already Exists This Organization",
					companyDTO.getCompanyName());
			throw new ApplicationException(errorMessage);
		}

		if (companyRepo.existsByEmployeeCodeAndId(companyDTO.getEmployeeCode(), companyDTO.getId())) {
			String errorMessage = String.format("The EmployeeCode : %s Already Exists This Organization",
					companyDTO.getEmployeeCode());
			throw new ApplicationException(errorMessage);
		}

		if (companyRepo.existsByEmailAndId(companyDTO.getEmail(), companyDTO.getId())) {
			String errorMessage = String.format("The Email : %s Already Exists This Organization",
					companyDTO.getEmail());
			throw new ApplicationException(errorMessage);
		}

		if (companyRepo.existsByPhoneAndId(companyDTO.getPhone(), companyDTO.getId())) {
			String errorMessage = String.format("The PhoneNumber : %s Already Exists This Organization",
					companyDTO.getPhone());
			throw new ApplicationException(errorMessage);
		}

		CompanyVO companyVO = new CompanyVO();
		getCompanyVOFromCompanyDTO(companyVO, companyDTO);
		companyRepo.save(companyVO);

		EmployeeVO employeeVO = new EmployeeVO();
		employeeVO.setEmployeeName(companyVO.getEmployeeName());
		employeeVO.setEmployeeCode(companyVO.getEmployeeCode());
		employeeVO.setActive(true);
		employeeVO.setOrgId(companyVO.getId());
		employeeRepo.save(employeeVO);

		UserVO userVO = new UserVO();
		userVO.setUserName(companyVO.getEmployeeCode());
		userVO.setEmployeeName(companyVO.getEmployeeName());
		userVO.setEmployeeCode(companyVO.getEmployeeCode());
		userVO.setEmail(companyVO.getEmail());
		userVO.setMobileNo(companyVO.getPhone());
		userVO.setRole(Role.ROLE_USER);
		userVO.setUserType("admin");
		userVO.setOrgId(companyVO.getId());
		userVO.setCreatedby(companyVO.getCreatedBy());
		userVO.setUpdatedby(companyVO.getCreatedBy());
		userVO.setActive(true);
		userVO.setLoginStatus(false);
		userVO.setCompanyVO(companyVO);

		try {
			userVO.setPassword(encoder.encode(CryptoUtils.getDecrypt(companyDTO.getPassword())));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationContextException("Unable To Encode Password");
		}

		userRepo.save(userVO);

		return companyVO;
	}

	private void getCompanyVOFromCompanyDTO(CompanyVO companyVO, CompanyDTO companyDTO) {
		companyVO.setCompanyCode(companyDTO.getCompanyCode());
		companyVO.setCompanyName(companyDTO.getCompanyName());
		companyVO.setCountry(companyDTO.getCountry());
		companyVO.setCurrency(companyDTO.getCurrency());
		companyVO.setMainCurrency(companyDTO.getMainCurrency());
		companyVO.setAddress(companyDTO.getAddress());
		companyVO.setZip(companyDTO.getZip());
		companyVO.setCity(companyDTO.getCity());
		companyVO.setState(companyDTO.getState());
		companyVO.setPhone(companyDTO.getPhone());
		companyVO.setEmail(companyDTO.getEmail());
		companyVO.setWebSite(companyDTO.getWebSite());
		companyVO.setNote(companyDTO.getNote());
		companyVO.setEmployeeCode(companyDTO.getEmployeeCode());
		companyVO.setEmployeeName(companyDTO.getEmployeeName());
		companyVO.setCreatedBy(companyDTO.getCreatedBy());
		companyVO.setUpdatedBy(companyDTO.getCreatedBy());
		companyVO.setActive(companyDTO.isActive());
		companyVO.setCancel(companyDTO.isCancel());
		companyVO.setGst(companyDTO.getGst());
		companyVO.setCeo(companyDTO.getCeo());

		try {
			companyVO.setPassword(encoder.encode(CryptoUtils.getDecrypt(companyDTO.getPassword())));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ApplicationContextException("Unable To Encode Password");
		}
	}

	public CompanyVO updateCompany(CompanyDTO companyDTO) throws ApplicationException {

		if (ObjectUtils.isEmpty(companyDTO.getId())) {
			throw new ApplicationException("Invalid Company Id");
		}

		CompanyVO companyVO = companyRepo.findById(companyDTO.getId())
				.orElseThrow(() -> new ApplicationException("Company not found for Id: " + companyDTO.getId()));

		mapCompanyDTOToCompanyVO(companyVO, companyDTO);

		return companyRepo.save(companyVO);
	}

	private void mapCompanyDTOToCompanyVO(CompanyVO companyVO, CompanyDTO companyDTO) {
		companyVO.setCompanyCode(companyDTO.getCompanyCode());
		companyVO.setCompanyName(companyDTO.getCompanyName());
		companyVO.setCountry(companyDTO.getCountry());
		companyVO.setCurrency(companyDTO.getCurrency());
		companyVO.setMainCurrency(companyDTO.getMainCurrency());
		companyVO.setAddress(companyDTO.getAddress());
		companyVO.setZip(companyDTO.getZip());
		companyVO.setCity(companyDTO.getCity());
		companyVO.setState(companyDTO.getState());
		companyVO.setPhone(companyDTO.getPhone());
		companyVO.setEmail(companyDTO.getEmail());
		companyVO.setWebSite(companyDTO.getWebSite());
		companyVO.setNote(companyDTO.getNote());
//			companyVO.setEmployeeCode(companyDTO.getEmployeeCode());
//			companyVO.setEmployeeName(companyDTO.getEmployeeName());
		companyVO.setCreatedBy(companyDTO.getCreatedBy());
		companyVO.setUpdatedBy(companyDTO.getUpdatedBy());
		companyVO.setActive(companyDTO.isActive());
		companyVO.setCancel(companyDTO.isCancel());
		companyVO.setRole(companyDTO.getRole());
		companyVO.setGst(companyDTO.getGst());
		companyVO.setCeo(companyDTO.getCeo());
	}

	@Override
	public void deleteCompany(Long companyid) {
		companyRepo.deleteById(companyid);
	}

//	@Override
//	public CompanyVO saveImage(MultipartFile file, @RequestParam Long id)
//			throws ApplicationException, java.io.IOException {
//
//		CompanyVO image = companyRepo.findById(id)
//				.orElseThrow(() -> new ApplicationException("Invalid company id" + id));
//
//		image.setImageName(file.getOriginalFilename());
//		image.setData(file.getBytes());
//		return companyRepo.save(image);
//
//	}

	// FinScreen-----------------------------------------------------------------------------------
	@Override
	public List<ScreenNamesVO> getFinScreenById(Long id) {
		List<ScreenNamesVO> finScreenVO = new ArrayList<>();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received FinScreen BY Id : {}", id);
			finScreenVO = screenNamesRepo.findFinScreenById(id);
		} else {
			LOGGER.info("Successfully Received FinScreen For All Id.");
			finScreenVO = screenNamesRepo.findAll();
		}
		return finScreenVO;
	}

	@Override
	public ScreenNamesVO updateCreateFinScreen(@Valid FinScreenDTO finScreenDTO) throws ApplicationException {
		ScreenNamesVO finScreenVO = new ScreenNamesVO();
		boolean isUpdate = false;
		if (ObjectUtils.isNotEmpty(finScreenDTO.getId())) {
			isUpdate = true;
			finScreenVO = screenNamesRepo.findById(finScreenDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid FinScreen Details"));
			finScreenVO.setUpdatedBy(finScreenDTO.getCreatedBy());

		} else {
			if (screenNamesRepo.existsByScreenName(finScreenDTO.getScreenName())) {
				throw new ApplicationException("The given Screen name already exists.");
			}
			if (screenNamesRepo.existsByScreenCode(finScreenDTO.getScreenCode())) {
				throw new ApplicationException("The given Screen code already exists.");
			}
			finScreenVO.setUpdatedBy(finScreenDTO.getCreatedBy());
			finScreenVO.setCreatedBy(finScreenDTO.getCreatedBy());
		}

		// update check
		if (isUpdate) {
			ScreenNamesVO finScreen = screenNamesRepo.findById(finScreenDTO.getId()).orElse(null);
			if (!finScreen.getScreenName().equalsIgnoreCase(finScreenDTO.getScreenName())) {
				if (screenNamesRepo.existsByScreenName(finScreenDTO.getScreenName())) {
					throw new ApplicationException("The given Screen name already exists.");
				}
			}
			if (!finScreen.getScreenCode().equals(finScreenDTO.getScreenCode())) {
				if (screenNamesRepo.existsByScreenCode(finScreenDTO.getScreenCode())) {
					throw new ApplicationException("The given Screen code already exists");
				}
			}
		}
		getFinScreenVOFromFinScreenDTO(finScreenDTO, finScreenVO);
		return screenNamesRepo.save(finScreenVO);
	}

	private void getFinScreenVOFromFinScreenDTO(@Valid FinScreenDTO finScreenDTO, ScreenNamesVO finScreenVO)
			throws ApplicationException {

		finScreenVO.setActive(finScreenDTO.isActive());
		finScreenVO.setScreenCode(finScreenDTO.getScreenCode());
		finScreenVO.setScreenName(finScreenDTO.getScreenName());
	}

	@Override
	public List<Map<String, Object>> getAllScreenCode(Long orgId) {
		Set<Object[]> getFinScreen = screenNamesRepo.findAllScreenCode(orgId);
		return getScreen(getFinScreen);
	}

	private List<Map<String, Object>> getScreen(Set<Object[]> getFinScreen) {
		List<Map<String, Object>> finScreenList = new ArrayList<>();

		for (Object[] finScreen : getFinScreen) {
			Map<String, Object> branchMap = new HashMap<>();
			branchMap.put("screenCode", finScreen[0] != null ? finScreen[0].toString() : "");
			branchMap.put("screenName", finScreen[1] != null ? finScreen[1].toString() : "");
			finScreenList.add(branchMap);
		}
		return finScreenList;
	}

	// Country

	@Override
	public List<CountryVO> getAllCountry(Long orgid) {
		return countryRepo.findAll(orgid);
	}

	@Override
	public Optional<CountryVO> getCountryById(Long countryid) {
		return countryRepo.findById(countryid);
	}

	@Override
	public Map<String, Object> createUpdateCountry(CountryDTO countryDTO) throws ApplicationException {

		CountryVO countryVO;
		String message = null;

		if (ObjectUtils.isEmpty(countryDTO.getId())) {
			if (countryRepo.existsByCountryNameAndCountryCodeAndOrgId(countryDTO.getCountryName(),
					countryDTO.getCountryCode(), countryDTO.getOrgId())) {
				String errorMessage = String.format(
						"The CountryName: %s and CountryCode: %s already exists This Organization.",
						countryDTO.getCountryName(), countryDTO.getCountryCode());
				throw new ApplicationException(errorMessage);
			}

			if (countryRepo.existsByCountryNameAndOrgId(countryDTO.getCountryName(), countryDTO.getOrgId())) {
				String errorMessage = String.format("The CountryName: %s already exists This Organization.",
						countryDTO.getCountryName());
				throw new ApplicationException(errorMessage);
			}

			if (countryRepo.existsByCountryCodeAndOrgId(countryDTO.getCountryCode(), countryDTO.getOrgId())) {
				String errorMessage = String.format("The CountryCode: %s already exists This Organization.",
						countryDTO.getCountryCode());
				throw new ApplicationException(errorMessage);
			}
			// Create new branch
			countryVO = new CountryVO();
			countryVO.setCreatedBy(countryDTO.getCreatedBy());
			countryVO.setUpdatedBy(countryDTO.getCreatedBy());
			message = "Country Creation SuccessFully";
		} else {
			// Update existing branch
			countryVO = countryRepo.findById(countryDTO.getId())
					.orElseThrow(() -> new ApplicationException("Branch not found with id: " + countryDTO.getId()));
			countryVO.setUpdatedBy(countryDTO.getCreatedBy());
			if (!countryVO.getCountryCode().equalsIgnoreCase(countryDTO.getCountryCode())) {
				if (countryRepo.existsByCountryCodeAndOrgId(countryDTO.getCountryCode(), countryDTO.getOrgId())) {
					String errorMessage = String.format("The CountryCode: %s already exists This Organization.",
							countryDTO.getCountryCode());
					throw new ApplicationException(errorMessage);
				}
				countryVO.setCountryCode(countryDTO.getCountryCode().toUpperCase());
			}
			if (!countryVO.getCountryName().equalsIgnoreCase(countryDTO.getCountryName())) {
				if (countryRepo.existsByCountryNameAndOrgId(countryDTO.getCountryName(), countryDTO.getOrgId())) {
					String errorMessage = String.format("The CountryName: %s already exists This Organization.",
							countryDTO.getCountryName());
					throw new ApplicationException(errorMessage);
				}
				countryVO.setCountryName(countryDTO.getCountryName().toUpperCase());

			}
			message = "Country Update Successfully";
		}

		getCountryVOFromCounytryDTO(countryVO, countryDTO);
		countryRepo.save(countryVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("message", message);
		response.put("countryVO", countryVO);
		return response;

	}

	private void getCountryVOFromCounytryDTO(CountryVO countryVO, CountryDTO countryDTO) {
		countryVO.setCountryName(countryDTO.getCountryName().toUpperCase());
		countryVO.setCountryCode(countryDTO.getCountryCode().toUpperCase());
		countryVO.setActive(countryDTO.isActive());
		countryVO.setOrgId(countryDTO.getOrgId());
		countryVO.setCancel(countryDTO.isCancel());

	}

	@Override
	public void deleteCountry(Long countryid) {

		countryRepo.deleteById(countryid);
	}

	// State

	@Override
	public List<StateVO> getAllgetAllStates(Long orgid) {
		return stateRepo.findAllByOrgId(orgid);
	}

	@Override
	public Optional<StateVO> getStateById(Long stateid) {
		return stateRepo.findById(stateid);
	}

	@Override
	public List<StateVO> getStatesByCountry(Long orgid, String country) {

		return stateRepo.findByCountry(orgid, country);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateState(StateDTO stateDTO) throws ApplicationException {
		StateVO stateVO;
		String message = null;

		if (ObjectUtils.isEmpty(stateDTO.getId())) {
			// Check for existing state by state code, state number, and state name
			if (stateRepo.existsByStateCodeAndOrgId(stateDTO.getStateCode(), stateDTO.getOrgId())) {
				String errorMessage = String.format("The StateCode: %s already exists in This Organization.",
						stateDTO.getStateCode());
				throw new ApplicationException(errorMessage);
			}
			if (stateRepo.existsByStateNumberAndOrgId(stateDTO.getStateNumber(), stateDTO.getOrgId())) {
				String errorMessage = String.format("The StateNumber: %s already exists in This Organization.",
						stateDTO.getStateNumber());
				throw new ApplicationException(errorMessage);
			}
			if (stateRepo.existsByStateNameAndOrgId(stateDTO.getStateName(), stateDTO.getOrgId())) {
				String errorMessage = String.format("The StateName: %s already exists in This Organization.",
						stateDTO.getStateName());
				throw new ApplicationException(errorMessage);
			}

			// Create new state
			stateVO = new StateVO();
			stateVO.setCreatedBy(stateDTO.getCreatedBy());
			stateVO.setUpdatedBy(stateDTO.getCreatedBy());
			message = "State Creation Successfully";
		} else {
			// Update existing state
			stateVO = stateRepo.findById(stateDTO.getId())
					.orElseThrow(() -> new ApplicationException("State not found with id: " + stateDTO.getId()));

			stateVO.setUpdatedBy(stateDTO.getCreatedBy());

			if (!stateVO.getStateCode().equalsIgnoreCase(stateDTO.getStateCode())) {
				if (stateRepo.existsByStateCodeAndOrgId(stateDTO.getStateCode(), stateDTO.getOrgId())) {
					String errorMessage = String.format("The StateCode: %s already exists in This Organization.",
							stateDTO.getStateCode());
					throw new ApplicationException(errorMessage);
				}
				stateVO.setStateCode(stateDTO.getStateCode().toUpperCase());
			}

			if (!stateVO.getStateName().equalsIgnoreCase(stateDTO.getStateName())) {
				if (stateRepo.existsByStateNameAndOrgId(stateDTO.getStateName(), stateDTO.getOrgId())) {
					String errorMessage = String.format("The StateName: %s already exists in This Organization.",
							stateDTO.getStateName());
					throw new ApplicationException(errorMessage);
				}
				stateVO.setStateName(stateDTO.getStateName().toUpperCase());
			}

			if (!stateVO.getStateNumber().equalsIgnoreCase(stateDTO.getStateNumber())) {
				if (stateRepo.existsByStateNumberAndOrgId(stateDTO.getStateNumber(), stateDTO.getOrgId())) {
					String errorMessage = String.format("The StateNumber: %s already exists in This Organization.",
							stateDTO.getStateNumber());
					throw new ApplicationException(errorMessage);
				}
				stateVO.setStateNumber(stateDTO.getStateNumber().toUpperCase());
			}

			message = "State Update Successfully";
		}

		// Map the remaining fields
		getStateVOFromStateDTO(stateVO, stateDTO);

		// Save the entity
		stateRepo.save(stateVO);

		// Prepare the response
		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("stateVO", stateVO);

		return response;
	}

	private void getStateVOFromStateDTO(StateVO stateVO, StateDTO stateDTO) {
		stateVO.setStateCode(stateDTO.getStateCode().toUpperCase());
		stateVO.setStateName(stateDTO.getStateName().toUpperCase());
		stateVO.setStateNumber(stateDTO.getStateNumber().toUpperCase());
		stateVO.setCountry(stateDTO.getCountry().toUpperCase());
		stateVO.setRegion(stateDTO.getRegion().toUpperCase());
		stateVO.setActive(stateDTO.isActive());
		stateVO.setCancel(stateDTO.isCancel());
		stateVO.setOrgId(stateDTO.getOrgId());
		// stateVO.setDupchk(stateDTO.getOrgId() + stateDTO.getStateCode() +
		// stateDTO.getStateName());
	}

	@Override
	public void deleteState(Long countryid) {
		stateRepo.deleteById(countryid);
	}

	// City

	@Override
	public List<CityVO> getAllgetAllCities(Long orgid) {
		return cityRepo.findAll(orgid);
	}

	@Override
	public List<CityVO> getAllCitiesByState(Long orgid, String state) {

		return cityRepo.findAll(orgid, state);
	}

	@Override
	public Optional<CityVO> getCityById(Long cityid) {
		return cityRepo.findById(cityid);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateCity(CityDTO cityDTO) throws ApplicationException {
		CityVO cityVO;
		String message;

		if (ObjectUtils.isEmpty(cityDTO.getId())) {
			if (cityRepo.existsByCityCodeAndOrgId(cityDTO.getCityCode(), cityDTO.getOrgId())) {
				String errorMessage = String.format("The CityCode: %s already exists in this organization.",
						cityDTO.getCityCode());
				throw new ApplicationException(errorMessage);
			}
			if (cityRepo.existsByCityNameAndOrgId(cityDTO.getCityName(), cityDTO.getOrgId())) {
				String errorMessage = String.format("The CityName: %s already exists in this organization.",
						cityDTO.getCityName());
				throw new ApplicationException(errorMessage);
			}
			// Create new city
			cityVO = new CityVO();
			cityVO.setCreatedBy(cityDTO.getCreatedBy());
			cityVO.setUpdatedBy(cityDTO.getCreatedBy());
			message = "City Created Successfully";
		} else {
			// Update existing city
			cityVO = cityRepo.findById(cityDTO.getId())
					.orElseThrow(() -> new ApplicationException("City not found with id: " + cityDTO.getId()));
			cityVO.setUpdatedBy(cityDTO.getCreatedBy());

			if (!cityVO.getCityCode().equalsIgnoreCase(cityDTO.getCityCode())) {
				if (cityRepo.existsByCityCodeAndOrgId(cityDTO.getCityCode(), cityDTO.getOrgId())) {
					String errorMessage = String.format("The CityCode: %s already exists in this organization.",
							cityDTO.getCityCode());
					throw new ApplicationException(errorMessage);
				}
				cityVO.setCityCode(cityDTO.getCityCode().toUpperCase());
			}

			if (!cityVO.getCityName().equalsIgnoreCase(cityDTO.getCityName())) {
				if (cityRepo.existsByCityNameAndOrgId(cityDTO.getCityName(), cityDTO.getOrgId())) {
					String errorMessage = String.format("The CityName: %s already exists in this organization.",
							cityDTO.getCityName());
					throw new ApplicationException(errorMessage);
				}
				cityVO.setCityName(cityDTO.getCityName().toUpperCase());
			}
			message = "City Updated Successfully";
		}

		getCityVOFromCityDTO(cityVO, cityDTO);
		cityRepo.save(cityVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("cityVO", cityVO);
		return response;
	}

	private void getCityVOFromCityDTO(CityVO cityVO, CityDTO cityDTO) {
		cityVO.setCityCode(cityDTO.getCityCode().toUpperCase());
		cityVO.setCityName(cityDTO.getCityName().toUpperCase());
		cityVO.setCountry(cityDTO.getCountry().toUpperCase());
		cityVO.setState(cityDTO.getState().toUpperCase());
		cityVO.setActive(cityDTO.isActive());
		cityVO.setOrgId(cityDTO.getOrgId());
		cityVO.setCancel(cityDTO.isCancel());
	}

	@Override
	public void deleteCity(Long cityid) {
		cityRepo.deleteById(cityid);
	}

	// Region

	@Override
	public List<RegionVO> getAllRegios() {

		return regionRepo.findAll();
	}

	@Override
	public List<RegionVO> getAllRegionsByOrgId(Long orgId) {
		return regionRepo.findAll(orgId);
	}

	@Override
	public Optional<RegionVO> getRegionById(Long Regionid) {
		return regionRepo.findById(Regionid);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateRegion(RegionDTO regionDTO) throws ApplicationException {
		RegionVO regionVO;
		String message;

		if (ObjectUtils.isEmpty(regionDTO.getId())) {
			if (regionRepo.existsByRegionNameAndOrgId(regionDTO.getRegionName(), regionDTO.getOrgId())) {
				String errorMessage = String.format("This RegionName:%s Already Exists in This Organization",
						regionDTO.getRegionName().toUpperCase());
				throw new ApplicationException(errorMessage);
			}
			if (regionRepo.existsByRegionCodeAndOrgId(regionDTO.getRegionCode(), regionDTO.getOrgId())) {
				String errorMessage = String.format("This RegionCode:%s Already Exists in This Organization",
						regionDTO.getRegionCode().toUpperCase());
				throw new ApplicationException(errorMessage);
			}
			// Create new region
			regionVO = new RegionVO();
			regionVO.setCreatedBy(regionDTO.getCreatedBy());
			regionVO.setUpdatedBy(regionDTO.getCreatedBy());
			message = "Region Created Successfully";
		} else {
			// Update existing region
			regionVO = regionRepo.findById(regionDTO.getId()).orElseThrow(
					() -> new ApplicationException("This Id Is Not Found Any Information: " + regionDTO.getId()));
			regionVO.setUpdatedBy(regionDTO.getCreatedBy());

			if (!regionVO.getRegionName().equalsIgnoreCase(regionDTO.getRegionName())) {
				if (regionRepo.existsByRegionNameAndOrgId(regionDTO.getRegionName(), regionDTO.getOrgId())) {
					String errorMessage = String.format("This RegionName:%s Already Exists in This Organization",
							regionDTO.getRegionName());
					throw new ApplicationException(errorMessage);
				}
				regionVO.setRegionName(regionDTO.getRegionName().toUpperCase());
			}

			if (!regionVO.getRegionCode().equalsIgnoreCase(regionDTO.getRegionCode())) {
				if (regionRepo.existsByRegionCodeAndOrgId(regionDTO.getRegionCode(), regionDTO.getOrgId())) {
					String errorMessage = String.format("This RegionCode:%s Already Exists in This Organization",
							regionDTO.getRegionCode());
					throw new ApplicationException(errorMessage);
				}
				regionVO.setRegionCode(regionDTO.getRegionCode().toUpperCase());
			}
			message = "Region Updated Successfully";
		}

		getRegionVOFromRegionDTO(regionVO, regionDTO);
		regionRepo.save(regionVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("regionVO", regionVO);
		return response;
	}

	private void getRegionVOFromRegionDTO(RegionVO regionVO, RegionDTO regionDTO) {
		regionVO.setActive(regionDTO.isActive());
		regionVO.setOrgId(regionDTO.getOrgId());
		regionVO.setCancel(regionDTO.isCancel());
		regionVO.setRegionCode(regionDTO.getRegionCode().toUpperCase());
		regionVO.setRegionName(regionDTO.getRegionName().toUpperCase());
	}

	@Override
	public void deleteRegion(Long regionid) {
		regionRepo.deleteById(regionid);
	}

	// Currency
	@Override
	public List<CurrencyVO> getAllCurrency(Long orgid) {

		return currencyRepo.findAll(orgid);
	}

	@Override
	public Optional<CurrencyVO> getCurrencyById(Long currencyid) {

		return currencyRepo.findById(currencyid);
	}

	@Override
	@Transactional
	public Map<String, Object> createUpdateCurrency(CurrencyDTO currencyDTO) throws ApplicationException {

		CurrencyVO currencyVO;
		String message = null;

		if (ObjectUtils.isEmpty(currencyDTO.getId())) {
			if (currencyRepo.existsByOrgIdAndCountryAndCurrencyIgnoreCase(currencyDTO.getOrgId(),
					currencyDTO.getCountry(), currencyDTO.getCurrency())) {
				String errorMessage = String.format("This Currency:%s Already Exists in This Organization.",
						currencyDTO.getCurrency());
				throw new ApplicationException(errorMessage);
			}
			if (currencyRepo.existsByOrgIdAndCountryAndCurrencyDescriptionIgnoreCase(currencyDTO.getOrgId(),
					currencyDTO.getCountry(), currencyDTO.getCurrencyDescription())) {
				String errorMessage = String.format("This CurrencyDescription:%s Already Exists in This Organization.",
						currencyDTO.getCurrencyDescription());
				throw new ApplicationException(errorMessage);
			}
			if (currencyRepo.existsByOrgIdAndCountryAndSubCurrencyIgnoreCase(currencyDTO.getOrgId(),
					currencyDTO.getCountry(), currencyDTO.getSubCurrency())) {
				String errorMessage = String.format("This SubCurrency:%s Already Exists in This Organization.",
						currencyDTO.getSubCurrency());
				throw new ApplicationException(errorMessage);
			}

			// Create new currency
			currencyVO = new CurrencyVO();
			currencyVO.setCreatedBy(currencyDTO.getCreatedBy());
			currencyVO.setUpdatedBy(currencyDTO.getCreatedBy());
			message = "Currency Created Successfully";
		} else {
			// Update existing currency
			currencyVO = currencyRepo.findById(currencyDTO.getId()).orElseThrow(
					() -> new ApplicationException("This Id Is Not Found Any Information: " + currencyDTO.getId()));
			currencyVO.setUpdatedBy(currencyDTO.getCreatedBy());

			if (!currencyVO.getCurrency().equalsIgnoreCase(currencyDTO.getCurrency())) {
				if (currencyRepo.existsByOrgIdAndCountryAndCurrencyIgnoreCase(currencyDTO.getOrgId(),
						currencyDTO.getCountry(), currencyDTO.getCurrency())) {
					String errorMessage = String.format("This Currency:%s Already Exists in This Organization.",
							currencyDTO.getCurrency());
					throw new ApplicationException(errorMessage);
				}
				currencyVO.setCurrency(currencyDTO.getCurrency().toUpperCase());
			}
			if (!currencyVO.getSubCurrency().equalsIgnoreCase(currencyDTO.getSubCurrency())) {
				if (currencyRepo.existsByOrgIdAndCountryAndSubCurrencyIgnoreCase(currencyDTO.getOrgId(),
						currencyDTO.getCountry(), currencyDTO.getSubCurrency())) {
					String errorMessage = String.format("This SubCurrency:%s Already Exists in This Organization.",
							currencyDTO.getSubCurrency());
					throw new ApplicationException(errorMessage);
				}
				if (currencyDTO.getSubCurrency() != null) {
					currencyVO.setSubCurrency(currencyDTO.getSubCurrency().toUpperCase());
				}
			}
			if (!currencyVO.getCurrencyDescription().equalsIgnoreCase(currencyDTO.getCurrencyDescription())) {
				if (currencyRepo.existsByOrgIdAndCountryAndCurrencyDescriptionIgnoreCase(currencyDTO.getOrgId(),
						currencyDTO.getCountry(), currencyDTO.getCurrencyDescription())) {
					String errorMessage = String.format(
							"This CurrencyDescription:%s Already Exists in This Organization.",
							currencyDTO.getCurrencyDescription());
					throw new ApplicationException(errorMessage);
				}
				if (currencyDTO.getCurrencyDescription() != null) {
					currencyVO.setCurrencyDescription(currencyDTO.getCurrencyDescription().toUpperCase());
				}
			}
			message = "Currency Updated Successfully";
		}

		getCurrencyVOFromCurrencyDTO(currencyVO, currencyDTO);
		currencyRepo.save(currencyVO);

		Map<String, Object> response = new HashMap<>();
		response.put("message", message);
		response.put("currencyVO", currencyVO);
		return response;
	}

	private void getCurrencyVOFromCurrencyDTO(CurrencyVO currencyVO, CurrencyDTO currencyDTO) {
		currencyVO.setCurrency(currencyDTO.getCurrency().toUpperCase());
		if (currencyDTO.getSubCurrency() != null) {
			currencyVO.setSubCurrency(currencyDTO.getSubCurrency().toUpperCase());
		}
		if (currencyDTO.getCurrencyDescription() != null) {
			currencyVO.setCurrencyDescription(currencyDTO.getCurrencyDescription().toUpperCase());
		}
		currencyVO.setActive(currencyDTO.isActive());
		currencyVO.setCountry(currencyDTO.getCountry().toUpperCase());
		currencyVO.setOrgId(currencyDTO.getOrgId());
	}

	@Override
	public void deleteCurrency(Long currencyid) {
		currencyRepo.deleteById(currencyid);

	}

	@Override
	public Map<String, Object> createUpdateScreenNames(ScreenNamesDTO screenNamesDTO) throws ApplicationException {
		ScreenNamesVO screenNamesVO = new ScreenNamesVO();
		String message = null;

		if (ObjectUtils.isEmpty(screenNamesDTO.getId())) {

			// Validate if responsibility already exists by responsibility name
			if (screenNamesRepo.existsByScreenName(screenNamesDTO.getScreenName())) {
				throw new ApplicationException("Screen Name already exists");
			}
			if (screenNamesRepo.existsByScreenCode(screenNamesDTO.getScreenCode())) {
				throw new ApplicationException("Screen Code already exists");
			}

			screenNamesVO.setCreatedBy(screenNamesDTO.getCreatedBy());
			screenNamesVO.setUpdatedBy(screenNamesDTO.getCreatedBy());
			screenNamesVO.setActive(screenNamesDTO.isActive());
			screenNamesVO.setScreenCode(screenNamesDTO.getScreenCode());
			screenNamesVO.setScreenName(screenNamesDTO.getScreenName());
			// Set the values from screenNamesDTO to responsibilityVO
			message = "ScreenName Created successfully";

		} else {

			// Retrieve the existing ResponsibilityVO from the repository
			screenNamesVO = screenNamesRepo.findById(screenNamesDTO.getId())
					.orElseThrow(() -> new ApplicationException("Screen Name not found"));

			// Validate and update unique fields if changed
			if (!screenNamesVO.getScreenName().equalsIgnoreCase(screenNamesDTO.getScreenName())) {
				if (screenNamesRepo.existsByScreenName(screenNamesDTO.getScreenName())) {
					throw new ApplicationException("Screen Name already exists");
				}
				screenNamesVO.setScreenName(screenNamesDTO.getScreenName());
			}
			if (!screenNamesVO.getScreenCode().equalsIgnoreCase(screenNamesDTO.getScreenCode())) {
				if (screenNamesRepo.existsByScreenCode(screenNamesDTO.getScreenCode())) {
					throw new ApplicationException("Screen Code already exists");
				}
				screenNamesVO.setScreenCode(screenNamesDTO.getScreenCode());
			}
			screenNamesVO.setActive(screenNamesDTO.isActive());
			screenNamesVO.setUpdatedBy(screenNamesDTO.getCreatedBy());
			// Update the remaining fields from screenNamesDTO to responsibilityVO
			message = "ScreenName Updated successfully";
		}

		screenNamesRepo.save(screenNamesVO);
		Map<String, Object> response = new HashMap<>();
		response.put("screenNamesVO", screenNamesVO);
		response.put("message", message);
		return response;
	}

	@Override
	public List<ScreenNamesVO> getAllScreenNames() {

		return screenNamesRepo.findAll();
	}

	@Override
	public ScreenNamesVO getScreenNamesById(Long id) throws ApplicationException {

		if (ObjectUtils.isEmpty(id)) {
			throw new ApplicationException("Invalid Id");
		}

		ScreenNamesVO screenNamesVO = screenNamesRepo.findById(id)
				.orElseThrow(() -> new ApplicationException("Screen Name not found for Id: " + id));

		return screenNamesVO;
	}

	// Financila Year
	@Override
	public Map<String, Object> createUpdateFinYear(FinancialYearDTO financialYearDTO) throws ApplicationException {
		FinancialYearVO financialYearVO = null;
		String message;

		if (ObjectUtils.isEmpty(financialYearDTO.getId())) {
			if (financialYearRepo.existsByFinYearAndOrgId(financialYearDTO.getFinYear(), financialYearDTO.getOrgId())) {
				String errorMessage = String.format("ThiS finyear:%s Already Exists This Organization .",
						financialYearDTO.getFinYear());
				throw new ApplicationException(errorMessage);
			}

			if (financialYearRepo.existsByFinYearIdentifierAndOrgId(financialYearDTO.getFinYearIdentifier(),
					financialYearDTO.getOrgId())) {
				String errorMessage = String.format("ThiS finyearidentifier:%s Already Exists This Organization .",
						financialYearDTO.getFinYearIdentifier());
				throw new ApplicationException(errorMessage);
			}

			if (financialYearRepo.existsByFinYearIdAndOrgId(financialYearDTO.getFinYearId(),
					financialYearDTO.getOrgId())) {
				String errorMessage = String.format("ThiS finyearid:%s Already Exists This Organization .",
						financialYearDTO.getFinYearId());
				throw new ApplicationException(errorMessage);
			}

			financialYearVO = new FinancialYearVO();
			financialYearVO.setCreatedBy(financialYearDTO.getCreatedBy());
			financialYearVO.setUpdatedBy(financialYearDTO.getCreatedBy());
			message = "Financial Year Creation Successfully";

		} else {
			financialYearVO = financialYearRepo.findById(financialYearDTO.getId())
					.orElseThrow(() -> new ApplicationException(String
							.format("This Id Is Not Found Any Information, Invalid Id: %s", financialYearDTO.getId())));

			if (financialYearVO.getFinYear() != financialYearDTO.getFinYear()) {
				if (financialYearRepo.existsByFinYearAndOrgId(financialYearDTO.getFinYear(),
						financialYearDTO.getOrgId())) {
					String errorMessage = String.format("This finyear: %s already exists for this organization.",
							financialYearDTO.getFinYear());
					throw new ApplicationException(errorMessage);
				}
				financialYearVO.setFinYear(financialYearDTO.getFinYear());
			}

			if (!financialYearVO.getFinYearIdentifier().equals(financialYearDTO.getFinYearIdentifier())) {
				if (financialYearRepo.existsByFinYearIdentifierAndOrgId(financialYearDTO.getFinYearIdentifier(),
						financialYearDTO.getOrgId())) {
					String errorMessage = String.format(
							"This finyearIdentifier: %s already exists for this organization.",
							financialYearDTO.getFinYearIdentifier());
					throw new ApplicationException(errorMessage);
				}
				financialYearVO.setFinYearIdentifier(financialYearDTO.getFinYearIdentifier());
			}

			if (financialYearVO.getFinYearId() != financialYearDTO.getFinYearId()) {
				if (financialYearRepo.existsByFinYearIdAndOrgId(financialYearDTO.getFinYearId(),
						financialYearDTO.getOrgId())) {
					String errorMessage = String.format("This finyearId: %s already exists for this organization.",
							financialYearDTO.getFinYearId());
					throw new ApplicationException(errorMessage);
				}
				financialYearVO.setFinYearId(financialYearDTO.getFinYearId());
			}

			financialYearVO.setUpdatedBy(financialYearDTO.getCreatedBy());
			message = "Financial Year Updation Successfully";

		}
		getFinancialYearVOFromFinancialYearDTO(financialYearVO, financialYearDTO);
		financialYearRepo.save(financialYearVO);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("financialYearVO", financialYearVO);
		response.put("message", response);
		return response;

	}

	private void getFinancialYearVOFromFinancialYearDTO(FinancialYearVO financialYearVO,
			FinancialYearDTO financialYearDTO) {
		financialYearVO.setFinYear(financialYearDTO.getFinYear());
		financialYearVO.setFinYearId(financialYearDTO.getFinYearId());
		financialYearVO.setFinYearIdentifier(financialYearDTO.getFinYearIdentifier());
		financialYearVO.setStartDate(financialYearDTO.getStartDate());
		financialYearVO.setEndDate(financialYearDTO.getEndDate());
		financialYearVO.setCurrentFinYear(financialYearDTO.isCurrentFinYear());
		financialYearVO.setClosed(financialYearDTO.isClosed());
		financialYearVO.setOrgId(financialYearDTO.getOrgId());
		financialYearVO.setActive(financialYearDTO.isActive());
	}

	@Override
	public List<FinancialYearVO> getAllActiveFInYear(Long orgId) {
		return financialYearRepo.findAllActiveFinYear(orgId);
	}

	@Override
	public List<FinancialYearVO> getAllFInYearByOrgId(Long orgId) {
		return financialYearRepo.findFinancialYearByOrgId(orgId);
	}

	@Override
	public Optional<FinancialYearVO> getAllFInYearById(Long id) {
		return financialYearRepo.findById(id);
	}

	@Override
	public List<Map<String, Object>> getAllCurrencyForExRate(Long orgId) {
		Set<Object[]> getFullGridCurrency = currencyRepo.findCurrencyForFullGrid(orgId);
		return getCurrency(getFullGridCurrency); // Returning a list of Map<String, Object>
	}

	private List<Map<String, Object>> getCurrency(Set<Object[]> getFullGridCurrency) {
		List<Map<String, Object>> currencyList = new ArrayList<>(); // Correct variable name

		for (Object[] currency : getFullGridCurrency) { // Iterating over getFullGridCurrency
			Map<String, Object> currencyMap = new HashMap<>();
			currencyMap.put("id", currency[0] != null ? Integer.parseInt(currency[0].toString()) : 0);
			currencyMap.put("currency", currency[1] != null ? currency[1].toString() : "");
			currencyMap.put("currencyDescription", currency[2] != null ? currency[2].toString() : "");

			currencyList.add(currencyMap); // Add the Map to the list
		}
		return currencyList;
	}

}