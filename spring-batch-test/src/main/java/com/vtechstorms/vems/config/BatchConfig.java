package com.vtechstorms.vems.config;

import com.vtechstorms.vems.dtos.EmployeeDTO;
import com.vtechstorms.vems.entities.Employee;
import com.vtechstorms.vems.service.HibernateBatchItemWriter;
import com.vtechstorms.vems.util.EmployeeItemProcessor;
import com.vtechstorms.vems.util.ExcelEmployeeItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    @StepScope
    public ItemReader<EmployeeDTO> reader(@Value("#{jobParameters['file']}") String filePath) throws Exception {
        InputStream inputStream = new FileInputStream(filePath);
        return new ExcelEmployeeItemReader(inputStream);  // returns EmployeeDTO
    }

    @Bean
    public ItemProcessor<EmployeeDTO, Employee> processor() {
        return new EmployeeItemProcessor();  // transforms DTO to Entity
    }

    @Bean
    public ItemWriter<Employee> writer() {
        return new HibernateBatchItemWriter();  // writes Entity
    }

    @Bean
    public Job importEmployeeJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importEmployeeJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      ItemReader<EmployeeDTO> reader,
                      ItemProcessor<EmployeeDTO, Employee> processor,
                      ItemWriter<Employee> writer) {

        return new StepBuilder("step1", jobRepository)
                .<EmployeeDTO, Employee>chunk(30, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
