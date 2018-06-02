package qq_server_thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;
import dao.bean.User;
import dao.impl.UserDaoImpl;

import mytools.LogsReaderWriter;
import mytools.MyDate;

import pub.PackType;
import pub.QQPackage;

import qq_server_jframe.QQ_Server_JFrame;

/**
 * ����һ����ȡ�߳��࣬����˶�Ӧÿ���ͻ��˶���һ�������Ķ�ȡ�߳�
 *
 */
public class Server_Read_Thread extends Thread {
	private Socket socket = null;
	private ObjectOutputStream objectOutputStream = null;
	private ObjectInputStream objectInputStream = null;
	private QQ_Server_JFrame qq_Server_JFrame = null;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private String ID;

	/**
	 * ����һ����ȡ�߳�
	 * @param qq_Server_JFrame
	 * @param socket
	 * @param objectOutputStream	�Կͻ��˵Ķ��������
	 * @param objectInputStream		�Կͻ��˵Ķ���������
	 * @param inputStream
	 * @param outputStream
	 * @param ID	ÿ���̶߳����ڶ�Ӧ��ÿ���û�
	 */
	public Server_Read_Thread(QQ_Server_JFrame qq_Server_JFrame, Socket socket,
			ObjectOutputStream objectOutputStream,
			ObjectInputStream objectInputStream, InputStream inputStream, OutputStream outputStream, String ID) {
		this.socket = socket;

		this.objectOutputStream = objectOutputStream;
		this.objectInputStream = objectInputStream;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.ID = ID;
		
		this.qq_Server_JFrame = qq_Server_JFrame;
	}
	
	/**
	 * ��д�����еķ���
	 */
	public void run() {

		while (!Thread.interrupted()) {
			try {
				QQPackage qqPackageReceive = (QQPackage) objectInputStream.readObject();
				PackType packType = qqPackageReceive.getPackType();
				// �յ����İ�ʱ
				if (packType == PackType.publicChat) {
					Collection<Server_Read_Thread> serverReadThreads = qq_Server_JFrame.getServerread_Thread_Map().values();
					String from = qqPackageReceive.getFrom();
					String message = qqPackageReceive.getData().toString();
					String time = MyDate.dateFormat(MyDate.FORMAT_HMS);
					String send = from + " " + time + " ��������˵:" + "\n" + "    " + message + "\n";
					System.out.println(send);
					// ��װ��
					QQPackage qqPackageSend = new QQPackage();
					qqPackageSend.setPackType(PackType.publicChat);
					qqPackageSend.setData(send);
					for (Server_Read_Thread serverReadThread : serverReadThreads) {
						ObjectOutputStream objectOutputStream = serverReadThread.getObjectOutputStream();
						if(!objectOutputStream.equals(this.objectOutputStream)){
							objectOutputStream.writeObject(qqPackageSend);
							objectOutputStream.flush();
						}

					}

					
				// �յ�˽�İ�ʱ
				} else if(packType == PackType.privateVideo) {
					String from = qqPackageReceive.getFrom();
					String to = qqPackageReceive.getTo();
					String time = MyDate.dateFormat(MyDate.FORMAT_HMS);
					System.out.println(to);
					
					QQPackage qqPackagePrivate = new QQPackage();
					qqPackagePrivate.setPackType(PackType.privateVideo);//���ڽ�������˵������˽�ĵĲ�����һ����....
					qqPackagePrivate.setData(to);
					ObjectOutputStream objectOutputStream = qq_Server_JFrame.getServerread_Thread_Map().get(to).getObjectOutputStream();
					objectOutputStream.writeObject(qqPackagePrivate);
					objectOutputStream.flush();
				} 
				
				else if (packType == PackType.privateChat) {
					//���
					String from = qqPackageReceive.getFrom();
					String to = qqPackageReceive.getTo();
					String message = qqPackageReceive.getData().toString();
					String time = MyDate.dateFormat(MyDate.FORMAT_HMS);
					//��װ��
					QQPackage qqPackagePrivate = new QQPackage();
					String send = from + " " + time + " ����˵:" + "\n" + "    " + message + "\n";
					System.out.println(send);
					qqPackagePrivate.setPackType(PackType.publicChat);//���ڽ�������˵������˽�ĵĲ�����һ����....
					qqPackagePrivate.setData(send);
					ObjectOutputStream objectOutputStream = qq_Server_JFrame.getServerread_Thread_Map().get(to).getObjectOutputStream();
					objectOutputStream.writeObject(qqPackagePrivate);
					objectOutputStream.flush();
				// �յ��û����߰�ʱ
				} else if (packType == PackType.userOffline) {
					String ID = qqPackageReceive.getFrom();
					String IDName = qqPackageReceive.getData().toString();
					
					// �����û��ļ���ˢ������
					UserDaoImpl.getInstance().upOrDown(ID, "����");
					qq_Server_JFrame.getServerManager().updateQQUsersInfo_JTable();
					qq_Server_JFrame.getUsersManager().updateQQUsersInfo_JTable();
					// ������־
					String ymdhms = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
					String ymd =  MyDate.dateFormat(MyDate.FORMAT_YMD);
					String log = ymdhms + " " + IDName + "������!" + "\n";
					qq_Server_JFrame.getServerManager().getTextArea_CommunicationInfo().append(log);
					LogsReaderWriter.writeIntoFile(log, "log/" + ymd+ ".log", true);
					// �ر������õ��̼߳���Ӧ����
					this.interrupt();
					try {
						this.objectOutputStream.close() ;
					} catch (IOException e1) {
						//e1.printStackTrace();
					}
					try {
						this.objectInputStream.close();
					} catch (IOException e2) {
						//e2.printStackTrace();
					}
					try {
						this.outputStream.close() ;
					} catch (IOException e1) {
						//e1.printStackTrace();
					}
					try {
						this.inputStream.close();
					} catch (IOException e1) {
						//e1.printStackTrace();
					}
					// ɾ����Ӧ���û��̶߳���
					qq_Server_JFrame.getServerread_Thread_Map().remove(ID);
					// ֪ͨ���������û������û�������
					String name1 = UserDaoImpl.getInstance().selectList(ID).getSname();
					String all1 = name1 + "(" + ID + ")" + "������!" + "\n";
					QQPackage qqPackageNotify = new QQPackage();
					qqPackageNotify.setPackType(PackType.publicChat);
					qqPackageNotify.setData("��ϵͳ��Ϣ�� " + all1);
					Collection<Server_Read_Thread> c2 = qq_Server_JFrame.getServerread_Thread_Map().values();//����
					for (Server_Read_Thread serverReadThread : c2) {
						ObjectOutputStream objectOutputStream =serverReadThread.getObjectOutputStream();
						objectOutputStream.writeObject(qqPackageNotify);
						objectOutputStream.flush();
					}
					// �ַ����µ������û��б�
					Set<String> IDs = qq_Server_JFrame.getServerread_Thread_Map().keySet();
					DefaultListModel defaultListModel = new DefaultListModel();
					defaultListModel.addElement("������");
					for (String s : IDs) {
						String name = UserDaoImpl.getInstance().selectList(s).getSname();
						String all = name + "(" + s + ")" ;
						defaultListModel.addElement(all);
					}
					QQPackage qqPackageOnlineUsers = new QQPackage();
					qqPackageOnlineUsers.setPackType(PackType.onlineuser);
					qqPackageOnlineUsers.setData(defaultListModel);
					
					Collection<Server_Read_Thread> c = qq_Server_JFrame.getServerread_Thread_Map().values();//����
					for (Server_Read_Thread serverReadThread : c) {
						ObjectOutputStream objectOutputStream =serverReadThread.getObjectOutputStream();
						objectOutputStream.writeObject(qqPackageOnlineUsers);
						objectOutputStream.flush();
					}
					
				//�յ��޸������ʱ
				}else if(packType == PackType.resetPassword){
					String ID = qqPackageReceive.getFrom();
					Vector<String> psw = (Vector<String>)qqPackageReceive.getData();
					String oldPsw = psw.get(0);
					String newPsw = psw.get(1);
					User user = UserDaoImpl.getInstance().selectList(ID);
					//��װ��
					QQPackage qqPackageReturn = new QQPackage();
					qqPackageReturn.setPackType(PackType.resetPassword);
					//���������
					if(user.getSpassword().equals(oldPsw)){
						 boolean isSuccess = UserDaoImpl.getInstance().resetPWD(ID, newPsw);
						 //�޸ĳɹ�
						 if(isSuccess == true){
							 qqPackageReturn.setData("�����޸ĳɹ�!���μ�������!");
						//�޸�ʧ��
						 }else {
							 qqPackageReturn.setData("����������,�����޸�ʧ��!���Ժ�����!");
						 }
					//�����벻���
					}else {
						qqPackageReturn.setData("ԭ�������벻��ȷ,�����޸�ʧ��!");
					}
					ObjectOutputStream objectOutputStream = qq_Server_JFrame.getServerread_Thread_Map().get(ID).getObjectOutputStream();
					objectOutputStream.writeObject(qqPackageReturn);
					objectOutputStream.flush();
				}

			} catch (IOException e) {
				//�Է��ϵ���������
				this.interrupt();
				try {
					this.objectOutputStream.close() ;
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
				try {
					this.objectInputStream.close();
				} catch (IOException e2) {
					//e2.printStackTrace();
				}
				try {
					this.outputStream.close() ;
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
				try {
					this.inputStream.close();
				} catch (IOException e1) {
					//e1.printStackTrace();
				}
				qq_Server_JFrame.getServerread_Thread_Map().remove(ID);	
				// ֪ͨ���������û������û�������
				String name1 = UserDaoImpl.getInstance().selectList(ID).getSname();
				String all1 = name1 + "(" + ID + ")" + "������!" + "\n";
				QQPackage qqPackageNotify = new QQPackage();
				qqPackageNotify.setPackType(PackType.publicChat);
				qqPackageNotify.setData("��ϵͳ��Ϣ�� " + all1);
				Collection<Server_Read_Thread> c2 = qq_Server_JFrame.getServerread_Thread_Map().values();//����
				for (Server_Read_Thread serverReadThread : c2) {
					ObjectOutputStream objectOutputStream =serverReadThread.getObjectOutputStream();
					try {
						objectOutputStream.writeObject(qqPackageNotify);
						objectOutputStream.flush();
					} catch (IOException e1) {
						//e1.printStackTrace();
					}
				}
				// ��������״̬��ˢ������
				UserDaoImpl.getInstance().upOrDown(ID, "����");
				qq_Server_JFrame.getServerManager().updateQQUsersInfo_JTable();
				qq_Server_JFrame.getUsersManager().updateQQUsersInfo_JTable();
				// ������־
				String ymdhms = MyDate.dateFormat(MyDate.FORMAT_YMDHMS);
				String ymd =  MyDate.dateFormat(MyDate.FORMAT_YMD);
				String name = UserDaoImpl.getInstance().selectList(ID).getSname();
				String IDName = name + "(" + ID + ")" ;
				String log = ymdhms + " " + IDName + "������!" + "\n";
				qq_Server_JFrame.getServerManager().getTextArea_CommunicationInfo().append(log);
				LogsReaderWriter.writeIntoFile(log, "log/" + ymd+ ".log", true);
				
				//e.printStackTrace();
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}

		}

	}

	/**
	 * ��ö��������
	 * @return	���������
	 */
	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	/**
	 * ��ö���������
	 * @return	����������
	 */
	public ObjectInputStream getObjectInputStream() {
		return objectInputStream;
	}

	/**
	 * ���������
	 * @return	������
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * ��������
	 * @return	�����
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
}
