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
 * 本地播放事件监听类
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
	 * RTP事件处理
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
	 * 实现监听器接口中的方法.此方法可通知应用程序对播放器的事件做出反应
	 */
	public synchronized void controllerUpdate(ControllerEvent e) {
		if (e instanceof javax.media.RealizeCompleteEvent) {
			Component comp;
			// 得到播放器的可视容器,即播放器显示视频的容器
			if ((comp = player.getVisualComponent()) != null) {
				// 将可视容器加到窗体上
				panel.add("Center", comp);
			}
			// 得到播放器的控制面板容器
			if ((comp = player.getControlPanelComponent()) != null) {
				// 添加到窗体上
				panel.add("South", comp);
			}
			// 刷新窗体
			jf.validate();
		} 
	}
}
