package qq_server_thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;

import mytools.LogsReaderWriter;
import mytools.MyDate;

import pub.PackType;
import pub.QQPackage;
import dao.bean.User;
import dao.impl.UserDaoImpl;

import qq_server_jframe.QQ_Server_JFrame;

/**
 * ����һ������˼����ͻ����ӵ��߳�
 *
 */
public class Accept_Thread extends Thread {
	private ServerSocket serverSocket = null;

	private QQ_Server_JFrame qq_Server_JFrame = null;
	
	/**
	 * ����һ������˼����ͻ����ӵ��߳�
	 * @param qq_Server_JFrame	����������
	 * @param serverSocket	
	 */
	public Accept_Thread(QQ_Server_JFrame qq_Server_JFrame,
			ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.qq_Server_JFrame = qq_Server_JFrame;
	}

	/**
	 * ��д�����еķ���
	 */
	public void run() {
		Socket socket = null;
		while (!Thread.interrupted()) {
			InputStream inputStream = null;
			OutputStream outputStream = null;
			ObjectInputStream objectInputStream = null;
			ObjectOutputStream objectOutputStream = null;

			try {
				socket = serverSocket.accept();
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				objectInputStream = new ObjectInputStream(inputStream);
				objectOutputStream = new ObjectOutputStream(outputStream);
				QQPackage qqPackageCheck = (QQPackage) objectInputStream
						.readObject();
				// �յ���¼�����
				if (qqPackageCheck.getPackType() == PackType.loginApply) {
					Vector<String> IDAndPsw = (Vector<String>) qqPackageCheck.getData();
					String ID = IDAndPsw.get(0);
					String psw = IDAndPsw.get(1);
					User user = UserDaoImpl.getInstance().selectList(ID);
					QQPackage qqPackageReturn = new QQPackage();

					// �û���������ʱ
					if (user == null) {
						qqPackageReturn.setPackType(PackType.loginFail);
						qqPackageReturn.setData("�û��������ڣ�");
						objectOutputStream.writeObject(qqPackageReturn);
						objectOutputStream.flush();
						// �û�������ʱ
					} else {
						// �û�������
						if (user.getNisonlin().equals("����")) {
							qqPackageReturn.setPackType(PackType.loginFail);
							qqPackageReturn.setData("�û��ѵ�¼��");
							objectOutputStream.writeObject(qqPackageReturn);
							objectOutputStream.flush();
							// �û�������
						} else {
							// �������
							if (!user.getSpassword().equals(psw)) {
								qqPackageReturn.setPackType(PackType.loginFail);
								qqPackageReturn.setData("���벻��ȷ��");
								objectOutputStream.writeObject(qqPackageReturn);
								objectOutputStream.flush();
								// ������ȷ
							} else {
								// new��ȡ�߳�
								Server_Read_Thread server_Read_Thread = new Server_Read_Thread
								(qq_Server_JFrame, socket, objectOutputStream, objectInputStream, inputStream, outputStream , ID);
								
								qq_Server_JFrame.getServerread_Thread_Map().put(ID,server_Read_Thread);// ��ID��read_Thread�ļ�ֵ�Լ��뼯��

								UserDaoImpl.getInstance().upOrDown(ID, "����");// �޸��û�����״̬
								qq_Server_JFrame.getServerManager().updateQQUsersInfo_JTable();// ˢ������
								qq_Server_JFrame.getUsersManager().updateQQUsersInfo_JTable();// ˢ������
								String ymd = MyDate.dateFormat(MyDate.FORMAT_YMD);
								String ymdhms = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
								String log = ymdhms + " " + user.getSname() + "(" + ID + ")������!" + "\n";
								qq_Server_JFrame.getServerManager().getTextArea_CommunicationInfo().append(log);
								LogsReaderWriter.writeIntoFile(log, "log/" + ymd + ".log", true);
								// ��װ��¼�ߵ�id,name,�����û��б�DefaultListModel,����
								qqPackageReturn.setPackType(PackType.loginSuccess);
								String name = user.getSname();
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
								LogsReaderWriter.createNewFile("notice/notice.txt");
								String notice = LogsReaderWriter.readFromFile("notice/notice.txt",null);
								if(notice == null || "".equals(notice)){//�ļ������ڻ��ļ�����Ϊ��Ҫ��nullת����
									notice = "��ʱû�й���";
								}
								Vector data = new Vector();
								data.add(ID);
								data.add(name);
								data.add(defaultListModel);
								data.add(notice);
								qqPackageReturn.setData(data);
								objectOutputStream.writeObject(qqPackageReturn);
								objectOutputStream.flush();
								// �ַ����µ������û��б�DefaultListModel��ÿ�����ߵ��û�
								Collection<Server_Read_Thread> readThreads = qq_Server_JFrame.getServerread_Thread_Map().values();
		
								QQPackage qqPackageOnlineUser = new QQPackage();
								qqPackageOnlineUser.setPackType(PackType.onlineuser);
								qqPackageOnlineUser.setData(defaultListModel);

								for (Server_Read_Thread readThread : readThreads) {
									readThread.getObjectOutputStream().writeObject(qqPackageOnlineUser);
									readThread.getObjectOutputStream().flush();
								}
								//�����߳�
								server_Read_Thread.start();

							}
						}
					}
				}
			} catch (IOException e) {
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}
		}
	}
}
