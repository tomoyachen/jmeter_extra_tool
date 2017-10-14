package com.tomoya.jmeter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用于Jmeter提供的测试工具类
 * 
 * @author tomoya
 *
 */
public class Tool {

	private Tool() {
	};

	/**
	 * 返回数据类型
	 * 
	 * @param obj
	 *            对象
	 * @return 数据类型
	 */
	public static String getType(Object obj) {
		return obj.getClass().getName();
	}

	/**
	 * 返回hashCode的绝对值
	 * 
	 * @param str
	 *            字符串
	 * @return hashCode的绝对值
	 */
	public static int getHashCode(String str) {
		return Math.abs(str.hashCode());
	}

	/**
	 * 返回32位小写md5值
	 * 
	 * @param str
	 *            字符串
	 * @return MD5值
	 */
	public static String getMD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			// UTF-8编译，否则影响中文生成MD5
			md5.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte b[] = md5.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}

		return buf.toString();
	}

	/**
	 * 返回32位大写md5值
	 * 
	 * @param str
	 *            字符串
	 * @return MD5值
	 */
	public static String getMD5Cap(String str) {
		return getMD5(str).toUpperCase();
	}

	/**
	 * 获取2~4个字的中文名，常用字版（名字为五个笔画以内）
	 * 
	 * @return 中文名
	 */
	public static String getChineseName() {
		Random random = new Random(System.currentTimeMillis());

		String[] Surname = { "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "宇文" };

		int index = random.nextInt(Surname.length);
		String name = Surname[index];

		if (random.nextBoolean()) {
			name = name + getChineseWord() + getChineseWord();
		} else {
			name = name + getChineseWord();
		}

		return name;
	}

	/**
	 * 获取单个常用汉字
	 * 
	 * @return 单个常用汉字
	 */
	private static String getChineseWord() {
		Random random = new Random();
		String[] firstName = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "丰", "王", "井", "开", "夫", "天", "无", "元",
				"专", "云", "扎", "艺", "木", "五", "支", "厅", "不", "太", "犬", "区", "历", "尤", "友", "匹", "车", "巨", "牙", "屯", "比",
				"互", "切", "瓦", "止", "少", "日", "中", "冈", "贝", "内", "水", "见", "午", "牛", "手", "毛", "气", "升", "长", "仁", "什",
				"片", "仆", "化", "仇", "币", "仍", "仅", "斤", "爪", "反", "介", "父", "从", "今", "凶", "分", "乏", "公", "仓", "月", "氏",
				"勿", "欠", "风", "丹", "匀", "乌", "凤", "勾", "文", "六", "方", "火", "为", "斗", "忆", "订", "计", "户", "认", "心", "尺",
				"引", "丑", "巴", "孔", "队", "办", "以", "允", "予", "劝", "双", "书", "幻", "千", "万", "亿", "星", "光" };

		int index = random.nextInt(firstName.length);
		return firstName[index];

	}

	/**
	 * 将字符串写入文件
	 * 
	 * @param str
	 *            字符串
	 * @param filePath
	 *            文件路径
	 */
	public static void writeFile(String str, String filePath) {

		// 写入文件.
		String WRITE_TO_FILE = str;
		String FILE_PATH = filePath;

		try {
			File file = new File(FILE_PATH);
			if (!file.exists()) {
				file.createNewFile();
			}

			boolean isExist = false;

			if (!isExist) {
				FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile(), false);
				fos.write(WRITE_TO_FILE.getBytes());
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 将Jmeter的JDBC Result中指定列，转换成单列多行的字符串
	 * 
	 * @param list
	 *            JDBC返回的ArrayList集
	 * @param key
	 *            JDBC返回的字段名
	 * @return 单列多行的字符串
	 */
	public static String JDBCResultToString(ArrayList list, String key) {

		// 接受数据库的JSON结果
		ArrayList DBResultList = list;

		// 拼接字符串
		String DBResultToStr = "";

		for (int i = 0; i < DBResultList.size(); i++) {
			if (DBResultList.get(i) instanceof HashMap) {
				DBResultToStr = DBResultToStr + ((HashMap) DBResultList.get(i)).get(key);
			} else {
				DBResultToStr = DBResultToStr + ((String) DBResultList.get(i));
			}

			if (i + 1 < DBResultList.size()) {
				DBResultToStr = DBResultToStr + "\r\n";
			}
		}
		return DBResultToStr;

	}

	/**
	 * 将Jmeter的JDBC Result中指定列，写入文件
	 * 
	 * @param list
	 *            JDBC返回的ArrayList集
	 * @param key
	 *            JDBC返回的字段名
	 * @param filePath
	 *            文件路径
	 */
	public static void JDBCResultWriteFile(ArrayList list, String key, String filePath) {
		String str = JDBCResultToString(list, key);
		writeFile(str, filePath);

	}
	
	/**
	 * 将Jmeter的JDBC Result中指定多列，转换成多列多行的字符串
	 * @param list JDBC返回的ArrayList集
	 * @param keyArray JDBC返回的字段名数组
	 * @return 多列多行的字符串
	 */
	public static String JDBCResultToString(ArrayList list, String[] keyArray) {

		// 接受数据库的JSON结果
		ArrayList DBResultList = list;

		// 拼接字符串
		String DBResultToStr = "";

		for (int i = 0; i < DBResultList.size(); i++) {
			String key = "";
			for (int x = 0; x < keyArray.length; x++) {
				if (DBResultList.get(i) instanceof HashMap) {
					key = key + ((HashMap) DBResultList.get(i)).get(keyArray[x]);
				} else {
					key = key + ((String) DBResultList.get(i));
				}
				if (x + 1 < keyArray.length) {
					key = key + ",";
				}

			}
			DBResultToStr = DBResultToStr + key;
			if (i + 1 < DBResultList.size()) {
				DBResultToStr = DBResultToStr + "\r\n";
			}
		}
		return DBResultToStr;

	}
	
	/**
	 * 将Jmeter的JDBC Result中指定多列，写入文件
	 * 
	 * @param list
	 *            JDBC返回的ArrayList集
	 * @param keyArray
	 *            JDBC返回的字段名数组
	 * @param filePath
	 *            文件路径
	 */
	public static void JDBCResultWriteFile(ArrayList list, String[] keyArray, String filePath) {
		String str = JDBCResultToString(list, keyArray);
		writeFile(str, filePath);

	}

}
