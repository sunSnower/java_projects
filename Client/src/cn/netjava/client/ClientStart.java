package cn.netjava.client;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 界面类
 */
public class ClientStart extends JFrame {
	public static JPanel panel = new JPanel();
	public static JFrame jf = new JFrame();

	/**
	 * 初始化界面
	 */
	public void initUI(String from) {
		jf.setTitle(from);
		jf.setSize(560, 480);
		jf.setLayout(new java.awt.BorderLayout());
		// 本地视频显示
		panel.setPreferredSize(new java.awt.Dimension(560, 410));
		panel.setLayout(new java.awt.BorderLayout());
		panel.setBackground(java.awt.Color.BLACK);
		JPanel panel1 = new JPanel();
		panel1.setPreferredSize(new java.awt.Dimension(560, 40));
		javax.swing.JLabel ipLabel = new javax.swing.JLabel("ServerIP:");
		final javax.swing.JTextField ipText = new javax.swing.JTextField("127.0.0.1");
		javax.swing.JLabel portLabel = new javax.swing.JLabel("ServerPort:");
		final javax.swing.JTextField portText = new javax.swing.JTextField("8888");
		javax.swing.JButton button = new javax.swing.JButton("开始视频");
		panel1.add(ipLabel);
		panel1.add(ipText);
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
				// 获得输入的IP和PORT
				String ip = ipText.getText();
				String sport = portText.getText();
				try {
					int port = Integer.parseInt(sport);
					if (port > 1000 && port < 10000) {
						ClientCaptureDevice ccd = new ClientCaptureDevice(ip,
								port);
						ccd.start();
					} else {
						javax.swing.JOptionPane.showMessageDialog(null,
								"Port must be between 1000 to 10000");
					}
				} catch (Exception ef) {
					javax.swing.JOptionPane.showMessageDialog(null,
							"Port only is number");
				}
			}
		});
	}
}
