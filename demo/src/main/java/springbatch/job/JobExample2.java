package springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
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
public class JobExample2 {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final ProductService productService;

	@Bean
	public Job make(ApplicationArguments args) throws Exception {
		return jobBuilderFactory.get("productMakeJob")
			.start(step1())
			.next(step2())
			// .incrementer(new RunIdIncrementer())
			.build();

	}

	@Bean
	public Step step1() {
		Tasklet tasklet = (contribution, chunkContext) -> {
			System.out.println("create data executed");
			ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();
			ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

			productService.addProductData(stepExecutionContext, jobExecutionContext);
			return RepeatStatus.FINISHED;
		};

		return stepBuilderFactory.get("makeStep")
			.tasklet(tasklet)
			.build();
	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("printStep")
			.tasklet(new Tasklet() {
				@Override
				public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

					ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
					System.out.println(jobExecutionContext.get("how much?") + "만큼의 실행이 끝났습니다!");
					return null;
				}
			})
			.build();
	}


	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}
}
