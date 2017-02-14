package ejf;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CodeStyleParser {

    public Properties toProperties(final String xmlFile)
            throws ParserConfigurationException, IOException, SAXException {
        Properties properties = new Properties();

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList profiles = doc.getElementsByTagName("profile");
        if (profiles.getLength() != 1) {
            throw new RuntimeException("XML style should contain exactly one <profile/>: " + xmlFile);
        }

        Node profile = profiles.item(0);
        if (profile.getNodeType() == Node.ELEMENT_NODE) {
            NodeList settings = ((Element) profile).getElementsByTagName("setting");
            for (int i = 0; i < settings.getLength(); i++) {
                Node setting = settings.item(i);
                if (setting.getNodeType() == Node.ELEMENT_NODE) {
                    String id = ((Element) setting).getAttribute("id");
                    String value = ((Element) setting).getAttribute("value");
                    properties.setProperty(id.trim(), value.trim());
                }
            }
        }

        return properties;
    }
}
