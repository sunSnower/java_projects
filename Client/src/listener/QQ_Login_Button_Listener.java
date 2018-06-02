package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JOptionPane;

import pub.PackType;
import pub.QQPackage;

import qq_client_jframe.QQ_Chat_JFrame;
import qq_client_jframe.QQ_Login_JFrame;
/**
 * ����һ����ť�¼��ļ������࣬��ʵ����java.awt.event.ActionListener�ӿ�
 *
 */
public class QQ_Login_Button_Listener implements ActionListener {
	private QQ_Login_JFrame qq_Login_JFrame;
	
	private Socket socket = null;
	
	/**
	 * ����ͳ��ʧ�ܵĴ���
	 */
	private int wrongCount = 0;

	/**
	 * ����һ����ť�¼��ļ�������
	 * @param qq_Login_JFrame	ʹ���������Զ�qq_Chat_JFrame�е�������в���
	 */
	public QQ_Login_Button_Listener(QQ_Login_JFrame qq_Login_JFrame) {
		this.qq_Login_JFrame = qq_Login_JFrame;
	}

	/**
	 * �����˸�����ť����ʱ��������Ӧ
	 */
	public void actionPerformed(ActionEvent e) {
		//���ð�ť
		if (qq_Login_JFrame.getButton_NetOption() == e.getSource()) {
			if (qq_Login_JFrame.getPanel_NetOption().isVisible() == true) {
				qq_Login_JFrame.getPanel_NetOption().setVisible(false);
				// qq_Login.setSize(new Dimension(340,300));
				qq_Login_JFrame.pack();
			} else {
				qq_Login_JFrame.getPanel_NetOption().setVisible(true);
				// qq_Login.setSize(new Dimension(340,300));
				qq_Login_JFrame.pack();
			}
		//��¼��ť
		} else if (qq_Login_JFrame.getButton_Login() == e.getSource()) {
			//�����ų��Ƿ�IP
			Object objectIP = qq_Login_JFrame.getComboBox_IP().getSelectedItem();
			if(objectIP == null){
				qq_Login_JFrame.getLabel_NetOption().setText("IP���Ϸ������������룡");
				return;
			}
			String IP = objectIP.toString();
			IP = IP.replaceAll(" ", "");
			if (!IP.matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$")) {
				qq_Login_JFrame.getLabel_NetOption().setText("IP���Ϸ������������룡");
				return;
			}
			//�����ų��Ƿ��˿�
			String port = qq_Login_JFrame.getTextField_Port().getText();
			port = port.replaceAll(" ", "");
			if(port == null){
				qq_Login_JFrame.getLabel_NetOption().setText("������˿ں�");
				return;
			}
			if(!port.matches("\\d{4,5}")){
				qq_Login_JFrame.getLabel_NetOption().setText("�˿ں�ӦΪ1024~65535֮�������");
				return;
			}
			int p = Integer.parseInt(port);
			if(p<1024 && p>65535){
				qq_Login_JFrame.getLabel_NetOption().setText("�˿ں�ӦΪ1024~65535֮�������");
				return;
			}
			//�����ų��Ƿ�QQ����
			Object objectID = qq_Login_JFrame.getComboBox_ID().getSelectedItem();
			if(objectID == null){
				JOptionPane.showMessageDialog(qq_Login_JFrame, "������QQ����!");
				return;
			}
			String ID = objectID.toString();
			ID = ID.replaceAll(" ", "");
			//�����ų��Ƿ�����
			char[] pswArr = qq_Login_JFrame.getPasswordField_Password().getPassword();
			String psw = String.valueOf(pswArr);
			if(psw == null){
				JOptionPane.showMessageDialog(qq_Login_JFrame, "����������!");
				return;
			}
			//4��Ϸ���
			try {
				socket = new Socket(IP, p);
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(qq_Login_JFrame, "�Ҳ���������!�����������ϵϵͳ����Ա!");
				return;
			} catch (IOException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(qq_Login_JFrame, "����������ʧ��!�����������ϵϵͳ����Ա!");
				return;
			}
			
			try {
				InputStream inputStream = socket.getInputStream();
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				//��װ��¼�����
				Vector<String> IDAndPsw = new Vector<String>();
				IDAndPsw.add(ID);
				IDAndPsw.add(psw);
				QQPackage qqPackageCheck = new QQPackage();
				qqPackageCheck.setPackType(PackType.loginApply);
				qqPackageCheck.setData(IDAndPsw);
				objectOutputStream.writeObject(qqPackageCheck);
				objectOutputStream.flush();

				//��ȡ������������Ϣ
				QQPackage qqPackageReturn = null;
				qqPackageReturn = (QQPackage)objectInputStream.readObject();
				PackType packType = qqPackageReturn.getPackType();
				String returnInfo = qqPackageReturn.getData().toString();
				//����,3�δ�����˳�����
				if(++wrongCount == 3){
					qq_Login_JFrame.dispose();
					System.exit(0);
				}
				//��¼ʧ��
				if(packType == PackType.loginFail){
					JOptionPane.showMessageDialog(qq_Login_JFrame, returnInfo + "�ۼ����ε�¼ʧ�ܺ󣬳����˳����������Բ�����" + (3-wrongCount) + "��");
				//��¼�ɹ�
				}else if(packType == PackType.loginSuccess){
					// �ѵ�������ĺ������ӵ��ļ���ʵ�ּ�¼�������Ĺ���
					qq_Login_JFrame.autoAppendID();
					qq_Login_JFrame.dispose();
					new QQ_Chat_JFrame(socket, qqPackageReturn,objectOutputStream,objectInputStream);
					
				}
			} catch (IOException e1) {
				//e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				//e1.printStackTrace();
			}
		//�˳���ť
		} else if (qq_Login_JFrame.getButton_Exit() == e.getSource()) {
			System.exit(0);
		}
	}
}
