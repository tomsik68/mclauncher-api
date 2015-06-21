package sk.tomsik68.mclauncher.impl.versions.mcassets;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ResourcesXMLParser {
    private final String url;

    ResourcesXMLParser(String url) {
        this.url = url;
    }

    /**
     * This method's responsibility is to parse some XML into a list of strings
     * The structure of XML: ListBucketResult -> Contents -> Key
     * In return, we get list of strings in all Key tags.
     * This is actually used while parsing resources list for pre-1.6 minecraft versions.
     * @return List of Strings in all Key tags
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    List<String> parse() throws SAXException, IOException, ParserConfigurationException {
        List<String> result = new ArrayList<String>();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url);
        for (int i = 0; i < doc.getElementsByTagName("ListBucketResult").item(0).getChildNodes().getLength(); i++) {
            Node node = doc.getElementsByTagName("ListBucketResult").item(0).getChildNodes().item(i);
            if ((node != null) && ("Contents".equalsIgnoreCase(node.getNodeName())) && (node.getChildNodes().getLength() > 0))
                if (("Key".equals(node.getFirstChild().getNodeName()))) {
                    Map<String, String> values = translateNode(node);
                    String entry = node.getFirstChild().getTextContent();
                    result.add(entry);
                }
        }
        return result;
    }

    /**
     * Translates an XML node's children tag-names and text values to a Map
     * @param node Node whose children should be transferred
     * @return
     */
    private Map<String, String> translateNode(Node node) {
        NodeList nl = node.getChildNodes();
        HashMap<String, String> result = new HashMap<String, String>();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            result.put(n.getNodeName(), n.getTextContent());
        }
        return result;
    }
}
