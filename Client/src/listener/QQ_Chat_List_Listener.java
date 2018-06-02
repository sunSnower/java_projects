package listener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import qq_client_jframe.QQ_Chat_JFrame;

/**
 * ����һ��Jlistѡ���¼��ļ������࣬��ʵ����javax.swing.event.ListSelectionEvent�ӿ�
 *
 */
public class QQ_Chat_List_Listener implements ListSelectionListener{
	
	/**
	 * һ��qq_Chat_JFrame��ʵ��
	 */
	private QQ_Chat_JFrame qq_Chat_JFrame = null;

	/**
	 * ����һ��Jlistѡ���¼��ļ�����
	 * @param qq_Chat_JFrame	ʹ���������Զ�qq_Chat_JFrame�е�������в���
	 */
	public QQ_Chat_List_Listener(QQ_Chat_JFrame qq_Chat_JFrame) {
		this.qq_Chat_JFrame = qq_Chat_JFrame;
	}

	/**
	 * �������б�ѡ��ʱ������Ӧ
	 */
	public void valueChanged(ListSelectionEvent e) {
		String selectUser = (String)qq_Chat_JFrame.getList_OnlineUsers().getSelectedValue();
		if(selectUser == null){
			qq_Chat_JFrame.getLable_To().setText("");
		}else{
			qq_Chat_JFrame.getLable_To().setText("To:" + selectUser);
		}
	}

}
