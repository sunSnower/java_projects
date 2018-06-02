package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import dao.impl.UserDaoImpl;

import pub.PackType;
import pub.QQPackage;

import mytools.LogsReaderWriter;
import mytools.MyDate;

import qq_server_jframe.QQ_Server_JFrame;
import qq_server_jframe.ServerManager_JPanel;
import qq_server_thread.Accept_Thread;
import qq_server_thread.Server_Read_Thread;

/**
 * ����һ��������尴ť�¼��ļ���������ʵ����java.awt.event.ActionListener�ӿ�
  *
 */
public class ServerManager_Button_Listener implements ActionListener {
	private ServerManager_JPanel serverManager_JPanel;
	private QQ_Server_JFrame qq_Server_JFrame;
	private Accept_Thread accept_Thread = null;
	
	/**
	 * ��ǽ��������������Ƿ���������
	 */
	private boolean isAccepted = false;
	
	public ServerManager_Button_Listener(
			ServerManager_JPanel serverManager_JPanel, final QQ_Server_JFrame qq_Server_JFrame) {

		this.serverManager_JPanel = serverManager_JPanel;
		this.qq_Server_JFrame = qq_Server_JFrame;

		// ���ڹرռ���
		this.qq_Server_JFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	/**
	 * ������һЩ��ť����ʱ��������Ӧ
	 */
	public void actionPerformed(ActionEvent e) {
		// ��������
		if (serverManager_JPanel.getButton_Start() == e.getSource()) {
			serverManager_JPanel.getLabel_Image().setIcon(new ImageIcon("./Image/serverstart.gif"));
			serverManager_JPanel.getTextArea_NoticeSend().setEditable(true);
			serverManager_JPanel.getButton_NoticeSend().setEnabled(true);
			// д��־
			String ymd = MyDate.dateFormat(MyDate.FORMAT_YMD);
			String ymdhms = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
			String log = ymdhms + " ��������ɹ�!" + "\n";
			String filePath = "log/" + ymd + ".log" ;
			serverManager_JPanel.getTextArea_CommunicationInfo().append(log);
			LogsReaderWriter.writeIntoFile(log, filePath, true);
			// �谴ť
			serverManager_JPanel.getButton_Start().setEnabled(false);
			serverManager_JPanel.getButton_End().setEnabled(true);
			serverManager_JPanel.getButton_Offline().setEnabled(false);
			// new Accept_Thread �߳�
			//�״ο�������
			if(isAccepted == false){
				Accept_Thread accept_Thread = new Accept_Thread(qq_Server_JFrame,qq_Server_JFrame.getServerSocket());
				this.accept_Thread = accept_Thread;// ����accept_Thread�̵߳�����,ֹͣ����ʱ��ϸ��߳�
				accept_Thread.start();
				isAccepted = true;
			//���״ο�������
			}else{
				try {
					ServerSocket serverSocket = new ServerSocket(qq_Server_JFrame.getServerSocket().getLocalPort());
					qq_Server_JFrame.setServerSocket(serverSocket);
					Accept_Thread accept_Thread = new Accept_Thread(qq_Server_JFrame,qq_Server_JFrame.getServerSocket());
					this.accept_Thread = accept_Thread;// ����accept_Thread�̵߳�����,ֹͣ����ʱ��ϸ��߳�
					accept_Thread.start();
				} catch (IOException e1) {
					// ��ͣ�����ٿ���ʱ,�粻��������������,������ʾ
					
					// �谴ť
					serverManager_JPanel.getButton_Start().setEnabled(true);
					serverManager_JPanel.getButton_End().setEnabled(false);
					serverManager_JPanel.getButton_Offline().setEnabled(false);
					// ��ͼƬ
					serverManager_JPanel.getLabel_Image().setIcon(new ImageIcon("./Image/serverstop.gif"));
					serverManager_JPanel.getTextArea_NoticeSend().setEditable(false);
					serverManager_JPanel.getButton_NoticeSend().setEnabled(false);
					// ����ʾ
					JOptionPane.showMessageDialog(qq_Server_JFrame, "��������ʧ��!��������������,�������Ժ����Ի�������������˿ں�!");
					//e1.printStackTrace();
				}
			}
			

			
			// ֹͣ����
		} else if (serverManager_JPanel.getButton_End() == e.getSource()) {
			int result = JOptionPane.showConfirmDialog(qq_Server_JFrame,"ȷ��Ҫֹͣ������?", "ȷ��ֹͣ����", JOptionPane.YES_NO_OPTION);
			if (result != 0) {
				return;
			}
			// ��ͼƬ
			serverManager_JPanel.getLabel_Image().setIcon(new ImageIcon("./Image/serverstop.gif"));
			serverManager_JPanel.getTextArea_NoticeSend().setEditable(false);
			serverManager_JPanel.getButton_NoticeSend().setEnabled(false);
			// д������ͣ����־
			String filePath = "log/" + MyDate.dateFormat(MyDate.FORMAT_YMD) + ".log" ;
			String time = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
			String log = time + " " + "ֹͣ����ɹ�!" + "\n";
			LogsReaderWriter.writeIntoFile(log, filePath, true);
			serverManager_JPanel.getTextArea_CommunicationInfo().append(log);
			// �޸��û�״̬
			UserDaoImpl.getInstance().upOrDown("����");

			// ˢ���û��б�
			serverManager_JPanel.updateQQUsersInfo_JTable();
			qq_Server_JFrame.getUsersManager().updateQQUsersInfo_JTable();
			// �谴ť
			serverManager_JPanel.getButton_Start().setEnabled(true);
			serverManager_JPanel.getButton_End().setEnabled(false);
			serverManager_JPanel.getButton_Offline().setEnabled(false);
			// ֪ͨ�û�����,��stopServer��
			QQPackage qqPackageStop = new QQPackage();
			qqPackageStop.setPackType(PackType.stopServer);
			qqPackageStop.setData("�������ر�,���ѱ�������!");
			Collection<Server_Read_Thread> serverReadThreads = qq_Server_JFrame.getServerread_Thread_Map().values();
			for (Server_Read_Thread serverReadThread : serverReadThreads) {
				try {
					ObjectOutputStream objectOutputStream = serverReadThread.getObjectOutputStream();
					objectOutputStream.writeObject(qqPackageStop);
					objectOutputStream.flush();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			}
			//д�û�������־(������<<//�ر�Server_Read_Thread����Ӧ����  >>ʱ������쳣,���쳣���������ж�Ӧ����־д��  )
/*			Set<String> IDs = qq_Server_JFrame.getServerread_Thread_Map().keySet();
			for (String id : IDs) {
				String ymdhms = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
				String name = UserDaoImpl.getInstance().selectList(id).getSname();
				String log2 = ymdhms + " " + name + "(" + id + ")" + "������!" + "\n" ;
				serverManager_JPanel.getTextArea_CommunicationInfo().append(log2);
				LogsReaderWriter.writeIntoFile(log2, filePath, true);
			}*/
			// ֹͣAccept_Thread�߳�,�ر�serversocket
			accept_Thread.interrupt();
			try {
				qq_Server_JFrame.getServerSocket().close();
			} catch (IOException e2) {
				//e2.printStackTrace();
			}
			// �ر�Server_Read_Thread����Ӧ����
			for (Server_Read_Thread serverReadThread : serverReadThreads) {

				serverReadThread.interrupt();

				try {
					serverReadThread.getObjectOutputStream().close();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
				try {
					serverReadThread.getObjectInputStream().close();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
				try {
					serverReadThread.getInputStream().close();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
				try {
					serverReadThread.getOutputStream().close();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			}
			// ��ռ���
			qq_Server_JFrame.getServerread_Thread_Map().clear();

		// ǿ���û�����
		} else if (serverManager_JPanel.getButton_Offline() == e.getSource()) {
			// ���ݵ���ж��û�id
			int row = serverManager_JPanel.getQqUsersInfo_JTable().getSelectedRow();
			String id = serverManager_JPanel.getQqUsersInfo_JTable().getValueAt(row, 0).toString();
			// ��װǿ�����߰�
			QQPackage qqPackageOffline = new QQPackage();
			qqPackageOffline.setPackType(PackType.enforceDown);
			qqPackageOffline.setData("���ѱ�ǿ������,�������������Ա��ϵ!");
			ObjectOutputStream objectOutputStream = qq_Server_JFrame
					.getServerread_Thread_Map().get(id).getObjectOutputStream();

			try {
				objectOutputStream.writeObject(qqPackageOffline);
				objectOutputStream.flush();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			// �رձ�ǿ�����ߵ��û��Ķ�Ӧ�߳�
			qq_Server_JFrame.getServerread_Thread_Map().get(id).interrupt();
			// �رձ�ǿ�����ߵ��û�����
			try {
				qq_Server_JFrame.getServerread_Thread_Map().get(id)
						.getObjectInputStream().close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			try {
				qq_Server_JFrame.getServerread_Thread_Map().get(id)
						.getObjectOutputStream().close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			try {
				qq_Server_JFrame.getServerread_Thread_Map().get(id)
						.getInputStream().close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			try {
				qq_Server_JFrame.getServerread_Thread_Map().get(id)
						.getOutputStream().close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			// �ڼ����������ǿ�����ߵ��û����߳�
			qq_Server_JFrame.getServerread_Thread_Map().remove(id);
/*			// ֪ͨ���������û������û�������
			String name1 = UserDaoImpl.getInstance().selectList(id).getSname();
			String all1 = name1 + "(" + id + ")" + "������!" + "\n";
			QQPackage qqPackageNotify = new QQPackage();
			qqPackageNotify.setPackType(PackType.publicChat);
			qqPackageNotify.setData("��ϵͳ��Ϣ�� " + all1);
			Collection<Server_Read_Thread> c2 = qq_Server_JFrame.getServerread_Thread_Map().values();//����
			for (Server_Read_Thread serverReadThread : c2) {
				ObjectOutputStream objectOutputStream2 =serverReadThread.getObjectOutputStream();
				try {
					objectOutputStream2.writeObject(qqPackageNotify);
					objectOutputStream2.flush();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			}
			// д��־
			String ymd = MyDate.dateFormat(MyDate.FORMAT_YMD);
			String ymdhms = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
			String name = UserDaoImpl.getInstance().selectList(id).getSname();
			String all = name + "(" + id + ")";
			String log = ymdhms + " " + all + "������!" + "\n";
			String filePath = "log/" + ymd + ".log" ;
			LogsReaderWriter.writeIntoFile(log, filePath, true);
			serverManager_JPanel.getTextArea_CommunicationInfo().append(log);*/
			// �����û�����״̬
			UserDaoImpl.getInstance().upOrDown(id, "����");
			// ��������
			serverManager_JPanel.updateQQUsersInfo_JTable();
			qq_Server_JFrame.getUsersManager().updateQQUsersInfo_JTable();
			// �ַ����µ������û��б�DefaultListModel��ÿ�����ߵ��û�
			Set<String> onlineUsers = qq_Server_JFrame.getServerread_Thread_Map().keySet();
			DefaultListModel defaultListModel = new DefaultListModel();
			defaultListModel.addElement("������");
			Iterator<String> i = onlineUsers.iterator();
			while (i.hasNext()) {
				String IDTemp = i.next();
				String nameTemp = UserDaoImpl.getInstance().selectList(IDTemp).getSname();
				String all = nameTemp + "(" + IDTemp + ")";
				defaultListModel.addElement(all);
			}
			
			Collection<Server_Read_Thread> readThreads = qq_Server_JFrame.getServerread_Thread_Map().values();
			QQPackage qqPackageOnlineUser = new QQPackage();
			qqPackageOnlineUser.setPackType(PackType.onlineuser);
			qqPackageOnlineUser.setData(defaultListModel);

			for (Server_Read_Thread readThread : readThreads) {
				try {
					readThread.getObjectOutputStream().writeObject(qqPackageOnlineUser);
					readThread.getObjectOutputStream().flush();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			}
		// ���͹���
		} else if (serverManager_JPanel.getButton_NoticeSend() == e.getSource()) {
			String message = serverManager_JPanel.getTextArea_NoticeSend()
					.getText();
			if(message.length() > 200){
				JOptionPane.showMessageDialog(qq_Server_JFrame, "������Ϣ���ݱ�����200(��)������!");
				return;
			}
			String time = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
			String all = time + "\n" + message;
			// д�뱾���ļ�,�û�����ʱĬ�Ͻ������ļ��еĹ�������
			LogsReaderWriter.writeIntoFile(all, "./notice/notice.txt", false);
			// ��װ�����
			QQPackage qqPackageNotice = new QQPackage();
			qqPackageNotice.setPackType(PackType.post);
			qqPackageNotice.setData(all);
			Collection<Server_Read_Thread> serverReadThreads = qq_Server_JFrame
					.getServerread_Thread_Map().values();
			for (Server_Read_Thread serverReadThread : serverReadThreads) {
				ObjectOutputStream objectOutputStream = serverReadThread
						.getObjectOutputStream();
				try {
					objectOutputStream.writeObject(qqPackageNotice);
					objectOutputStream.flush();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
			}
			serverManager_JPanel.getTextArea_NoticeSend().setText("");
			JOptionPane.showMessageDialog(qq_Server_JFrame, "���淢�ͳɹ�!");
		}
	}

	/**
	 * �����¼��������е�windowClosing()�����ô˷���
	 */
	private void close() {
		int result = JOptionPane.showConfirmDialog(qq_Server_JFrame,"ȷ��Ҫ�رճ�����?��������ѿ���,���������û�����ǿ������!", "ȷ�Ϲرճ���",
				JOptionPane.YES_NO_OPTION);
		if (result != 0) {
			return;
		}

		// дͣ����־
		String filePath = "log/" + MyDate.dateFormat(MyDate.FORMAT_YMD) + ".log";
		String time = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
		String log = time + " " + "ֹͣ����ɹ�!" + "\n";
		LogsReaderWriter.writeIntoFile(log, filePath, true);
		serverManager_JPanel.getTextArea_CommunicationInfo().append(log);
		// д�û�������־
		Set<String> IDs = qq_Server_JFrame.getServerread_Thread_Map().keySet();
		for (String id : IDs) {
			String name = UserDaoImpl.getInstance().selectList(id).getSname();
			String log2 = name + "(" + id + ")" + "������!" + "\n";
			serverManager_JPanel.getTextArea_CommunicationInfo().append(log2);
			LogsReaderWriter.writeIntoFile(log2, filePath, true);
		}
		// �޸��û�״̬
		UserDaoImpl.getInstance().upOrDown("����");
		// ֪ͨ�û�����,��stopServer��
		QQPackage qqPackageStop = new QQPackage();
		qqPackageStop.setPackType(PackType.stopServer);
		qqPackageStop.setData("�������ر�,���ѱ�������!");
		Collection<Server_Read_Thread> serverReadThreads = qq_Server_JFrame
				.getServerread_Thread_Map().values();
		for (Server_Read_Thread serverReadThread : serverReadThreads) {
			try {
				ObjectOutputStream objectOutputStream = serverReadThread
						.getObjectOutputStream();
				objectOutputStream.writeObject(qqPackageStop);
				objectOutputStream.flush();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
		}

		System.exit(0);
	}

}
