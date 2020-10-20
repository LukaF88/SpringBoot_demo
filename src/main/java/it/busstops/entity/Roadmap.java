package it.busstops.entity;

import java.util.HashMap;
import java.util.Map;

public class Roadmap {
	Map<Integer, Arrivals> roadmap; // key = stopId
	String line;

	public Map<Integer, Arrivals> getRoadmap() {
		if (roadmap == null) {
			roadmap = new HashMap<Integer, Arrivals>();
		}
		return roadmap;
	}

	public void setRoadmap(Map<Integer, Arrivals> roadmap) {
		this.roadmap = roadmap;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	

}
