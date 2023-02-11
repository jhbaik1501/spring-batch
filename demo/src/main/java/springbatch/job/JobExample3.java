package springbatch.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.RequiredArgsConstructor;
import springbatch.jobExample3.ProductFieldSetMapper;
import springbatch.entity.Product;
import springbatch.entity.AvailableProduct;
import springbatch.jobExample3.ProductItemProcessor;

@Configuration
@RequiredArgsConstructor
public class JobExample3 {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	@Bean
	public Job extract(ApplicationArguments args) throws Exception {
		return jobBuilderFactory.get("extractJob")
			.start(extractStep1())
			.next(extractStep2())
			.incrementer(new RunIdIncrementer())
			.build();

	}

	@Bean
	public Step extractStep1() {
		return stepBuilderFactory.get("makeStep")
			.<Product, AvailableProduct>chunk(100)
			.reader(csvItemReader())
			.processor(new ProductItemProcessor())
			.writer(dbItemWriter())
			// .taskExecutor(taskExecutor())
			.build();
	}

	private Step extractStep2() {
		return stepBuilderFactory.get("makeStepPrint")
			.tasklet((contribution, chunkContext) -> {
				System.out.println(
					"성공, " + contribution.getStepExecution().getJobExecution().getExecutionContext().get("number")
						+ "개 데이터 INSERT");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	private ItemReader<? extends Product> csvItemReader() {
		return new FlatFileItemReaderBuilder<Product>()
			.name("Products")
			.resource(new ClassPathResource("product.csv"))
			.fieldSetMapper(new ProductFieldSetMapper())
			.delimited().delimiter(",")
			.names("id", "creationDate", "description", "expirationDate", "name", "price", "stock")
			.linesToSkip(1)
			.build();
	}

	private ItemWriter<? super AvailableProduct> dbItemWriter() {
		return new JpaItemWriterBuilder<>()
			.entityManagerFactory(entityManagerFactory)
			.build();
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	// @Bean
	// public TaskExecutor taskExecutor2() {
	// 	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	// 	executor.setCorePoolSize(5);
	// 	executor.setMaxPoolSize(5);
	// 	executor.setQueueCapacity(500);
	// 	executor.setThreadNamePrefix("async-");
	// 	executor.initialize();
	// 	return executor;
	// }
}
