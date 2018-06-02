package qq_server_jframe;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import listener.Login_Button_Listener;
import mytools.LogsReaderWriter;

/**
 * ����һ������˵�¼���壬���̳���javax.swing.JFrame
 *
 */
public class Login_JFrame extends JFrame {
	private JLabel label_Message = new JLabel(" ");
	private JTextField textField_File = new JTextField(15);
	private JTextField textField_Port = new JTextField(15);
	private JButton button_Test = new JButton("���Զ˿�");
	private JButton button_Save = new JButton("��������");
	private JButton button_Enter = new JButton("���������");
	private File file = new File("config/config.ini");
	
	/**
	 * ����һ������˵�¼���壬���ǿɼ���
	 */
	public Login_JFrame() {
		super("��������������");
		
		launchFrame();
		makeAll();
	}

	/**
	 * ����һ����������˵�¼������������Ҫ�Ŀؼ���JPanel
	 */
	private void makeAll(){
		
		JPanel panel_Message = new JPanel(); 
		label_Message.setForeground(Color.red);
		panel_Message.add(label_Message);
		
		JPanel panel_File = new JPanel(); 
		JLabel label_File = new JLabel("��     ��");
		label_File.setPreferredSize(new Dimension(50,23));
		textField_File.setEditable(false);
		panel_File.add(label_File);
		panel_File.add(textField_File);
		
		JPanel panel_Port = new JPanel();
		JLabel label_Port = new JLabel("�˿ں�");
		label_Port.setPreferredSize(new Dimension(50,23));
		panel_Port.add(label_Port);
		panel_Port.add(textField_Port);
		
		Login_Button_Listener buttonListener = new Login_Button_Listener(this);
		button_Test.addActionListener(buttonListener);
		//button_Save.setEnabled(false);
		button_Save.addActionListener(buttonListener);
		//button_Enter.setEnabled(false);
		button_Enter.addActionListener(buttonListener);
		
		JPanel panel_Button = new JPanel();
		panel_Button.add(button_Test);
		panel_Button.add(button_Save);
		panel_Button.add(button_Enter);
		
		Box box = Box.createVerticalBox();
		box.add(Box.createRigidArea(new Dimension(1,5)));
		box.add(panel_Message);
		box.add(Box.createRigidArea(new Dimension(1,5)));
		box.add(panel_File);
		box.add(Box.createRigidArea(new Dimension(1,5)));
		box.add(panel_Port);
		box.add(Box.createRigidArea(new Dimension(1,5)));
		box.add(panel_Button);
		
		this.add(box);
		this.pack();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
	 * ��������ļ������ڽ���������ʼ���ļ���ַ
	 */
	private void launchFrame(){
		//��֤��Ӧ���ļ�������
		LogsReaderWriter.createNewFile(file.getPath());
		//��þ���·����ʾ���ı�����
		String absolutePath = file.getAbsolutePath();
		textField_File.setText(absolutePath);
		
		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
			String port = properties.getProperty("port");
			textField_Port.setText(port);
		} catch (FileNotFoundException e) {
			//textField_Port.setText("");
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}finally{
			try {
				fileInputStream.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * ����˿ڵ������ļ���
	 * @param properties
	 */
	public void saveConfig (Properties properties) {
		//��֤��Ӧ���ļ�������
		LogsReaderWriter.createNewFile(file.getPath());
		
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file,false);
			properties.store(fileOutputStream, "comments");
		} catch (FileNotFoundException e1) {
			//e1.printStackTrace();
		} catch (IOException e1) {
			//e1.printStackTrace();
		}finally{
			try {
				fileOutputStream.close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
		}
	}

	/**
	 * ��ö˿������
	 * @return	�˿������
	 */
	public JTextField getTextField_Port() {
		return textField_Port;
	}

	/**
	 * ��ò��԰�ť
	 * @return	���԰�ť
	 */
	public JButton getButton_Test() {
		return button_Test;
	}

	/**
	 * ��ñ��水ť
	 * @return	���水ť
	 */
	public JButton getButton_Save() {
		return button_Save;
	}

	/**
	 * ��ý����������ť
	 * @return	�����������ť
	 */
	public JButton getButton_Enter() {
		return button_Enter;
	}

	/**
	 * �����Ϣ��ǩ
	 * @return	��Ϣ��ǩ
	 */
	public JLabel getLabel_Message() {
		return label_Message;
	}

}
