package qq_client_jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import listener.QQ_Login_Button_Listener;
import mytools.LogsReaderWriter;

/**
 * ����һ��QQ��¼�����࣬���̳���javax.swing.JFrame
 *
 */
public class QQ_Login_JFrame extends JFrame{
	private JComboBox comboBox_ID = new JComboBox();
	private JPasswordField passwordField_Password = new JPasswordField("123456");
	private JPanel panel_NetOption = null;
	private JLabel label_NetOption = new JLabel();
	private JButton button_NetOption = new JButton("�� ��");
	private JButton button_Login = new JButton("�� ¼");
	private JButton button_Exit = new JButton("�� ��");
	private File file = new File("id/id.txt");
	private JComboBox comboBox_IP = new JComboBox(new String[]{"127.0.0.1"});
	private JTextField textField_Port = new JTextField("6000");
	
	/**
	 * ����һ����¼����
	 */
	public QQ_Login_JFrame() {
		super("�û���¼");	
		
		//��ʼ��QQ�����,
		launchComboBox_ID();
		
		this.panel_NetOption = this.makeNetOption();
		this.panel_NetOption.setVisible(false);
		this.makeAll();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
	 * ����һ����ʾ�ڵ�¼�����ϵ���ͼƬ��JLable
	 * @return		��ʾ�ڵ�¼�����ϵ���ͼƬ��JLable
	 */
	private JLabel makeLoginImage(){
		Icon i = new ImageIcon("image/QQ_Login.gif");
		JLabel l = new JLabel(i);
		return l;
	}
	
	/**
	 * ����һ����¼�����JPanel
	 * @return		һ����¼�����JPanel
	 */
	private JPanel makeLoginArea(){
		JPanel p = new JPanel();
		BoxLayout bl = new BoxLayout(p,BoxLayout.PAGE_AXIS);
		p.setLayout(bl);
		//p.setBackground(Color.getHSBColor(54, 0.2f, 0.2f));//��ɫ
		Border border1 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border border2 = BorderFactory.createLineBorder(Color.blue);
		Border border3 = BorderFactory.createTitledBorder(border2, "QQ��¼");
		Border bordercom = BorderFactory.createCompoundBorder(border1, border3);
		p.setBorder(bordercom);
		
//		p.setBorder(border1);
		//p.setBorder(border2);
//		p.setBorder(border3);
		
		JPanel panel_ID = new JPanel();
		JLabel label_ID = new JLabel("QQ����:");
		//l1.setPreferredSize(new Dimension(50,23));
		panel_ID.add(label_ID);
		
		comboBox_ID.setPreferredSize(new Dimension(175,23));
		comboBox_ID.setEditable(true);
		panel_ID.add(comboBox_ID);
		
		JPanel panel_Password = new JPanel();
		JLabel label_Password = new JLabel("QQ����:");
		//l2.setPreferredSize(new Dimension(50,23));
		panel_Password.add(label_Password);
		
		passwordField_Password.setPreferredSize(new Dimension(175,23));
		passwordField_Password.setEchoChar('��');
		panel_Password.add(passwordField_Password);
		
		p.add(Box.createRigidArea(new Dimension(1,20)));
		p.add(panel_ID);
		p.add(Box.createRigidArea(new Dimension(1,10)));
		p.add(panel_Password);
		p.add(Box.createRigidArea(new Dimension(1,30)));
		
		return p;
	}
	
	/**
	 * ����һ����ť�����JPanel
	 * @return		һ����ť�����JPanel
	 */
	private JPanel makeButton(){
		JPanel p = new JPanel();
		
		QQ_Login_Button_Listener listener = new QQ_Login_Button_Listener(this);
		button_NetOption.setPreferredSize(new Dimension(70,23));
		button_NetOption.addActionListener(listener);
		button_Login.setPreferredSize(new Dimension(70,23));
		button_Login.addActionListener(listener);
		button_Exit.setPreferredSize(new Dimension(70,23));
		button_Exit.addActionListener(listener);
		p.add(this.button_NetOption);
		p.add(Box.createRigidArea(new Dimension(20,1)));
		p.add(button_Login);
		p.add(Box.createRigidArea(new Dimension(20,1)));
		p.add(this.button_Exit);
		
		return p;
	}

	/**
	 * ���ɵ�¼����JPanel
	 * @return		��¼����JPanel
	 */
	private JPanel makeMain(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.makeLoginImage(),BorderLayout.NORTH);
		panel.add(this.makeLoginArea(),BorderLayout.CENTER);
		panel.add(this.makeButton(),BorderLayout.SOUTH);
		
		return panel;
	}
	
	/**
	 * ������������JPanel
	 * @return		��������JPanel
	 */
	private JPanel makeNetOption(){
		JPanel panel = new JPanel(new GridLayout(3,1));
		Border border1 = BorderFactory.createEmptyBorder(5,5,5,5);
		Border border2 = BorderFactory.createLineBorder(Color.BLUE);
		Border border3 = BorderFactory.createTitledBorder(border2,"��������");
		Border bordercom = BorderFactory.createCompoundBorder(border1, border3);
		panel.setBorder(bordercom);
		
		JPanel panel0 = new JPanel();
		label_NetOption.setForeground(Color.red);
		panel0.add(label_NetOption);
		
		JPanel panel1 = new JPanel();
		JLabel label1 = new JLabel("��������ַ:");
		label1.setPreferredSize(new Dimension(68,23));
		
		comboBox_IP.setEditable(true);
		comboBox_IP.setPreferredSize(new Dimension(175,23));
		panel1.add(label1);
		panel1.add(comboBox_IP);
		
		JPanel panel2 = new JPanel();
		JLabel label2 = new JLabel("��    ��    ��:");
		label2.setPreferredSize(new Dimension(68,23));
		
		textField_Port.setPreferredSize(new Dimension(175,23));
		panel2.add(label2);
		panel2.add(textField_Port);
		
		panel.add(panel0);
		panel.add(panel1);
		panel.add(panel2);
		
		return panel;
		
	}
	
	/**
	 * ���ɼ����˵�¼�������������ý�����ܴ���
	 */
	private void makeAll(){
		this.add(this.makeMain(),BorderLayout.CENTER);
		this.add(this.panel_NetOption,BorderLayout.SOUTH);
	}
	
	/**
	 * ��¼�ɹ�ʱ��ʹ�ô˷����ɽ����ε�¼�����粻���������ID�ļ�ĩβ������ȡ��ԭ��λ���ϵĺ��루�����ƶ���ĩβ��
	 */
	public void autoAppendID(){	
		LogsReaderWriter.createNewFile(file.getPath());
		
		String id = comboBox_ID.getSelectedItem().toString();
		
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		StringBuffer stringBuffer = new StringBuffer();
		try {
			String row = "";
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			while((row = bufferedReader.readLine()) != null){
				//id�ظ�ʱ����
				if(row.equals(id)){
					continue;
				}
				stringBuffer.append(row + "\n");
			}
			//���ε�¼ʹ�õ�id�ӵ�ĩβ
			stringBuffer.append(id + "\n");
			
			fileWriter = new FileWriter(file, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringBuffer.toString());
			bufferedWriter.flush();
		} catch (FileNotFoundException e1) {
			//e1.printStackTrace();
		} catch (IOException e1) {
			//e1.printStackTrace();
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
		}
	}
	
	/**
	 * ���ļ���ȡ���ݳ�ʼ��ID�����
	 */
	private void launchComboBox_ID(){
		LogsReaderWriter.createNewFile(file.getPath());
		
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			Vector<String> id = new Vector<String>();
			Vector<String> idReverse = new Vector<String>();
			String row = null;
			while((row = bufferedReader.readLine()) != null){
				id.add(row);
			}
			ListIterator<String> li = id.listIterator(id.size());
			while(li.hasPrevious()){
				idReverse.add(li.previous());
			}
			comboBox_ID.setModel(new DefaultComboBoxModel(idReverse));
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}finally{
			try {
				fileReader.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			try {
				bufferedReader.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * ������ð�ť
	 * @return		���ð�ť
	 */
	public JButton getButton_NetOption() {
		return button_NetOption;
	}

	/**
	 * ����˳���ť
	 * @return		�˳���ť
	 */
	public JButton getButton_Exit() {
		return button_Exit;
	}

	/**
	 * ��������������
	 * @return		�����������
	 */
	public JPanel getPanel_NetOption() {
		return panel_NetOption;
	}

	/**
	 * ��õ�¼��ť
	 * @return		��¼��ť
	 */
	public JButton getButton_Login() {
		return button_Login;
	}
	
	/**
	 * ���ID�����
	 * @return		ID�����
	 */
	public JComboBox getComboBox_ID() {
		return comboBox_ID;
	}

	/**
	 * ������������
	 * @return		���������
	 */
	public JPasswordField getPasswordField_Password() {
		return passwordField_Password;
	}

	/**
	 * ���IP�����
	 * @return		IP�����
	 */
	public JComboBox getComboBox_IP() {
		return comboBox_IP;
	}

	/**
	 * ��ö˿������
	 * @return		�˿������
	 */
	public JTextField getTextField_Port() {
		return textField_Port;
	}

	/**
	 * ���������������ϵ���ʾ��ǩ
	 * @return		������������ϵ���ʾ��ǩ
	 */
	public JLabel getLabel_NetOption() {
		return label_NetOption;
	}
}

