package dao.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import mytools.LogsReaderWriter;


import dao.bean.User;
import dao.inf.UserDAO;

/**
 * ����һ���������ݵķ����࣬��ʵ����dao.inf.UserDAO�ӿڣ����·����Ĺ�������Ľӿ��е�˵��
 *
 */
public class UserDaoImpl implements UserDAO {
	
	/**
	 * selectList�����еĲ�����ָʾɸѡ��������ʾ��sid��ȷ��ѯʱȡ�ø�sid�������ֶ�
	 */
	public static final int UP_AND_DOWN_WITH_ALLINFO = 0;
	
	/**
	 * selectList�����еĲ�����ָʾɸѡ��������Ϊȡ�������û�����ɾ��һЩ����Ҫ���ֶ�
	 */
	public static final int UP_AND_DOWN = 1;

	/**
	 * selectList�����еĲ�����ָʾɸѡ��������Ϊֻȡ�������û�����ɾ��һЩ����Ҫ���ֶ�
	 */
	public static final int ONLY_UP = 2;

	/**
	 * �û������ļ�
	 */
	private static final File file = new File("data/user.txt");
	
	/**
	 * ����
	 */
	private static UserDaoImpl userDaoImpl = new UserDaoImpl();

	
	private UserDaoImpl() {

	}
	
	/**
	 * ��ø����Ψһʵ��
	 * @return
	 */
	public static UserDaoImpl getInstance(){
		return userDaoImpl;
	}

	public synchronized boolean deleteUser(String sid) {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		boolean flag = true;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String rowIn = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				if (rowArr[0].equals(sid)) {
					continue;
				}
				stringBuffer.append(rowIn + "\n");
			}
			fileWriter = new FileWriter(file, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringBuffer.toString());
			bufferedWriter.flush();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			flag = false;
		} catch (IOException e) {
			//e.printStackTrace();
			flag = false;
		} finally {
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
			try {
				fileWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}

		}
		
		if (flag == false) {
			return flag;
		} else {
			return flag;
		}

	}

	public synchronized String getNextSid() {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		String nextSid = null;
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			int maxSid = 0;
			String rowIn = null;
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				int temp = Integer.parseInt(rowArr[0]);
				if (maxSid < temp) {
					maxSid = temp;
				}
			}
			nextSid = String.format("%06d", maxSid + 1);

		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}

		return nextSid;
	}

	public synchronized boolean insertUser(User user) {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		boolean flag = true;

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(user.getSid() + ",");
		stringBuffer.append(user.getSpassword() + ",");
		stringBuffer.append(user.getSname() + ",");
		stringBuffer.append(user.getSsex() + ",");
		stringBuffer.append(user.getNage() + ",");
		stringBuffer.append(user.getSaddress() + ",");
		stringBuffer.append(user.getNisonlin() + ",");
		stringBuffer.append(user.getDregtime());

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file, true);
			fileWriter.write(stringBuffer.toString() + "\n");
			fileWriter.flush();
		} catch (IOException e) {
			//e.printStackTrace();
			flag = false;
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return flag;

	}

	public synchronized boolean resetAllPWD() {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		boolean flag = true;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String rowIn = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				for (int i = 0;;) {
					if (i == 1) {
						stringBuffer.append("123456,");
						i++;
						continue;
					}
					stringBuffer.append(rowArr[i]);
					if (++i == rowArr.length) {
						stringBuffer.append("\n");
						break;
					}
					stringBuffer.append(",");
				}
			}
			fileWriter = new FileWriter(file, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringBuffer.toString());
			bufferedWriter.flush();
		} catch (FileNotFoundException e) {
			flag = false;
			//e.printStackTrace();
		} catch (IOException e) {
			flag = false;
			//e.printStackTrace();
		} finally {
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
			try {
				fileWriter.close();
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}

		return flag;
	}

	public synchronized boolean resetPWD(String sid) {
		return resetPWD(sid, "123456");
	}
	
	public synchronized boolean resetPWD(String sid, String newPWD) {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		boolean flag = true;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String rowIn = null;
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				if (rowArr[0].equals(sid)) {
					for (int i = 0;;) {
						if (i == 1) {
							stringBuffer.append(newPWD + ",");
							i++;
							continue;
						}
						stringBuffer.append(rowArr[i]);
						if (++i == rowArr.length) {
							stringBuffer.append("\n");
							break;
						}
						stringBuffer.append(",");
					}
				} else {
					stringBuffer.append(rowIn + "\n");
				}
			}
			fileWriter = new FileWriter(file, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringBuffer.toString());
			bufferedWriter.flush();
		} catch (FileNotFoundException e) {
			flag = false;
			//e.printStackTrace();
		} catch (IOException e) {
			flag = false;
			//e.printStackTrace();
		} finally {
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
			try {
				fileWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return flag;
	}
	
	public synchronized Vector<Vector<String>> selectList(String sid, String sname, int state) {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		
		try {// ���ļ��м��������û���Ϣ��Vector<Vector<String>>
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String rowIn = null;
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				Vector<String> row = new Vector<String>();
				for (String string : rowArr) {
					row.add(string);
				}
				data.add(row);
			}
			
			if(state == 1){//����usermanager���
				Iterator<Vector<String>> i = data.iterator();
				while (i.hasNext()) {
					Vector<String> rowTemp = i.next();
					//rowTemp.removeElementAt(6);// ���Ƴ��Ƿ�������Թ���ʾ
					rowTemp.removeElementAt(1);// ���Ƴ�������Թ���ʾ
				}
				
			}else if(state == 2){//����servermanager���
				Iterator<Vector<String>> i = data.iterator();
				while (i.hasNext()) {
					Vector<String> rowTemp = i.next();
					if(rowTemp.get(6).equals("����")){
						i.remove();
						continue;
					}
					rowTemp.removeElementAt(7);// ���Ƴ�ע��ʱ����Թ���ʾ
					rowTemp.removeElementAt(5);// ���Ƴ���ַ��Թ���ʾ
					rowTemp.removeElementAt(1);// ���Ƴ�������Թ���ʾ
				}
			}
			
			String sidNoSpc = null;// <--sid == null
			String snameNoSpc = null;// <--sname == null
			if (sid != null) {// ȥ���û�����Ŀո�
				sidNoSpc = sid.replaceAll(" ", "");
			}
			if (sname != null) {// ȥ���û�����Ŀո�
				snameNoSpc = sname.replaceAll(" ", "");
			}

			if ((sidNoSpc == null || "".equals(sidNoSpc))
					&& (snameNoSpc == null || "".equals(snameNoSpc))) {// �ղ�ѯ�����������û���������
				return data;

			} else if ((sidNoSpc == null || "".equals(sidNoSpc))
					&& (snameNoSpc != null && !"".equals(snameNoSpc))) {// ��ʵ�������snameʱ��������
				Iterator<Vector<String>> i2 = data.iterator();
				while (i2.hasNext()) {
					Vector<String> rowTemp = i2.next();
					if (!rowTemp.get(1).contains(snameNoSpc)) {// ���������û�����������Ƚϣ�!equals��ɾ����
						i2.remove();
					}
				}
				return data;

			} else if ((sidNoSpc != null && !"".equals(sidNoSpc))
					&& (snameNoSpc == null || "".equals(snameNoSpc))) {// ��ʵ�������sidʱ��������
				Iterator<Vector<String>> i3 = data.iterator();
				while (i3.hasNext()) {
					Vector<String> rowTemp = i3.next();
					if (!rowTemp.get(0).equals(sidNoSpc)) {// ���������û�����������Ƚϣ�!equals��ɾ����
						i3.remove();
					}
				}
				return data;

			} else if ((sidNoSpc != null && !"".equals(sidNoSpc))
					&& (snameNoSpc != null && !"".equals(snameNoSpc))) {// sid��sname����ʱ��������
				Iterator<Vector<String>> i4 = data.iterator();
				while (i4.hasNext()) {
					Vector<String> rowTemp = i4.next();
					if (!rowTemp.get(0).equals(sidNoSpc)
							|| !rowTemp.get(1).equals(snameNoSpc)) {// ���������û�����������Ƚϣ�!equals��ɾ����
						i4.remove();
					}
				}
				return data;
			}

		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
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

		return null;
	}

	public synchronized User selectList(String sid) {
		Vector<Vector<String>> data = selectList(sid, "", UserDaoImpl.UP_AND_DOWN_WITH_ALLINFO);
		Iterator<Vector<String>> i = data.iterator();
		if (i.hasNext()) {
			Vector<String> row = i.next();
			User user = new User();
			user.setSid(sid);
			user.setSpassword(row.get(1));
			user.setSname(row.get(2));
			user.setSsex(row.get(3));
			user.setNage(row.get(4));
			user.setSaddress(row.get(5));
			user.setNisonlin(row.get(6));
			user.setDregtime(row.get(7));
			return user;

		} else {
			return null;
		}

	}


	public synchronized boolean upOrDown(String sid, String tag) {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		boolean flag = true;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String rowIn = null;
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				if (rowArr[0].equals(sid)) {
					for (int i = 0;;) {
						if (i == 6) {
							stringBuffer.append(tag + ",");
							i++;
							continue;
						}
						stringBuffer.append(rowArr[i]);
						if (++i == rowArr.length) {
							stringBuffer.append("\n");
							break;
						}
						stringBuffer.append(",");
					}
				} else {
					stringBuffer.append(rowIn + "\n");
				}
			}
			fileWriter = new FileWriter(file, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringBuffer.toString());
			bufferedWriter.flush();
		} catch (FileNotFoundException e) {
			flag = false;
			//e.printStackTrace();
		} catch (IOException e) {
			flag = false;
			//e.printStackTrace();
		} finally {
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
			try {
				fileWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}

		return flag;
	}

	public synchronized boolean upOrDown(String tag) {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		boolean flag = true;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String rowIn = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				for (int i = 0;;) {
					if (i == 6) {
						stringBuffer.append(tag + ",");
						i++;
						continue;
					}
					stringBuffer.append(rowArr[i]);
					if (++i == rowArr.length) {
						stringBuffer.append("\n");
						break;
					}
					stringBuffer.append(",");
				}
			}
			fileWriter = new FileWriter(file, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringBuffer.toString());
			bufferedWriter.flush();
		} catch (FileNotFoundException e) {
			flag = false;
			//e.printStackTrace();
		} catch (IOException e) {
			flag = false;
			//e.printStackTrace();
		} finally {
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
			try {
				fileWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}

		return flag;
	}

	public synchronized boolean updateUser(User user) {
		//��ʹ��ǰ��֤�ļ��Ѵ���
		LogsReaderWriter.createNewFile(file.getPath());
		
		boolean flag = true;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String rowIn = null;
			StringBuffer stringBuffer = new StringBuffer();
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				if (rowArr[0].equals(user.getSid())) {
					stringBuffer.append(user.getSid() + ",");
					stringBuffer.append(user.getSpassword() + ",");
					stringBuffer.append(user.getSname() + ",");
					stringBuffer.append(user.getSsex() + ",");
					stringBuffer.append(user.getNage() + ",");
					stringBuffer.append(user.getSaddress() + ",");
					stringBuffer.append(user.getNisonlin() + ",");
					stringBuffer.append(user.getDregtime() + "\n");

				} else {
					stringBuffer.append(rowIn + "\n");
				}
			}
			fileWriter = new FileWriter(file, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringBuffer.toString());
			bufferedWriter.flush();
		} catch (FileNotFoundException e) {
			flag = false;
			//e.printStackTrace();
		} catch (IOException e) {
			flag = false;
			//e.printStackTrace();
		} finally {
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
			try {
				fileWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		return flag;
	}



	/*public User selectUserAllInfo(String sid) {
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		try {// ���ļ��м��������û���Ϣ��Vector<Vector<String>>
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String rowIn = null;
			while ((rowIn = bufferedReader.readLine()) != null) {
				String[] rowArr = rowIn.split(",");
				Vector<String> row = new Vector<String>();
				for (String string : rowArr) {
					row.add(string);
				}
				data.add(row);
			}
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
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

		Iterator<Vector<String>> i3 = data.iterator();
		while (i3.hasNext()) {
			Vector<String> rowTemp = i3.next();
			if (!rowTemp.get(0).equals(sid)) {// ���������û�����������Ƚϣ�!equals��ɾ����
				i3.remove();
			}
		}

		Vector<String> row = data.get(0);
		User user = new User();
		user.setSid(sid);
		user.setSpassword(row.get(1));
		user.setSname(row.get(2));
		user.setSsex(row.get(3));
		user.setNage(row.get(4));
		user.setSaddress(row.get(5));
		user.setNisonlin(row.get(6));
		user.setDregtime(row.get(7));
		
		return user;

	}*/
}
