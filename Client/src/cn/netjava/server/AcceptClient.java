package cn.netjava.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 服务端建立连接,等待接收客户机
*/
public class AcceptClient extends Thread {
	private int port;
	public AcceptClient(int port) {
		this.port = port;
	}
	@Override
	public void run(){
		setConnection();
	}
	/** 建立连接 */
	public void setConnection() {
		try {
			ServerSocket server = new ServerSocket(port);
			while (true) {
				// 将每个进来客户启动一个线程
				Socket client = server.accept();
				ServerReceive receive = new ServerReceive(client);
				receive.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
