package cn.netjava.client;
import javax.media.Player;
import javax.media.protocol.DataSource;
/**
 * ���ű��ػ�ȡ������Դ
*/
public class ChatSource extends Thread {
	private Player player;
	private DataSource dataSource;
	public ChatSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	@Override
	public void run() {
		playSource();
	}
	/**
	 * ���ز��ŵķ���
	 * @param:dataSource Ҫ��ʼ��������Դ
	 */
	public void playSource() {
		try {
			// ����һ������������
			player = javax.media.Manager.createPlayer(dataSource);
			// ����һ���������Ŀ���������������
			MyRTPListener l = new MyRTPListener(player, ClientStart.jf,
					ClientStart.panel);
			// ������������ע�������������
			player.addControllerListener(l);
			// ��ʼ����
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
