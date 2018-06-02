package cn.netjava.client;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.IOException;
import javax.media.protocol.DataSource;
import javax.media.*;
import javax.media.rtp.*;
import javax.media.rtp.event.*;
/**
 * ���ز����¼�������
*/
public class MyRTPListener implements ReceiveStreamListener, ControllerListener {
	private Player player;
	private JFrame jf;
	private JPanel panel;
	public MyRTPListener(Player player, JFrame jf, JPanel panel) {
		this.player = player;
		this.jf = jf;
		this.panel = panel;
	}
	/**
	 * RTP�¼�����
	 */
	public synchronized void update(ReceiveStreamEvent evt) {
		if (evt instanceof NewReceiveStreamEvent) {
			ReceiveStream stream = ((NewReceiveStreamEvent) evt).getReceiveStream();
			DataSource dataSource = stream.getDataSource();
			try {
				Player player = javax.media.Manager.createPlayer(dataSource);
				player.addControllerListener(this);
				player.realize();
			} catch (NoPlayerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * ʵ�ּ������ӿ��еķ���.�˷�����֪ͨӦ�ó���Բ��������¼�������Ӧ
	 */
	public synchronized void controllerUpdate(ControllerEvent e) {
		if (e instanceof javax.media.RealizeCompleteEvent) {
			Component comp;
			// �õ��������Ŀ�������,����������ʾ��Ƶ������
			if ((comp = player.getVisualComponent()) != null) {
				// �����������ӵ�������
				panel.add("Center", comp);
			}
			// �õ��������Ŀ����������
			if ((comp = player.getControlPanelComponent()) != null) {
				// ��ӵ�������
				panel.add("South", comp);
			}
			// ˢ�´���
			jf.validate();
		} 
	}
}
