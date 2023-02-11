package springbatch.job;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class JobExample2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        StepDecider decider = new StepDecider();

        return jobBuilderFactory.get("flowJob")
                .incrementer(new RunIdIncrementer())
                .start(flowJobStep1())
                // .next(decider)
                .on("COMPLETED").to(flowJobStep2())
                // .from(decider)
                .from(flowJobStep1())
                .on("FAILED").to(flowJobStep3())
                .end()
                .build();
    }

    @Bean
    public Step flowJobStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 has executed");
//                        throw new RuntimeException("");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step flowJobStep2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 has executed");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step flowJobStep3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step3 has executed");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    public class StepDecider implements JobExecutionDecider{
        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
            Object ex = jobExecutionContext.get("ex");
            if (ex == null) {
                return FlowExecutionStatus.FAILED;
            }
            return FlowExecutionStatus.COMPLETED;
        }
    }


    public class ExeuctionListener implements StepExecutionListener {
        @Override
        public void beforeStep(StepExecution stepExecution) {

        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            return ExitStatus.FAILED;
        }
    }
}
