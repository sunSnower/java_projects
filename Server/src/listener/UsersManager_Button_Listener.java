package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import qq_server_jdialog.UserInfo_JDialog;
import qq_server_jframe.QQ_Server_JFrame;
import qq_server_jframe.UsersManager_JPanel;

import dao.impl.UserDaoImpl;

/**
 * ����һ���û�������尴ť�ļ���������ʵ����java.awt.event.ActionListener�ӿ�
 *
 */
public class UsersManager_Button_Listener implements ActionListener {
	private UsersManager_JPanel usersManager_JPanel;
	private QQ_Server_JFrame qq_Server_JFrame;

	public UsersManager_Button_Listener(
			UsersManager_JPanel usersManager_JPanel,
			QQ_Server_JFrame qq_Server_JFrame) {
		this.usersManager_JPanel = usersManager_JPanel;
		this.qq_Server_JFrame = qq_Server_JFrame;
	}

	/**
	 * ������һЩ��ť����ʱ��������Ӧ
	 */
	public void actionPerformed(ActionEvent e) {

		// ����û�
		if (usersManager_JPanel.getButton_Add() == e.getSource()) {
			new UserInfo_JDialog(usersManager_JPanel, qq_Server_JFrame,
					UserInfo_JDialog.USER_INFO_ADD);
		// ���������û�����
		} else if (usersManager_JPanel.getButton_All() == e.getSource()) {
			if(qq_Server_JFrame.getServerread_Thread_Map().size() != 0){
				JOptionPane.showMessageDialog(qq_Server_JFrame, "ϵͳ��ǰ���û�����,����ǿ�����������û����ߺ�����!");
				return;
			}
			int result = JOptionPane.showConfirmDialog(usersManager_JPanel,
					"ȷ��Ҫ���������û�������", "���������û�����", JOptionPane.YES_NO_OPTION);
			if (result == 0) {
				UserDaoImpl.getInstance().resetAllPWD();
				usersManager_JPanel.getQQUsersInfo_JTable().clearSelection();
				usersManager_JPanel.getButton_Del().setEnabled(false);
				usersManager_JPanel.getButton_Mod().setEnabled(false);
				usersManager_JPanel.getButton_Sel().setEnabled(false);
				JOptionPane.showMessageDialog(usersManager_JPanel, "�������óɹ���");
			} else {
				return;
			}
		// ��ѯ
		} else if (usersManager_JPanel.getButton_Query() == e.getSource()) {
			String str_ID = usersManager_JPanel.getTextField_ID().getText()
					.replaceAll(" ", "");
			String str_Name = usersManager_JPanel.getTextField_Name().getText()
					.replaceAll(" ", "");
			Vector<Vector<String>> data = UserDaoImpl.getInstance().selectList(str_ID,
					str_Name, UserDaoImpl.UP_AND_DOWN);
			usersManager_JPanel.updateQQUsersInfo_JTable(data);
			usersManager_JPanel.getTextField_ID().setText(str_ID);
			usersManager_JPanel.getTextField_Name().setText(str_Name);

		} else {
			// �Ȼ���û�������״̬
			int row = usersManager_JPanel.getQQUsersInfo_JTable()
					.getSelectedRow();
			String sidSelect = usersManager_JPanel.getQQUsersInfo_JTable()
					.getValueAt(row, 0).toString();
			String name = UserDaoImpl.getInstance().selectList(sidSelect)
					.getSname();
			String message = "�û�" + name + "(" + sidSelect + ")"
					+ "�ѵ�¼,��ǿ�������ߺ�����!";
			String isOnline = UserDaoImpl.getInstance().selectList(sidSelect)
					.getNisonlin();

			// �û�����ʱ ��ʾ�䲻���޸�
			if (isOnline.equals("����")) {
				JOptionPane.showMessageDialog(usersManager_JPanel, message);
				return;
			}
			// ɾ���û�
			if (usersManager_JPanel.getButton_Del() == e.getSource()) {
				int result = JOptionPane.showConfirmDialog(usersManager_JPanel,
						"ȷ��Ҫɾ���û�" + sidSelect + "��", "ɾ���û�",
						JOptionPane.YES_NO_OPTION);
				if (result == 0) {
					UserDaoImpl.getInstance().deleteUser(sidSelect);
					usersManager_JPanel.updateQQUsersInfo_JTable();
					JOptionPane.showMessageDialog(usersManager_JPanel, "�û�" + sidSelect + "��ɾ����");
					usersManager_JPanel.getQQUsersInfo_JTable().clearSelection();
				} else {
					return;
				}
			// �޸��û�
			} else if (usersManager_JPanel.getButton_Mod() == e.getSource()) {
				new UserInfo_JDialog(usersManager_JPanel, qq_Server_JFrame,
						UserInfo_JDialog.USER_INFO_MOD);
			// ������ѡ�û�����
			} else if (usersManager_JPanel.getButton_Sel() == e.getSource()) {
				int result = JOptionPane.showConfirmDialog(usersManager_JPanel,"ȷ��Ҫ�����û�" + sidSelect + "��������", "������ѡ�û�����",JOptionPane.YES_NO_OPTION);
				if (result == 0) {
					UserDaoImpl.getInstance().resetPWD(sidSelect);
					usersManager_JPanel.getQQUsersInfo_JTable().clearSelection();
					JOptionPane.showMessageDialog(usersManager_JPanel,"�������óɹ���");
				} else {
					return;
				}

			}

		}

	}
}
