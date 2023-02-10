package springbatch.jobExample3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.core.io.ClassPathResource;

import springbatch.entity.AvailableProduct;
import springbatch.entity.Product;

public class ProductItemProcessor implements ItemProcessor<Product, AvailableProduct>, StepExecutionListener {

	private StepExecution stepExecution;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return ExitStatus.COMPLETED;
	}

	@Override
	public AvailableProduct process(Product item) throws Exception {
		List<String> includeProduct = includeProduct();
		if(includeProduct.contains(item)) {
			return new AvailableProduct(item);
		}

		if (item.getExpirationDate().compareTo(new Date()) != 1) return null;

		ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
		try {
			jobExecutionContext.put("number", (int)jobExecutionContext.get("number") + 1);
		} catch (NullPointerException nullPointerException) {
			jobExecutionContext.put("number", 1);
		}
		return new AvailableProduct(item);
	}

	private List<String> includeProduct() throws IOException {
		ClassPathResource resource = new ClassPathResource("exclude.csv");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
		String readLine = bufferedReader.readLine();
		return Arrays.stream(readLine.split(",")).toList();
	}
}
