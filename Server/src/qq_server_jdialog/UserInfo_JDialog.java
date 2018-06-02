package qq_server_jdialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import listener.UserInfo_Button_Listener;

import qq_server_jframe.QQ_Server_JFrame;
import qq_server_jframe.UsersManager_JPanel;

import dao.bean.User;
import dao.impl.UserDaoImpl;

/**
 * ����һ������û����޸��û��Ի������̳���javax.swing.JDialog
 *
 */
public class UserInfo_JDialog extends JDialog {
	private JLabel label_Message = new JLabel();
	private JTextField textField_ID = new JTextField(15);
	private JPasswordField passwordField_Password = new JPasswordField(15);
	private JPasswordField passwordField_PasswordAgn = new JPasswordField(15);
	private JTextField textField_Name = new JTextField(15);
	private JRadioButton radioButton_Male = new JRadioButton("��        ", true);
	private JRadioButton radioButton_Female = new JRadioButton("Ů");
	private JTextField textField_Age = new JTextField(15);
	private JTextField textField_Address = new JTextField(15);
	private JComboBox comboBox_IsOnline = new JComboBox();
	private JTextField textField_RegTime = new JTextField(15);
	private JButton button_Save = new JButton("����");
	private JButton button_Cancel = new JButton("ȡ��");
	private QQ_Server_JFrame qq_Server_JFrame;
	private UsersManager_JPanel usersManager_JPanel;
	
	/**
	 * ����û�
	 */
	public final static int USER_INFO_ADD = 1;
	
	/**
	 * �޸��û�
	 */
	public final static int USER_INFO_MOD = 2;

	/**
	 * ����һ���û���Ϣ�ĶԻ���
	 * @param usersManager_JPanel		�û��������
	 * @param qq_Server_JFrame		����˴���
	 * @param kind		ѡ�񴴽��ĶԻ�������࣬������û����޸��û����֡�
	 */
	public UserInfo_JDialog(UsersManager_JPanel usersManager_JPanel,
			QQ_Server_JFrame qq_Server_JFrame, int kind) {
		super(qq_Server_JFrame);
		//ADD-----------------------------------------------------------------
		if(kind == 1){
			this.qq_Server_JFrame = qq_Server_JFrame;
			this.setTitle("����û�");
			this.add(this.makeMainPane());

			// ��ʼ�����
			String id_Next = UserDaoImpl.getInstance().getNextSid();
			this.textField_ID.setText(id_Next);
			this.textField_ID.setEditable(false);
			// ��ʼ������
			this.passwordField_Password.setText("123456");
			// this.passwordField_Password.setEditable(false);
			this.passwordField_PasswordAgn.setText("123456");
			// this.passwordField_PasswordAgn.setEditable(false);
			// ��ʼ��ע��ʱ��
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			this.textField_RegTime.setText(sdf.format(date));
			this.textField_RegTime.setEditable(false);

			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.pack();
			this.setResizable(false);
			this.setLocationRelativeTo(qq_Server_JFrame);
			this.setModal(true);
			this.setVisible(true);
		//MOD-------------------------------------------------------------------
		}else if(kind == 2){
			this.qq_Server_JFrame = qq_Server_JFrame;
			this.setTitle("�޸�����");
			this.add(this.makeMainPane());

			// ��ñ��
			int row = this.qq_Server_JFrame.getUsersManager().getQQUsersInfo_JTable().getSelectedRow();
			String sid = this.qq_Server_JFrame.getUsersManager().getQQUsersInfo_JTable().getValueAt(row, 0).toString();
			this.textField_ID.setText(sid);
			this.textField_ID.setEditable(false);
			// �������
			User user = UserDaoImpl.getInstance().selectList(sid);
			this.passwordField_Password.setText(user.getSpassword());
			this.passwordField_Password.setEditable(false);
			this.passwordField_PasswordAgn.setText(user.getSpassword());
			this.passwordField_PasswordAgn.setEditable(false);
			// �������
			this.textField_Name.setText(user.getSname());
			// ����Ա�
			String sex = user.getSsex();
			if(sex.equals("��")){
				this.radioButton_Male.setSelected(true);
			}else{
				this.radioButton_Female.setSelected(true);
			}
			//�������
			this.textField_Age.setText(user.getNage());
			//��õ�ַ
			this.textField_Address.setText(user.getSaddress());
			//�Ƿ�����
			this.comboBox_IsOnline.setSelectedItem("����");
			// ��ʼ��ע��ʱ��
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			this.textField_RegTime.setText(sdf.format(date));
			this.textField_RegTime.setEditable(false);

			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.pack();
			this.setResizable(false);
			this.setLocationRelativeTo(qq_Server_JFrame);
			this.setModal(true);
			this.setVisible(true);
		}
		
	}

	/**
	 * ����һ����ŶԻ������ݵ�JPanel
	 * @return		��ŶԻ������ݵ�JPanel
	 */
	private JPanel makeMainPane() {
		JPanel panel_Message = new JPanel();
		label_Message.setForeground(Color.red);
		panel_Message.add(label_Message);

		JPanel panel_ID = new JPanel();
		panel_ID.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_ID = new JLabel("��            ��:");
		label_ID.setPreferredSize(new Dimension(80, 15));
		panel_ID.add(label_ID);
		panel_ID.add(this.textField_ID);

		JPanel panel_Password = new JPanel();
		panel_Password.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_Password = new JLabel("��            ��:");
		label_Password.setPreferredSize(new Dimension(80, 15));
		this.passwordField_Password.setEchoChar('��');
		panel_Password.add(label_Password);
		panel_Password.add(this.passwordField_Password);

		JPanel panel_PasswordAgn = new JPanel();
		panel_PasswordAgn.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_PasswordAgn = new JLabel("ȷ �� �� ��:");
		label_PasswordAgn.setPreferredSize(new Dimension(80, 15));
		this.passwordField_PasswordAgn.setEchoChar('��');
		panel_PasswordAgn.add(label_PasswordAgn);
		panel_PasswordAgn.add(this.passwordField_PasswordAgn);

		JPanel panel_Name = new JPanel();
		panel_Name.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_Name = new JLabel("��            ��:");
		label_Name.setPreferredSize(new Dimension(80, 15));
		panel_Name.add(label_Name);
		panel_Name.add(this.textField_Name);

		JPanel panel_Sex = new JPanel();
		panel_Sex.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_Sex = new JLabel("��            ��:");
		label_Sex.setPreferredSize(new Dimension(80, 15));

		ButtonGroup bg5 = new ButtonGroup();
		bg5.add(radioButton_Male);
		bg5.add(radioButton_Female);
		panel_Sex.add(label_Sex);
		panel_Sex.add(radioButton_Male);
		panel_Sex.add(radioButton_Female);

		JPanel panel_Age = new JPanel();
		panel_Age.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_Age = new JLabel("��            ��:");
		label_Age.setPreferredSize(new Dimension(80, 15));

		panel_Age.add(label_Age);
		panel_Age.add(textField_Age);

		JPanel panel_Address = new JPanel();
		panel_Address.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_Address = new JLabel("ס            ַ:");
		label_Address.setPreferredSize(new Dimension(80, 15));
		panel_Address.add(label_Address);
		panel_Address.add(textField_Address);

		JPanel panel_IsOnline = new JPanel();
		panel_IsOnline.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_IsOnline = new JLabel("�� �� �� ��:");
		label_IsOnline.setPreferredSize(new Dimension(80, 15));
		comboBox_IsOnline.setPreferredSize(new Dimension(60, 22));
		comboBox_IsOnline.addItem("����");
		comboBox_IsOnline.addItem("����");
		comboBox_IsOnline.setSelectedItem("����");
		comboBox_IsOnline.setEnabled(false);
		panel_IsOnline.add(label_IsOnline);
		panel_IsOnline.add(comboBox_IsOnline);

		JPanel panel_RegTime = new JPanel();
		panel_RegTime.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label_RegTime = new JLabel("ע �� ʱ ��:");
		label_RegTime.setPreferredSize(new Dimension(80, 15));

		panel_RegTime.add(label_RegTime);
		panel_RegTime.add(textField_RegTime);

		JPanel panel_SaveCancel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		UserInfo_Button_Listener button_Listener = new UserInfo_Button_Listener(
				this, qq_Server_JFrame);
		button_Save.addActionListener(button_Listener);
		button_Cancel.addActionListener(button_Listener);
		panel_SaveCancel.add(button_Save);
		panel_SaveCancel.add(button_Cancel);

		JPanel p = new JPanel(new GridLayout(11, 1, 0, 0));
		Border border = BorderFactory.createEmptyBorder(7, 7, 7, 7);
		p.setBorder(border);
		p.add(panel_Message);
		p.add(panel_ID);
		p.add(panel_Password);
		p.add(panel_PasswordAgn);
		p.add(panel_Name);
		p.add(panel_Sex);
		p.add(panel_Age);
		p.add(panel_Address);
		p.add(panel_IsOnline);
		p.add(panel_RegTime);
		p.add(panel_SaveCancel);

		return p;
	}

	/**
	 * �����Ϣ��ǩ
	 * @return	��Ϣ��ǩ
	 */
	public JLabel getLabel_Message() {
		return label_Message;
	}

	/**
	 * ���ID�����
	 * @return	ID�����
	 */
	public JTextField getTextField_ID() {
		return textField_ID;
	}

	/**
	 * ������������
	 * @return	���������
	 */
	public JPasswordField getPasswordField_Password() {
		return passwordField_Password;
	}

	/**
	 * ���ȷ�����������
	 * @return	ȷ�����������
	 */
	public JPasswordField getPasswordField_PasswordAgn() {
		return passwordField_PasswordAgn;
	}

	/**
	 * ������������
	 * @return	���������
	 */
	public JTextField getTextField_Name() {
		return textField_Name;
	}

	/**
	 * ��õ�ѡ���С�
	 * @return	��ѡ���С�
	 */
	public JRadioButton getRadioButton_Male() {
		return radioButton_Male;
	}

	/**
	 * ��õ�ѡ�򡰸���
	 * @return	��ѡ�򡰸���
	 */
	public JRadioButton getRadioButton_Female() {
		return radioButton_Female;
	}

	/**
	 * ������������
	 * @return	���������
	 */
	public JTextField getTextField_Age() {
		return textField_Age;
	}

	/**
	 * ��õ�ַ�����
	 * @return	��ַ�����
	 */
	public JTextField getTextField_Address() {
		return textField_Address;
	}

	/**
	 * ����Ƿ�����������
	 * @return	�Ƿ�����������
	 */
	public JComboBox getComboBox_IsOnline() {
		return comboBox_IsOnline;
	}

	/**
	 * ���ע��ʱ�������
	 * @return	ע��ʱ�������
	 */
	public JTextField getTextField_RegTime() {
		return textField_RegTime;
	}

	/**
	 * ��ñ��水ť
	 * @return	���水ť
	 */
	public JButton getButton_Save() {
		return button_Save;
	}

	/**
	 * �����ȡ��ť
	 * @return	��ȡ��ť
	 */
	public JButton getButton_Cancel() {
		return button_Cancel;
	}

	/**
	 * ��÷�����������
	 * @return	������������
	 */
	public QQ_Server_JFrame getQQ_Server_JFrame() {
		return qq_Server_JFrame;
	}

}

