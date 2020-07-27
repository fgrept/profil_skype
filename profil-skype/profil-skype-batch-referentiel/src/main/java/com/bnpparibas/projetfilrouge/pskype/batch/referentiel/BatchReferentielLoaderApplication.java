package com.bnpparibas.projetfilrouge.pskype.batch.referentiel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import com.bnpparibas.projetfilrouge.pskype.batch.referentiel.dto.CollaboraterDtoBatch;
import com.bnpparibas.projetfilrouge.pskype.batch.referentiel.dto.ItCorrespondantDto;
import com.bnpparibas.projetfilrouge.pskype.batch.referentiel.dto.OrganizationUnityDtoBatch;
import com.bnpparibas.projetfilrouge.pskype.domain.Collaborater;
import com.bnpparibas.projetfilrouge.pskype.domain.OrganizationUnity;
import com.bnpparibas.projetfilrouge.pskype.domain.Site;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.CollaboraterEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.ItCorrespondantEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.OrganizationUnityEntity;
import com.bnpparibas.projetfilrouge.pskype.infrastructure.user.SiteEntity;
/**
 * Batch de chargement des données du référentiel.
 * Il comporte 3 fichiers en entrée : site, uo et collaborateur.
 * Les fichiers sont traités séquentiellement pour mettre à jour les tables correspondantes.
 * @author Judicaël
 *
 */
@SpringBootApplication
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = {"com.bnpparibas.projetfilrouge.pskype"})
@EntityScan("com.bnpparibas.projetfilrouge.pskype")
public class BatchReferentielLoaderApplication implements CommandLineRunner {
	
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
	
	static Logger log = LoggerFactory.getLogger(BatchReferentielLoaderApplication.class);
	
	public static void main(String[] args) {
		
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		Date date = new Date();
		log.info(format.format(date));
		SpringApplication.run(BatchReferentielLoaderApplication.class,args);

	}
	
	@Override
	public void run(String... args) throws Exception {
		
		//Permet d'avoir une jobid différent à chaque exécution
		JobParameters parameters = new JobParametersBuilder()
				.addString("JOBID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		log.debug("chunsize : "+chunkSize);
		jobLauncher.run(loadReferentiel(),parameters);

	}
	
	@Bean
	public Job loadReferentiel() throws Exception {
		return jobBuilderFactory.get("loadReferentiel")
				.listener(batchJobListener())
				.start(stepSite())
				.next(stepUo())
				.next(stepCollaborater())
				.next(stepItCorrespondant())
//				.end()
				.build();
	}
	
	@Bean
	public Step stepItCorrespondant() {
		return stepBuilderFactory

				.get("stepItCorrespondant")
				.listener(batchStepListener())
				.<ItCorrespondantDto, ItCorrespondantEntity>chunk(chunkSize)
				.reader(batchItCorrespondantReader())
				.processor(batchItCorrespondantProcessor())
				.writer(batchItCorrespondantWriter())
				.build();
	}

	@Bean
	public Step stepSite() {
		
		return stepBuilderFactory

				.get("stepSite")
				.listener(batchStepListener())
				.<Site, SiteEntity>chunk(chunkSize)
				.reader(batchSiteReader())
				.processor(batchSiteProcessor())
				.writer(batchSiteWriter())
				.build();
	}
	@Bean
	public JpaItemWriter<SiteEntity> batchSiteWriter() {
		
		JpaItemWriter<SiteEntity> siteEntity = new JpaItemWriter<SiteEntity>();
		siteEntity.setEntityManagerFactory(entityManagerFactory);
		return siteEntity;
	}

	@Bean
	public BatchSiteProcessor batchSiteProcessor() {
		// TODO Auto-generated method stub
		return new BatchSiteProcessor();
	}
	
	@Bean
	public BatchItCorrespondantProcessor batchItCorrespondantProcessor() {
		// TODO Auto-generated method stub
		return new BatchItCorrespondantProcessor();
	}

	@Bean
	public FlatFileItemReader<Site> batchSiteReader() {
		// TODO Auto-generated method stub
		FlatFileItemReaderBuilder<Site> fileReaderBuilder = new FlatFileItemReaderBuilder<Site>();
		fileReaderBuilder.name("siteItemReader")
		.resource(new FileSystemResource("src/main/resources/input/site.csv"))
		.delimited()
		.delimiter(";")
		.names(new String[] {"siteCode","siteName","siteAddress","sitePostalCode","siteCity"})
		.linesToSkip(1)
		.fieldSetMapper(new BeanWrapperFieldSetMapper<Site>() {
			{
				setTargetType(Site.class);
			}
		});
		return fileReaderBuilder.build();
	}
	
	@Bean
	public FlatFileItemReader<ItCorrespondantDto> batchItCorrespondantReader() {
		// TODO Auto-generated method stub
		FlatFileItemReaderBuilder<ItCorrespondantDto> fileReaderBuilder = new FlatFileItemReaderBuilder<ItCorrespondantDto>();
		fileReaderBuilder.name("siteItCorrespondantReader")
		.resource(new FileSystemResource("src/main/resources/input/itcorrespondant.csv"))
		.delimited()
		.delimiter(";")
		.names(new String[] {"idAnnuaire","password","role"})
		.linesToSkip(1)
		.fieldSetMapper(new BeanWrapperFieldSetMapper<ItCorrespondantDto>() {
			{
				setTargetType(ItCorrespondantDto.class);
			}
		});
		return fileReaderBuilder.build();
	}

	@Bean
	public BatchStepListener batchStepListener() {
		return new BatchStepListener();
	}
	
	@Bean
	public Step stepUo() {
		
		return stepBuilderFactory

				.get("stepUo")
				.listener(batchStepListener())
				.<OrganizationUnityDtoBatch, OrganizationUnityEntity>chunk(chunkSize)
				.reader(batchUoReader())
				.processor(batchUoProcessor())
				.writer(batchUoWriter())
				.build();
	}
	

	@Bean
	public FlatFileItemReader<OrganizationUnityDtoBatch> batchUoReader() {
		
		FlatFileItemReaderBuilder<OrganizationUnityDtoBatch> fileReaderBuilder = new FlatFileItemReaderBuilder<OrganizationUnityDtoBatch>();
		fileReaderBuilder.name("uoItemReader")
		.resource(new FileSystemResource("src/main/resources/input/uo.csv"))
		.delimited()
		.delimiter(";")
		.names(new String[] {"orgaUnityCode","orgaUnityType","orgaShortLabel","siteCode"})
		.linesToSkip(1)
		.fieldSetMapper(new BeanWrapperFieldSetMapper<OrganizationUnityDtoBatch>() {
			{
				setTargetType(OrganizationUnityDtoBatch.class);
			}
		});
		return fileReaderBuilder.build();
	}
	@Bean
	public BatchUoProcessor batchUoProcessor() {
		// TODO Auto-generated method stub
		return new BatchUoProcessor();
	}
	@Bean
	public JpaItemWriter<OrganizationUnityEntity> batchUoWriter() {
		
		JpaItemWriter<OrganizationUnityEntity> orgaEntity = new JpaItemWriter<OrganizationUnityEntity>();
		orgaEntity.setEntityManagerFactory(entityManagerFactory);
		return orgaEntity;
	}

	
	@Bean
	public Step stepCollaborater() {
		
		return stepBuilderFactory

				.get("stepCollaborater")
				.listener(batchStepListener())
				.<CollaboraterDtoBatch, CollaboraterEntity>chunk(chunkSize)
				.reader(batchCollaboraterReader())
				.processor(batchCollaboraterProcessor())
				.writer(batchCollaboraterWriter())
				.build();
	}
	
	@Bean
	public FlatFileItemReader<CollaboraterDtoBatch> batchCollaboraterReader() {
		
		FlatFileItemReaderBuilder<CollaboraterDtoBatch> fileReaderBuilder = new FlatFileItemReaderBuilder<CollaboraterDtoBatch>();
		fileReaderBuilder.name("collaboraterItemReader")
		.resource(new FileSystemResource("src/main/resources/input/collaborater.csv"))
		.delimited()
		.delimiter(";")
		.names(new String[] {"collaboraterId","lastName","firstName","deskPhoneNumber","mobilePhoneNumber","mailAdress", "orgaUnityCode"})
		.linesToSkip(1)
		.fieldSetMapper(new BeanWrapperFieldSetMapper<CollaboraterDtoBatch>() {
			{
				setTargetType(CollaboraterDtoBatch.class);
			}
		});
		return fileReaderBuilder.build();
	}
	@Bean
	public BatchCollaboraterProcessor batchCollaboraterProcessor() {
		
		return new BatchCollaboraterProcessor();
	}
	@Bean
	public JpaItemWriter<CollaboraterEntity> batchCollaboraterWriter() {
		
		JpaItemWriter<CollaboraterEntity> collaboraterEntity = new JpaItemWriter<CollaboraterEntity>();
		collaboraterEntity.setEntityManagerFactory(entityManagerFactory);
		return collaboraterEntity;
	}
	
	@Bean
	public JpaItemWriter<ItCorrespondantEntity> batchItCorrespondantWriter() {
		
		JpaItemWriter<ItCorrespondantEntity> collaboraterEntity = new JpaItemWriter<ItCorrespondantEntity>();
		collaboraterEntity.setEntityManagerFactory(entityManagerFactory);
		return collaboraterEntity;
	}
	
	@Bean
	public JobExecutionListener batchJobListener() {
		
		return new BatchJobListener();
	}

}
