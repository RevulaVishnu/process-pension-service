package com.cts.processPension.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cts.processPension.exception.NotFoundException;
import com.cts.processPension.feign.PensionerDetailsClient;
import com.cts.processPension.model.Bank;
import com.cts.processPension.model.PensionDetail;
import com.cts.processPension.model.PensionerDetail;
import com.cts.processPension.model.PensionerInput;
import com.cts.processPension.util.DateUtil;

/**
 * 
 * Class to test Service class functionality for process pension micro-service
 *
 */
@SpringBootTest
class ProcessPensionServiceImplTest {

	@Autowired
	private ProcessPensionService processPensionService;

	@MockBean
	private PensionerDetailsClient pensionerDetailClient;

	@Test
	void testCheckDetailsForCorrectPensionerInput() throws ParseException {
		PensionerInput input = new PensionerInput("123456789011");

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Vishnu","123456789011", DateUtil.parseDate("14-09-1999"), "BRPPV3218K",
				100000, 10000, "self", 456678,bank);

		assertTrue(processPensionService.checkdetails(input, details));
		assertTrue(processPensionService.checkdetails(input, details));
		assertEquals(456678, bank.getAccountNumber());
		assertNotNull(details);
	}

	@Test
	void testCheckDetailsForIncorrectPensionerInput() throws ParseException {
		PensionerInput input = new PensionerInput("123456789011");

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Vishnu","123456789012", DateUtil.parseDate("14-09-1999"), "BRPPV3218K",
				100000, 10000, "self", 456678,bank);

		assertFalse(processPensionService.checkdetails(input, details));
	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetailsForSelfPensionTypeAndPublicBank() throws ParseException {

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Vishnu","123456789011", DateUtil.parseDate("14-09-1999"), "BRPPV3218K",
				100000, 10000, "self", 456678, bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(details);

		assertEquals(89500, actualDetail.getPensionAmount());
	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetailsForFamilyPensionTypeAndPublicBank() throws ParseException {

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Vishnu","123456789011", DateUtil.parseDate("14-09-1999"), "BRPPV3218K",
				100000, 10000, "family", 456678,bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(details);

		assertEquals(59500, actualDetail.getPensionAmount());
	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetailsForFamilyPensionTypeAndPrivateBank() throws ParseException {

		Bank bank = new Bank("SBI", 456678, "private");

		PensionerDetail details = new PensionerDetail("Vishnu","123456789011", DateUtil.parseDate("14-09-1999"), "BRPPV3218K",
				100000, 10000, "family", 456678,bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(details);

		assertEquals(59450, actualDetail.getPensionAmount());
	}

	@Test
	void testGettingPensionDetailByPassingPensionerDetailsForSelfPensionTypeAndPrivateBank() throws ParseException {

		Bank bank = new Bank("SBI", 456678, "private");

		PensionerDetail details = new PensionerDetail("Vishnu","123456789011", DateUtil.parseDate("14-09-1999"), "BRPPV3218K",
				100000, 10000, "self", 456678, bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(details);

		assertEquals(89450, actualDetail.getPensionAmount());
	}

	/**
	 * Throws ParseException
	 */
	@Test
	@DisplayName("Method to test getPensionDetails() method")
	void testGetPensionDetailsForSelf() throws ParseException {
		PensionerInput pensionerInput = new PensionerInput("123456789011");

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail detailsSelf = new PensionerDetail("Vishnu","123456789011", DateUtil.parseDate("14-09-1999"), "BRPPV3218K",
				100000, 10000, "self", 456678,bank);

		Mockito.when(pensionerDetailClient.getPensionerDetailByAadhaar(pensionerInput.getAadhaarNumber()))
				.thenReturn(detailsSelf);

		PensionDetail pensionDetailSelf = processPensionService.getPensionDetails(pensionerInput);

		assertEquals(89500, pensionDetailSelf.getPensionAmount());
		assertNotNull(pensionDetailSelf);
	}

	/**
	 * Throws ParseException
	 */
	@DisplayName("Method to test getPensionDetails() method")
	@Test
	void testGetPensionDetailsForFamily() throws ParseException {
		PensionerInput pensionerInput = new PensionerInput("123456789011");

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail detailsFamily = new PensionerDetail("Vishnu","123456789011", DateUtil.parseDate("14-09-1999"), "BRPPV3218K",
				100000, 10000, "family", 456678,bank);

		// mock the feign client
		Mockito.when(pensionerDetailClient.getPensionerDetailByAadhaar(pensionerInput.getAadhaarNumber()))
				.thenReturn(detailsFamily);

		// get the actual result
		PensionDetail pensionDetailFamily = processPensionService.getPensionDetails(pensionerInput);

		// test cases
		assertEquals(59500, pensionDetailFamily.getPensionAmount());
		assertNotNull(pensionDetailFamily);
	}
}