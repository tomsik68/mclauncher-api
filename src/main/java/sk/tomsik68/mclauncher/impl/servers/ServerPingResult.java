package sk.tomsik68.mclauncher.impl.servers;

public final class ServerPingResult {
    private boolean successful = false;
    private Throwable error = null;
    private PingedServerInfo info;

    ServerPingResult(PingedServerInfo info){
        this(info, null, true);
    }

    ServerPingResult(Throwable error){
        this(null, error, false);
    }

    private ServerPingResult(PingedServerInfo info, Throwable err, boolean successful){
        this.error = err;
        this.info = info;
        this.successful = successful;
    }

    public boolean isSuccessful(){
        return successful;
    }

    public PingedServerInfo getInfo(){
        return info;
    }

    public Throwable getError(){
        return error;
    }

}
