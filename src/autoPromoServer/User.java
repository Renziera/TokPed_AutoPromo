package autoPromoServer;

import java.util.ArrayList;
import java.util.List;

public class User {
	String username;
	String password;
	String namaToko;
	List<String> links = new ArrayList<>();
	byte linkIndex = 0;

	public User(String username, String password, List<String> links) {
		this.links.addAll(links);
		this.username = username;
		this.password = password;
	}

	protected String getLink() {
		String returnValue = links.get(linkIndex);
		addIteration();
		return returnValue;
	}
	protected void addIteration() {
		linkIndex++;
		if (linkIndex >= links.size()) {
			linkIndex = 0;
		}
	}
}
