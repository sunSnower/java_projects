package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import mytools.MyCheck;

import qq_server_jdialog.UserInfo_JDialog;
import qq_server_jframe.QQ_Server_JFrame;

import dao.bean.User;
import dao.impl.UserDaoImpl;

/**
 * ����һ������û����޸��û��Ի���ļ���������ʵ����java.awt.event.ActionListener�ӿ�
 *
 */
public class UserInfo_Button_Listener implements ActionListener{
	private UserInfo_JDialog userInfo_JDialog;
	private QQ_Server_JFrame qq_Server_JFrame;
	
	public UserInfo_Button_Listener(UserInfo_JDialog userInfo_JDialog,
			QQ_Server_JFrame qq_Server_JFrame) {
		this.userInfo_JDialog = userInfo_JDialog;
		this.qq_Server_JFrame = qq_Server_JFrame;
	}
	
	/**
	 * ������һЩ��ť����ʱ��������Ӧ
	 */
	public void actionPerformed(ActionEvent e) {
		if (this.userInfo_JDialog.getButton_Save() == e.getSource()) {
			// �ӶԻ����л�ø�����Ϣ
			String str_ID = this.userInfo_JDialog.getTextField_ID().getText();
			char[] ch_Password = this.userInfo_JDialog.getPasswordField_Password().getPassword();
			String str_Password = String.valueOf(ch_Password);
			char[] ch_PasswordAgn = this.userInfo_JDialog.getPasswordField_PasswordAgn().getPassword();
			String str_PasswordAgn = String.valueOf(ch_PasswordAgn);
			String str_Name = this.userInfo_JDialog.getTextField_Name().getText();
			String str_Sex;
			if (this.userInfo_JDialog.getRadioButton_Male().isSelected()) {
				str_Sex = "��";
			} else {
				str_Sex = "Ů";
			}
			String str_Age = this.userInfo_JDialog.getTextField_Age().getText();
			String str_Address = this.userInfo_JDialog.getTextField_Address().getText();
			String str_IsOnline = this.userInfo_JDialog.getComboBox_IsOnline().getSelectedItem().toString();
			String str_RegTime = this.userInfo_JDialog.getTextField_RegTime().getText();
			// �ж�ע����Ϣ�Ƿ����Ҫ�� ,Ϊtrueʱ����Ҫ��
			boolean flag_Password, flag_Name, flag_Age, flag_Address;
			if (str_Password.matches("[a-z|A-Z|0-9|_]{3,16}")) {
				if (str_Password.equals(str_PasswordAgn)) {
					flag_Password = true;
				} else {
					flag_Password = false;
				}
			} else {
				flag_Password = false;
			}
			if(flag_Password == false){
				this.userInfo_JDialog.getLabel_Message().setText("���볤�ȱ����� 3~16 λ֮�� (ֻ����������,��ĸ�� _)");
				return;
			}

			if (str_Name.matches("[\\u4e00-\\u9fa5]{2,10}")) {
				flag_Name = true;
			} else {
				flag_Name = false;
			}
			if(flag_Name == false){
				this.userInfo_JDialog.getLabel_Message().setText("��ʵ������������ 2~10 ����֮�� (����������)");
				return;
			}

			if (str_Age.matches("\\d{2,3}") && Integer.parseInt(str_Age) >= 0
					&& Integer.parseInt(str_Age) <= 100) {
				flag_Age = true;
			} else {
				flag_Age = false;
			}
			if(flag_Age == false){
				this.userInfo_JDialog.getLabel_Message().setText("����ֻ��Ϊ�����ұ����� 0~100 ֮�� ");
				return;
			}
			
			if (MyCheck.checkSaddress(str_Address)) {
				flag_Address = true;
			} else {
				flag_Address = false;
			}
			if(flag_Address == false){
				this.userInfo_JDialog.getLabel_Message().setText("��ַ�������ܴ��� 100 ��,�Ҳ��ܰ���\",\"");
				return;
			}

/*			// ��֤ʧ�ܣ�������ʾ
			if (flag_Password == false || flag_Name == false
					|| flag_Age == false || flag_Address == false) {
				// ������Ϣ���ݣ�false������ش�����Ϣ��true���ؿ��ַ���
				String msg_Password = (flag_Password == true) ? ""
						: "���볤�ȱ����� 3~16 λ֮�� (ֻ���������֣���ĸ �� _ )" + "\n";
				String msg_Name = (flag_Name == true) ? ""
						: "��ʵ������������ 2~10 ����֮�� (����������)" + "\n";
				String msg_Age = (flag_Age == true) ? ""
						: "����ֻ��Ϊ�����ұ����� 20~150 ֮�� " + "\n";
				String msg_Address = (flag_Address == true) ? ""
						: "��ַ�������ܴ��� 100 ��";
				JOptionPane.showMessageDialog(userInfoAdd_JDialog, "ʧ��ԭ��:"
						+ "\n" + msg_Password + msg_Name + msg_Age
						+ msg_Address, "ע��ʧ��", JOptionPane.ERROR_MESSAGE);
				// ��֤�ɹ������û���ӵ����ݿ�
			} else {*/
				User user = new User();
				user.setSid(str_ID);
				user.setSpassword(str_Password);
				user.setSname(str_Name);
				user.setSsex(str_Sex);
				user.setNage(str_Age);
				user.setSaddress(str_Address);
				user.setNisonlin("����");
				user.setDregtime(str_RegTime);
				if(this.userInfo_JDialog.getTitle().equals("����û�")){
					UserDaoImpl.getInstance().insertUser(user);
					JOptionPane.showMessageDialog(userInfo_JDialog, "ע��ɹ���","ע��ɹ�", JOptionPane.INFORMATION_MESSAGE);
				}else{
					UserDaoImpl.getInstance().updateUser(user);
					JOptionPane.showMessageDialog(userInfo_JDialog, "�޸ĳɹ���","�޸ĳɹ�", JOptionPane.INFORMATION_MESSAGE);
				}
				
				this.qq_Server_JFrame.getUsersManager().updateQQUsersInfo_JTable();
				this.userInfo_JDialog.dispose();
//			}

		} else if (this.userInfo_JDialog.getButton_Cancel() == e.getSource()) {
			this.userInfo_JDialog.dispose();
		}
		
	}

}
