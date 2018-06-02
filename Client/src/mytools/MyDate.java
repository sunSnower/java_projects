package mytools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * ����һ�������������ڻ�ʱ�����ݵĹ�����
 *
 */
public class MyDate {
	
	/**
	 * ָʾ��������ʱ���ʽΪ"yyyyMMdd"
	 */
	public static final int FORMAT_YMD = 1;
	
	/**
	 * ָʾ��������ʱ���ʽΪ"[yyyy-MM-dd HH:mm:ss]"
	 */
	public static final int FORMAT_YMDHMS = 2;
	
	/**
	 * "HH:mm:ss"
	 */
	public static final int FORMAT_HMS = 3;

	/**
	 * ����������ݵ�һ��Vector<Integer>����Ҫ����������JComboBox�е�ѡ��
	 * @return		������ݵ�һ��Vector<Integer>
	 */
	public static Vector<Integer> year() {
		Vector<Integer> vector_Year = new Vector<Integer>();
		for (int i = 2010; i <= 2100; i++) {
			vector_Year.add(i);
		}

		return vector_Year;
	}

	/**
	 * ���������·ݵ�һ��Vector<Integer>����Ҫ����������JComboBox�е�ѡ��
	 * @return		�����·ݵ�һ��Vector<Integer>
	 */
	public static Vector<Integer> month() {
		Vector<Integer> vector_Month = new Vector<Integer>();
		for (int i = 1; i <= 12; i++) {
			vector_Month.add(i);
		}

		return vector_Month;
	}

	/**
	 * �����������ڴ����·��е�������һ��Vector<Integer>����Ҫ����������JComboBox�е�ѡ��
	 * @return		�������ڴ����·��е�������һ��Vector<Integer>����31��
	 */
	public static Vector<Integer> dateOf31() {
		Vector<Integer> vector_Date = new Vector<Integer>();
		for (int i = 1; i <= 31; i++) {
			vector_Date.add(i);
		}

		return vector_Date;
	}

	/**
	 * ������������С���·��е�������һ��Vector<Integer>����Ҫ����������JComboBox�е�ѡ��
	 * @return		��������С���·��е�������һ��Vector<Integer>����30��
	 */
	public static Vector<Integer> dateOf30() {
		Vector<Integer> vector_Date = new Vector<Integer>();
		for (int i = 1; i <= 30; i++) {
			vector_Date.add(i);
		}

		return vector_Date;
	}

	/**
	 * ����������������2�·��е�������һ��Vector<Integer>����Ҫ����������JComboBox�е�ѡ��
	 * @return		������������2�·��е�������һ��Vector<Integer>����29��
	 */
	public static Vector<Integer> dateOf29() {
		Vector<Integer> vector_Date = new Vector<Integer>();
		for (int i = 1; i <= 29; i++) {
			vector_Date.add(i);
		}

		return vector_Date;
	}

	/**
	 * ������������ƽ��2�·��е�������һ��Vector<Integer>����Ҫ����������JComboBox�е�ѡ��
	 * @return		��������ƽ��2�·��е�������һ��Vector<Integer>����28��
	 */
	public static Vector<Integer> dateOf28() {
		Vector<Integer> vector_Date = new Vector<Integer>();
		for (int i = 1; i <= 28; i++) {
			vector_Date.add(i);
		}

		return vector_Date;
	}

	/**
	 * �ж��Ƿ�Ϊ����
	 * @param year		Ҫ�жϵ����
	 * @return		�����������򷵻�true�����򷵻�false
	 */
	public static boolean isLeapYear(int year) {
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ȡ�õ�ǰϵͳʱ���Vector<Integer>��������3�����ݣ��ֱ����꣬�£���
	 * @return
	 */
	public static Vector<Integer> currentDate(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String strDate = simpleDateFormat.format(date);
		Vector<Integer> currentDate = new Vector<Integer>();
		String[] arrDate = strDate.split("-");
		for (String string : arrDate) {
			currentDate.add(Integer.parseInt(string));
		}
		
		return currentDate;
	}
	
	/**
	 * ȡ�õ�ǰϵͳʱ����ַ�����ʾ��ʽ��
	 * @param format	Ҫ���Ƶĸ�ʽ����������ɲο����ྲ̬����ֵ
	 * @return		���ݶ���Ҫ�󷵻ص�ǰϵͳʱ����ַ�����ʾ��ʽ
	 */
	public static String dateFormat(int format){
		SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfYMDHMS = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
		SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss");
		
		Date date = new Date();
		String strYMD = sdfYMD.format(date);
		String strYMDHMS = sdfYMDHMS.format(date);
		String strHMS = sdfHMS.format(date);
		
		if(format == 1){
			return strYMD;
		}else if(format == 2){
			return strYMDHMS;
		}else if(format == 3){
			return strHMS;
		}
		
		return null;
	}

}
