package web_maker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.w3c.dom.Element;


public class XMLParser {
	private File f;
	private HashMap<String, HashSet<String>> map;
	
	public XMLParser(String fName) {
		f = openFile(fName);  
		map = new HashMap<String, HashSet<String>>();
	}
	
	public HashMap<String, HashSet<String>> parse() {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
        try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("member");
			for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            	HashSet<String> memberNetwork = new HashSet<String>();
	                Element eElement = (Element) nNode;
	                String member = eElement.getAttribute("value");
	                NodeList networkList = eElement.getElementsByTagName("network");
	                for (int i = 0; i < networkList.getLength(); i++) {
	                	Node n = networkList.item(i);
	                	String network = n.getTextContent();
	                	memberNetwork.add(network);
	                }
	                if (map.get(member) == null) {
	                	map.put(member, memberNetwork);
	                }
	                else {
	                	map.get(member).addAll(memberNetwork);
	                }
	                for (String s : memberNetwork) {
	                	if (map.get(s) == null) {
	                		HashSet<String> otherNetwork = new HashSet<String>();
	                		otherNetwork.add(member);
		                	map.put(s, otherNetwork);
		                }
		                else {
		                	map.get(s).add(member);
		                }
	                }
	            }
			}
			System.out.println(map.toString());
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
        return map;
	}
	
	private File openFile(String fName) {
		File f = new File(fName);
		return f;
	}
}
