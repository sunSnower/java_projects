package cn.netjava.server;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
/**
 * JMF语音视频获取远程数据源
*/
public class ServerReceive extends Thread {
	private Socket sc;
	public ServerReceive(Socket sc) {
		this.sc = sc;
	}
	@Override
	public void run() {
		// 接收视频
		startReceive();
	}
	/**
	 * 接收视频
	 */
	public void startReceive() {
		try {
			//得到输入流
			InputStream input = sc.getInputStream();
			DataInputStream data = new DataInputStream(input);
			// 获取客户机发上来的请求
			int totalLength = data.readInt();
			int type = data.readInt();
			byte[] address = new byte[totalLength - 8];
			//如果请求类型是视频请求,这里我们只处理视频请求，
			//注意客户机发过来的视频消息类型是固定的10001
			if (10001 == type) {
				data.read(address);
				String session = new String(address);
				System.out.println("----" + session);
				String[] sessions = new String[1];
				sessions[0] = session;
				System.out.println("session is:"+sessions[0]);
				//根据得到的session创建视频接收线程
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
