package cn.netjava.client;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.MediaLocator;
import javax.media.format.VideoFormat;

/**
 * JMF语音视频获取本地数据源
 * 
 * @author kowloon
 */
public class ClientCaptureDevice extends Thread {
	private Socket sc; // 连接对象
	private String serverAddress; // 目标主机IP地址
	private int port; // 目标主机IP端口
	private CaptureDeviceInfo audioCapDevInfo; // 多媒体采集信息对象
	private VideoFormat currentFormat; // 多媒体格式化对象
	private MediaLocator audioCapDevLoc; // 设备定位器对象

	public ClientCaptureDevice(String serverAddress, int port) {
		this.port = port;
		this.serverAddress = serverAddress;
	}

	@Override
	public void run() {
		initCaptureDevice();
		// 调用传输数据的方法
		startTransmit();
	}

	/**
	 * 初始化采集设备
	 */
	public void initCaptureDevice() {
		// 获取所有的多媒体采集对象
		Vector<CaptureDeviceInfo> audioCapDevList = CaptureDeviceManager
				.getDeviceList(null);
		// 如果存在多媒体采集对象
		if (audioCapDevList.size() != 0) {
			for (int i = 0; i < audioCapDevList.size(); i++) {
				// 获取一个可用设备名字，转换成采集对象
				audioCapDevInfo = audioCapDevList.elementAt(i);
				Format[] videoFormats = audioCapDevInfo.getFormats();
				// 如果设备名称以vfw开头
				if (audioCapDevInfo.getName().startsWith("vfw:")) {
					// 获得所有支持RGB格式
					for (int j = 0; j < videoFormats.length; j++) {
						// 我们只需要第一种YUV格式
						if (videoFormats[j] instanceof VideoFormat) {
							System.out.println("hello");
							currentFormat = (VideoFormat) videoFormats[i];
							break;
						}
					}
					if (currentFormat == null) {
						System.err.println("Search For VideoFormat Failed");
						System.exit(-1);
					}
					// 得到该设备的定位器
					audioCapDevLoc = audioCapDevInfo.getLocator();
				}
			}
		}
	}

	/**
	 * 发送视频
	 */
	public void startTransmit() {
		try {
			// 建立Socket连接
			sc = new Socket(serverAddress, port);
			// 此处只针对视屏 本地地址,端口
			InetAddress add = sc.getLocalAddress();
			int sport = sc.getLocalPort();
			String ip = add.toString().substring(1);
			String session = ip + "/" + sport;
			// 得到输出流
			OutputStream out = sc.getOutputStream();
			DataOutputStream output = new DataOutputStream(out);
			byte[] address = session.getBytes();
			int len = address.length;
			// 将视频信息发送给目标主机(包括发送视频的主机地址,端口,消息类型)
			output.writeInt(8 + len);
			output.writeInt(10001); // 这里我们设定10001为发送视频请求
			output.write(address);
			// 创建视频格式化对象
			Format format = new Format(VideoFormat.JPEG);
			// 创建视频发送对象
			VedioTransmit vedio = new VedioTransmit(audioCapDevLoc,
					serverAddress, "" + sport, format);
			vedio.start();// 启动视频发送
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
