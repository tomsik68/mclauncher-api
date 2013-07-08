package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.impl.common.Observable;

public class MCAssetsVersionList extends Observable<IVersion> implements IVersionList {
    private static final Pattern snapshotPattern = Pattern.compile("((\\d\\d\\w\\d\\d\\w)|(\\d_\\d-pre)|(\\d_\\d-pre\\d)|(rc)|(rc\\d))");

    @Override
    public void startDownload() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("http://assets.minecraft.net/");
        for (int i = 0; i < doc.getElementsByTagName("ListBucketResult").item(0).getChildNodes().getLength(); i++) {
            Node node = doc.getElementsByTagName("ListBucketResult").item(0).getChildNodes().item(i);
            if ((node != null) && ("Contents".equalsIgnoreCase(node.getNodeName())) && (node.getChildNodes().getLength() > 0))
                if (("Key".equals(node.getFirstChild().getNodeName())) && (node.getFirstChild().getTextContent().contains("minecraft.jar")))
                    notifyObservers(new MCAssetsVersion(node.getFirstChild().getTextContent().split("/")[0]));
        }
    }
    public static final boolean isSnapshot(IVersion version){
        return snapshotPattern.matcher(version.getId()).matches();
    }
}
