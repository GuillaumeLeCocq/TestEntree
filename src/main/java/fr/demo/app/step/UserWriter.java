package fr.demo.app.step;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import fr.demo.app.pojo.User;

public class UserWriter implements StepExecutionListener, ItemWriter<User>, InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(UserWriter.class);

	private LdifReader ldifReader;
	private String pathOut;
	private File fileOut;
	private FileWriter fw;

	@Override
	public void beforeStep(StepExecution stepExecution) {

		pathOut = stepExecution.getJobParameters().getString("fileOut");
		LOG.debug("Writing on : " + pathOut);

		if (StringUtils.isBlank(pathOut)) {
			throw new IllegalArgumentException("Asking to write an empty path  for FileOut !");
		}

		fileOut = new File(pathOut);

		if (fileOut.exists()) {
			throw new IllegalArgumentException("Asking to write an ALREADY existing file !");
		}


		try {
			fw = new FileWriter(fileOut);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void write(List<? extends User> items) throws Exception {
		LOG.info("Writing " + items.size() + " Users");

		for (User user : items) {
			fw.write("#" + user.getHeader() + "\n");
			for (String str : user.getLines()) {
				fw.write(str+ "\n");
			}
			fw.write("\n");
		}
		
		fw.flush();
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}

	public LdifReader getLdifReader() {
		return ldifReader;
	}

	public void setLdifReader(LdifReader ldifReader) {
		this.ldifReader = ldifReader;
	}

}
