package it.busstops.utils;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.busstops.entity.Arrival;
import it.busstops.entity.Arrivals;
import it.busstops.entity.Line;
import it.busstops.entity.LineStops;
import it.busstops.entity.Stop;

public class LineStopsUtils {

	
	public static Arrivals getStopsFromXML(String xmlData) {
		
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

	public static List<Line> getElencoLinee() {
		RestTemplate restTemplate = new RestTemplate();
		String xml = restTemplate.getForObject("http://www.gtt.to.it/cms/percorari/urbano", String.class);
		xml = xml.substring(xml.lastIndexOf("<table"), xml.length()-1);

		final String regex = "<strong>([\\w \\/]+)<\\/strong>[\\W \\S]*?<\\/td>[\\W \\S .]*?<td>[\\W \\S .]*?<a href=\"\\/cms\\/percorari\\/urbano\\?view=percorsi&amp;bacino=U&amp;linea=(\\w+)&";
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(xml);

	/*	try (PrintStream out = new PrintStream(new FileOutputStream("debug.txt"))) {
			out.print(xml);
		}
		catch(Exception e) {}
*/
		final ArrayList<Line> result = new ArrayList<>();

		while (matcher.find()) {
			Line l = new Line();
			l.setName(matcher.group(1));
			l.setInternalId(matcher.group(2));
			result.add(l);

			for (int i = 1; i <= matcher.groupCount(); i++) {
				System.out.println("Group " + i + ": " + matcher.group(i));
			}
		}
		
		System.out.println("TOT: " + result.size());
		return result;
	}


public static LineStops getElencoFermateLinea(String linea) {

	String uri = "http://www.gtt.to.it/cms/percorari/urbano";
	String BASE_URL = "http://www.gtt.to.it";
	LineStops result = new LineStops();
	
	RestTemplate restTemplate = new RestTemplate();
	String tmpXml = restTemplate.getForObject(uri, String.class);

	String regex = "<a href=\"(.*linea=" + linea + "\\W.*)\"";
	Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
	Matcher matcher = pattern.matcher(tmpXml);
	String link = "";
	while (matcher.find())
		link = matcher.group(1);
	result.setLine(linea);
	//System.out.println("Link trovato per linea " + linea + ": " + tmpLink);

	// 2ND STEP
	String xml = restTemplate.getForObject(BASE_URL + link, String.class);

	//System.out.println("XML -> " + uri);

	regex = "(\\/cms\\/percorari\\/urbano\\?view=percorso&amp;linea=" + linea + "&amp;percorso=.*)\"";
	matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(xml);
	
	while (matcher.find())
		link = matcher.group(0);
	result.setLine(linea);
	//System.out.println("Link finale trovato per linea " + linea + " -> " + uri);

	if (StringUtils.isEmpty(link))
		return result;

	// STEP 3
	String finalXml = restTemplate.getForObject(BASE_URL + link, String.class);
	//finalXml = finalXml.replaceAll("\r", "");
	//finalXml = finalXml.replaceAll("\n", "");

	matcher = Pattern.compile("\\s{2,}").matcher(finalXml);
	finalXml = matcher.replaceAll("");

	//System.out.println("DOPO: " + finalXml);

	regex = "<a href=\"\\/cms\\/percorari\\/arrivi\\?view=palina&amp;palina=.*\">([\\w . \\/ -]+)<\\/a>.*<td class=\"step\">([\\w . \\/ -]+)<\\/td><td>([\\w . \\/ \\d -]+)<\\/td>";
	pattern = Pattern.compile(regex, Pattern.MULTILINE);
	matcher = pattern.matcher(finalXml);
	while (matcher.find()){
		Stop s = new Stop();
		s.setId(matcher.group(1));
		s.setName(matcher.group(2));
		s.setDescription(matcher.group(3));
		result.getStops().add(s);
	}
			
	return result;
	}

}
