package fr.demo.app.step;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import fr.demo.app.config.Const;
import fr.demo.app.pojo.LdifBase;

public class LdifReader implements Tasklet, StepExecutionListener {

	private static final Logger LOG = LoggerFactory.getLogger(LdifReader.class);

	private String pathLdif;
	private File fileLdif;
	private LdifBase ldif;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOG.debug("beforeStep of Step LDIF Reader");

		pathLdif = stepExecution.getJobExecution().getJobParameters().getString("fileIn");

		if (StringUtils.isBlank(pathLdif)) {
			throw new IllegalArgumentException("Asking to read an empty path  LDIF base file !");
		}

		fileLdif = new File(pathLdif);

		if (!fileLdif.exists()) {
			throw new IllegalArgumentException("Asking to read an non existing LDIF base file !");
		}

	}

	@Override
	public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		LOG.debug("execute of Step LDIF Reader");

		// Reading LDIF File
		ldif = new LdifBase();
		// All Token
		List<String> tokens = Const.list;
		
		for (String string : tokens) {
			ldif.getTokenAndIndexLineWithCpt().put(string, new ArrayList<Integer>());
		}
		

		FileInputStream inputStream = new FileInputStream(fileLdif);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				ldif.getLines().add(line);

				// Setting Line to be updated
				for (String aToken : tokens) {
					if (line.contains(aToken)) {
						ldif.getTokenAndIndexLineWithCpt().get(aToken).add(ldif.getLines().size() - 1);
					}
				}
			}
		}

		
		boolean empty = true;
		for (String aToken : tokens) {
			if (!ldif.getTokenAndIndexLineWithCpt().get(aToken).isEmpty()) {
				empty = false;
				break;
			}
		}
		
		if(empty) {
			LOG.warn(" /!\\ None line with token in the file  /!\\ "  );
			LOG.warn("None line with token : " + tokens);
			LOG.warn("Within the file 	   : " + pathLdif);
			LOG.warn(" /!\\ None line with token in the file  /!\\ "  );
		}

		return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOG.debug("afterStep of Step LDIF Reader with " + ldif.getLines().size() + " lines and "
				+ ldif.getTokenAndIndexLineWithCpt().size() + " lines ");
		stepExecution.getJobExecution().getExecutionContext().put(Const.KEY_LdifBase, ldif);

		return ExitStatus.COMPLETED;
	}

	public LdifBase getLdif() {
		return ldif;
	}

}
