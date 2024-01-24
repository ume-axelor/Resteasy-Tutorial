package com.example;

import javax.ws.rs.core.Application;
import com.example.resource.HelloResource;
import java.util.HashSet;
import java.util.Set;

public class App extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();

	public App() {
		singletons.add(new HelloResource());
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
