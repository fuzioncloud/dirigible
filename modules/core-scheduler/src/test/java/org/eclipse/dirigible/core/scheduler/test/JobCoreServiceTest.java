package org.eclipse.dirigible.core.scheduler.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.dirigible.core.scheduler.api.ISchedulerCoreService;
import org.eclipse.dirigible.core.scheduler.api.SchedulerException;
import org.eclipse.dirigible.core.scheduler.service.SchedulerCoreService;
import org.eclipse.dirigible.core.scheduler.service.definition.JobDefinition;
import org.eclipse.dirigible.core.test.AbstractGuiceTest;
import org.junit.Before;
import org.junit.Test;

public class JobCoreServiceTest extends AbstractGuiceTest {
	
	@Inject
	private ISchedulerCoreService jobCoreService;
	
	@Before
	public void setUp() throws Exception {
		this.jobCoreService = getInjector().getInstance(SchedulerCoreService.class);
	}
	
	@Test
	public void createJob() throws SchedulerException {
		jobCoreService.removeJob("test_job1");
		jobCoreService.createJob("test_job1", "test_group", "org....", "Test", "expr...", false);
		List<JobDefinition> list = jobCoreService.getJobs();
		assertEquals(1, list.size());
		JobDefinition jobDefinition = list.get(0);
		System.out.println(jobDefinition.toString());
		assertEquals("test_job1", jobDefinition.getName());
		assertEquals("Test", jobDefinition.getDescription());
		jobCoreService.removeJob("test_job1");
	}
	
	@Test
	public void getJob() throws SchedulerException {
		jobCoreService.removeJob("test_job1");
		jobCoreService.createJob("test_job1", "test_group", "org....", "Test", "expr...", false);
		JobDefinition extensionPointDefinition = jobCoreService.getJob("test_job1");
		assertEquals("test_job1", extensionPointDefinition.getName());
		assertEquals("Test", extensionPointDefinition.getDescription());
		jobCoreService.removeJob("test_job1");
	}
	
	@Test
	public void updatetJob() throws SchedulerException {
		jobCoreService.removeJob("test_job1");
		jobCoreService.createJob("test_job1", "test_group", "org....", "Test", "expr...", false);
		JobDefinition extensionPointDefinition = jobCoreService.getJob("test_job1");
		assertEquals("test_job1", extensionPointDefinition.getName());
		assertEquals("Test", extensionPointDefinition.getDescription());
		jobCoreService.updateJob("test_job1", "test_group", "org....", "Test 2", "expr...", false);
		extensionPointDefinition = jobCoreService.getJob("test_job1");
		assertEquals("test_job1", extensionPointDefinition.getName());
		assertEquals("Test 2", extensionPointDefinition.getDescription());
		jobCoreService.removeJob("test_job1");
	}
	
	@Test
	public void removeJob() throws SchedulerException {
		jobCoreService.removeJob("test_job1");
		jobCoreService.createJob("test_job1", "test_group", "org....", "Test", "expr...", false);
		JobDefinition extensionPointDefinition = jobCoreService.getJob("test_job1");
		assertEquals("test_job1", extensionPointDefinition.getName());
		assertEquals("Test", extensionPointDefinition.getDescription());
		jobCoreService.removeJob("test_job1");
		extensionPointDefinition = jobCoreService.getJob("test_job1");
		assertNull(extensionPointDefinition);
	}
	
	
	
	
	
}
