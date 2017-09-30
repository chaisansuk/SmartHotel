/*
Author	: Yu Shiping <iamysp@163.com>
Created	: 2014/11/25
Copyright© 2014 Kedao i-tone.cn。All Rights Reserved�?
 */

package com.example.meka_it.smarthotel2;

public class KdNetJni {

	/* Packet priority */
	public static final class Priority {
		public static final int PRIO_IMMEDIATE	= 0;
		public static final int PRIO_HIGH		= 1;
		public static final int PRIO_MEDIUM		= 2;
		public static final int PRIO_LOW		= 3;
	}
	/* Packet Reliability */
	public static final class Reliability {
		public static final int RELI_UNRELIABLE							= 0;
		public static final int RELI_UNRELIABLE_SEQUENCED				= 1;
		public static final int RELI_RELIABLE							= 2;
		public static final int RELI_RELIABLE_ORDERED					= 3;
		public static final int RELI_RELIABLE_SEQUENCED					= 4;
		public static final int RELI_UNRELIABLE_WITH_ACK_RECEIPT		= 5;
		public static final int RELI_RELIABLE_WITH_ACK_RECEIPT			= 6;
		public static final int RELI_RELIABLE_ORDERED_WITH_ACK_RECEIPT	= 7;		
	}
	/* Server type */
	public static final class ServerType {
		public static final int ST_GATEWAY_SERVER	= 0;
		public static final int ST_PUNCH_SERVER		= 1;
		public static final int ST_PROXY_SERVER		= 2;
		public static final int ST_ROUTER_SERVER	= 3;
		public static final int ST_NUMBERS			= 4;
	}
	/* Connect state */
	public static final class ConnectState {
		public static final int CS_PENDING		= 0;
		public static final int CS_CONNECTING2	= 1;
		public static final int CS_FAILED		= 2;
		public static final int CS_SUCCEEDED	= 3;
		public static final int CS_NUMBERS		= 4;
	}
	/* Host address with port */
	public static final class Address {
		public Address(String host, int port) {
			this.host = host;
			this.port = port;
		}
		public String host;
		public int port;
	}
	
	public KdNetJni(int maxConnections, int listenPort) throws Exception {
		peer = kdnNew(maxConnections, listenPort);
		if (peer == 0) {
			throw new Exception("KdNetJni.kdnNew() failed.");
		}
	}
	
	protected void finalize() {
		destroy();
	}
	
	public void destroy() {
		if (peer != 0) {
			kdnFree(peer);
			peer = 0;
		}
	}

	public int setServer(int serverType, Address addr) {
		return kdnSetServer(peer, serverType, addr);
	}
	
	public int autoConf(String username, String password) {
		return kdnAutoConf(peer, username, password);
	}
	
	public int listen(int maxIncomingConnections, String password) {
		return kdnListen(peer, maxIncomingConnections, password);
	}

	public int login(String userId, String password) {
		return kdnLogin(peer, userId, password);
	}
	
	public int logout(String userId) {
		return kdnLogout(peer, userId);
	}
	
	public int close(String guid) {
		return kdnClose(peer, guid);
	}
	
	public long send2Addr(Address addr, byte[] buf, int len, int priority, int reliability) {
		return kdnSend2Addr(peer, addr, buf, len, priority, reliability);
	}
	
	public long send2Addr(Address addr, byte[] buf, int len) {
		return send2Addr(addr, buf, len, Priority.PRIO_MEDIUM, Reliability.RELI_RELIABLE_ORDERED_WITH_ACK_RECEIPT);
	}
	
	public long send2Guid(String guid, byte[] buf, int len, int priority, int reliability) {
		return kdnSend2Guid(peer, guid, buf, len, priority, reliability);
	}
	
	public long send2Guid(String guid, byte[] buf, int len) {
		return send2Guid(guid, buf, len, Priority.PRIO_MEDIUM, Reliability.RELI_RELIABLE_ORDERED_WITH_ACK_RECEIPT);
	}
	
	public long sendRTVS(String guid, byte[] buf, int len) {
		return kdnSendRTVS(peer, guid, buf, len);
	}

	// callback functions
	protected void onServer(int serverType, int messageId) {}
	protected void onLoginSuccess(String userId, String nodeServer, Address punchServer, boolean registerAsServer) {}
	protected void onLoginFailed(String userId, int messageId) {}
	protected void onConnect(int connectType, int connectState, boolean incoming, String guid, Address addr) {}
	protected void onDisconnect(String guid, Address addr) {}
	protected void onReceive(String guid, byte[] buf, int len) {}
	protected void onReceiveRTVS(String guid, byte[] buf, int len) {}
	
	// peer node native object
	private long peer = 0;
	
	// native functions
	private synchronized native long kdnNew(int maxConnections, int listenPort);
	private synchronized native void kdnFree(long peer);
	private synchronized native int kdnSetServer(long peer, int serverType, Address addr);
	private synchronized native int kdnAutoConf(long peer, String username, String password);
	private synchronized native int kdnListen(long peer, int maxIncomingConnections, String password);
	public synchronized native int kdnLogin(long peer, String userId, String password);
	private synchronized native int kdnLogout(long peer, String userId);
	private synchronized native int kdnClose(long peer, String guid);
	private synchronized native long kdnSend2Addr(long peer, Address addr, byte[] buf, int len, int priority, int reliability);
	private synchronized native long kdnSend2Guid(long peer, String guid, byte[] buf, int len, int priority, int reliability);
	private synchronized native long kdnSendRTVS(long peer, String guid, byte[] buf, int len);

	static {
		System.loadLibrary("KdNet");
	}
	
}