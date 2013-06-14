package tgseminar.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;
import org.slim3.tester.TestEnvironment;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.apphosting.api.ApiProxy;

public class UpdateControllerTest extends ControllerTestCase {

	@Test
	public void respond400IfIdNotSpecified() throws NullPointerException,
			IllegalArgumentException, IOException, ServletException {
		// tester.param("id", "1");
		tester.param("title", "To-Do #1");
		tester.start("/Update");

		// assert response from server is 400
		assertThat(tester.response.getStatus(), is(400));
	}

	@Test
	public void respond400IfIdSpecifiedIsNotNumeric()
			throws NullPointerException, IllegalArgumentException, IOException,
			ServletException {
		tester.param("id", "a");
		tester.param("title", "To-Do #1");
		tester.start("/Update");

		// assert response from server is 400
		assertThat(tester.response.getStatus(), is(400));
	}

	@Test
	public void respond400IfTitleNotSpecified() throws NullPointerException,
			IllegalArgumentException, IOException, ServletException {
		tester.param("id", "1");
		// tester.param("title", "To-Do #1");
		tester.start("/Update");

		// assert response from server is 400
		assertThat(tester.response.getStatus(), is(400));
	}

	@Test
	public void respond404IfEntityIsNotExist() throws NullPointerException,
			IllegalArgumentException, IOException, ServletException {
		tester.param("id", "3");
		tester.param("title", "To-Do #3 modified");
		tester.start("/Update");

		assertThat(tester.response.getStatus(), is(404));
	}

	@Test
	public void respond403IfEntityIsAnotherUsers() throws NullPointerException,
			IllegalArgumentException, IOException, ServletException {

		Entity entity = Datastore.getOrNull(Datastore.createKey("ToDo", 2));
		assertThat("Pre-condition", entity, is(notNullValue()));
		assertThat("Pre-condition", (String) entity.getProperty("createdBy"),
				is(not("loginuser@example.com")));

		tester.param("id", "2");
		tester.param("title", "To-Do #2 modified");
		tester.start("/Update");

		assertThat(tester.response.getStatus(), is(403));
	}

	@Test
	public void respond200() throws NullPointerException,
			IllegalArgumentException, IOException, ServletException {
		tester.param("id", "1");
		tester.param("title", "To-Do #1 modified");
		tester.start("/Update");

		assertThat(tester.response.getStatus(), is(200));
	}

	@Test
	public void success() throws NullPointerException,
			IllegalArgumentException, IOException, ServletException {
		String modifiedTitle = "To-Do #1 modified";

		tester.param("id", "1");
		tester.param("title", modifiedTitle);
		tester.start("/Update");

		Key key = Datastore.createKey("ToDo", 1);
		Entity entity = Datastore.get(key);
		assertThat((String) entity.getProperty("title"), is(modifiedTitle));
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();

		TestEnvironment environment = (TestEnvironment) ApiProxy
				.getCurrentEnvironment();
		environment.setEmail("loginuser@example.com");

		Entity entity1 = new Entity(Datastore.createKey("ToDo", 1));
		entity1.setProperty("title", "To-Do #1");
		entity1.setProperty("createdBy", "loginuser@example.com");
		entity1.setProperty("createdAt", new Date());

		Entity entity2 = new Entity(Datastore.createKey("ToDo", 2));
		entity2.setProperty("title", "To-Do #2");
		entity2.setProperty("createdBy", "anotheruser@example.com");
		entity2.setProperty("createdAt", new Date());

		Datastore.put(entity1, entity2);
	}

}
