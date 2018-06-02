package cn.netjava.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * ����˽�������,�ȴ����տͻ���
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
	/** �������� */
	public void setConnection() {
		try {
			ServerSocket server = new ServerSocket(port);
			while (true) {
				// ��ÿ�������ͻ�����һ���߳�
				Socket client = server.accept();
				ServerReceive receive = new ServerReceive(client);
				receive.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
