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

public class NginxLogParser {

	public static int SLEEP_INTERVAL = 5000;

	Pattern logPattern = Pattern.compile(
			"(^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})[-\\s]+(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).+?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).+?\\[(.+?)\\]\\s.?(.+?)\\s(.+?)\\s(.+?)\\s.*?\\\"(.+?)\\\"\\s(\\d{3})\\s(\\d{1,10})\\s\\\".+?\\\".*$");

	public static void main(String[] args) {
		String nginxLog = "/var/log/nginx/sample.log";
		try {
			long filePosition = 0;
			while (true) {
				NginxLogParser parser = new NginxLogParser();
				System.out.println("filePosition + " + filePosition);
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
	 * @param pathToLog
	 * @param position
	 * @return
	 * @throws Exception
	 */
	public long parseLog(String pathToLog, long position) throws Exception {
		RandomAccessFile file = new RandomAccessFile(pathToLog, "r");
		//seek to current position in the log file
		file.seek(position);
		OutputFile outputFile = new OutputFile();
		Map<String, Integer> outputMap = new HashMap<String, Integer>();
		String line = file.readLine();
		while(line != null){
			addEntryToOutputMap(outputMap, parseLogEntry(line));
			line = file.readLine();
		}
		
		//output the results of log parsing
		outputFile.output(outputMap);
		file.close();
		return file.getFilePointer();
	}

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
	 * 
	 * @param map
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
					incrementMapVal(map, routeKey);
				}
			}
		}
	}

	public void incrementMapVal(Map<String, Integer> map, String key) {
		Integer count = map.get(key);
		if (count != null && count > 0) {
			map.put(key, ++count);
		} else {
			map.put(key, 1);
		}
	}

	/**
	 * Get the status key
	 * 
	 * @param status
	 * @return status key for the given response status
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
