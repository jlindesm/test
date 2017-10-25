package com.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.audit.VariableInstanceLog;
import org.kie.api.runtime.process.ProcessInstance;

public class MultiInstanceTest extends JbpmJUnitBaseTestCase {

	private static List<String> list = new ArrayList<String>();
	
	public MultiInstanceTest() {
		super(true, true);
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		list.add("Test String 1");
		list.add("Test String 2");
		list.add("Test String 3");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testMultiInstance() {
		
		createRuntimeManager("com/sample/multiinstance.bpmn2");
		RuntimeEngine engine = getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		Map<String, Object> processVars = new HashMap<>();
		processVars.put("list", list);
		ProcessInstance pi = ksession.startProcess("com.sample.multiInstance",
				processVars);
		List<? extends VariableInstanceLog> logs = getLogService().findVariableInstances(pi.getId(), "outputList");
		assertTrue(logs.get(0).getValue().
				contains("Output Item for Test String 1, Output Item for Test String 2, Output Item for Test String 3"));
		disposeRuntimeManager();
	}

}
