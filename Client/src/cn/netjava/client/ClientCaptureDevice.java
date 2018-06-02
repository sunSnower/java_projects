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
 * JMF������Ƶ��ȡ��������Դ
 * 
 * @author kowloon
 */
public class ClientCaptureDevice extends Thread {
	private Socket sc; // ���Ӷ���
	private String serverAddress; // Ŀ������IP��ַ
	private int port; // Ŀ������IP�˿�
	private CaptureDeviceInfo audioCapDevInfo; // ��ý��ɼ���Ϣ����
	private VideoFormat currentFormat; // ��ý���ʽ������
	private MediaLocator audioCapDevLoc; // �豸��λ������

	public ClientCaptureDevice(String serverAddress, int port) {
		this.port = port;
		this.serverAddress = serverAddress;
	}

	@Override
	public void run() {
		initCaptureDevice();
		// ���ô������ݵķ���
		startTransmit();
	}

	/**
	 * ��ʼ���ɼ��豸
	 */
	public void initCaptureDevice() {
		// ��ȡ���еĶ�ý��ɼ�����
		Vector<CaptureDeviceInfo> audioCapDevList = CaptureDeviceManager
				.getDeviceList(null);
		// ������ڶ�ý��ɼ�����
		if (audioCapDevList.size() != 0) {
			for (int i = 0; i < audioCapDevList.size(); i++) {
				// ��ȡһ�������豸���֣�ת���ɲɼ�����
				audioCapDevInfo = audioCapDevList.elementAt(i);
				Format[] videoFormats = audioCapDevInfo.getFormats();
				// ����豸������vfw��ͷ
				if (audioCapDevInfo.getName().startsWith("vfw:")) {
					// �������֧��RGB��ʽ
					for (int j = 0; j < videoFormats.length; j++) {
						// ����ֻ��Ҫ��һ��YUV��ʽ
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
					// �õ����豸�Ķ�λ��
					audioCapDevLoc = audioCapDevInfo.getLocator();
				}
			}
		}
	}

	/**
	 * ������Ƶ
	 */
	public void startTransmit() {
		try {
			// ����Socket����
			sc = new Socket(serverAddress, port);
			// �˴�ֻ������� ���ص�ַ,�˿�
			InetAddress add = sc.getLocalAddress();
			int sport = sc.getLocalPort();
			String ip = add.toString().substring(1);
			String session = ip + "/" + sport;
			// �õ������
			OutputStream out = sc.getOutputStream();
			DataOutputStream output = new DataOutputStream(out);
			byte[] address = session.getBytes();
			int len = address.length;
			// ����Ƶ��Ϣ���͸�Ŀ������(����������Ƶ��������ַ,�˿�,��Ϣ����)
			output.writeInt(8 + len);
			output.writeInt(10001); // ���������趨10001Ϊ������Ƶ����
			output.write(address);
			// ������Ƶ��ʽ������
			Format format = new Format(VideoFormat.JPEG);
			// ������Ƶ���Ͷ���
			VedioTransmit vedio = new VedioTransmit(audioCapDevLoc,
					serverAddress, "" + sport, format);
			vedio.start();// ������Ƶ����
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
