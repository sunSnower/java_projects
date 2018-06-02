package listener;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cn.netjava.client.ClientStart;
import mytools.MyDate;

import pub.PackType;
import pub.QQPackage;

import qq_client_jdialog.ResetPassword_JDialog;
import qq_client_jframe.QQ_Chat_JFrame;
/**
 * ����һ����ť�����࣬��ʵ����java.awt.event.ActionListener�ӿڡ�
 *
 */
public class QQ_Chat_Button_Listener implements ActionListener {
	
	/**
	 * һ��QQ_Chat_JFrame��ʵ��
	 */
	private QQ_Chat_JFrame qq_Chat_JFrame = null;

	/**
	 * ����һ�����������÷���Ҫ����һ��QQ_Chat_JFrame��ʵ��
	 * @param qq_Chat_JFrame ʹ���������Զ�qq_Chat_JFrame�е�������в���
	 */
	public QQ_Chat_Button_Listener(QQ_Chat_JFrame qq_Chat_JFrame) {
		this.qq_Chat_JFrame = qq_Chat_JFrame;
		// ���ڹرռ���
		this.qq_Chat_JFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	/**
	 * ʵ�ֽӿ��еķ��������ݸ�����ť������ͬ����Ӧ��
	 */
	public void actionPerformed(ActionEvent e) {
		// �����¼
		if (e.getSource() == qq_Chat_JFrame.getButton_ChatLogs()) {
			if (qq_Chat_JFrame.getPanel_ChatLogs().isVisible() == true) {
				qq_Chat_JFrame.getPanel_ChatLogs().setVisible(false);
				qq_Chat_JFrame.getButton_ChatLogs().setText("�����¼��");
				
				//�����ǰΪ���״̬,�����¼����ڲ����Ӻ���Ȼ�����״��ʾ
				if(JFrame.MAXIMIZED_BOTH == qq_Chat_JFrame.getExtendedState()){
					qq_Chat_JFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				//������ݵ�ǰ��С�������С:��Ȳ���,�߶�-130
				}else{
					int width = (int)qq_Chat_JFrame.getSize().getWidth();
					int height = (int)qq_Chat_JFrame.getSize().getHeight();
					qq_Chat_JFrame.setSize(new Dimension(width, height - 130));
				}
				qq_Chat_JFrame.validate();
				
			} else {
				qq_Chat_JFrame.getPanel_ChatLogs().setVisible(true);
				qq_Chat_JFrame.getButton_ChatLogs().setText("�����¼��"); 
				
				//�����ǰΪ���״̬,�����¼����ڲ����Ӻ���Ȼ�����״��ʾ
				if(JFrame.MAXIMIZED_BOTH == qq_Chat_JFrame.getExtendedState()){
					qq_Chat_JFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					//������ݵ�ǰ��С�������С:��Ȳ���,�߶�+130
				}else{
					int width = (int)qq_Chat_JFrame.getSize().getWidth();
					int height = (int)qq_Chat_JFrame.getSize().getHeight();
					qq_Chat_JFrame.setSize(new Dimension(width, height + 130));
				}
				qq_Chat_JFrame.validate();
			}
		// ����
		}
		
		
		else if (e.getSource() == qq_Chat_JFrame.getButton_Send()) {
			QQPackage qqPackage_Chat = new QQPackage();
			Object select = qq_Chat_JFrame.getList_OnlineUsers()
					.getSelectedValue();
			// û��ѡ���û�
			if (select == null) {
				JOptionPane.showMessageDialog(qq_Chat_JFrame, "����δѡ����Ϣ������!");
				return;
			}
			String to = select.toString();
			int index = to.indexOf("(");
			String toID = to.substring(index + 1, to.length() - 1);
			String from = qq_Chat_JFrame.getName_ID();
			String input = qq_Chat_JFrame.getTextArea_Input().getText();
			// ���Լ�������Ϣ
			if (from.equals(to)) {
				JOptionPane.showMessageDialog(qq_Chat_JFrame, "�޷����Լ�������Ϣ!");
				return;

			// �������˻������û�������Ϣ
			} else {
				// ����Ϣ
				if (input == null || "".equals(input)) {
					JOptionPane.showMessageDialog(qq_Chat_JFrame, "��Ϣ����Ϊ��!");
					return;
				// ����2000
				} else if (input.length() > 2000) {
					JOptionPane.showMessageDialog(qq_Chat_JFrame,"��Ϣ���Ȳ��ܳ���2000����!");
					return;
				}
				// �Ϸ���Ϣ--������
				if ("������".equals(to)) {
					String time = MyDate.dateFormat(MyDate.FORMAT_HMS);
					String self = "��" + " " + time + " ��" + to + "˵:" + "\n" + "    " + input + "\n";
					qqPackage_Chat.setPackType(PackType.publicChat);
					qqPackage_Chat.setFrom(from);
					qq_Chat_JFrame.getTextArea_Dsp().append(self);
					qq_Chat_JFrame.getTextArea_ChatLogs().append(self);

				// �Ϸ���Ϣ--˽��
				} else {
					qqPackage_Chat.setPackType(PackType.privateChat);
					qqPackage_Chat.setFrom(from);
					qqPackage_Chat.setTo(toID);
					String time = MyDate.dateFormat(MyDate.FORMAT_HMS);
					String self = "��" + " " + time + " ��" + to + "˵:" + "\n" + "    " + input + "\n";
					qq_Chat_JFrame.getTextArea_Dsp().append(self);
					qq_Chat_JFrame.getTextArea_ChatLogs().append(self);
				}

			}

			ObjectOutputStream objectOutputStream = null;
			try {
				objectOutputStream = qq_Chat_JFrame.getObjectOutputStream();
				qqPackage_Chat.setData(input);
				objectOutputStream.writeObject(qqPackage_Chat);
				objectOutputStream.flush();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			qq_Chat_JFrame.getTextArea_Input().setText("");
		// �ر�
		} else if (e.getSource() == qq_Chat_JFrame.getButton_Close()) {

			close();
		// �޸�����
		}else if (e.getSource() == qq_Chat_JFrame.getButton_ResPsw()){
			new ResetPassword_JDialog(qq_Chat_JFrame);
			
		} 
	}
	/**
	 * �÷��������˴��ڹر�ʱӦ��������Ӧ
	 *
	 */
	private void close() {
		int result = JOptionPane.showConfirmDialog(qq_Chat_JFrame, "ȷ��Ҫ�˳���", "�˳�ȷ��", JOptionPane.YES_NO_OPTION);
		if (result != 0){
			return;
		}
		QQPackage qqPackage_offLine = new QQPackage();
		qqPackage_offLine.setPackType(PackType.userOffline);
		qqPackage_offLine.setFrom(qq_Chat_JFrame.getID());
		qqPackage_offLine.setData(qq_Chat_JFrame.getName_ID());

		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = qq_Chat_JFrame.getObjectOutputStream();
			objectOutputStream.writeObject(qqPackage_offLine);
			objectOutputStream.flush();
		} catch (IOException e1) {
			//e1.printStackTrace();
		}

		System.exit(0);
	}
}