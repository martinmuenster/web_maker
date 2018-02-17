package web_maker;

import java.util.HashMap;
import java.util.HashSet;

public class Main {
	public static void main(String[] args) {
		XMLParser myXMLParser = new XMLParser("input.xml");
		HashMap<String, HashSet<String>> map = myXMLParser.parse();
		XMLMaker myXMLMaker = new XMLMaker();
		myXMLMaker.writeXML(map);
	}
	
}
