package it.busstops.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arrivals {
	Map<String, List<Arrival>> arrivals;
	String stopName;
	int stopId;

	public Map<String, List<Arrival>> getArrivals() {
		if (arrivals == null) {
			arrivals = new HashMap<String, List<Arrival>>();
		}
		return arrivals;
	}

	public void setArrivals(Map<String, List<Arrival>> arrivals) {
		this.arrivals = arrivals;
	}

	public String getStopName() {
		return stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public int getStopId() {
		return stopId;
	}

	public void setStopId(int stopId) {
		this.stopId = stopId;
	}

}
