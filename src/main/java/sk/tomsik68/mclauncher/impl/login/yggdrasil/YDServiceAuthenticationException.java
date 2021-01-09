package sk.tomsik68.mclauncher.impl.login.yggdrasil;


import org.jetbrains.annotations.Nullable;

public class YDServiceAuthenticationException extends Exception {
	private Exception thrown;
	@Nullable
	private YDResponse reason;

	public YDServiceAuthenticationException(String msg) {
		super(msg);
	}

	public YDServiceAuthenticationException(String msg, Exception e) {
		super(msg);
		this.thrown = e;
	}

	public YDServiceAuthenticationException(String msg, Exception e, YDResponse reason) {
		this(msg, e);
		this.reason = reason;
	}

	public Exception getThrown() {
		return thrown;
	}

	@Nullable
	public YDResponse getReason() {
		return reason;
	}
}
