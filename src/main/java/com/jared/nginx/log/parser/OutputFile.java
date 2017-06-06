package com.jared.nginx.log.parser;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class OutputFile {

	File outputFile;

	public OutputFile() throws Exception {
		outputFile = new File("/var/log/stats.log");
	}

	public OutputFile(String outputFile) throws Exception {
		this.outputFile = new File(outputFile);
	}

	public void output(Map<String, Integer> output) throws Exception {
		// if the map is false
		if (output == null) {
			throw new Exception("Invalid there is no data to output to file " + outputFile );
		}

		Set<String> keys = output.keySet();
		List<String> lines = new LinkedList<String>();
		for (String key : keys) {
			Integer val = output.get(key);
			lines.add(key + ":" + val + "|s");
		}
		FileUtils.writeLines(outputFile, lines, System.lineSeparator(), true);
	}
}
