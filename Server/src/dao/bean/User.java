package dao.bean;

/**
 * �û���Ϣ�ķ�װ��
 *
 */
public class User {
	
	/**
	 * QQ���
	 */
	private String sid;

	/**
	 * ����
	 */
	private String sname;

	/**
	 * ����
	 */
	private String spassword;

	/**
	 * ����
	 */
	private String nage;

	/**
	 * �Ա�
	 */
	private String ssex;

	/**
	 * ��ַ
	 */
	private String saddress;

	/**
	 * �Ƿ�����
	 */
	private String nisonlin;

	/**
	 * ע��ʱ��
	 */
	private String dregtime;

	/**
	 * ����û���ע��ʱ��
	 * @return		�û���ע��ʱ��
	 */
	public String getDregtime() {
		return dregtime;
	}

	/**
	 * �����û���ע��ʱ��
	 * @param		�û���ע��ʱ��     
	 */
	public void setDregtime(String dregtime) {
		this.dregtime = dregtime;
	}

	/**
	 * ����û�������
	 * @return		�û�������
	 */
	public String getNage() {
		return nage;
	}

	/**
	 * �����û�������
	 * @param		�û�������
	 */
	public void setNage(String nage) {
		this.nage = nage;
	}

	/**
	 * ����û��Ƿ�����
	 * @return		�Ƿ�����
	 */
	public String getNisonlin() {
		return nisonlin;
	}

	/**
	 * �����û��Ƿ�����
	 * @param		�Ƿ�����
	 */
	public void setNisonlin(String nisonlin) {
		this.nisonlin = nisonlin;
	}

	/**
	 * ����û��ĵ�ַ
	 * @return		�û��ĵ�ַ
	 */
	public String getSaddress() {
		return saddress;
	}

	/**
	 * �����û��ĵ�ַ
	 * @param		�û��ĵ�ַ
	 */
	public void setSaddress(String saddress) {
		this.saddress = saddress;
	}

	/**
	 * ����û���id
	 * @return		�û���id
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * �����û���id
	 * @param		�û���id
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * ����û�������
	 * @return�û�������
	 */
	public String getSname() {
		return sname;
	}

	/**
	 * �����û�������
	 * @param		�û�������
	 */
	public void setSname(String sname) {
		this.sname = sname;
	}

	/**
	 * ����û�������
	 * @return		�û�������
	 */
	public String getSpassword() {
		return spassword;
	}

	/**
	 * �����û�������
	 * @param		�û�������
	 */
	public void setSpassword(String spassword) {
		this.spassword = spassword;
	}

	/**
	 * ����û����Ա�
	 * @return		�û����Ա�
	 */
	public String getSsex() {
		return ssex;
	}

	/**
	 * �����û����Ա�
	 * @param		�û����Ա�
	 */
	public void setSsex(String ssex) {
		this.ssex = ssex;
	}
	
}
