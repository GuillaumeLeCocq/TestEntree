package fr.cnp.sec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.cnp.sec.config.BatchConfiguration;
import fr.cnp.sec.config.Const;

public class App {

	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	private static Job jobOfApp;
	private static JobExecution execution;

	public static void main(String[] args) {

		if (args.length != 3 && args.length != 4) {
			LOG.error  ("Usage is not good, " + args.length );
			LOG.info("");
			LOG.info("Usage : [Path File LDIF] [Path File OUT] [Amount of Creation]");
			LOG.info("or");
			LOG.info("Usage : [Path File LDIF] [Path File OUT] [Begin Number] [Amount of Creation]");
			LOG.info(" 		LDIF file with " + Const.TOKEN_CPT + " will be changed");
			LOG.info(" 		" + Const.TOKEN_CPT + " will be change to 0001 > 0010 ... until Amount of creation is reached");
			throw new IllegalArgumentException("Usage : [Path File LDIF] [Path File OUT] [Amount of Creation]");
		}

		// Spring Java config
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(BatchConfiguration.class);
		context.register(UserJob.class);
		context.refresh();

		JobParametersBuilder parameterBuilder = null;
		
		if (args.length == 3) {
			parameterBuilder = new JobParametersBuilder().addString("fileIn", args[0])
					.addString("fileOut", args[1]).addLong("amount", Long.parseLong(args[2]));
		} else {
			parameterBuilder = new JobParametersBuilder().addString("fileIn", args[0])
					.addString("fileOut", args[1]).addLong("begin", Long.parseLong(args[2])).addLong("amount", Long.parseLong(args[3]));
		}
		

		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		jobOfApp = (Job) context.getBean("UserJob");

		LOG.info("Starting the batch job with " + parameterBuilder.toJobParameters());
		try {
			execution = jobLauncher.run(jobOfApp, parameterBuilder.toJobParameters());
			LOG.info("Job Status : " + execution.getStatus());
			LOG.info("Job done");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Job failed");
		}
		finally {
			context.close();
		}

	}
	
	public static Job getJob() {
		return jobOfApp;
	}
	

	public static JobExecution getJobExecution() {
		return execution;
	}

}
