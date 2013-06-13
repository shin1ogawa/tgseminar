package tgseminar.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;
import org.slim3.tester.TestEnvironment;

import com.google.apphosting.api.ApiProxy;

public class PostControllerTest extends ControllerTestCase {

	@Test
	public void reponds400() throws Exception {
		super.tester.start("/Post");
		assertThat(tester.response.getStatus(), is(400));
	}

	@Test
	public void reponds200() throws Exception {
		super.tester.param("title", "test");
		super.tester.start("/Post");
		assertThat(tester.response.getStatus(), is(201));
	}

	@Test
	public void save() throws Exception {
		int before = tester.count("ToDo");

		super.tester.param("title", "test");
		super.tester.start("/Post");

		int after = tester.count("ToDo");

		assertThat(after, is(before + 1));
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();

		TestEnvironment env = (TestEnvironment) ApiProxy
				.getCurrentEnvironment();
		env.setEmail("shin1@exampl.com");
	}

}
