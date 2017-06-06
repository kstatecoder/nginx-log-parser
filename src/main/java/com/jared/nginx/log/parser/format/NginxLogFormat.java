package com.jared.nginx.log.parser.format;

public class NginxLogFormat {
	
	public static class DefaultFormat{
		public static int REMOTE_ADDR_INDEX = 0;
		public static int HTTP_X_FORWARDED_FOR = 1;
		public static int HTTP_X_REALIP = 2;
		public static int TIME_LOCAL = 3;
		public static int SCHEME = 4;
		public static int HTTP_X_FORWARDED_PROTO = 5;
		public static int X_FORWARDED_PROTO_OR_SCHEME = 6;
		public static int REQUEST = 7;
		public static int STATUS = 8;
		public static int BODY_BYTES_SENT = 9;	
		
		public static int SIZE = 9;
	}
	
	public static String getPathFromRequest(String request){
		// routeKey
		// Just get path out of the request/route
		String[] splits = request.split("\\s+");
		
		
		if (splits.length > 1) {
			return splits[1];
		} else {
			return null;
		}
	}
}
