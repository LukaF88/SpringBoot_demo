package it.busstops;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.busstops.entity.Arrivals;
import it.busstops.entity.LineStops;
import it.busstops.service.ArrivalsService;
import it.busstops.utils.LineStopsUtils;

@RestController
public class ArrivalsController {

	@Autowired
	ArrivalsService arrivalService;
	
	@RequestMapping("/lines")
	public List<String> lines() {
		return LineStopsUtils.getElencoLinee();
	}

	@RequestMapping("/stop/{stopId}")
	public Arrivals nextBuses(@PathVariable int stopId) {
		return arrivalService.getArrivals(stopId);
	}

	@RequestMapping("/line/{lineId}")
	public LineStops stopList(@PathVariable String lineId) {
		return LineStopsUtils.getElencoFermateLinea(lineId);
	}
	
	@RequestMapping("/{stopId}/{lineId}")
	public Arrivals nextBuses(@PathVariable int stopId, @PathVariable String lineId) {
		return arrivalService.getArrivals(stopId, lineId, -1);
	}
	
	@RequestMapping("/next/{stopId}/{lineId}")
	public Arrivals nextBus(@PathVariable int stopId, @PathVariable String lineId) {
		return arrivalService.getArrivals(stopId, lineId, 1);
	}

}
