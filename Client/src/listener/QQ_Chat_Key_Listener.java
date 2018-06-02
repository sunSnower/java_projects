package listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import mytools.MyDate;
import pub.PackType;
import pub.QQPackage;

import qq_client_jframe.QQ_Chat_JFrame;
/**
 * ����һ�����̼������࣬���̳���KeyAdapter
 *
 */
public class QQ_Chat_Key_Listener extends KeyAdapter {
	
	/**
	 * һ��QQ_Chat_JFrame��ʵ��
	 */
	private QQ_Chat_JFrame qq_Chat_JFrame = null;
	
	/**
	 * ����һ�����̼�����
	 * @param qq_Chat_JFrame	ʹ���������Զ�qq_Chat_JFrame�е�������в���
	 */
	public QQ_Chat_Key_Listener(QQ_Chat_JFrame qq_Chat_JFrame) {
		this.qq_Chat_JFrame = qq_Chat_JFrame;
	}

	@Override
	/**
	 * �����˰���ͬʱ����ctrl����enter��ʱ��������Ӧ����������Ϣ
	 */
	public void keyTyped(KeyEvent e) {
		// ����
		if (e.isControlDown() && e.getKeyChar() == KeyEvent.VK_ENTER) {

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
			String from = qq_Chat_JFrame.getTitle();
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
					JOptionPane.showMessageDialog(qq_Chat_JFrame,
							"��Ϣ���Ȳ��ܳ���2000����!");
					return;
				}
				// �Ϸ���Ϣ--������
				if ("������".equals(to)) {
					String time = MyDate.dateFormat(MyDate.FORMAT_HMS);
					String self = "��" + " " + time + " ��" + to + "˵:" + "\n"
							+ "    " + input + "\n";
					qqPackage_Chat.setPackType(PackType.publicChat);
					qqPackage_Chat.setFrom(from);
					qq_Chat_JFrame.getTextArea_Dsp().append(self);
					qq_Chat_JFrame.getTextArea_ChatLogs().append(self);
					qq_Chat_JFrame.getTextArea_ChatLogs().append(self);
					qq_Chat_JFrame.getTextArea_ChatLogs().setCaretPosition(
							qq_Chat_JFrame.getTextArea_ChatLogs().getDocument()
									.getLength());

					// �Ϸ���Ϣ--˽��
				} else {
					qqPackage_Chat.setPackType(PackType.privateChat);
					qqPackage_Chat.setFrom(from);
					qqPackage_Chat.setTo(toID);
					String time = MyDate.dateFormat(MyDate.FORMAT_HMS);
					String self = "��" + " " + time + " ��" + to + "˵:" + "\n"
							+ "    " + input + "\n";
					qq_Chat_JFrame.getTextArea_Dsp().append(self);
					qq_Chat_JFrame.getTextArea_Dsp().setCaretPosition(
							qq_Chat_JFrame.getTextArea_Dsp().getDocument()
									.getLength());
					qq_Chat_JFrame.getTextArea_ChatLogs().append(self);
					qq_Chat_JFrame.getTextArea_ChatLogs().setCaretPosition(
							qq_Chat_JFrame.getTextArea_ChatLogs().getDocument()
									.getLength());
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

		}
	}
}
