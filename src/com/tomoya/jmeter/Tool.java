package com.tomoya.jmeter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
	 * @param isCap
	 *            是否大小写
	 * 
	 * @return MD5值
	 */
	public static String getMD5(String str, boolean isCap) {
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

		if (isCap == true) {
			return buf.toString().toUpperCase();
		} else {
			return buf.toString();
		}
	}

	/**
	 * 返回32位大写md5值
	 * 
	 * @param str
	 *            字符串
	 * @return MD5值
	 */
	public static String getMD5(String str) {
		return getMD5(str, true);
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
	 * 将字符串写入文件（覆盖文本）
	 * 
	 * @param str
	 *            字符串
	 * @param filePath
	 *            文件路径
	 * @throws Exception
	 *             抛出异常
	 */
	public static void writeFile(String str, String filePath) throws Exception {

		writeFile(str, filePath, false);

	}

	/**
	 * 将字符串写入文件
	 * 
	 * @param str
	 *            字符串
	 * @param filePath
	 *            文件路径
	 * @param keep
	 *            是否继续写入 flase 覆盖, ture 增量
	 * @throws Exception
	 *             抛出异常
	 */
	public static void writeFile(String str, String filePath, boolean keep) throws Exception {

		// 文件路径的文件夹不存在时，创建文件夹
		createNewFile(filePath);

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
				FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile(), keep);
				fos.write(WRITE_TO_FILE.getBytes());
				fos.write("\r\n".getBytes());
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
	 * @throws Exception
	 *             抛出异常
	 */
	public static void JDBCResultWriteFile(ArrayList list, String key, String filePath) throws Exception {

		String str = JDBCResultToString(list, key);
		writeFile(str, filePath);

	}

	/**
	 * 将Jmeter的JDBC Result中指定多列，转换成多列多行的字符串
	 * 
	 * @param list
	 *            JDBC返回的ArrayList集
	 * @param keyArray
	 *            JDBC返回的字段名数组
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
	 * @throws Exception
	 *             抛出异常
	 */
	public static void JDBCResultWriteFile(ArrayList list, String[] keyArray, String filePath) throws Exception {

		String str = JDBCResultToString(list, keyArray);
		writeFile(str, filePath);

	}

	/**
	 * 
	 * @param type
	 *            日期类型 -1代表昨日 0代表今日 1代表明日
	 * @return 返回日期 默认格式为yyyy-MM-dd
	 */
	public static String getDate(int type) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, type);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		return date;
	}

	/**
	 * 
	 * @param type
	 *            日期类型 -1代表昨日 0代表今日 1代表明日
	 * @param format
	 *            日期格式
	 * @return 返回日期
	 */
	public static String getDate(int type, String format) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, type);
		String date = new SimpleDateFormat(format).format(cal.getTime());
		return date;
	}

	/**
	 * 
	 * @param fileDir
	 *            文件路径
	 * @throws Exception
	 *             抛出异常
	 */
	public static void createNewFile(String fileDir) throws Exception {
		// 文件路径的文件夹不存在时，创建文件夹
		File file = new File(fileDir);
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		file.createNewFile();
	}

	/**
	 * 生成'value1', 'value2'...用于sql语句的in条件
	 * 
	 * @param list
	 *            JDBC返回的ArrayList集
	 * @param key
	 *            JDBC返回的字段名
	 * @return 'value1', 'value2', 'value3' ...
	 */
	public static String JDBCResultToSql(ArrayList list, String key) {

		// 接受数据库的JSON结果
		ArrayList DBResultList = list;

		// 拼接字符串
		String DBResultToStr = "";

		for (int i = 0; i < DBResultList.size(); i++) {
			if (DBResultList.get(i) instanceof HashMap) {
				DBResultToStr = DBResultToStr + "'" + ((HashMap) DBResultList.get(i)).get(key) + "'";
			} else {
				DBResultToStr = DBResultToStr + "'" + ((String) DBResultList.get(i)) + "'";
			}

			if (i + 1 < DBResultList.size()) {
				DBResultToStr = DBResultToStr + ",";
			}
		}
		return DBResultToStr;

	}

	/**
	 * 
	 * @param start
	 *            最小范围
	 * @param end
	 *            最大范围
	 * @return 随机数
	 */
	private static int getNum(int start, int end) {
		return (int) (Math.random() * (end - start + 1) + start);
	}

	/**
	 * 
	 * @return 返回11位手机号码
	 */
	public static String getMobile() {
		String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

		int index = getNum(0, telFirst.length - 1);
		String first = telFirst[index];
		String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
		String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
		return first + second + third;
	}

	/**
	 * 返回Email
	 * 
	 * @param Min
	 *            最小长度
	 * @param Max
	 *            最大长度
	 * @return 返回指定范围的邮箱帐号
	 */
	public static String getEmail(int Min, int Max) {
		final String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		final String[] email_suffix = "@qq.com,@163.com,@gmail.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");

		int length = getNum(Min, Max);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = (int) (Math.random() * base.length());
			sb.append(base.charAt(number));
		}
		sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
		return sb.toString();
	}

	/**
	 * 正则表达式返回所有匹配项的整个字符串
	 * 
	 * @param regex
	 *            正则表达式
	 * @param source
	 *            来源文本
	 * @return 匹配的所有项 默认mode0
	 */
	public static List<String> getRegexResult(String regex, String source) {
		return getRegexResult(regex, source, 0);
	}

	/**
	 * 正则表达式返回所有匹配项 mode0为整个字符串，mode1为第1个括号内容，mode2为第2个括号内容...
	 * 
	 * @param regex
	 *            正则表达式
	 * @param source
	 *            来源文本
	 * @param mode
	 *            模式 0完整 1第一个括号内容 2第二个括号内容
	 * @return 匹配的所有项
	 */
	public static List<String> getRegexResult(String regex, String source, int mode) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group(mode));
		}
		return list;
	}

	/**
	 * 判断是否包含特殊字符
	 * 
	 * @param str
	 *            传入字符串
	 * @return true包含, false不包含
	 */
	public static boolean isSpecialChar(String str) {
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	// 桌面路径
	public static final String DESKTOP = FileSystemView.getFileSystemView().getHomeDirectory().getPath();
	// 程序运行路径，jmeter调用时为jmeter安装路径/bin
	public static final String JMETER_HOME = System.getProperty("user.dir");

	/**
	 * 判断一个字符串中，是否包含另一个字符串。
	 * 
	 * @param Source
	 *            源字符串 例aabca
	 * @param str
	 *            判断字符串 例abc
	 * @return true包含 false不包含
	 */
	public static boolean ifInclude(String Source, String str) {
		boolean ifInclude = false;
		if (Source.indexOf(str) != -1) {
			ifInclude = true;
		} else {
			ifInclude = false;
		}
		return ifInclude;
	}

	/**
	 * 日期天数计算
	 * 
	 * @param initDate
	 *            初始日期
	 * @param number
	 *            加多少天
	 * @return 最终日期
	 */
	public static String dateJiSuan(String initDate, int number) {
		return dateJiSuan(initDate, "yyyy-MM-dd HH:mm:ss", number);
	}

	/**
	 * 日期天数计算
	 * 
	 * @param initDate
	 *            初始日期
	 * @param dateFormat
	 *            日期格式
	 * @param number
	 *            加多少天
	 * @return 最终日期
	 */
	public static String dateJiSuan(String initDate, String dateFormat, int number) {
		// String initDate = "2017-12-01 00:00:01";
		String finalDate = null;
		// 页面传递到后台的时间 为String类型
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date tmpDate = null;
		try {
			tmpDate = sdf.parse(initDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 要实现日期+1 需要String转成Date类型
		Format f = new SimpleDateFormat(dateFormat);
		Calendar c = Calendar.getInstance();
		c.setTime(tmpDate);
		// 利用Calendar 实现 Date日期+1天
		c.add(Calendar.DAY_OF_MONTH, number);
		tmpDate = c.getTime();
		SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat);
		// 将日期转成String类型 方便进入数据库比较
		finalDate = sdf1.format(tmpDate);
		return finalDate;
	}

	/**
	 * 生成文件的16位MD5密文（大写）
	 * 
	 * @param dirPath
	 *            文件路径
	 * @return 16位MD5密文
	 */
	public static String getFileMD5(String dirPath) {
		return getFileMD5(dirPath, true);
	}

	/**
	 * 生成文件的16位MD5密文
	 * 
	 * @param dirPath
	 *            文件路径
	 * @param cap
	 *            是否大写
	 * @return 16位MD5密文
	 */
	public static String getFileMD5(String dirPath, boolean cap) {
		String md5 = null;
		try {
			md5 = DigestUtils.md5Hex(new FileInputStream(dirPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cap == true) {
			return md5.toUpperCase();
		} else {
			return md5;
		}

	}

}
