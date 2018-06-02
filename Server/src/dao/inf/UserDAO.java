package dao.inf;

import java.util.Vector;

import dao.bean.User;

/**
 * ����һ���ӿ�;���ڶ��� �����û���Ϣ�ķ���.
 * 
 * 
 */
public interface UserDAO {

	/**
	 * ����������ѯ.
	 * 
	 * @param sid
	 *            �û����;
	 * @param sname
	 *            �û���;
	 * @param state
	 *            ������ʾ�����û���ֻ��ʾ���ߵ��û�;
	 *            
	 * @return �û���Ϣ��.
	 */
	public Vector<Vector<String>> selectList(String sid, String sname, int state);

	/**
	 * ����������ѯ.
	 * 
	 * @param sid
	 *            �û����;
	 * @return �û�����.
	 */
	public User selectList(String sid);


	/**
	 * �����û�.
	 * 
	 * @param user
	 *            �û�����.
	 * @return �����ɹ����
	 */
	public boolean insertUser(User user);

	/**
	 * ɾ���û�.
	 * 
	 * @param sid
	 *            �û����.
	 * @return �����Ƿ�ɹ�.
	 */
	public boolean deleteUser(String sid);

	/**
	 * �޸��û�.
	 * 
	 * @param user
	 *            �û�����.
	 * @return �����Ƿ�ɹ�.
	 */
	public boolean updateUser(User user);

	/**
	 * ����ѡ���û�������.
	 * 
	 * @param sid
	 *            �û����.
	 * @return �����Ƿ�ɹ�.
	 */
	public boolean resetPWD(String sid);
	
	/**
	 * �޸�ѡ���û�������.
	 * 
	 * @param sid
	 *            �û����.
	 * @param newPWD
	 *            ������.
	 * @return �����Ƿ�ɹ�.
	 */
	public boolean resetPWD(String sid, String newPWD);

	/**
	 * ���������û�����.
	 * 
	 * @return �����Ƿ�ɹ�.
	 */
	public boolean resetAllPWD();

	/**
	 * �޸�������.
	 * 
	 * @param sid
	 *            �û����.
	 * @param tag
	 *            ��ʶ״̬; up:����;down����
	 * @return �����Ƿ�ɹ�.
	 */
	public boolean upOrDown(String sid, String tag);

	/**
	 * �޸������û�������״̬.
	 * 
	 * @param tag
	 *            ��ʶ״̬; up:����;down����
	 * @return �����Ƿ�ɹ�.
	 */
	public boolean upOrDown(String tag);

	/**
	 * @return ��ȡ��һ���û����.
	 * 
	 */
	public String getNextSid();
}