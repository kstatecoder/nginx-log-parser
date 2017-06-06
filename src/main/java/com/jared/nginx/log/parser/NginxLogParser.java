package com.jared.nginx.log.parser;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.jared.nginx.log.parser.format.NginxLogFormat;

public class NginxLogParser {

	public static int SLEEP_INTERVAL = 5000;

	Pattern logPattern = Pattern.compile(
			"(^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})[-\\s]+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).+?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).+?\\[(.+?)\\]\\s.?(.+?)\\s(.+?)\\s(.+?)\\s.*?\\\"(.+?)\\\"\\s(\\d{3})\\s(\\d{1,10})\\s\\\".+?\\\".*$");

	public static void main(String[] args) {

		String nginxLog = "/var/log/nginx/access.log";
		try {
			File nginxLogFile = new File(nginxLog);

			if (!nginxLogFile.exists()) {
				System.out.println("Nginx log file " + nginxLog + " doesn't exist shutting down");
				System.exit(1);
			}
			// check to see if we can read the file
			if (!nginxLogFile.canRead()) {
				System.out.println("Can't read log file at " + nginxLog + " shutting down");
				System.exit(1);
			}

			long filePosition = 0;
			while (true) {
				NginxLogParser parser = new NginxLogParser();
				filePosition = parser.parseLog(nginxLog, filePosition);
				Thread.sleep(SLEEP_INTERVAL);
			}
		} catch (IOException e) {
			System.out.println("IOException - Error processing nginx file at " + nginxLog);
		} catch (InterruptedException e) {
			System.out.println("InterruptedException : Error processing nginx file at " + nginxLog + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception : Error processing nginx file at " + nginxLog + e.getMessage());
		}
	}

	/**
	 * This allows parsing a nginx log file with random access
	 * 
	 * @param pathToLog
	 * @param position
	 * @return
	 * @throws Exception
	 */
	public long parseLog(String pathToLog, long position) throws Exception {
		RandomAccessFile file = new RandomAccessFile(pathToLog, "r");
		try {
			// seek to current position in the log file
			file.seek(position);
			OutputFile outputFile = new OutputFile();
			Map<String, Integer> outputMap = new HashMap<String, Integer>();
			String line = file.readLine();
			while (line != null) {
				addEntryToOutputMap(outputMap, parseLogEntry(line));
				line = file.readLine();
			}

			// output the results of log parsing
			outputFile.output(outputMap);
			return file.getFilePointer();
		} finally {
			file.close();
		}
	}

	/**
	 * Method to parse the Nginx log defined by the regex pattern denoted above
	 * 
	 * @param entry
	 *            nginx log line/entry
	 * @return ArrayList of elements defined by the log entry
	 */
	public ArrayList<String> parseLogEntry(String entry) {
		Matcher matcher = logPattern.matcher(entry);
		ArrayList<String> logElements = new ArrayList<String>();
		if (matcher.matches()) {
			int groupsSize = matcher.groupCount();

			for (int index = 1; index < groupsSize; index++) {
				logElements.add(matcher.group(index));
			}
		}
		return logElements;
	}

	/**
	 * Helper for adding a log entry to the map to be generate the output
	 * 
	 * @param map
	 *            map that will house key/value pairs Status Code/Route and
	 *            their total value for now
	 * @param entryData
	 */
	public void addEntryToOutputMap(Map<String, Integer> map, ArrayList<String> entryData) {
		if (map != null && entryData != null && entryData.size() >= NginxLogFormat.DefaultFormat.SIZE) {
			int status = Integer.valueOf(entryData.get(NginxLogFormat.DefaultFormat.STATUS));
			String statusKey = getKeyFromStatus(status);

			if (statusKey != null) {
				incrementMapVal(map, statusKey);
				// if a 50x add route and increment the occurances
				if (statusKey.equals("50x")) {
					String routeKey = entryData.get(NginxLogFormat.DefaultFormat.REQUEST);

					// routeKey
					// Just get path out of the request/route
					String[] splits = routeKey.split("\\s+");
					if (splits.length > 1) {
						incrementMapVal(map, splits[1]);
					}
				}
			}
		}
	}

	/**
	 * Helper method to tabulate output values
	 * 
	 * @param map
	 *            Gather of the output values
	 * @param key
	 *            Status (ex. 20x) or Route Key (ex. /some-path)
	 */
	public void incrementMapVal(Map<String, Integer> map, String key) {
		Integer count = map.get(key);
		if (count != null && count > 0) {
			map.put(key, ++count);
		} else {
			map.put(key, 1);
		}
	}

	/**
	 * Get the status key that will be used for a status (ex. 200)
	 * 
	 * @param status
	 *            HTTP status code
	 * @return status key for the given response status (ex. 20x)
	 */
	public String getKeyFromStatus(int status) {

		if (status >= 200 && status <= 299) {
			return "20x";
		} else if (status >= 300 && status <= 399) {
			return "30x";
		} else if (status >= 400 && status <= 499) {
			return "40x";
		} else if (status >= 500 && status <= 599) {
			return "50x";
		}

		return null;
	}
}
