package com.cts.processPension.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.processPension.exception.NotFoundException;
import com.cts.processPension.feign.PensionerDetailsClient;
import com.cts.processPension.model.PensionDetail;
import com.cts.processPension.model.PensionerDetail;
import com.cts.processPension.model.PensionerInput;
import com.cts.processPension.repository.PensionDetailsRepository;
import com.cts.processPension.repository.PensionerDetailsRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Process Pension Interface implementation class
 *
 */
@Service
@Slf4j
public class ProcessPensionServiceImpl implements ProcessPensionService {

	@Autowired
	private PensionerDetailsClient pensionerDetailClient;

	@Autowired
	private PensionDetailsRepository pensionDetailsRepository;

	@Autowired
	private PensionerDetailsRepository pensionerDetailsRepository;

	/**
	 * This method is responsible to get the pension details if input details are
	 * valid
	 * @param pensionerInput
	 * @return Verified Pension Detail with pension amount
	 */
	@Override
	public PensionDetail getPensionDetails(PensionerInput pensionerInput) {

		// get the pensioner details from the pensionerDetailService
		PensionerDetail pensionerDetail = pensionerDetailClient
				.getPensionerDetailByAadhaar(pensionerInput.getAadhaarNumber());

		System.out.println("Pensioner Details:"+pensionerDetail);
		log.info("Pensioner details found");

		if (pensionerDetail.getAadhaarNumber() != null) {
			// save the input pensioner details into the database
			pensionerDetailsRepository.save(pensionerDetail);
			// calculate the amount and return the pension detail object
			return calculatePensionAmount(pensionerDetail);
		} else {
			throw new NotFoundException("Details entered are incorrect");
		}
	}
/**
	 * This method is responsible to get the pension details if input details are
	 * valid
	 *
	 * @param pensionerInput
	 * @return Verified Pension Detail with pension amount
	 */

	/**
	 * Calculate the pension amount and return the pensioner details according to
	 * the type of pension "self" or "family"
	 * 
	 * 
	 * @param pensionDetail Pensioner Details
	 * @return Pension Details with Pension amount
	 */
	@Override
	public PensionDetail calculatePensionAmount(PensionerDetail pensionDetail) {
		double pensionAmount = 0;
		if (pensionDetail.getPensionType().equalsIgnoreCase("self")) {
			if(pensionDetail.getBank().getBankType().equalsIgnoreCase("public")){
				pensionAmount = (pensionDetail.getSalary() * 0.8 + pensionDetail.getAllowance()) - 500;
			}
			else{
				pensionAmount = (pensionDetail.getSalary() * 0.8 + pensionDetail.getAllowance()) - 550;
			}
		}
		else if (pensionDetail.getPensionType().equalsIgnoreCase("family")) {
			if(pensionDetail.getBank().getBankType().equalsIgnoreCase("public")){
				pensionAmount = (pensionDetail.getSalary() * 0.5 + pensionDetail.getAllowance()) - 500;
			}
			else{
				pensionAmount = (pensionDetail.getSalary() * 0.5 + pensionDetail.getAllowance()) - 550;
			}
		}
		return new PensionDetail(
				pensionDetail.getName(),
				pensionDetail.getDateOfBirth(),
				pensionDetail.getPan(),
				pensionDetail.getPensionType(),
				pensionAmount);
	}

	/**
	 * Method to check the details entered by the user
	 * 
	 * @Data {"aadhaarNumber":"123456789012","pensionAmount":31600,"bankServiceCharge":550}
	 * @param pensionerInput .
	 * @param pensionerDetail .
	 * @return true if details match, else false
	 */
	@Override
	public boolean checkdetails(PensionerInput pensionerInput, PensionerDetail pensionerDetail) {
/*
		return (pensionerInput.getName().equalsIgnoreCase(pensionerDetail.getName())
				&& (pensionerInput.getDateOfBirth().compareTo(pensionerDetail.getDateOfBirth()) == 0)
				&& pensionerInput.getPan().equalsIgnoreCase(pensionerDetail.getPan())
				&& pensionerInput.getPensionType().equalsIgnoreCase(pensionerDetail.getPensionType()));
*/
		return (pensionerInput.getAadhaarNumber().equals(pensionerDetail.getAadhaarNumber()));
	}
}