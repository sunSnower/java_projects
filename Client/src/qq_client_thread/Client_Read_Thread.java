package qq_client_thread;

import java.io.IOException;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import cn.netjava.server.ServerStart;
import pub.PackType;
import pub.QQPackage;

import qq_client_jframe.QQ_Chat_JFrame;
/**
 * ����һ���ͻ��˵Ķ�ȡ�̣߳����̳���java.lang.Thread
 *
 */
public class Client_Read_Thread extends Thread {
	private QQ_Chat_JFrame qq_Chat_JFrame = null;
	
	/**
	 * ����һ���ͻ��˶�ȡ�߳�
	 * @param qq_Chat_JFrame	����Ӧ�����촰��
	 * @param socket		����������ӵ�socket
	 */
	public Client_Read_Thread(QQ_Chat_JFrame qq_Chat_JFrame, Socket socket) {
		this.qq_Chat_JFrame = qq_Chat_JFrame;
		
	}

	/**
	 * ��д�����е�run����
	 */
	public void run() {
		
		QQPackage qqPackageRec = null;
		PackType packType = null;
		Object object = null;
		
		while (!Thread.interrupted()) {
			try {
				qqPackageRec = (QQPackage) qq_Chat_JFrame.getObjectInputStream().readObject();
				packType = qqPackageRec.getPackType();
				System.out.println(packType);
				object = qqPackageRec.getData();
			} catch (IOException e) {
				//�Է��ϵ���������
				JOptionPane.showMessageDialog(qq_Chat_JFrame, "�������ر�,���ѱ�������!");
				System.exit(0);
				
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}

			// �����
			if (packType == PackType.publicChat) {
				String message = object.toString();
				qq_Chat_JFrame.getTextArea_Dsp().append(message);
				qq_Chat_JFrame.getTextArea_ChatLogs().append(message);
			// �����û��б��
			} else if (packType == PackType.onlineuser) {
				DefaultListModel defaultListModel = (DefaultListModel) qqPackageRec.getData();
				qq_Chat_JFrame.getList_OnlineUsers().setModel(defaultListModel);
				qq_Chat_JFrame.validate();
			// ������ֹͣ��
			} else if (packType == PackType.stopServer){
				String message = qqPackageRec.getData().toString();
				JOptionPane.showMessageDialog(qq_Chat_JFrame, message);
				qq_Chat_JFrame.dispose();
				System.exit(0);
			// �����
			} else if (packType == PackType.post){
				String message = qqPackageRec.getData().toString();
				qq_Chat_JFrame.getTextArea_Notice().setText(message);
			// ���߰�
			} else if (packType == PackType.enforceDown){
				String message = qqPackageRec.getData().toString();
				JOptionPane.showMessageDialog(qq_Chat_JFrame, message);
				System.exit(0);
			// �����޸Ļظ���
			} else if (packType == PackType.resetPassword){
				String message = qqPackageRec.getData().toString();
				if("�����޸ĳɹ�!���μ�������!".equals(message)){
					qq_Chat_JFrame.getResetPassword_JDialog().getLabel_Message().setText("");
					JOptionPane.showMessageDialog(qq_Chat_JFrame, message);
					qq_Chat_JFrame.getResetPassword_JDialog().dispose();
				}else{
					qq_Chat_JFrame.getResetPassword_JDialog().getLabel_Message().setText(message);
				}
			} else if(packType == PackType.privateVideo) {
				String to = qqPackageRec.getData().toString();
				ServerStart test = new ServerStart();
				test.initUI(to);
			}
		}
	}
}
