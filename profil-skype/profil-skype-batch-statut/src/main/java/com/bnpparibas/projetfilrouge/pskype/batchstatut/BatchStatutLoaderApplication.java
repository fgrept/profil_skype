package com.bnpparibas.projetfilrouge.pskype.batchstatut;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.skypeprofile.SkypeProfileEntity;

/**
 * Batch de mise à jour des statuts
 * @author Judicaël
 *
 */

@SpringBootApplication
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = {"com.bnpparibas.projetfilrouge.pskype"})
@EntityScan("com.bnpparibas.projetfilrouge.pskype")
public class BatchStatutLoaderApplication implements CommandLineRunner{

	@Autowired
	private JobLauncher jobLauncher;
	
	public static void main(String[] args) {
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		//obtenir la date courante
		Date date = new Date();
		System.out.println(format.format(date));
		SpringApplication.run(BatchStatutLoaderApplication.class,args);

	}
	
	/**
	 * Le run est exécuté juste après le lancement de l'application
	 * Il permet d'instancier un nouveau jobid à chaque nouvelle exécution.
	 */
	@Override
	public void run(String... args) throws Exception {
		
		JobParameters parameters = new JobParametersBuilder()
				.addString("JOBID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(readUser(),parameters);
		
	}
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Bean
	public Job readUser() throws Exception {
		return jobBuilderFactory.get("readUser").flow(step1()).end().build();
	}
	
	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory
				//Le chunk correspond à un pas de commit => ici tous les 10 items lus
				//A adapter et à externaliser !!!
				.get("step1").<SkypeProfileEntity, SkypeProfileEntity>chunk(10)
				.reader(batchReader())
				.processor(batchProcessor())
				.writer(batchWriter()).build();
	}
	
	@Bean
	public BatchProcessor batchProcessor() {
		return new BatchProcessor();
	}
	
	@Bean
	public JpaPagingItemReader<SkypeProfileEntity> batchReader() throws Exception {
		JpaPagingItemReader<SkypeProfileEntity> databaseReader = new JpaPagingItemReader<>();
		databaseReader.setEntityManagerFactory(entityManagerFactory);
		//JpaQueryProviderImpl<ItCorrespondantEntity> jpaQueryProvider = new JpaQueryProviderImpl<>();
	//	jpaQueryProvider.setQuery("User.findAll");
	//	databaseReader.setQueryProvider();
		databaseReader.setQueryString("SELECT u FROM SkypeProfileEntity u WHERE u.statusProfile=com.bnpparibas.projetfilrouge.pskype.domain.StatusSkypeProfileEnum.ENABLED");
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
