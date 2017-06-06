package com.jared.nginx_log_parser;

import java.io.File;
import java.util.HashMap;

import com.jared.nginx.log.parser.OutputFile;

import junit.framework.Assert;
import junit.framework.TestCase;

public class OutputFileTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testOutput(){
		String filename = "/tmp/junit.log";
			
		HashMap<String, Integer> map = new HashMap();
		map.put("50x", 15);
		map.put("40x", 15);
		map.put("30x", 15);
		map.put("20x", 15);
		map.put("/foo/bar", 25);
		try {
			OutputFile outputFile = new OutputFile(filename);
			outputFile.output(map);
		} catch (Exception e) {
			Assert.assertEquals(false, true);
		}
		
		//count the errors number of ROUTES and compare to 50x errors to ensure they are the same
	}
}
