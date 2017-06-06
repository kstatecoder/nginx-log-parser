package com.jared.nginx_log_parser;

import com.jared.nginx.log.parser.NginxLogParser;

import junit.framework.Assert;
import junit.framework.TestCase;

public class NginxLogParserTest extends TestCase {
	NginxLogParser parser = new NginxLogParser();
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/*
	 * Test the key generation for certain status codes
	 */
	public void testGetKeyFromStatus() {
		int status = 201;
		String key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "20x");
	
		status = 200;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "20x");
		
		status = 299;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "20x");
		
		status = 301;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "30x");
	
		status = 300;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "30x");
		
		status = 399;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "30x");
		
		status = 401;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "40x");
	
		status = 400;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "40x");
		
		status = 499;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "40x");
		
		status = 501;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "50x");
	
		status = 500;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "50x");
		
		status = 599;
		key = parser.getKeyFromStatus(status);
		Assert.assertEquals(key, "50x");
		
		status = 600;
		key = parser.getKeyFromStatus(status);
		Assert.assertNull(key);
	}
}
