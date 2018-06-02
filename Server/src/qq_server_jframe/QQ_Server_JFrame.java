package qq_server_jframe;

import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Map;


import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import qq_server_thread.Server_Read_Thread;

/**
 * ���Ƿ����������
 *
 */
public class QQ_Server_JFrame extends JFrame{
	private ServerSocket serverSocket = null;
	
	/**
	 * ������id���ȡ�̵߳ļ�ֵ��
	 */
	private Map<String, Server_Read_Thread> serverread_Thread_Map = new Hashtable<String, Server_Read_Thread>();
	
	private ServerManager_JPanel serverManager = new ServerManager_JPanel(this);
	private UsersManager_JPanel usersManager = new UsersManager_JPanel(this);
	private LogsManager_JPanel logsManager = new LogsManager_JPanel(this);
	
	/**
	 * ����һ�������������
	 * @param serverSocket	�ڵ�¼���洴����ServerSocekt����
	 */
	public QQ_Server_JFrame(ServerSocket serverSocket) {
		super("�����");
		this.serverSocket = serverSocket;
//		this.usersManager.getUserInfoModify_JDialog().setVisible(false);
//		this.usersManager.getUserInfoModify_JDialog().setModal(true);

		this.makeAll();
		this.pack();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}

	/**
	 * ���ɷ����������û��������־����
	 */
	private void makeAll(){
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("����������",null,this.serverManager,"����������");
		tabbedPane.addTab("�û�����",null,this.usersManager,"�û�����");
		tabbedPane.addTab("��־����",null,this.logsManager,"��־����");
		
		this.getContentPane().add(tabbedPane);
	}
	
	/**
	 * ��÷����������
	 * @return
	 */
	public ServerManager_JPanel getServerManager() {
		return serverManager;
	}

	/**
	 * ����û��������
	 * @return	�û��������
	 */
	public UsersManager_JPanel getUsersManager() {
		return usersManager;
	}

	/**
	 * �����־�������
	 * @return	��־�������
	 */
	public LogsManager_JPanel getLogsManager() {
		return logsManager;
	}

	/**
	 * ��ô�����ServerSocket
	 * @return	������ServerSocket
	 */
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * ����һ���µ�ServerSocket
	 * @param serverSocket
	 */
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * ���id���ȡ�̵߳ļ�ֵ�Լ���
	 * @return	id���ȡ�̵߳ļ�ֵ�Լ���
	 */
	public Map<String, Server_Read_Thread> getServerread_Thread_Map() {
		return serverread_Thread_Map;
	}
}
