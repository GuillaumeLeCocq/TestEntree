package fr.demo.app.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;


  private JobRepository getJobRepository() throws Exception {
      JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
      
      factory.setTransactionManager(getTransactionManager());
      factory.afterPropertiesSet();
      return (JobRepository) factory.getObject();
  }

  private PlatformTransactionManager getTransactionManager() {
      return new ResourcelessTransactionManager();
  }

  public JobLauncher getJobLauncher() throws Exception {
      SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
      jobLauncher.setJobRepository(getJobRepository());
      jobLauncher.afterPropertiesSet();
      return jobLauncher;
  }

}