package org.sec.create.user;

import java.io.File;

import org.springframework.batch.core.BatchStatus;

import fr.cnp.sec.App;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		
		//* Example of Test but won't work on junit 
		
		String fileIn = "C:\\Users\\Guigui\\Documents\\Code\\TestEntree\\src\\test\\resources\\LdifExemple.ldif";
		String fileOut = "C:\\Users\\Guigui\\Documents\\Code\\TestEntree\\src\\test\\resources\\LdifExempleOUT.ldif";
		String[] args = new String[] {
				fileIn,fileOut,
				"3" };

		File f = new File(fileOut);
		f.delete();

		App.main(args);

		System.out.println("Job " + App.getJob().getName() + " Done with status " + App.getJobExecution().getStatus());

		assertEquals("Job didn't Go well :(", App.getJobExecution().getStatus(), BatchStatus.COMPLETED);
		
	}
}
