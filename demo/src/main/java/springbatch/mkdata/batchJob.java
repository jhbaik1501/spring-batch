package springbatch.mkdata;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import lombok.RequiredArgsConstructor;
import springbatch.service.ProductService;

@Configuration
@RequiredArgsConstructor
public class batchJob {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final ProductService productService;

	@Bean
	public Job make(ApplicationArguments args) throws Exception {
		return jobBuilderFactory.get("job1")
			.start(step1())
			.incrementer(new RunIdIncrementer())
			.build();

	}

	@Bean
	public Step step1() {
		Tasklet tasklet = (contribution, chunkContext) -> {
			System.out.println("create data executed");
			productService.addProductData();
			return RepeatStatus.FINISHED;
		};

		return stepBuilderFactory.get("step1")
			.tasklet(tasklet)
			.build();
	}


	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}
}
