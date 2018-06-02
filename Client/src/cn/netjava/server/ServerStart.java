package cn.netjava.server;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 界面类
 */
public class ServerStart extends JFrame {
	public static JPanel panel = new JPanel();
	public static JFrame jf = new JFrame();

	/**
	 * 初始化界面
	 */
	public void initUI(String to) {
		jf.setTitle(to);
		jf.setSize(560, 480);
		jf.setLayout(new java.awt.BorderLayout());
		// 视频显示
		panel.setPreferredSize(new java.awt.Dimension(560, 410));
		panel.setLayout(new java.awt.BorderLayout());
		panel.setBackground(java.awt.Color.BLACK);
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new java.awt.Dimension(560, 40));
		javax.swing.JLabel portLabel = new javax.swing.JLabel("Port:");
		final javax.swing.JTextField portText = new javax.swing.JTextField("8888");
		javax.swing.JButton button = new javax.swing.JButton("开始接收");
		panel1.add(portLabel);
		panel1.add(portText);
		panel1.add(button);
		jf.add(java.awt.BorderLayout.NORTH, panel);
		jf.add(java.awt.BorderLayout.SOUTH, panel1);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(2);
		jf.setVisible(true);
		button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sport = portText.getText();
				try {
					int port = Integer.parseInt(sport);
					if (port > 1000 && port < 10000) {// 指定启动端口在1000~10000之间
						AcceptClient ac = new AcceptClient(port);
						ac.start();
						javax.swing.JOptionPane.showMessageDialog(null,
								"ServerStart");
					} else
						javax.swing.JOptionPane.showMessageDialog(null,
								"Port must be between 1000 to 10000");
				} catch (Exception ef) {
					javax.swing.JOptionPane.showMessageDialog(null,
							"Port only is number");
				}
			}
		});
	}
}
