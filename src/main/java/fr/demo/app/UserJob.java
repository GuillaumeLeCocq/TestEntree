package fr.demo.app;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import fr.demo.app.pojo.User;
import fr.demo.app.step.LdifReader;
import fr.demo.app.step.UserReader;
import fr.demo.app.step.UserWriter;

public class UserJob {
	    
	    @Autowired
	    private JobBuilderFactory jobs;

	    @Autowired
	    private StepBuilderFactory steps;

	    @Bean
	    protected LdifReader LdifReader() {
	    	return new LdifReader();
	    }

	    
	    @Bean
	    protected Step step1(LdifReader ldifReader) {
	        return steps.get("step1").tasklet(ldifReader).build();
	    }
	    
	    @Bean
	    protected Step step2() {
	        return steps.get("step2").<User, User> chunk(100)
	          .reader(new UserReader()).writer(new UserWriter()).build();
	    }


	    @Bean(name = "UserJob")
	    public Job job(@Qualifier("step1") Step step1, @Qualifier("step2") Step step2) {
	        return jobs.get("UserJob").start(step1).next(step2).build();
	    }

}
