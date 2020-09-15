package com.example.projetfilrouge.pskype.batchstatut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManagerFactory;

import com.example.projetfilrouge.pskype.domain.skypeprofile.StatusSkypeProfileEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;


import com.example.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

/**
 * Batch de mise à jour des statuts
 * @author Judicaël
 *
 */

@SpringBootApplication
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = {"com.example.projetfilrouge.pskype"})
@EntityScan("com.example.projetfilrouge.pskype")
@ComponentScan({"com.example.projetfilrouge.pskype"})
public class BatchStatutLoaderApplication implements CommandLineRunner{

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Value("${chunksize}")
	private int chunkSize;
	
	// variables globales dédiées à l'envoi du mail
	static String message ="";
	static int nbProfilUpdate = 0;
	static final int MAX_PROFILES = 20;
	
	static Logger log = LoggerFactory.getLogger(BatchStatutLoaderApplication.class);
	
	public static void main(String[] args) {
		
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		Date date = new Date();
		log.info(format.format(date));
		SpringApplication.run(BatchStatutLoaderApplication.class,args);

	}
	
	/**
	 * Le run est exécuté juste après le lancement de l'application
	 * Il permet d'instancier un nouveau jobid à chaque nouvelle exécution.
	 */
	@Override
	public void run(String... args) throws Exception {
		
		//Permet d'avoir une jobid différent à chaque exécution
		JobParameters parameters = new JobParametersBuilder()
				.addString("JOBID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		log.debug("chunsize : "+chunkSize);
		jobLauncher.run(updateStatus(),parameters);
		
	}


	@Bean
	public Job updateStatus() throws Exception {
		return jobBuilderFactory.get("updateStatus").listener(batchJobListener()).flow(step1()).end().build();
	}
	
	@Bean
	public BatchJobListener batchJobListener() {
		return new BatchJobListener();
	}
	
	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory

				.get("step1")
				.listener(batchStepListener())
				.<SkypeProfileEntity, SkypeProfileEntity>chunk(chunkSize)
				.reader(batchReader())
				.processor(batchProcessor())
				.writer(batchWriter())
				.listener(batchWriteListener())
				.build();
	}
	
	@Bean
	public BatchStepListener batchStepListener() {
		return new BatchStepListener();
	}
	
	@Bean
	public BatchProcessor batchProcessor() {
		return new BatchProcessor();
	}
	
	@Bean 
	BatchWriteListener batchWriteListener() {
		return new BatchWriteListener();
	}
	@Bean
	public JpaPagingItemReader<SkypeProfileEntity> batchReader() throws Exception {
		JpaPagingItemReader<SkypeProfileEntity> databaseReader = new JpaPagingItemReader<>();
		databaseReader.setEntityManagerFactory(entityManagerFactory);
		databaseReader.setQueryString("SELECT u FROM SkypeProfileEntity u WHERE u.statusProfile=com.example.projetfilrouge.pskype.domain.skypeprofile.StatusSkypeProfileEnum.ENABLED");
		databaseReader.setPageSize(1000);
		databaseReader.afterPropertiesSet();
		return databaseReader;
	}

	@Bean
	public JpaItemWriter<SkypeProfileEntity> batchWriter(){
		
		JpaItemWriter<SkypeProfileEntity> skypeProfileItemWriter = new JpaItemWriter<SkypeProfileEntity>();
		skypeProfileItemWriter.setEntityManagerFactory(entityManagerFactory);
		return skypeProfileItemWriter;
		
	}

}
