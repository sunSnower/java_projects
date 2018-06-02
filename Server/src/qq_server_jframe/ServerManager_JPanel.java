package qq_server_jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import dao.impl.UserDaoImpl;
import ext.JTextAreaExt;

import listener.ServerManager_Button_Listener;
import listener.ServerManager_Table_Listener;

/**
 * ����һ������������壬���̳���javax.swing.JPanel
 *
 */
public class ServerManager_JPanel extends JPanel {
	private JPanel panel_OnlineUsersList = new JPanel();
	private JTable qqUsersInfo_JTable = new JTable(){
		@Override
		public boolean isCellEditable(int row, int column) {
			// �в����Ա༭
			return false;
		}
		//��Ⱦ��,���־���
		public TableCellRenderer getCellRenderer(int row, int column)
		{
			TableCellRenderer renderer = super.getCellRenderer(row, column);
			if (renderer instanceof JLabel)
			{
				((JLabel) renderer).setHorizontalAlignment(JLabel.CENTER);
			}
			return renderer;
		}
	};
	private JButton button_Start = new JButton("����ͨѶ����");
	private JButton button_End = new JButton("ֹͣͨѶ����");
	private JButton button_Offline = new JButton("ǿ���û�����");
	private JLabel label_Image = new JLabel();
	private JTextAreaExt textArea_CommunicationInfo = new JTextAreaExt(6, 80);
	private JTextArea textArea_NoticeSend = new JTextArea(4, 80);
	private JButton button_NoticeSend = new JButton("����");
	private QQ_Server_JFrame qq_Server_JFrame = null;
	//����һ����������ȫ��,����������panel,ʹ֮����һ����������
	private ServerManager_Button_Listener button_Listener = null;
	//public JScrollPane scrollPane = null;

	private JLabel label_UsersTotal = new JLabel();
	private int total = 0;

	/**
	 * ����һ������������
	 */
	public ServerManager_JPanel(QQ_Server_JFrame qq_Server_JFrame){
		super(new BorderLayout());
		
		this.qq_Server_JFrame = qq_Server_JFrame;
		this.button_Listener = new ServerManager_Button_Listener(this, qq_Server_JFrame);
		this.makeAll();
	}

	/**
	 * ����һ����ʾ�����û��б��JPanel
	 * @return	��ʾ�����û��б��JPanel
	 */
	private JPanel makeOnlineUsersList() {
		ServerManager_Table_Listener  listener = new ServerManager_Table_Listener(this,qq_Server_JFrame);
		
		Border border_Line = BorderFactory.createLineBorder(Color.blue);
		Border border_LineTitle = BorderFactory.createTitledBorder(border_Line,"�����û��б�");
		
		panel_OnlineUsersList.setBorder(border_LineTitle);
		panel_OnlineUsersList.setPreferredSize(new Dimension(1, 260));
		panel_OnlineUsersList.setLayout(new BorderLayout());
		qqUsersInfo_JTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//���õ�ѡ
		qqUsersInfo_JTable.getTableHeader().setReorderingAllowed(false); //���ò����ƶ���
		qqUsersInfo_JTable.getSelectionModel().addListSelectionListener(listener);
		
		panel_OnlineUsersList.add(new JScrollPane(qqUsersInfo_JTable), BorderLayout.CENTER);
		UserDaoImpl.getInstance().upOrDown("����");//�������û���Ϊ����״̬
		updateQQUsersInfo_JTable();//��������ʱ��ʼ�������û��б�
		
		return panel_OnlineUsersList;
	}

	/**
	 * ����һ����ʾͨѶ��Ϣ��ʾ��JPanel
	 * @return	ͨѶ��Ϣ��ʾ��JPanel
	 */
	private JPanel makeCommunicationInfo() {
		//Servermanager_TextArea_Listener listener = new Servermanager_TextArea_Listener(this);
		
		JPanel panel = new JPanel(new BorderLayout());
		Border border_Line = BorderFactory.createLineBorder(Color.blue);
		Border border_LineTitle = BorderFactory.createTitledBorder(border_Line, "ͨѶ��Ϣ��ʾ");

		panel.setBorder(border_LineTitle);
		textArea_CommunicationInfo.setEditable(false);
		textArea_CommunicationInfo.setLineWrap(true);
		//textArea_CommunicationInfo.addCaretListener(listener);//��Ӳ��������¼�����
		JScrollPane scrollPane = new JScrollPane(textArea_CommunicationInfo,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(scrollPane, BorderLayout.CENTER);
		// panel.setPreferredSize(new Dimension(200,50));//���� �����������ѡ��СΪ��������
		// JSplitPane �Ĳ��֡�

		return panel;
	}

	/**
	 * ����һ�����淢�͵�Box
	 * @return	һ�����淢�͵�Box
	 */
	private Box makeNoticeSend() {
		Box box = Box.createVerticalBox();
		Border border_Line = BorderFactory.createLineBorder(Color.blue);
		Border border_LineTitle = BorderFactory.createTitledBorder(border_Line,	"���淢��");
		box.setBorder(border_LineTitle);

		textArea_NoticeSend.setEditable(false);
		textArea_NoticeSend.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(textArea_NoticeSend,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		
		button_NoticeSend.addActionListener(button_Listener);
		button_NoticeSend.setEnabled(false);
		//button_NoticeSend.setAlignmentX(Component.RIGHT_ALIGNMENT);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.add(button_NoticeSend);
		box.add(scrollPane);
		box.add(panel);
		// box.setPreferredSize(new Dimension(200,50));//���� �����������ѡ��СΪ��������
		// JSplitPane �Ĳ��֡�

		return box;
	}

	/**
	 * ����һ����������Box
	 * @return	��������Box
	 */
	private Box makeServerManage() {
		Box box = Box.createHorizontalBox();
		Border border_Line = BorderFactory.createLineBorder(Color.blue);
		Border border_LineTitle = BorderFactory.createTitledBorder(border_Line,"����������");
		box.setBorder(border_LineTitle);

		Box box_Right = Box.createVerticalBox();
		
		ImageIcon icon = new ImageIcon("./Image/serverstop.gif");
		this.label_Image.setIcon(icon);
		
		
		button_Start.addActionListener(button_Listener);
		button_End.addActionListener(button_Listener);
		button_End.setEnabled(false);
		button_Offline.addActionListener(button_Listener);
		button_Offline.setEnabled(false);
		label_UsersTotal.setForeground(Color.blue);
		
		box_Right.add(button_Start);
		box_Right.add(Box.createRigidArea(new Dimension(1, 6)));
		box_Right.add(button_End);
		box_Right.add(Box.createRigidArea(new Dimension(1, 50)));
		box_Right.add(button_Offline);
		box_Right.add(Box.createVerticalGlue());
		box_Right.add(label_UsersTotal);
		
		box.add(this.label_Image);
		box.add(Box.createHorizontalGlue());
		box.add(Box.createRigidArea(new Dimension(40, 1)));
		box.add(box_Right);

		return box;
	}

	/**
	 * �����˱�������
	 */
	private void makeAll() {
		JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.makeCommunicationInfo(), this.makeNoticeSend());
		// splitPane1.resetToPreferredSizes();//�����������ѡ��СΪ�������� JSplitPane �Ĳ��֡�
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,splitPane1, this.makeServerManage());
		splitPane2.setResizeWeight(1);
		JSplitPane splitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.makeOnlineUsersList(), splitPane2);
		splitPane3.setResizeWeight(1);
		this.add(splitPane3,BorderLayout.CENTER);
	}
	
	/**
	 * ���ļ��л�ȡ���µ��û���Ϣ���ص�table��,�û������ߺ�ִ�д˷���
	 */
	public void updateQQUsersInfo_JTable(){
		Vector<Vector<String>> data = UserDaoImpl.getInstance().selectList("", "", UserDaoImpl.ONLY_UP);
		updateQQUsersInfo_JTable(data);
		
		
	}
	
	/**
	 * ��ʾ��ѯ���������Ϊ��ѯ�󷵻صĽ��
	 * @param data	�û�����
	 */
	public void updateQQUsersInfo_JTable(Vector<Vector<String>> data){
		Vector<String> columnName = new Vector<String>();//��ͷ
		columnName.add("���(QQ)");	
		columnName.add("����");
		columnName.add("�Ա�");
		columnName.add("����");
		columnName.add("�Ƿ�����");
		
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(data, columnName);
		qqUsersInfo_JTable.setModel(dtm);
		qqUsersInfo_JTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		//��ʾ��ǰ��������
		total = data.size();
		label_UsersTotal.setText("��ǰ�����û�������" + total + "    ");
	}

	/**
	 * ��ÿ�ʼ����ť
	 * @return	��ʼ����ť
	 */
	public JButton getButton_Start() {
		return button_Start;
	}

	/**
	 * ���ֹͣ����ť
	 * @return	ֹͣ����ť
	 */
	public JButton getButton_End() {
		return button_End;
	}

	/**
	 * ���ǿ�������û���ť
	 * @return	ǿ�������û���ť
	 */
	public JButton getButton_Offline() {
		return button_Offline;
	}

	/**
	 * �����ʾͼƬ��JLabel
	 * @return	��ʾͼƬ��JLabel
	 */
	public JLabel getLabel_Image() {
		return label_Image;
	}

	/**
	 * �����Ϣ���Ϳ�
	 * @return	��Ϣ���Ϳ�
	 */
	public JTextArea getTextArea_NoticeSend() {
		return textArea_NoticeSend;
	}

	/**
	 * �����Ϣ���Ͱ�ť
	 * @return	��Ϣ���Ͱ�ť
	 */
	public JButton getButton_NoticeSend() {
		return button_NoticeSend;
	}

	/**
	 * ��������û��б��JPanel
	 * @return	�����û��б��JPanel
	 */
	public JPanel getPanel_OnlineUsersList() {
		return panel_OnlineUsersList;
	}

	/**
	 * ��������û��б��JTable
	 * @return	�����û��б��JTable
	 */
	public JTable getQqUsersInfo_JTable() {
		return qqUsersInfo_JTable;
	}

	/**
	 * ���ͨѶ��Ϣ��ʾ��
	 * @return	ͨѶ��Ϣ��ʾ��
	 */
	public JTextArea getTextArea_CommunicationInfo() {
		return textArea_CommunicationInfo;
	}

	/**
	 * ��÷����������
	 * @return	�����������
	 */
	public QQ_Server_JFrame getQq_Server_JFrame() {
		return qq_Server_JFrame;
	}

	/**
	 * ��������û��б�
	 * @return	�����û��б�
	 */
	public JTable getQQUsersInfo_JTable() {
		return qqUsersInfo_JTable;
	}
}
