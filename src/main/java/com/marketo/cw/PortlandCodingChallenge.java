package com.marketo.cw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.json.simple.*; // normally, I would only req this dependency in one place

public class PortlandCodingChallenge {
	
	private static final String SYSVAR_INPUT_KEY = "portland.input";
	private static final String SYSVAR_OUTPUT_KEY = "portland.output";
	private static final String DEFAULT_INPUT_PATH = System.getProperty("user.dir") + File.separator + "leads.json";
	private static final File DEFAULT_INPUT_FILE = new File(DEFAULT_INPUT_PATH);
	private static final String DEFAULT_OUTPUT_PATH = System.getProperty("user.dir") + File.separator + "leads.output.json";
	private static final File DEFAULT_OUTPUT_FILE = new File(DEFAULT_OUTPUT_PATH);
	
	private static Logger logger;

	public static void main(String[] args) {
		logger = initLogger();
		logger.info(">>> Starting Portland Coding Challenge...");
		JSONObject rawLeads = null;
		String sysvarInput = System.getProperty(SYSVAR_INPUT_KEY);
		if (sysvarInput == null) {
			logger.info("Parsing default input file...");
			rawLeads = parseLeads(DEFAULT_INPUT_FILE);
		} else {
			logger.info("Parsing input file (sysvar): " + sysvarInput);
			rawLeads = parseLeads(new File(sysvarInput));
		}
		LeadSet leads = dedupLeads((JSONArray) rawLeads.get("leads"));
		logger.info("Final lead set: " + leads.toString());
		String sysvarOutput = System.getProperty(SYSVAR_OUTPUT_KEY);
		if (sysvarOutput == null) {
			logger.info("Parsing default output file...");
			leads.toFile(DEFAULT_OUTPUT_FILE);
		} else {
			logger.info("Parsing output file (sysvar): " + sysvarOutput);
			leads.toFile(new File(sysvarOutput));
		}
		logger.info(">>> Ending Portland Coding Challenge...");
	}
	
	private static LeadSet dedupLeads(JSONArray rawLeads) {
		LeadSet leads = new LeadSet();
		Iterator<JSONObject> iter = rawLeads.iterator();
		while (iter.hasNext()) {
			Lead lead = new Lead(iter.next());
			logger.info("Parsed: " + lead.toString());
			leads.addIfNotDup(lead);
		}
		return leads;
	}
	
	private static JSONObject parseLeads(File input) {
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) JSONValue.parse(new FileReader(input));
		} catch (FileNotFoundException e) {
			logger.info("File not found.");
			e.printStackTrace();
		} // catch (casting exception) {}
		return (JSONObject) jsonObj;
	}
	
	private static Logger initLogger() {
		try {
			String loggingConfig = System.getProperty("java.util.logging.config.file");
			if (loggingConfig == null) System.setProperty("java.util.logging.config.file", "./logging.properties");
			// Gets the logger based on the configuration file specified at 'java.util.logging.config.file'
			LogManager.getLogManager().reset();
			LogManager.getLogManager().readConfiguration();
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		return Logger.getLogger(PortlandCodingChallenge.class.getSimpleName());
	}
}
