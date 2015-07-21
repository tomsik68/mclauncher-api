package sk.tomsik68.mclauncher.impl.versions.mcassets;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.api.versions.LatestVersionInformation;
import sk.tomsik68.mclauncher.impl.common.Observable;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.regex.Pattern;

/**
 * This is the old list, which still works, but you're highly encouraged to use
 * {@link sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList} instead!
 *
 * @author Tomsik68
 */
@Deprecated
public final class MCAssetsVersionList extends Observable<String> implements IVersionList {
    private static final Pattern snapshotPattern = Pattern.compile("((\\d\\d\\w\\d\\d\\w)|(\\d_\\d-pre)|(\\d_\\d-pre\\d)|(rc)|(rc\\d))");
    private static final LatestVersionInformation LATEST_VERSION_INFORMATION = new LatestVersionInformation("1_5_2", "13w11a");

    public static boolean isSnapshot(IVersion version) {
        return snapshotPattern.matcher(version.getId()).matches();
    }

    @Override
    public void startDownload() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("http://assets.minecraft.net/");
        for (int i = 0; i < doc.getElementsByTagName("ListBucketResult").item(0).getChildNodes().getLength(); i++) {
            Node node = doc.getElementsByTagName("ListBucketResult").item(0).getChildNodes().item(i);
            if ((node != null) && ("Contents".equalsIgnoreCase(node.getNodeName())) && (node.getChildNodes().getLength() > 0))
                if (("Key".equals(node.getFirstChild().getNodeName())) && (node.getFirstChild().getTextContent().contains("minecraft.jar")))
                    notifyObservers(node.getFirstChild().getTextContent().split("/")[0]);
        }
    }

    @Override
    public IVersion retrieveVersionInfo(String id) throws Exception {
        return new MCAssetsVersion(id);
    }

    @Override
    public LatestVersionInformation getLatestVersionInformation() {
        return LATEST_VERSION_INFORMATION;
    }
}
