package com.cts.processPension.service;

import com.cts.processPension.model.PensionDetail;
import com.cts.processPension.model.PensionerDetail;
import com.cts.processPension.model.PensionerInput;

/**
 * Process Pension Service Interface
 *
 */
public interface ProcessPensionService {

	/**
	 * This method is responsible to get the pension details if input details are
	 * valid
	 * 
	 * @param pensionerInput
	 * @return Verified Pension Detail with pension amount
	 */
	public PensionDetail getPensionDetails(PensionerInput pensionerInput);

	/**
	 * Calculate the pension amount and return the pensioner details according to
	 * the type of pension "self" or "family"
	 * 
	 * @param pensionDetail
	 * @return Pension Details with Pension amount
	 */
	public PensionDetail calculatePensionAmount(PensionerDetail pensionDetail);

	/**
	 * Method to check the details entered by the user
	 * 
	 * @param pensionerInput
	 * @param pensionerDetail
	 * @return true if details match, else false
	 */
	public boolean checkdetails(PensionerInput pensionerInput, PensionerDetail pensionerDetail);
	
}
