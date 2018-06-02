package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JLabel;

import pub.PackType;
import pub.QQPackage;

import mytools.MyCheck;

import qq_client_jdialog.ResetPassword_JDialog;
import qq_client_jframe.QQ_Chat_JFrame;
/**
 * ����һ����ť�¼��ļ������࣬��ʵ����java.awt.event.ActionListener�ӿ�
 *
 */
public class ResetPassword_Button_Listener implements ActionListener{
	
	private ResetPassword_JDialog resetPassword_JDialog = null;
	private  QQ_Chat_JFrame qq_Chat_JFrame = null;

	/**
	 * ����һ����ť�¼��ļ�����
	 * @param resetPassword_JDialog		ʹ���������Զ�resetPassword_JDialog�е�������в���
	 * @param qq_Chat_JFrame	ʹ���������Զ�qq_Chat_JFrame�е�������в���
	 */
	public ResetPassword_Button_Listener(ResetPassword_JDialog resetPassword_JDialog, QQ_Chat_JFrame qq_Chat_JFrame) {
		this.resetPassword_JDialog = resetPassword_JDialog;
		this.qq_Chat_JFrame = qq_Chat_JFrame;
	}

	/**
	 * �����˸�����ť����ʱ������Ӧ
	 */
	public void actionPerformed(ActionEvent e) {
		
		//ȷ��
		if(e.getSource() == resetPassword_JDialog.getButton_Confirm()){
			JLabel message = resetPassword_JDialog.getLabel_Message();
			char[] oldChar = resetPassword_JDialog.getPasswordField_Old().getPassword();
			String oldPsw = String.valueOf(oldChar);
			char[] newChar = resetPassword_JDialog.getPasswordField_New().getPassword();
			String newPsw = String.valueOf(newChar);
			char[] newAgnChar = resetPassword_JDialog.getPasswordField_NewAgn().getPassword();
			String newPswAgn = String.valueOf(newAgnChar);
			//δ����ԭ����
			if(oldPsw == null){
				message.setText("������ԭ����!");
				return;
			}
			//δ����������
			if(oldPsw == null){
				message.setText("������������!");
				return;
			}
			//δ����ȷ��������
			if(oldPsw == null){
				message.setText("��ȷ��������!");
				return;
			}
			//�ų��Ƿ�������
			if (!MyCheck.checkPassword(newPsw)){
				message.setText("���볤�ȱ�����3~16λ֮��(ֻ����������,��ĸ�� _)");
				return;
			}
			//�ų��Ƿ�ȷ��������
			if (!MyCheck.checkPassword(newPswAgn)){
				message.setText("���볤�ȱ�����3~16λ֮��(ֻ����������,��ĸ�� _)");
				return;
			}
			//�ų����������벻һ��
			if (!newPsw.equals(newPswAgn)){
				message.setText("����������������벻һ��!");
				return;
			}
			//��װ�޸������
			Vector<String> psw = new Vector<String>();
			psw.add(oldPsw);
			psw.add(newPsw);
			QQPackage qqPackageResPsw = new QQPackage();
			qqPackageResPsw.setPackType(PackType.resetPassword);
			qqPackageResPsw.setFrom(qq_Chat_JFrame.getID());
			qqPackageResPsw.setData(psw);
			//����
			ObjectOutputStream objectOutputStream = qq_Chat_JFrame.getObjectOutputStream();
			try {
				objectOutputStream.writeObject(qqPackageResPsw);
				objectOutputStream.flush();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			
		//ȡ��
		}else if(e.getSource() == resetPassword_JDialog.getButton_Cancle()){
			resetPassword_JDialog.dispose();
		}
		
	}

}
