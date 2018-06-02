package cn.netjava.server;
import javax.media.Player;
import javax.media.protocol.DataSource;
/**
 * ���ű��ػ�ȡ������Դ
*/
public class ChatSourceServer extends Thread {
	private Player player;
	private DataSource dataSource;
	public ChatSourceServer(DataSource dataSource) {
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
			ServerRTPListener l = new ServerRTPListener(player, ServerStart.jf,
					ServerStart.panel);
			// ������������ע�������������
			player.addControllerListener(l);
			// ��ʼ����
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
