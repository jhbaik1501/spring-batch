package springbatch.job;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ChunkProgram {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job chunkEx(ApplicationArguments args) throws Exception {
		return jobBuilderFactory.get("chunkEx")
			.start(chunkStep1())
			.incrementer(new RunIdIncrementer())
			.build();

	}

	@Bean
	public Step chunkStep1() {

		List<String> s = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k");
		Iterator<String> stringIterator = s.listIterator();
		return stepBuilderFactory.get("chunkStep1")
			.<String, String>chunk(2)
			.reader(new ItemReader<String>() {
				@Override
				public String read() throws
					Exception,
					UnexpectedInputException,
					ParseException,
					NonTransientResourceException {
					String next;
					try {
						next = stringIterator.next();
					} catch (Exception e) {
						return null;
					}
					return next;
				}
			})
			.writer(new ItemWriter<String>() {
				@Override
				public void write(List<? extends String> items) throws Exception {
					System.out.println(items);
				}
			})
			// .taskExecutor(taskExecutor())
			.build();
	}
}
