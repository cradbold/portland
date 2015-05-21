package com.marketo.cw;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

public class Lead {
	
	public final String id;
	public final String email;
	public final JSONObject json;
	public Date entryDate;

	private static final Logger logger = Logger.getLogger(Lead.class.getSimpleName()); 

	public Lead(final JSONObject lead) {
		this.id = lead.get("_id").toString();
		this.email = lead.get("email").toString();
		this.json = lead;
		try {
			// 2014-05-07T17:30:20+00:00
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
			this.entryDate = df.parse(lead.get("entryDate").toString());
		} catch (ParseException e) {
			logger.severe("Date parsing exception...");
			e.printStackTrace();
		}
	}

//  Could go this route and let the Set do the work, but then have to over-engineer, lose order, 
//	and lose granularity of logging
//	@Override
//	public int hashCode() {
//		return this.id.hashCode() ? this.email.hashCode();
//	}
//	
//	@Override
//	public boolean equals(Object o) {
//		logger.info("Equating first object to second: " + this.toString() + ", " + o.toString());
//		if (this == o) return true;
//		if (!this.getClass().equals(o.getClass())) return false;
//		Lead targetLead = (Lead) o;
//		if (this.id.equals(targetLead.id)) return true;
//		if (this.email.equals(targetLead.email)) return true;
//		return false;
//	}
	
	public String toString(boolean allInfo) {
		if (allInfo) return this.toString();
		String s = "Lead(";
		s += "id:" + this.json.get("_id").toString() + "; ";
		s += "email:" + this.json.get("email").toString() + "; ";
		s += "entryDate:" + this.json.get("entryDate").toString() + ")";
		return s;
	}
	
	@Override
	public String toString() {
		String s = "Lead(";
		s += "id:" + this.json.get("_id").toString() + "; ";
		s += "email:" + this.json.get("email").toString() + "; ";
		s += "firstName:" + this.json.get("firstName").toString() + "; ";
		s += "lastName:" + this.json.get("lastName").toString() + "; ";
		s += "address:" + this.json.get("address").toString() + "; ";
		s += "entryDate:" + this.json.get("entryDate").toString() + ")";
		return s;
	}
}
