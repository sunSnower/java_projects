package cn.netjava.server;
import javax.media.Player;
import javax.media.protocol.DataSource;
/**
 * 播放本地获取的数据源
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
	 * 本地播放的方法
	 * @param:dataSource 要初始化的数据源
	 */
	public void playSource() {
		try {
			// 创建一个播放器对象
			player = javax.media.Manager.createPlayer(dataSource);
			// 创建一个播放器的控制器监听器对象
			ServerRTPListener l = new ServerRTPListener(player, ServerStart.jf,
					ServerStart.panel);
			// 给播放器对象注册控制器监听器
			player.addControllerListener(l);
			// 开始播放
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
