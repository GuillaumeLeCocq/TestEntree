package fr.demo.app.step;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import fr.demo.app.config.Const;
import fr.demo.app.pojo.LdifBase;
import fr.demo.app.pojo.User;

public class UserReader implements StepExecutionListener, ItemReader<User>, InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(UserReader.class);

	@Autowired
	private LdifReader ldifReader;
	private LdifBase ldif;
	private long cpt;
	private long amount;

	private SimpleDateFormat sdf;
	private String now;
	private String yesterday;	
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		amount = stepExecution.getJobParameters().getLong("amount");

		cpt =  stepExecution.getJobParameters().getLong("begin",Long.valueOf(0L));
		ldif = (LdifBase) stepExecution.getJobExecution().getExecutionContext().get(Const.KEY_LdifBase);
		LOG.debug("beforeStep of Step UserReader asking to read " + amount + " users");
	
		sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		now = sdf.format(new Date());
		yesterday = sdf.format(DateUtils.addDays(new Date(), -1));
		
		
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// Nothing
	}

	@Override
	public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		if (cpt < amount) {
			
			LOG.debug("read UserReader N°" + cpt);

			String cptInSTR = String.format("%04d", cpt);
			User u = new User();

			u.setNumber(cpt);
			u.setHeader("User N°" + cptInSTR);

			u.setLines(new ArrayList<String>());
			u.getLines().addAll(ldif.getLines());

			// Update all Line
			for (Integer i : ldif.getTokenAndIndexLineWithCpt().get(Const.TOKEN_CPT)) {
				u.getLines().set(i, u.getLines().get(i).replace(Const.TOKEN_CPT, cptInSTR));
			}
			for (Integer i : ldif.getTokenAndIndexLineWithCpt().get(Const.TOKEN_NOW)) {
				u.getLines().set(i, u.getLines().get(i).replace(Const.TOKEN_NOW, now));
			}

			cpt++;
			return u;
		}

		return null;
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
