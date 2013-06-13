package tgseminar.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;
import org.slim3.repackaged.org.json.JSONObject;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserServiceFactory;

public class ListController extends Controller {

	@Override
	protected Navigation run() throws Exception {

		String email = UserServiceFactory.getUserService().getCurrentUser()
				.getEmail();

		// retrieve entities for logged in user
		List<Entity> entities = Memcache.get(email);
		if (entities == null) {
			// not hit then run query
			entities = Datastore.query("ToDo")
					.filter("createdBy", FilterOperator.EQUAL, email)
					.sort("createdAt", SortDirection.DESCENDING).asList();
		} else {
			// hit
			System.out.println("Hit!");
		}

		// build object to respond json
		List<Map<String, Object>> entitiesForJSON = new ArrayList<Map<String, Object>>();
		for (Entity entity : entities) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("id", entity.getKey().getId());
			map.put("title", entity.getProperty("title"));
			map.put("createdAt", entity.getProperty("createdAt"));
			entitiesForJSON.add(map);
		}

		// set response headers
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		// convert object to json
		String json = JSONObject.valueToString(entitiesForJSON);
		response.getWriter().println(json);

		response.flushBuffer();

		return null;
	}

}
