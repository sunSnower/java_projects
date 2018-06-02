package qq_client_jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import cn.netjava.client.ClientStart;
import ext.JTextAreaExt;
import pub.PackType;
import pub.QQPackage;
import qq_client_jdialog.ResetPassword_JDialog;
import qq_client_thread.Client_Read_Thread;


import listener.QQ_Chat_Button_Listener;
import listener.QQ_Chat_Key_Listener;
import listener.QQ_Chat_List_Listener;
import mytools.MyDate;

/**
 * ����һ��QQ���촰���࣬���̳���javax.swing.JFrame
 *
 */
public class QQ_Chat_JFrame extends JFrame{
	private JButton button_ChatLogs = new JButton("�����¼��");
	private JButton button_Send = new JButton("����");
	private JButton button_Close = new JButton("�ر�");
	private Socket socket = null;
	private JLabel lable_To = new JLabel();
	private JButton button_ResPsw = new JButton("�޸�����");
	private JPanel panel_ChatLogs = null;
	private JTextAreaExt textArea_Dsp = new JTextAreaExt(15, 60);
	private JTextArea textArea_Input = new JTextArea(5, 60);
	private JList list_OnlineUsers = new JList();
	private JTextArea textArea_Notice = new JTextArea(25,25);
	private JTextAreaExt textArea_ChatLogs = new JTextAreaExt();
	private Client_Read_Thread readThread_Client = null;
	private ResetPassword_JDialog resetPassword_JDialog = null;
	private String ID = null;
	private String name_ID = null;
	private ObjectOutputStream objectOutputStream = null;
	private ObjectInputStream objectInputStream = null;
	
	
	private JButton fontButton = new JButton("A");
	private JButton videoButton = new JButton("video");
	private JComboBox box = null;
	
	public QQ_Chat_JFrame(Socket socket, QQPackage qqPackageReturn,ObjectOutputStream objectOutputStream ,
	ObjectInputStream objectInputStream ) {
		super("QQ����");
		this.socket = socket;
		this.objectInputStream = objectInputStream;
		this.objectOutputStream = objectOutputStream;
		//���
		Vector data = (Vector)qqPackageReturn.getData();
		ID = data.get(0).toString();
		String name = data.get(1).toString();
		DefaultListModel defaultListModel = (DefaultListModel)data.get(2);
		String notice = data.get(3).toString();
		name_ID = name + "(" + ID + ")";
		setTitle(name_ID);
		list_OnlineUsers.setModel(defaultListModel);
		textArea_Notice.setText(notice);
		
		makeAll();
		setSize(new Dimension(549,490));
		setPreferredSize(new Dimension(549,480));
		panel_ChatLogs.setVisible(false);
		//this.pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		readThread_Client = new Client_Read_Thread(this,socket);
		readThread_Client.start();
	}

	public JButton getVideoButton() {
		return videoButton;
	}

	public void setVideoButton(JButton videoButton) {
		this.videoButton = videoButton;
	}

	/**
	 * ����һ������Ϊ�����ܵ�JPanel
	 * @return		һ������Ϊ�����ݵ�JPanel
	 */
	private JPanel makeChatArea(){
		QQ_Chat_Button_Listener listener = new QQ_Chat_Button_Listener(this);
		
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(panel1,BoxLayout.LINE_AXIS);
		panel1.setLayout(boxLayout1);
		
		button_ResPsw.setPreferredSize(new Dimension(95,23));
		button_ResPsw.addActionListener(listener);
		panel1.add(lable_To);
		panel1.add(Box.createHorizontalGlue());
		panel1.add(button_ResPsw);
	
		JScrollPane scrollPane_Dsp = new JScrollPane(textArea_Dsp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textArea_Dsp.setLineWrap(true);
		textArea_Dsp.setEditable(false);
		
		textArea_Input.setLineWrap(true);
		textArea_Input.addKeyListener(new QQ_Chat_Key_Listener(this));
		JScrollPane scrollPane_Input = new JScrollPane(textArea_Input,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		/*
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollPane_Dsp,scrollPane_Input);
		splitPane.setResizeWeight(1);
		*/
		GraphicsEnvironment  environment = GraphicsEnvironment.getLocalGraphicsEnvironment();// GraphicsEnvironment��һ�������࣬����ʵ������ֻ�������еľ�̬������ȡһ��ʵ��
        final String[] str = environment.getAvailableFontFamilyNames();
		box = new JComboBox(str);
		JPanel panel2 = new JPanel();
		BoxLayout boxLayout2 = new BoxLayout(panel2,BoxLayout.LINE_AXIS);
		panel2.setLayout(boxLayout2);
		
		
		
		JPanel panel4 = new JPanel();
		BoxLayout boxLayout4 = new BoxLayout(panel4, BoxLayout.LINE_AXIS);
		panel4.setLayout(boxLayout4);
		panel4.setPreferredSize(new Dimension(90, 23));
		box.setPreferredSize(new Dimension(90,23));

		box.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent event) {
				// TODO Auto-generated method stub
				if (event.getStateChange() == event.SELECTED) {
					int style = textArea_Dsp.getFont().getStyle(); // ��ȡ��ǰ���������
                    int size = textArea_Dsp.getFont().getSize();
					textArea_Dsp.setFont(new Font(str[box.getSelectedIndex()], style, size));
                }
			}
		});
		
		videoButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
					QQPackage qqPackage_Chat = new QQPackage();
					Object select = getList_OnlineUsers()
							.getSelectedValue();
					// û��ѡ���û�
					if (select == null) {
						JOptionPane.showMessageDialog(null, "����δѡ����Ƶ������!");
						return;
					}
					String to = select.toString();
					int index = to.indexOf("(");
					String toID = to.substring(index + 1, to.length() - 1);
					String from = getName_ID();
					// ���Լ�������Ϣ
					if (from.equals(to)) {
						JOptionPane.showMessageDialog(null, "�޷����Լ�����Ƶ!");
						return;

					// �������˻������û�������Ϣ
					} else {
						// �Ϸ���Ϣ--������
						if ("������".equals(to)) {
							return;

						// �Ϸ���Ϣ--˽��
						} else {
							qqPackage_Chat.setPackType(PackType.privateVideo);
							qqPackage_Chat.setFrom(from);
							qqPackage_Chat.setTo(toID);
							String time = MyDate.dateFormat(MyDate.FORMAT_HMS);
							String self = "��" + " " + time + " ��" + to + "��Ƶ" + "\n";
							getTextArea_Dsp().append(self);
							getTextArea_ChatLogs().append(self);
						}

					}

					ObjectOutputStream objectOutputStream = null;
					try {
						objectOutputStream = getObjectOutputStream();
						qqPackage_Chat.setData("");
						objectOutputStream.writeObject(qqPackage_Chat);
						objectOutputStream.flush();
					} catch (IOException e1) {
						//e1.printStackTrace();
					}

					ClientStart test = new ClientStart();
					test.initUI(from);
			}
		});
		
		videoButton.setPreferredSize(new Dimension(95,23));
		panel4.add(box);
		panel4.add(Box.createRigidArea(new Dimension(5,1)));
		panel4.add(videoButton);
		
		
		JPanel panel3 = new JPanel(new BorderLayout());
		panel3.add(scrollPane_Dsp, BorderLayout.NORTH);
		panel3.add(panel4, BorderLayout.CENTER);
		panel3.add(scrollPane_Input, BorderLayout.SOUTH);
		
		
		JLabel label_SendKey = new JLabel("Ctrl+Enter������Ϣ  ");
		label_SendKey.setForeground(Color.GRAY);
		button_ChatLogs.setPreferredSize(new Dimension(99,23));
		button_ChatLogs.addActionListener(listener);
		button_Send.setPreferredSize(new Dimension(60,23));
		button_Send.addActionListener(listener);
		button_Close.setPreferredSize(new Dimension(60,23));
		button_Close.addActionListener(listener);
		panel2.add(button_ChatLogs);
		panel2.add(Box.createHorizontalGlue());
		panel2.add(label_SendKey);
		panel2.add(button_Send);
		panel2.add(Box.createRigidArea(new Dimension(5,1)));
		panel2.add(button_Close);
		
		/*
		panel.add(panel1,BorderLayout.NORTH);
		panel.add(splitPane,BorderLayout.CENTER);
		panel.add(panel2,BorderLayout.SOUTH);
		*/
		
		panel.add(panel1,BorderLayout.NORTH);
		panel.add(panel3,BorderLayout.CENTER);
		panel.add(panel2,BorderLayout.SOUTH);
		
		return panel;
	}
	
	/**
	 * ����һ�����й�����Ϣ�������û��б��JSplitPane
	 * @return		���й�����Ϣ�������û��б��JSplitPane
	 */
	private JSplitPane makeImfomation(){
		QQ_Chat_List_Listener listener = new QQ_Chat_List_Listener(this);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JScrollPane scrollPane_Notice = new JScrollPane(textArea_Notice,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
		textArea_Notice.setCaretPosition(0);
		textArea_Notice.setEditable(false);
		textArea_Notice.setLineWrap(true);
		scrollPane_Notice.setPreferredSize(new Dimension(150,150));
		tabbedPane.addTab("������Ϣ",null,scrollPane_Notice,"������Ϣ");
		
		
		JPanel panel_OnlineUsers = new JPanel(new BorderLayout());
		JLabel lable_OnlineUsers = new JLabel("�����û�");
		list_OnlineUsers.addListSelectionListener(listener);
		JScrollPane scrollPane_OnlineUsers = new JScrollPane(list_OnlineUsers,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_OnlineUsers.getVerticalScrollBar().setValue(0); 
		
		panel_OnlineUsers.add(lable_OnlineUsers,BorderLayout.NORTH);
		panel_OnlineUsers.add(scrollPane_OnlineUsers,BorderLayout.CENTER);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,tabbedPane,panel_OnlineUsers);
		
		return splitPane;
		
	}
	
	/**
	 * ����һ����������壬��makeChatArea()��makeImfomation()�ļ������
	 * @return		makeChatArea()��makeImfomation()�ļ������
	 */
	private JSplitPane makeMain(){
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,makeChatArea(),makeImfomation());
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(1);
		
		return splitPane;
	}
	
	/**
	 * ����һ�������¼��壬��Ĭ���ǲ��ɼ���
	 * @return		һ�������¼���
	 */
	private JPanel makeChatLogs(){
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel panel1 = new JPanel();
		BoxLayout boxLayout1 = new BoxLayout(panel1, BoxLayout.LINE_AXIS);
		panel1.setLayout(boxLayout1);
		JLabel lable0 = new JLabel("�����¼");

//		JComboBox comboBox1_1 = new JComboBox(new String[]{"2009","2010"});
//		comboBox1_1.setPreferredSize(new Dimension(40,23));
//		JLabel lable1_1 = new JLabel("��");
//		JComboBox comboBox1_2 = new JComboBox(new String[]{"1","2"});
//		comboBox1_2.setPreferredSize(new Dimension(40,23));
//		JLabel lable1_2 = new JLabel("��");
//		JComboBox comboBox1_3 = new JComboBox(new String[]{"29","30"});
//		comboBox1_3.setPreferredSize(new Dimension(40,23));
//		JLabel lable1_3 = new JLabel("��");
//		JButton button1 = new JButton("��ѯ");
//		button1.setPreferredSize(new Dimension(60,23));
		panel1.add(lable0);
		panel1.add(Box.createHorizontalGlue());
//		panel1.add(comboBox1_1);
//		panel1.add(lable1_1);
//		panel1.add(comboBox1_2);
//		panel1.add(lable1_2);
//		panel1.add(comboBox1_3);
//		panel1.add(lable1_3);
//		panel1.add(button1);
		
		//textArea_ChatLogs.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea_ChatLogs,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textArea_ChatLogs.setLineWrap(true);
		textArea_ChatLogs.setEditable(false);
		
		panel.add(panel1,BorderLayout.NORTH);
		panel.add(scrollPane,BorderLayout.CENTER);
		
		return panel;
	}
	
	/**
	 * ����һ����������������������¼��������壬�����¼���Ĭ��Ϊ���ɼ�
	 */
	private void makeAll(){
		JPanel panel = new JPanel(new BorderLayout());
		
		panel_ChatLogs = makeChatLogs();
		panel_ChatLogs.setPreferredSize(new Dimension(549,130));
		panel.add(makeMain(),BorderLayout.CENTER);
		panel.add(panel_ChatLogs,BorderLayout.SOUTH);
		
		add(panel);
		
	}
	
	/**
	 * ��������¼��ť
	 * @return		�����¼��ť
	 */
	public JButton getButton_ChatLogs() {
		return button_ChatLogs;
	}

	/**
	 * ���socket
	 * @return	socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * �����ʾ�����ߵı�ǩ
	 * @return		��ʾ�����ߵı�ǩ
	 */
	public JLabel getLable_To() {
		return lable_To;
	}

	/**
	 * ��������¼���
	 * @return		�����¼���
	 */
	public JPanel getPanel_ChatLogs() {
		return panel_ChatLogs;
	}

	/**
	 * ����޸����밴ť
	 * @return		�޸����밴ť
	 */
	public JButton getButton_ResPsw() {
		return button_ResPsw;
	}

	/**
	 * �����ʾ��ǰ�������ݵ��ı���
	 * @return		��ʾ��ǰ�������ݵ��ı���
	 */
	public JTextArea getTextArea_Dsp() {
		return textArea_Dsp;
	}

	/**
	 * ���������Ϣ���ı���
	 * @return		������Ϣ���ı���
	 */
	public JTextArea getTextArea_Input() {
		return textArea_Input;
	}

	/**
	 * ��÷��Ͱ�ť
	 * @return	���Ͱ�ť
	 */
	public JButton getButton_Send() {
		return button_Send;
	}

	/**
	 * ��ùرհ�ť
	 * @return		�رհ�ť
	 */
	public JButton getButton_Close() {
		return button_Close;
	}

	/**
	 * ��������û��б�JList
	 * @return		�����û��б�JList
	 */
	public JList getList_OnlineUsers() {
		return list_OnlineUsers;
	}

	/**
	 * ��ù�����Ϣ���ı���
	 * @return		������Ϣ���ı���
	 */
	public JTextArea getTextArea_Notice() {
		return textArea_Notice;
	}

	/**
	 * ���ʹ�ø����촰����û�ID
	 * @return		ʹ�ø����촰����û�ID
	 */
	public String getID() {
		return ID;
	}
	
	/**
	 * ��øô����û��Է������Ķ��������
	 * @return		�ô����û��Է������Ķ��������
	 */
	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	/**
	 * ��øô����û��Է������Ķ���������
	 * @return		�ô����û��Է������Ķ���������
	 */
	public ObjectInputStream getObjectInputStream() {
		return objectInputStream;
	}

	/**
	 * ����socket
	 * @param socket
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * ����޸�����Ի���
	 * @return		�޸�����Ի���
	 */
	public ResetPassword_JDialog getResetPassword_JDialog() {
		return resetPassword_JDialog;
	}

	/**
	 * ��������¼���ı���
	 * @return		�����¼���ı���
	 */
	public JTextArea getTextArea_ChatLogs() {
		return textArea_ChatLogs;
	}

	/**
	 * �����޸�����Ի���
	 * @param resetPassword_JDialog
	 */
	public void setResetPassword_JDialog(
			ResetPassword_JDialog resetPassword_JDialog) {
		this.resetPassword_JDialog = resetPassword_JDialog;
	}

	public String getName_ID() {
		return name_ID;
	}

	public void setName_ID(String nameID) {
		name_ID = nameID;
	}
}

