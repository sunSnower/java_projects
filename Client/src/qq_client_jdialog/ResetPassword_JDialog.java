package qq_client_jdialog;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import qq_client_jframe.QQ_Chat_JFrame;

import listener.ResetPassword_Button_Listener;

/**
 * ����һ���޸�����ĶԻ������̳���javax.swing.JDialog
 *
 */
public class ResetPassword_JDialog extends JDialog {
	private JLabel label_Message = new JLabel(" ");
	private JPasswordField passwordField_Old = new JPasswordField();
	private JPasswordField passwordField_New = new JPasswordField();
	private JPasswordField passwordField_NewAgn = new JPasswordField();
	private JButton button_Confirm = new JButton("ȷ��");
	private JButton button_Cancle = new JButton("ȡ��");
	
	private QQ_Chat_JFrame qq_Chat_JFrame = null;
	
	/**
	 * ����һ������ָ��ӵ���ߵ��޸�����Ի���
	 * @param qq_Chat_JFrame	ӵ����
	 */
	public ResetPassword_JDialog(QQ_Chat_JFrame qq_Chat_JFrame) {
		super(qq_Chat_JFrame,true);
		this.setTitle("�޸�����");
		this.qq_Chat_JFrame = qq_Chat_JFrame;
		this.qq_Chat_JFrame.setResetPassword_JDialog(this);//�����촰��ӵ�д��޸�����Ի��������,���������ڶ�ȡ�߳��н��йرղ���
		this.makeAll();
		
	}
	
	/**
	 * ����һ���ɼ����޸�����Ի���
	 */
	private void makeAll() {

		JPanel panel_Message = new JPanel();
		label_Message.setForeground(Color.red);
		panel_Message.add(label_Message);

		JPanel panel_Old = new JPanel();
		JLabel label_Old = new JLabel("ԭ      ��      ��");
		label_Old.setPreferredSize(new Dimension(80, 23));
		passwordField_Old.setPreferredSize(new Dimension(175,23));
		passwordField_Old.setEchoChar('��');
		panel_Old.add(label_Old);
		panel_Old.add(passwordField_Old);

		JPanel panel_New = new JPanel();
		JLabel label_New = new JLabel("��      ��      ��");
		label_New.setPreferredSize(new Dimension(80, 23));
		passwordField_New.setPreferredSize(new Dimension(175,23));
		passwordField_New.setEchoChar('��');
		panel_New.add(label_New);
		panel_New.add(passwordField_New);

		JPanel panel_NewAgn = new JPanel();
		JLabel label_NewAgn = new JLabel("ȷ �� �� �� ��");
		label_NewAgn.setPreferredSize(new Dimension(80, 23));
		passwordField_NewAgn.setPreferredSize(new Dimension(175,23));
		passwordField_NewAgn.setEchoChar('��');
		panel_NewAgn.add(label_NewAgn);
		panel_NewAgn.add(passwordField_NewAgn);

		ResetPassword_Button_Listener buttonListener = new ResetPassword_Button_Listener(this,qq_Chat_JFrame);
		button_Confirm.addActionListener(buttonListener);
		button_Cancle.addActionListener(buttonListener);

		JPanel panel_Button = new JPanel();
		panel_Button.add(button_Confirm);
		panel_Button.add(button_Cancle);
		
		
		Box box = Box.createVerticalBox();
		box.add(Box.createRigidArea(new Dimension(1, 1)));
		box.add(panel_Message);
		box.add(Box.createRigidArea(new Dimension(1, 1)));
		box.add(panel_Old);
		box.add(Box.createRigidArea(new Dimension(1, 1)));
		box.add(panel_New);
		box.add(Box.createRigidArea(new Dimension(1, 1)));
		box.add(panel_NewAgn);
		box.add(Box.createRigidArea(new Dimension(1, 1)));
		box.add(panel_Button);

		this.add(box);
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(qq_Chat_JFrame);
		this.setSize(new Dimension(315,225));
		this.setVisible(true);
	}

	/**
	 * �����Ϣ��ǩ
	 * @return		��Ϣ��ǩ
	 */
	public JLabel getLabel_Message() {
		return label_Message;
	}

	/**
	 * ���ȷ�ϰ�ť
	 * @return		ȷ�ϰ�ť
	 */
	public JButton getButton_Confirm() {
		return button_Confirm;
	}

	/**
	 * ���ȡ����ť
	 * @return		ȡ����ť
	 */
	public JButton getButton_Cancle() {
		return button_Cancle;
	}

	/**
	 * ��þ����������
	 * @return		�����������
	 */
	public JPasswordField getPasswordField_Old() {
		return passwordField_Old;
	}

	/**
	 * ������������
	 * @return		�����������
	 */
	public JPasswordField getPasswordField_New() {
		return passwordField_New;
	}

	/**
	 * ���ȷ�������������
	 * @return		ȷ�������������
	 */
	public JPasswordField getPasswordField_NewAgn() {
		return passwordField_NewAgn;
	}
}
