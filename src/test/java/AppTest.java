import java.io.File;

import org.springframework.batch.core.BatchStatus;

import fr.demo.app.App;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	public AppTest(String testName) {
		super(testName);
	}
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		
		
		String fileIn = "C:\\Users\\Guigui\\Documents\\Code\\TestEntree\\src\\test\\resources\\LdifExemple.ldif";
		String fileOut = "C:\\Users\\Guigui\\Documents\\Code\\TestEntree\\src\\test\\resources\\LdifExempleOUT.ldif";
		String[] args = new String[] {
				fileIn,fileOut,
				"3" };

		File f = new File(fileOut);
		f.delete();

		System.out.println(" args are : "  );
		System.out.println(" fileIn " + args[0] );
		System.out.println(" fileOut " + args[1] );
		System.out.println(" Amount " + args[2] );

		App.main(args);

		System.out.println("Job " + App.getJob().getName() + " Done with status " + App.getJobExecution().getStatus());

		assertEquals("Job didn't Go well :(", BatchStatus.COMPLETED, App.getJobExecution().getStatus());
		
	}
}
