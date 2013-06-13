package tgseminar.controller;

import java.util.Date;
import java.util.List;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.users.UserServiceFactory;

public class PostController extends Controller {

	@Override
	protected Navigation run() throws Exception {
		String title = super.request.getParameter("title");
		if (title == null || "".equals(title)) {
			super.response.setStatus(400);
			return null;
		}

		String createdBy = UserServiceFactory.getUserService().getCurrentUser()
				.getEmail();

		Date createdAt = new Date();

		Entity entity = new Entity("ToDo");
		entity.setProperty("title", title);
		entity.setProperty("createdBy", createdBy);
		entity.setProperty("createdAt", createdAt);

		Datastore.put(entity);

		List<Entity> entities = Datastore.query("ToDo")
				.filter("createdBy", FilterOperator.EQUAL, createdBy)
				.sort("createdAt", SortDirection.DESCENDING).asList();
		Memcache.put(createdBy, entities, Expiration.byDeltaSeconds(60));

		super.response.setStatus(201);
		return null;
	}

}
