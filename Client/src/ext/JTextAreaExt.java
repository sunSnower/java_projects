package ext;

import javax.swing.JTextArea;

/**
 * �̳���javax.swing.JTextArea��������ֱ�Ӹ��࣬���ַ�����׷�ӵ�ĩβʱ
 * �����꽫�Զ������µ�ĩβ
 *
 */
public class JTextAreaExt extends JTextArea{
	
	/**
	 * ���� һ���µ�JTextAreaExt����
	 */
	public JTextAreaExt() {
		
	}
	
	/**
	 * ���� һ���µ�JTextAreaExt����������ָ��������������
	 * @param row	ָ��������
	 * @param column	ָ��������
	 */
	public JTextAreaExt(int row, int column){
		super(row, column);
	}

	@Override
	/**
	 * ��д�����еķ��������ַ�����׷�ӵ�ĩβʱ �����꽫�Զ������µ�ĩβ
	 * @param str	Ҫ׷�ӵ��ַ���
	 */
	public void append(String str) {
		super.append(str);
		setCaretPosition(getText().length());
	}
	
}
