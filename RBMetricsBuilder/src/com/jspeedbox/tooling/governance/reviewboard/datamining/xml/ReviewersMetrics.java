package com.jspeedbox.tooling.governance.reviewboard.datamining.xml;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.jspeedbox.tooling.governance.dashboard.Totals;

@XmlRootElement
public class ReviewersMetrics {
	
	private Map<String, Totals> totals = null;
	
	public ReviewersMetrics(){
		
	}
	
	public ReviewersMetrics(Map<String, Totals> totals){
		this.totals = totals;
	}

	@XmlElement
	public Map<String, Totals> getTotals() {
		return totals;
	}

	public void setTotals(Map<String, Totals> totals) {
		this.totals = totals;
	}
	
	

}
