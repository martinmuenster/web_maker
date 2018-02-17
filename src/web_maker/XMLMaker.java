package web_maker;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLMaker {
	private Document doc;
	
	private void setAttribute(Element e, String attributeName, String val) {
		Attr attr = doc.createAttribute(attributeName);
		attr.setValue(val);
		e.setAttributeNode(attr);
	}
	
	public void writeXML(HashMap<String, HashSet<String>> map) {
		int maxNetwork = 1;
		for (String s : map.keySet()) {
			int networkSize = map.get(s).size();
			if (networkSize > maxNetwork) {
				maxNetwork = networkSize;
			}
		}
		try {
	
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
			doc = docBuilder.newDocument();
			
			Element mxGraphModel = doc.createElement("mxGraphModel");
			setAttribute(mxGraphModel, "dx", "1010");
			setAttribute(mxGraphModel, "dy", "693");
			setAttribute(mxGraphModel, "grid", "1");
			setAttribute(mxGraphModel, "gridSize", "10");
			setAttribute(mxGraphModel, "guides", "1");
			setAttribute(mxGraphModel, "tooltips", "1");
			setAttribute(mxGraphModel, "connect", "1");
			setAttribute(mxGraphModel, "arrows", "1");
			setAttribute(mxGraphModel, "fold", "1");
			setAttribute(mxGraphModel, "page", "1");
			setAttribute(mxGraphModel, "pageScale", "1");
			setAttribute(mxGraphModel, "pageWidth", "850");
			setAttribute(mxGraphModel, "pageHeight", "1100");
			setAttribute(mxGraphModel, "background", "#ffffff");
			setAttribute(mxGraphModel, "math", "0");
			setAttribute(mxGraphModel, "shadow", "0");
			doc.appendChild(mxGraphModel);
	
			Element root = doc.createElement("root");
			mxGraphModel.appendChild(root);
			
			Element mxCell0 = doc.createElement("mxCell");
			setAttribute(mxCell0, "id", "0");
			root.appendChild(mxCell0);
			
			Element mxCell1 = doc.createElement("mxCell");
			setAttribute(mxCell1, "id", "1");
			setAttribute(mxCell1, "parent", "0");
			root.appendChild(mxCell1);
			
			int index = 2;
			HashMap<String, int[]> locations = new HashMap<String, int[]>();
			for (String s : map.keySet()) {
				double networkRatio = map.get(s).size()/(double)maxNetwork;
				int color = 255 - ((int) (255*networkRatio));
				
				String cellColor = String.format("#FFFF%02X", color);
				Element mxCell = doc.createElement("mxCell");
				setAttribute(mxCell, "id", Integer.toString(index));
				setAttribute(mxCell, "value", s);
				setAttribute(mxCell, "style", "ellipse;whiteSpace=wrap;html=1;fillColor="+cellColor+";");
				setAttribute(mxCell, "vertex", "1");
				setAttribute(mxCell, "parent", "1");
				root.appendChild(mxCell);
				
				int x = ((index - 2) % 5) * 200 + 40 + ((index - 2) % 2)*100;
				int y = ((index - 2) / 5) * 200 + 120 - ((index - 2) % 2)*50;
				if ((index-2) == 2)
					x+=25;
					y+=50;
				int[] data = {x,y,index};
				locations.put(s, data);
				
				Element mxGeometry = doc.createElement("mxGeometry");
				setAttribute(mxGeometry, "x", Integer.toString(x));
				setAttribute(mxGeometry, "y", Integer.toString(y));
				setAttribute(mxGeometry, "width", "80");
				setAttribute(mxGeometry, "height", "80");
				setAttribute(mxGeometry, "as", "geometry");
				mxCell.appendChild(mxGeometry);
				
				index++;
			}
			
			for (String s : map.keySet()) {
				String source = Integer.toString(locations.get(s)[2]);
				for (String g : map.get(s)) {
					String target = Integer.toString(locations.get(g)[2]);
					
					Element mxCell = doc.createElement("mxCell");
					setAttribute(mxCell, "id", Integer.toString(index));
					setAttribute(mxCell, "value", "");
					setAttribute(mxCell, "style", "endArrow=classic;startArrow=classic;html=1;strokeColor=#000000;");
					setAttribute(mxCell, "edge", "1");
					setAttribute(mxCell, "parent", "1");
					setAttribute(mxCell, "source", source);
					setAttribute(mxCell, "target", target);
					root.appendChild(mxCell);
					
					Element mxGeometry = doc.createElement("mxGeometry");
					setAttribute(mxGeometry, "width", "50");
					setAttribute(mxGeometry, "height", "50");
					setAttribute(mxGeometry, "relative", "1");
					setAttribute(mxGeometry, "as", "geometry");
					mxCell.appendChild(mxGeometry);
					
					Element mxPoint1 = doc.createElement("mxPoint");
					setAttribute(mxPoint1, "x", Integer.toString(locations.get(s)[0]));
					setAttribute(mxPoint1, "y", Integer.toString(locations.get(s)[1]));
					setAttribute(mxPoint1, "as", "sourcePoint");
					mxGeometry.appendChild(mxPoint1);
					
					Element mxPoint2 = doc.createElement("mxPoint");
					setAttribute(mxPoint2, "x", Integer.toString(locations.get(g)[0]));
					setAttribute(mxPoint2, "y", Integer.toString(locations.get(g)[1]));
					setAttribute(mxPoint2, "as", "targetPoint");
					mxGeometry.appendChild(mxPoint2);
					
					index++;
				}
			}

	
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("output.xml"));
	
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	
			transformer.transform(source, result);
	
			System.out.println("File saved!");
	
		} catch (ParserConfigurationException pce) {
		  pce.printStackTrace();
	  	} catch (TransformerException tfe) {
	  		tfe.printStackTrace();
	  	}
	}
}
