package pub;

import java.io.Serializable;

/**
 * ��װ���ݵİ���������java.io.Serializable�ӿ�
 *
 */
public class QQPackage implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * ��Ϣ������
	 */
	private String from;
	
	/**
	 * ��Ϣ������
	 */
	private String to;
	
	/**
	 * ������
	 */
	private PackType packType;
	
	/**
	 * ���ݰ�
	 */
	private Object data;
	
	/**
	 * �����Ϣ������
	 * @return		��Ϣ������
	 */
	public String getFrom() {
		return from;
	}
	
	/**
	 * ������Ϣ������
	 * @return		��Ϣ������
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * �����Ϣ������
	 * @return		��Ϣ������
	 */
	public String getTo() {
		return to;
	}
	
	/**
	 * ������Ϣ������
	 * @param 		��Ϣ������
	 */
	public void setTo(String to) {
		this.to = to;
	}
	
	/**
	 * ������ݰ�
	 * @return		���ݰ�
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * �������ݰ�
	 * @param		 ���ݰ�
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * ��ð�����
	 * @return		������
	 */
	public PackType getPackType() {
		return packType;
	}
	
	/**
	 * ���ð�����
	 * @param 		������
	 */
	public void setPackType(PackType packType) {
		this.packType = packType;
	}
}
