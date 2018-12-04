package learn.course.springboot.example.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import learn.course.springboot.example.entity.Arrival;
import learn.course.springboot.example.entity.Arrivals;
import learn.course.springboot.example.utils.ArrivalsUtils;

@Service
public class ArrivalsService {

	public Arrivals getArrivals(int stopId) {
		
		Arrivals result;
		String rawXml = ArrivalsUtils.getXML(stopId);
		result = ArrivalsUtils.getArrivalsFromXML(rawXml);
		result.setStopId(stopId);
		
		return result;
	}
	
	
	public Arrivals getArrivals(int stopId, String lineId, int howMany) {
		Arrivals result = getArrivals(stopId);
		if (result.getArrivals().keySet().contains(lineId)) {
			List<Arrival> arrivalsLine = result.getArrivals().get(lineId);
			result.getArrivals().clear();
			int aSize = arrivalsLine.size();
			if (howMany != -1 && aSize > howMany) {
				arrivalsLine = arrivalsLine.subList(0, howMany);
			}
			result.getArrivals().put(lineId, arrivalsLine);
		}
		
		else {
			result.getArrivals().clear();
		}
		
		
		return result;
	}

	public Arrivals getArrivals(String lineId, int stopId) {
		return null;
	}


}
