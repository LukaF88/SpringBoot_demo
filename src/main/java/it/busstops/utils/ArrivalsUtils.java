package it.busstops.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.busstops.entity.Arrival;
import it.busstops.entity.Arrivals;

public class ArrivalsUtils {

	
	public static Arrivals getArrivalsFromXML(String xmlData) {
		
		Arrivals result = new Arrivals();
		
		if (xmlData.indexOf("non trovata") != -1) {
			return result;
		}
		
		Document doc = null;
		String line = "";
		try {
			doc = parseXML(xmlData);

			Element root = doc.getDocumentElement();			
			String stopInfo = root.getChildNodes().item(0).getTextContent().split("in ")[1];
			NodeList infos =  root.getChildNodes().item(1).getChildNodes();

			result.setStopName(stopInfo);
			
			for (int k = 0; k < infos.getLength(); k++) {
				NodeList wrap = infos.item(k).getChildNodes();

				for (int i = 0; i < wrap.getLength() ; i++) {
					Node curr = wrap.item(i);
					if (i == 0) {
						line = curr.getChildNodes().item(0).getNodeValue().split("Linea;")[1];
						result.getArrivals().put(line, new ArrayList<Arrival>());
					}

					else if (curr.getChildNodes().getLength() > 0) {
						NodeList fermate = curr.getChildNodes();
						for (int j = 0; j < fermate.getLength(); j++) {
							Node fermata = fermate.item(j);
							if (Strings.isNotEmpty(Strings.trimToNull(fermata.getNodeValue()))) {
								Arrival arrival = new Arrival();
								arrival.setRealTime(fermata.getNodeValue().contains(";"));
								arrival.setTime(fermata.getNodeValue().replaceAll("[^0-9:]", ""));
								result.getArrivals().get(line).add(arrival);
							}
						} 
					}
				}	
			}

		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static Document parseXML(String rawXml) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append(rawXml.replaceAll("&nbsp", ""));

		int delimiter_1 = xmlStringBuilder.indexOf("<p> Fermata richiesta:");
		int delimiter_2 = xmlStringBuilder.indexOf("</p>") + 4;

		int delimiter_3 = xmlStringBuilder.indexOf("<ul", delimiter_2);
		int delimiter_4 = xmlStringBuilder.indexOf("</ul>", delimiter_3) + 5;

		CharSequence stopInfos = xmlStringBuilder.subSequence(delimiter_1, delimiter_2);
		CharSequence stopsInfos = xmlStringBuilder.subSequence(delimiter_3, delimiter_4);

		ByteArrayInputStream input = new ByteArrayInputStream(("<div>" + stopInfos.toString() + stopsInfos.toString() + "</div>").getBytes("UTF-8"));
		
		Document doc = builder.parse(input);
		return doc;
	}

	public static String getXML(int stopId) {
		String uri = "http://www.5t.torino.it/pda/it/arrivi.jsp?n=" + stopId;
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);
		return result;
	}
}
