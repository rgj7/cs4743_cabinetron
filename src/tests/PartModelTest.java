package tests;

import static org.junit.Assert.*;
import cabinetron3.PartModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/*
 * CS4743 Assignment 2, Spring 2015
 * Students: Raul Gonzalez, Derek Deshaies
 * 
 * File: PartModelTest.java
 * Description: JUnit tests for the PartModel
 */

public class PartModelTest {

	private PartModel testPart;
	private int validPartID;
	private String longString;
	private String validPartNumber;
	private String validExNum;
	private String validPartName;
	private String validPartVendor;
	private int validPartUnit;

	@Rule
	public ExpectedException thrown = ExpectedException.none();	
	
	@Before
	public void setUp() throws Exception {
		for(int i=0; i<256; i++) {
			longString += "A";
		}
		validPartID = 1;
		validPartNumber = "X145846";
		validExNum = "GD4930";
		validPartName = "VG2448QX";
		validPartVendor = "ASUS";
		validPartUnit = 1;
	}

	// test a valid part number parameter
	@Test
	public void testPartNumber() {
		testPart = new PartModel(validPartID, validPartNumber, validPartName, validPartUnit, validExNum, validPartVendor);
		assertEquals(validPartNumber, testPart.getPartNumber());
	}

	// test a null part number
	@Test
	public void testNullPartNumber() {
		thrown.expect(NullPointerException.class);
		testPart = new PartModel(validPartID, null, validPartName, validPartUnit, validExNum, validPartVendor);
	}
	
	// test when part number exceeds max length of 20 characters
	@Test
	public void testPartNumberLength() {
		thrown.expect(IllegalArgumentException.class);
		testPart = new PartModel(validPartID, longString, validPartName, validPartUnit, validExNum, validPartVendor);
	}
	
	// test a valid part name parameter
	@Test
	public void testPartName() {
		testPart = new PartModel(validPartID, validPartNumber, validPartName, validPartUnit, validExNum, validPartVendor);
		assertEquals(validPartName, testPart.getPartName());
	}
	
	// test a null part name
	@Test
	public void testNullPartName() {
		thrown.expect(NullPointerException.class);
		testPart = new PartModel(validPartID, validPartNumber, null, validPartUnit, validExNum, validPartVendor);
	}

	// test when part name exceeds max length of 255 characters
	@Test
	public void testPartNameLength() {
		thrown.expect(IllegalArgumentException.class);
		testPart = new PartModel(validPartID, validPartNumber, longString, validPartUnit, validExNum, validPartVendor);
	}
	
	// test a valid part vendor parameter
	@Test
	public void testPartVendor1() {
		testPart = new PartModel(validPartID, validPartNumber, validPartName, validPartUnit, validExNum, validPartVendor);
		assertEquals(validPartVendor, testPart.getPartVendor());
	}

	// test vendor as empty string
	@Test
	public void testPartVendor2() {
		testPart = new PartModel(validPartID, validPartNumber, validPartName, validPartUnit, validExNum, "");
		assertEquals("", testPart.getPartVendor());
	}
	
	// test vendor as null, should return empty string
	@Test
	public void testPartVendor3() {
		testPart = new PartModel(validPartID, validPartNumber, validPartName, validPartUnit, validExNum, null);
		assertEquals("", testPart.getPartVendor());
	}
	
	// test when part vendor exceeds max length of 255 characters
	@Test
	public void testPartVendorLength() {
		thrown.expect(IllegalArgumentException.class);
		testPart = new PartModel(validPartID, validPartNumber, validPartName, validPartUnit, validExNum, longString);
	}
	
	// test unit of quantity string
	@Test
	public void testUnitQuantityString() {
		testPart = new PartModel(validPartID, validPartNumber, validPartName, 1, validExNum, validPartVendor);
		assertEquals(testPart.getPartUnit(), "Linear Feet");
	}
	
	// test when unit of quantity is not set (unknown)
	@Test
	public void testUnknownUnitQuantity() {
		thrown.expect(IllegalArgumentException.class);
		testPart = new PartModel(validPartID, validPartNumber, validPartName, 0, validExNum, validPartVendor);
	}
	
	// test a valid part external number parameter
	@Test
	public void testPartExternalNumber() {
		testPart = new PartModel(validPartID, validPartNumber, validPartName, validPartUnit, validExNum, validPartVendor);
		assertEquals(validExNum, testPart.getExternalPartNumber());
	}
	
	// test a valid part external number length
	@Test
	public void testPartExternalNumberLength() {
		thrown.expect(IllegalArgumentException.class);
		testPart = new PartModel(validPartID, validPartNumber, validPartName, validPartUnit, longString, validPartVendor);
	}
}
