package qq_server_jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import listener.UsersManager_Button_Listener;
import listener.UsersManager_Table_Listener;

import dao.impl.UserDaoImpl;

/**
 * ����һ���û�������壬���̳���javax.swing.JPanel
 *
 */
public class UsersManager_JPanel extends JPanel{
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
	private JTextField textField_ID = new JTextField();
	private JTextField textField_Name = new JTextField();
	private JButton button_Query = new JButton("��ѯ");
	private JLabel label_Total = new JLabel();
	private JButton button_Add = new JButton("����û�");
	private JButton button_Del = new JButton("ɾ���û�");
	private JButton button_Mod = new JButton("�޸�����");
	private JButton button_Sel = new JButton("������ѡ����");
	private JButton button_All = new JButton("������������");
	//private JButton button_Ref = new JButton("ˢ���û��б�");

	private QQ_Server_JFrame qq_Server_JFrame = null;

	/**
	 * ����һ���û��������
	 * @param qq_Server_JFrame	�����������
	 */
	public UsersManager_JPanel(QQ_Server_JFrame qq_Server_JFrame) {
		super(new BorderLayout());
		this.qq_Server_JFrame = qq_Server_JFrame;
		this.makeAll();
	}

	/**
	 * ����һ���û��������
	 */
	public void makeAll(){
		JPanel panel = new JPanel(new BorderLayout());
		
		UsersManager_Button_Listener listener = new UsersManager_Button_Listener(this,qq_Server_JFrame);
		
		JPanel panel_North = new JPanel();
		JLabel lable_Account = new JLabel("���QQ:");
		textField_ID.setPreferredSize(new Dimension(140,23));
		JLabel lable_UserName = new JLabel("�û���:");
		textField_Name.setPreferredSize(new Dimension(140,23));
		//JLabel lable_State = new JLabel("����״̬:");
		//Vector<String> vector_State = new Vector<String>();
		//vector_State.add("ȫ��");
		//vector_State.add("����");
		//vector_State.add("����");
		//JComboBox comboBox_State = new JComboBox(vector_State);
		button_Query.addActionListener(listener);
		panel_North.add(lable_Account);
		panel_North.add(textField_ID);
		panel_North.add(lable_UserName);
		panel_North.add(textField_Name);
		//panel_North.add(lable_State);
		//panel_North.add(comboBox_State);
		panel_North.add(button_Query);
		panel_North.add(label_Total);
		
		Border border_Line = BorderFactory.createLineBorder(Color.blue);
		Border border_LineTitle = BorderFactory.createTitledBorder(border_Line, "�û���Ϣ�б�");
		
		JPanel panel_UsersInfoList = new JPanel(new BorderLayout());
		panel_UsersInfoList.setBorder(border_LineTitle);
		panel_UsersInfoList.setPreferredSize(new Dimension(744,480));
		panel_UsersInfoList.setLayout(new BorderLayout());
		panel_UsersInfoList.add(new JScrollPane(qqUsersInfo_JTable),BorderLayout.CENTER);
		qqUsersInfo_JTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//���õ�ѡ
		qqUsersInfo_JTable.getTableHeader().setReorderingAllowed(false); //���ò����ƶ���

		
		JPanel panel_South = new JPanel();
		button_Add.addActionListener(listener);
		button_Del.setEnabled(false);
		button_Del.addActionListener(listener);
		button_Mod.setEnabled(false);
		button_Mod.addActionListener(listener);
		button_Sel.setEnabled(false);
		button_Sel.addActionListener(listener);
		button_All.addActionListener(listener);
		//button_Ref.addActionListener(listener);
		panel_South.add(button_Add);
		panel_South.add(button_Del);
		panel_South.add(button_Mod);
		panel_South.add(button_Sel);
		panel_South.add(button_All);
		//panel_South.add(button_Ref);
		UsersManager_Table_Listener table_Listener = new UsersManager_Table_Listener(this,qq_Server_JFrame);
		qqUsersInfo_JTable.getSelectionModel().addListSelectionListener(table_Listener);//��table���ѡ��ģ�͵ļ���
		

		updateQQUsersInfo_JTable();//��������ʱ���������û���Ϣ��table
		
		panel.add(panel_North,BorderLayout.NORTH);
		panel.add(panel_UsersInfoList,BorderLayout.CENTER);
		panel.add(panel_South,BorderLayout.SOUTH);
		
		add( panel);
	}
	
	/**
	 * ���ļ��л�ȡ���µ��û���Ϣ���ص�table��,ÿ�ζԱ����ɾ�Ĳ����֮��ִ�д˷���
	 */
	public void updateQQUsersInfo_JTable(){
		Vector<Vector<String>> data = UserDaoImpl.getInstance().selectList("", "", UserDaoImpl.UP_AND_DOWN);
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
		columnName.add("סַ");
		columnName.add("�Ƿ�����");
		columnName.add("ע��ʱ��");
		
		DefaultTableModel dtm = new DefaultTableModel();
		dtm.setDataVector(data, columnName);
		qqUsersInfo_JTable.setModel(dtm);
		qqUsersInfo_JTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		//����ģ�ͺ������п�
		qqUsersInfo_JTable.getColumnModel().getColumn(0).setPreferredWidth(28);
		qqUsersInfo_JTable.getColumnModel().getColumn(1).setPreferredWidth(45);
		qqUsersInfo_JTable.getColumnModel().getColumn(2).setPreferredWidth(2);
		qqUsersInfo_JTable.getColumnModel().getColumn(3).setPreferredWidth(3);
		qqUsersInfo_JTable.getColumnModel().getColumn(4).setPreferredWidth(230);
		qqUsersInfo_JTable.getColumnModel().getColumn(5).setPreferredWidth(2);
		qqUsersInfo_JTable.getColumnModel().getColumn(6).setPreferredWidth(90);
		//�������û���
		int total = data.size();
		label_Total.setForeground(Color.blue);
		label_Total.setText("    ��¼����: " + total);
	}
	
	/**
	 * ����û��б��JTable
	 * @return	�û��б��JTable
	 */
	public JTable getQQUsersInfo_JTable() {
		return qqUsersInfo_JTable;
	}

	/**
	 * �������û���ť
	 * @return	����û���ť
	 */
	public JButton getButton_Add() {
		return button_Add;
	}

	/**
	 * ���ɾ���û���ť
	 * @return	ɾ�����ð�ť
	 */
	public JButton getButton_Del() {
		return button_Del;
	}

	/**
	 * ����޸��û���ť
	 * @return	�޸��û���ť
	 */
	public JButton getButton_Mod() {
		return button_Mod;
	}

	/**
	 * ���������ѡ�û���ť
	 * @return	������ѡ�û���ť
	 */
	public JButton getButton_Sel() {
		return button_Sel;
	}

	/**
	 * ������������û���ť
	 * @return	���������û���ť
	 */
	public JButton getButton_All() {
		return button_All;
	}

//	public JButton getButton_Ref() {
//		return button_Ref;
//	}

	/**
	 * ��ò�ѯ��ť
	 * @return	��ѯ��ť
	 */
	public JButton getButton_Query() {
		return button_Query;
	}

	/**
	 * �������ID��
	 * @return	����ID��
	 */
	public JTextField getTextField_ID() {
		return textField_ID;
	}

	/**
	 * �������������
	 * @return	����������
	 */
	public JTextField getTextField_Name() {
		return textField_Name;
	}

	/**
	 * ��÷���������
	 * @return	����������
	 */
	public QQ_Server_JFrame getQQ_Server_JFrame() {
		return qq_Server_JFrame;
	}
}
