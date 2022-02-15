package daoPattern;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import enumsStore.CircuitTypes;
import enumsStore.ModeSwitches;

/**
 * This is a class for storing data obtained from a database from a "tsh_kbr_info" table.
 * <p>
 * @author Aleksander Demichev
 * @version 1.0.0.0
 */

public final class InformationStore {

	private String 	urlSendProm,
					urlReciveProm,
					mainIPProm,
					rezervIPProm,
					urlLKProm,
					urlSendTest,
					urlReciveTest,
					mainIPTest,
					rezervIPTest,
					urlLKTest;

	
	public Object clone() {
		
		InformationStore clonedObject = new InformationStore();
		
		clonedObject.urlSendProm = this.urlSendProm;
		clonedObject.urlReciveProm = this.urlReciveProm;
		clonedObject.mainIPProm = this.mainIPProm;
		clonedObject.rezervIPProm = this.rezervIPProm;
		clonedObject.urlLKProm = this.urlLKProm;
		
		clonedObject.urlSendTest = this.urlSendTest;
		clonedObject.urlReciveTest = this.urlReciveTest;
		clonedObject.mainIPTest = this.mainIPTest;
		clonedObject.rezervIPTest = this.rezervIPTest;
		clonedObject.urlLKTest = this.urlLKTest;
		
		return clonedObject;
	 }
	
	

	/*
	 * Set urlSendProm
	 */
	public void setUrlSendProm(String urlSendProm) {
		this.urlSendProm = urlSendProm;
	}

	/*
	 * Get urlSendProm
	 */
	public String getUrlSendProm() {
		return this.urlSendProm;
	}

	/*
	 * Set urlReciveProm
	 */
	public void setUrlReciveProm(String urlReciveProm) {
		this.urlReciveProm = urlReciveProm;
	}

	/*
	 * Get urlReciveProm
	 */
	public String getUrlReciveProm() {
		return this.urlReciveProm;
	}

	/*
	 * Set mainIPProm
	 */
	public void setMainIPProm(String mainIPProm) {
		this.mainIPProm = mainIPProm;
	}

	/*
	 * Get mainIPProm
	 */
	public String getMainIPProm() {
		return this.mainIPProm;
	}

	/*
	 * Set rezervIPProm
	 */
	public void setRezervIPProm(String rezervIPProm) {
		this.rezervIPProm = rezervIPProm;
	}

	/*
	 * Get rezervIPProm
	 */
	public String getRezervIPProm() {
		return this.rezervIPProm;
	}

	/*
	 * Set urlLKProm
	 */
	public void setUrlLKProm(String urlLKProm) {
		this.urlLKProm = urlLKProm;
	}

	/*
	 * Get urlLKProm
	 */
	public String getUrlLKProm() {
		return this.urlLKProm;
	}

	/*
	 * Set urlSendTest
	 */
	public void setUrlSendTest(String urlSendTest) {
		this.urlSendTest = urlSendTest;
	}

	/*
	 * Get urlSendTest
	 */
	public String getUrlSendTest() {
		return this.urlSendTest;
	}

	/*
	 * Set urlReciveTest
	 */
	public void setUrlReciveTest(String urlReciveTest) {
		this.urlReciveTest = urlReciveTest;
	}

	/*
	 * Get urlReciveTest
	 */
	public String getUrlReciveTest() {
		return this.urlReciveTest;
	}

	/*
	 * Set mainIPTest
	 */
	public void setMainIPTest(String mainIPTest) {
		this.mainIPTest = mainIPTest;
	}

	/*
	 * Get mainIPTest
	 */
	public String getMainIPTest() {
		return this.mainIPTest;
	}
	
	/*
	 * Set rezervIPTest
	 */
	public void setRezervIPTest(String rezervIPTest) {
		this.rezervIPTest = rezervIPTest;
	}

	/*
	 * Get rezervIPTest
	 */
	public String getRezervIPTest() {
		return this.rezervIPTest;
	}
	
	/*
	 * Set urlLKTest
	 */
	public void setUrlLKTest(String urlLKTest) {
		this.urlLKTest = urlLKTest;
	}

	/*
	 * Get urlLKTest
	 */
	public String getUrlLKTest() {
		return this.urlLKTest;
	}
	
	/*
	 * Get this.class fields
	 */
	public Field[] getInformationFields(CircuitTypes circuitSwitch) throws NoSuchFieldException, SecurityException {
		Field[] informationFields = new Field[5];

		switch (circuitSwitch) {
		case PROM:
			informationFields[0] = this.getClass().getDeclaredField("urlSendProm");
			informationFields[1] = this.getClass().getDeclaredField("urlReciveProm");
			informationFields[2] = this.getClass().getDeclaredField("mainIPProm");
			informationFields[3] = this.getClass().getDeclaredField("rezervIPProm");
			informationFields[4] = this.getClass().getDeclaredField("urlLKProm");
			break;
		case TEST:
			informationFields[0] = this.getClass().getDeclaredField("urlSendTest");
			informationFields[1] = this.getClass().getDeclaredField("urlReciveTest");
			informationFields[2] = this.getClass().getDeclaredField("mainIPTest");
			informationFields[3] = this.getClass().getDeclaredField("rezervIPTest");
			informationFields[4] = this.getClass().getDeclaredField("urlLKTest");
			break;
		}

		return informationFields;
	}
	

	
	
	/* Get this.class methods */ 
	public Method[] getInformationMethods(ModeSwitches modeSwitch) throws NoSuchMethodException, SecurityException {
		
		//No parameters
		Class<?> noparams[] = {};
			
		//String parameter
		Class<?>[] paramString = new Class[1];	
		paramString[0] = String.class;
		
		Method[] definedMethods = new Method[5];
		
		switch (modeSwitch) {
		case GET_PROM:
			// All getters PROM methods
			definedMethods[0] = this.getClass().getMethod("getUrlSendProm", noparams);
			definedMethods[1] = this.getClass().getMethod("getUrlReciveProm", noparams);
			definedMethods[2] = this.getClass().getMethod("getMainIPProm", noparams);
			definedMethods[3] = this.getClass().getMethod("getRezervIPProm", noparams);
			definedMethods[4] = this.getClass().getMethod("getUrlLKProm", noparams);
			break;
		case GET_TEST:
			// All getters TEST methods
			definedMethods[0] = this.getClass().getMethod("getUrlSendTest", noparams);
			definedMethods[1] = this.getClass().getMethod("getUrlReciveTest", noparams);
			definedMethods[2] = this.getClass().getMethod("getMainIPTest", noparams);
			definedMethods[3] = this.getClass().getMethod("getRezervIPTest", noparams);
			definedMethods[4] = this.getClass().getMethod("getUrlLKTest", noparams);
			break;
		case SET_PROM:
			// All setters PROM methods
			definedMethods[0] = this.getClass().getMethod("setUrlSendProm", paramString);
			definedMethods[1] = this.getClass().getMethod("setUrlReciveProm", paramString);
			definedMethods[2] = this.getClass().getMethod("setMainIPProm", paramString);
			definedMethods[3] = this.getClass().getMethod("setRezervIPProm", paramString);
			definedMethods[4] = this.getClass().getMethod("setUrlLKProm", paramString);
			break;
		case SET_TEST:
			// All setters TEST methods
			definedMethods[0] = this.getClass().getMethod("setUrlSendTest", paramString);
			definedMethods[1] = this.getClass().getMethod("setUrlReciveTest", paramString);
			definedMethods[2] = this.getClass().getMethod("setMainIPTest", paramString);
			definedMethods[3] = this.getClass().getMethod("setRezervIPTest", paramString);
			definedMethods[4] = this.getClass().getMethod("setUrlLKTest", paramString);
			break;
		}

		return definedMethods;
	}
	

}
