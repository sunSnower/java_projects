package cn.netjava.server;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
/**
 * JMF������Ƶ��ȡԶ������Դ
*/
public class ServerReceive extends Thread {
	private Socket sc;
	public ServerReceive(Socket sc) {
		this.sc = sc;
	}
	@Override
	public void run() {
		// ������Ƶ
		startReceive();
	}
	/**
	 * ������Ƶ
	 */
	public void startReceive() {
		try {
			//�õ�������
			InputStream input = sc.getInputStream();
			DataInputStream data = new DataInputStream(input);
			// ��ȡ�ͻ���������������
			int totalLength = data.readInt();
			int type = data.readInt();
			byte[] address = new byte[totalLength - 8];
			//���������������Ƶ����,��������ֻ������Ƶ����
			//ע��ͻ�������������Ƶ��Ϣ�����ǹ̶���10001
			if (10001 == type) {
				data.read(address);
				String session = new String(address);
				System.out.println("----" + session);
				String[] sessions = new String[1];
				sessions[0] = session;
				System.out.println("session is:"+sessions[0]);
				//���ݵõ���session������Ƶ�����߳�
				VedioReceive vedio = new VedioReceive(sessions);
				if (!vedio.initialize()) {
					System.err.println("Failed to initialize the sessions.");
					System.exit(-1);
				}
				System.err.println("Exiting AVReceive3");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
