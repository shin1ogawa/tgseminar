package tgseminar.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;
import org.slim3.tester.TestEnvironment;

import com.google.appengine.api.datastore.Entity;
import com.google.apphosting.api.ApiProxy;

public class ListControllerTest extends ControllerTestCase {

	@Test
	public void test() throws Exception {

		tester.start("/List");

		assertThat(tester.response.getStatus(), is(200));
		System.out.println("------");
		System.out.println(tester.response.getOutputAsString());
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();

		TestEnvironment env = (TestEnvironment) ApiProxy
				.getCurrentEnvironment();
		env.setEmail("ogawa@exampl.com");

		Entity entity1 = new Entity(Datastore.createKey("ToDo", 1L));
		entity1.setProperty("title", "test1");
		entity1.setProperty("createdBy", "shin1@exampl.com");
		entity1.setProperty("createdAt", new Date());
		Datastore.put(entity1);

		Entity entity2 = new Entity(Datastore.createKey("ToDo", 2L));
		entity2.setProperty("title", "test2");
		entity2.setProperty("createdBy", "shin1@exampl.com");
		entity2.setProperty("createdAt", new Date());
		Datastore.put(entity2);
		
		Entity entity3 = new Entity(Datastore.createKey("ToDo", 3L));
		entity3.setProperty("title", "ogawa's test");
		entity3.setProperty("createdBy", "ogawa@exampl.com");
		entity3.setProperty("createdAt", new Date());
		Datastore.put(entity3);

	}
}
