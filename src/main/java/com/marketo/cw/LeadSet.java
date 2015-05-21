package com.marketo.cw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LeadSet {
	
	private final List<Lead> leads;
	
	private static final Logger logger = Logger.getLogger(LeadSet.class.getSimpleName());
	
	public LeadSet() {
		// Originally, the DS of choice was a hashset, but:
		// 1) Order is required for reconciling dups, 
		// 2) We lose efficiency of hashing by iterating over elements anyway, and
		// 3) We need to control addition of elements
		this.leads = new ArrayList<Lead>();
	}
	
	public void addIfNotDup(Lead targetLead) {
		for (Lead lead : this.leads) {
			if (lead.id.equals(targetLead.id) || lead.email.equals(targetLead.email)) {
				String logStr = "Duplicate found: \n    Candidate: " + targetLead.toString() + "\n    Existing: " + lead.toString();
				if (lead.entryDate.after(targetLead.entryDate)) {
					// keep existing lead if entry date is later
					logger.info(logStr + "\n    Keeping existing lead due to later date...");
				} else {
					// if target is later or if same, add last listed
					// remove first lead, add second lead (don't swap; preserve order) <-- best test case
					logger.info(logStr + "\n    Removing existing; adding candidate due to date or order...");
					LeadSet.popFirstAddSecond(this.leads, lead, targetLead);
					return;
				}
			}
		}
		this.leads.add(targetLead);
	}
	
	public int size() {
		return this.leads.size();
	}
	
	public Iterator<Lead> iterator() {
		return this.leads.iterator();
	}
	
	// sorry 'bout formatting; don't want to overdo it!
	@SuppressWarnings("unchecked")
	public void toFile(File file) {
		try {
			JSONObject head = new JSONObject();
			JSONArray leadsArray = new JSONArray();
			for (Lead lead : this.leads) {
				leadsArray.add(lead.json);
			}
			head.put("leads", leadsArray);
			FileWriter writer = new FileWriter(file);
			writer.write(head.toJSONString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		String s = "LeadSet(\n";
		for (Lead lead : this.leads) {
			s += "    " + lead.toString(false) + ";\n";
		}
		return s + ")";
	}
	
	private static void popFirstAddSecond(List<Lead> leadSet, Lead swapOutLead, Lead swapInLead) {
		leadSet.remove(swapOutLead);
		leadSet.add(swapInLead);
	}
}
