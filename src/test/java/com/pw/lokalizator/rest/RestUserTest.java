package com.pw.lokalizator.rest;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.junit.BeforeClass;

public class RestUserTest {
	
	private Dispatcher dispatcher;
	
	@BeforeClass
	public void beforeClass(){
		dispatcher = MockDispatcherFactory.createDispatcher();
	}
}
