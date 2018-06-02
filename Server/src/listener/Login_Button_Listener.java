package listener;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

import qq_server_jframe.Login_JFrame;
import qq_server_jframe.QQ_Server_JFrame;

/**
 * ����һ������˵�¼���ڵİ�ť����������ʵ����java.awt.event.ActionListener�ӿ�
  * 
 */
public class Login_Button_Listener implements ActionListener{
	private Login_JFrame login_JFrame;
	
	public Login_Button_Listener(Login_JFrame login_JFrame) {
		this.login_JFrame = login_JFrame;
	}

	/**
	 * ������һЩ��ť����ʱ��������Ӧ
	 */
	public void actionPerformed(ActionEvent e) {
		ServerSocket serverSocket = null;
		
		Properties properties = new Properties();
		String temp = login_JFrame.getTextField_Port().getText();
		if(temp == null){
			login_JFrame.getLabel_Message().setText("������˿ں�!");
			return;
		}
		String port = temp.replaceAll(" ", "");
		login_JFrame.getTextField_Port().setText(port);
		
		if(serverSocket == null){
			int p = 0;
			if(port.matches("[0-9]{1,5}")){
				p = Integer.parseInt(port);
				if(p>1023 && p<65536){
					try {
						serverSocket = new ServerSocket(p);
						properties.setProperty("port", port);
						if(e.getSource() != login_JFrame.getButton_Test()){
							login_JFrame.saveConfig(properties);
						}
						login_JFrame.getLabel_Message().setText("�ö˿ڿ���ʹ��!");
					} catch (IOException e1) {
						if(e.getSource() == login_JFrame.getButton_Test()){
							login_JFrame.getLabel_Message().setText("�˿�ָ��ʧ��,�����¸����˿ںź�����!");
						}else if(e.getSource() == login_JFrame.getButton_Save()){
							login_JFrame.getLabel_Message().setText("����ָ���˿�ʧ��,�����¸����˿ںź�����!");
						}
					}

				}else{
					login_JFrame.getLabel_Message().setText("�˿ں�ӦΪ1024~65535֮�������!");
					return;
				}
			}else{
				login_JFrame.getLabel_Message().setText("�˿ں�ӦΪ1024~65535֮�������!");
				return;
			}
		}
		//���԰�ť
		if(login_JFrame.getButton_Test() == e.getSource()){
			try {
				serverSocket.close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			
		}else{
			//��������
			if(login_JFrame.getButton_Save() == e.getSource()){
				try {
					serverSocket.close();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
				login_JFrame.getLabel_Message().setText("�������óɹ�!");

			//���������
			}else if(login_JFrame.getButton_Enter() == e.getSource()){
				//��serversocket���������������
				login_JFrame.dispose();
				new QQ_Server_JFrame(serverSocket);
			}
		}
	}
}
