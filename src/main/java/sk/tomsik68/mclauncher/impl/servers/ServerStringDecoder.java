package sk.tomsik68.mclauncher.impl.servers;

final class ServerStringDecoder {

    static String parseProperty(String serverStr, String property) {
        String beginStr = getPropertyBeginStr(property);
        String endStr = getPropertyEndStr(property);
        int propBegin = serverStr.indexOf(beginStr);
        int propEnd = serverStr.indexOf(endStr);
        if (propBegin < 0 || propEnd < 0 || propEnd < propBegin) {
            return "<data scrambled>";
        } else
            return serverStr.substring(propBegin + beginStr.length(), propEnd);

    }

    private static String getPropertyEndStr(String property) {
        return "[/".concat(property).concat("]");
    }

    private static String getPropertyBeginStr(String property) {
        return "[".concat(property).concat("]");
    }
}
