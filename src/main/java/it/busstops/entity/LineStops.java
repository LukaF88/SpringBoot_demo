package it.busstops.entity;

import java.util.ArrayList;
import java.util.List;


public class LineStops {
	String line;
	List<Stop> stops;

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public List<Stop> getStops() {
		if (stops == null)
			stops = new ArrayList<Stop>();
		return stops;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	

}
