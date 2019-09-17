package fr.training.spring.shop.batch.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.training.spring.shop.SpringBootBatchApp;

/**
 * End-To-End test job "import offer management from csv"
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootBatchApp.class)
@ImportResource("classpath:/orderexport-job.xml")
@EnableAutoConfiguration
public class OrderExportJobTest {
	private static final Logger LOG = LoggerFactory.getLogger(OrderExportJobTest.class);

	/** directory for temporary test files */
	private static final String TMP_DIR = "./tmp";

	@Autowired
	private Job orderExportJob;

	@Autowired
	private JobLauncher jobLauncher;

	@Before
	public void setup() throws Exception {

		// setup test data
		LOG.debug("Delete tmp directory");
		FileUtils.deleteDirectory(new File(TMP_DIR));
	}

	/**
	 * @throws Exception
	 *             thrown by JobLauncherTestUtils
	 */
	@Test
	public void testJob() throws Exception {
		File targetFile = new File(TMP_DIR + "/orders.csv");
		// File expectedFile = new
		// ClassPathResource("OrderExportJobTest/expected/orders.csv").getFile();

		// configure job
		JobParametersBuilder jobParameterBuilder = new JobParametersBuilder();
		jobParameterBuilder.addString("orders.file", targetFile.getAbsolutePath());
		JobParameters jobParameters = jobParameterBuilder.toJobParameters();

		// run job
		JobExecution jobExecution = getJobLauncherTestUtils(this.orderExportJob).launchJob(jobParameters);

		// check results
		// - job status
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

		// - exported data
		assertTrue(targetFile.exists());
		// assertTrue(expectedFile.exists());

		// AssertFile.assertFileEquals(expectedFile, targetFile);
	}

	private JobLauncherTestUtils getJobLauncherTestUtils(Job job) {

		JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
		jobLauncherTestUtils.setJob(job);
		jobLauncherTestUtils.setJobLauncher(this.jobLauncher);

		return jobLauncherTestUtils;
	}
}
