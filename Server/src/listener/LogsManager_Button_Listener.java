package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mytools.LogsReaderWriter;

import qq_server_jframe.LogsManager_JPanel;
import qq_server_jframe.QQ_Server_JFrame;

/**
 * ����һ����־���İ�ť�������࣬��ʵ���� java.awt.event.ActionListener�ӿ�
 *
 */
public class LogsManager_Button_Listener implements ActionListener {
	private LogsManager_JPanel logsManager_JPanel;
	private QQ_Server_JFrame qq_Server_JFrame;

	public LogsManager_Button_Listener(LogsManager_JPanel logsManager_JPanel, QQ_Server_JFrame qq_Server_JFrame) {
		this.logsManager_JPanel = logsManager_JPanel;
		this.qq_Server_JFrame = qq_Server_JFrame;
	}

	/**
	 * д����һЩ��ť����ʱ��������Ӧ
	 */
	public void actionPerformed(ActionEvent e) {
		String str_Year = this.logsManager_JPanel.getComboBox_Year()
				.getSelectedItem().toString();
		String str_Month = this.logsManager_JPanel.getComboBox_Month()
				.getSelectedItem().toString();
		String str_Day = this.logsManager_JPanel.getComboBox_Day()
				.getSelectedItem().toString();
		if (str_Month.length() == 1) {
			str_Month = 0 + str_Month;
		}
		if (str_Day.length() == 1) {
			str_Day = 0 + str_Day;
		}

		String filePath = "log/" + str_Year + str_Month + str_Day + ".log";
		String keyWords = logsManager_JPanel.getTextField_KeyWords().getText();
		String result = LogsReaderWriter.readFromFile(filePath, keyWords);
		//�����ڸ�����־
		if(result == null){
			logsManager_JPanel.getTextArea().setText("ָ�����ڵ���־�ļ�������!");
		//���ڸ�����־�������ڰ����ùؼ��ֵ���ؼ�¼
		}else if("".equals(result)){
			logsManager_JPanel.getTextArea().setText("ָ�����ڵ���־�ļ��в����ڰ����ùؼ��ֵ���ؼ�¼!");
		//���ڸ�����־(�Ҵ��ڰ����ùؼ��ֵ���ؼ�¼)
		}else{
			logsManager_JPanel.getTextArea().setText(result);
		}

	}

}
