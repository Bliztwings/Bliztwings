package com.ehyf.ewashing.util;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串操作工具类. <br>
 * @author chenxiaozhong
 * @version 1.0.0 2014-1-7 上午9:33:37 <br>
 * @see 
 * @since JDK 1.6.0
 */
public final class StringUtils {

    public static final String IMG ="<\\s*?img\\s*?src=\"?[^>\"]*\"?/>";
    public static final String A ="<*?a[^>]*href=[\"\"|\']?([^>]+)[\"\"|\']?[^>]*>";
    public static final String A1 ="</a>";
    public static final String nbsp ="&nbsp";
    public static final String P ="<p>";
    public static final String P1 ="</p>";
    public static final String BR ="<br/>";
    
	private StringUtils() {

	}

	/**
	 * 将一个字串的首字母大写.
	 * @param s String 源字串
	 * @return String 首字母大写后的字串
	 */
	public static String toUpperCaseFirstLetter(final String s) {
		if (s == null || s.length() < 1) {
			return "";
		}

		final char[] arrC = s.toCharArray();
		arrC[0] = Character.toUpperCase(arrC[0]);
		return String.copyValueOf(arrC);
	}

	/**
	 * 错误字符.
	 * @param s
	 * @return String
	 */
	public static String escapeErrorChar(final String s) {
		String s1 = null;
		s1 = s;
		if (s1 == null) {
			return s1;
		} else {
			s1 = replace(s1, "\\", "\\\\");
			s1 = replace(s1, "\"", "\\\"");
			return s1;
		}
	}

	/**
	 * HTML标签.
	 * @param s
	 * @return
	 */
	public static String escapeHTMLTags(final String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		final StringBuffer stringbuffer = new StringBuffer(s.length());
		// byte byte0 = 32;
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '<') {
				stringbuffer.append("&lt;");
			} else if (c == '>') {
				stringbuffer.append("&gt;");
			} else {
				stringbuffer.append(c);
			}
		}

		return stringbuffer.toString();
	}

	/**
	 * 字符串替换.
	 * @param s 搜索字符串
	 * @param s1 要查找字符串
	 * @param s2 要替换字符串
	 * @return
	 */
	public static String replace(final String s, final String s1, final String s2) {
		if (s == null)
			return null;
		int i = 0;
		if ((i = s.indexOf(s1, i)) >= 0) {
			char ac[] = s.toCharArray();
			char ac1[] = s2.toCharArray();
			int j = s1.length();
			StringBuffer stringbuffer = new StringBuffer(ac.length);
			stringbuffer.append(ac, 0, i).append(ac1);
			i += j;
			int k;
			for (k = i; (i = s.indexOf(s1, i)) > 0; k = i) {
				stringbuffer.append(ac, k, i - k).append(ac1);
				i += j;
			}

			stringbuffer.append(ac, k, ac.length - k);
			return stringbuffer.toString();
		} else {
			return s;
		}
	}

	/**
	 * 替换字符串.
	 * @param s
	 * @param s1
	 * @param s2
	 * @param ai
	 * @return
	 */
	public static String replace(final String s, final String s1, final String s2, int ai[]) {
		if (s == null)
			return null;
		int i = 0;
		if ((i = s.indexOf(s1, i)) >= 0) {
			int j = 0;
			j++;
			char ac[] = s.toCharArray();
			char ac1[] = s2.toCharArray();
			int k = s1.length();
			StringBuffer stringbuffer = new StringBuffer(ac.length);
			stringbuffer.append(ac, 0, i).append(ac1);
			i += k;
			int l;
			for (l = i; (i = s.indexOf(s1, i)) > 0; l = i) {
				j++;
				stringbuffer.append(ac, l, i - l).append(ac1);
				i += k;
			}

			stringbuffer.append(ac, l, ac.length - l);
			ai[0] = j;
			return stringbuffer.toString();
		} else {
			return s;
		}
	}

	/**
	 * 判断此字符串是否为空、空字符串，或"null".
	 * @param str
	 * @return
	 */
	public static boolean isNullStr(final String s) {
		return (s == null || s.equals("null") || s.equals("")) ? true : false;
	}

	/**
	 * 判断此字符串是否为空、空字符串，或"null"
	 * @param str
	 * @return
	 */
	public static final boolean isNullStr(Object o) {
		return (o == null || o.toString().equals("null") || o.toString().equals("")) ? true : false;
	}

	/**
	 * 如果字符串str为空则转换为str1
	 * @param str
	 * @param str1
	 * @return
	 */
	public static String getNullStr(final String str, final String str1) {
		if (isNullStr(str))
			return str1;
		else
			return str;
	}

	/**
	 * 此方法将给出的字符串source使用delim划分为单词数组.
	 * @param source 需要进行划分的原字符串
	 * @param delim 单词的分隔字符串
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组， 如果delim为null则使用逗号作为分隔字符串.
	 */
	public static String[] split(final String source, String delim) {
		String[] wordLists;
		if (source == null) {
			wordLists = new String[1];
			wordLists[0] = source;
			return wordLists;
		}
		if (delim == null) {
			delim = ",";
		}
		StringTokenizer st = new StringTokenizer(source, delim);
		int total = st.countTokens();
		wordLists = new String[total];
		for (int i = 0; i < total; i++) {
			wordLists[i] = st.nextToken();
		}
		return wordLists;
	}

	/**
	 * 此方法将给出的字符串source使用delim划分为单词数组.
	 * @param source 需要进行划分的原字符串
	 * @param delim 单词的分隔字符
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组.
	 */
	public static String[] split(final String source, char delim) {
		return split(source, String.valueOf(delim));
	}

	/**
	 * 此方法将给出的字符串source使用逗号划分为单词数组.
	 * @param source 需要进行划分的原字符串
	 * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组.
	 */
	public static String[] split(final String source) {
		return split(source, ",");
	}

	/**
	 * 字符串数组中是否包含指定的字符串.
	 * @param strings 字符串数组
	 * @param string 字符串
	 * @param caseSensitive 是否大小写敏感
	 * @return 包含时返回true，否则返回false
	 */
	public static final boolean contains(final String[] strings, final String string, boolean caseSensitive) {
		for (int i = 0; i < strings.length; i++) {
			if (caseSensitive == true) {
				if (strings[i].equals(string)) {
					return true;
				}
			} else {
				if (strings[i].equalsIgnoreCase(string)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 字符串中是否包含另一个指定的字符串.
	 * @param source 源字符串
	 * @param target 目标字符串
	 * @return boolean 是否包含
	 */
	public static boolean contains(final String source, final String target) {
		return source.indexOf(target) != -1;
	}

	/**
	 * 字符串数组中是否包含指定的字符串.大小写敏感.
	 * @param strings 字符串数组
	 * @param string 字符串
	 * @return 包含时返回true，否则返回false
	 */
	public static boolean contains(final String[] strings, final String string) {
		return contains(strings, string, true);
	}

	/**
	 * 不区分大小写判定字符串数组中是否包含指定的字符串.
	 * @param strings 字符串数组
	 * @param string 字符串
	 * @return 包含时返回true，否则返回false
	 */
	public static boolean containsIgnoreCase(final String[] strings, final String string) {
		return contains(strings, string, false);
	}

	/**
	 * 得到字符串的字节长度.
	 * @param source 字符串
	 * @return 字符串的字节长度
	 */
	public static int getByteLength(final String source) {
		int len = 0;
		for (int i = 0; i < source.length(); i++) {
			char c = source.charAt(i);
			int highByte = c >>> 8;
			len += highByte == 0 ? 1 : 2;
		}
		return len;
	}

	/**
	 * 判断字符是否为双字节字符，如中文.
	 * @param c char
	 * @return boolean
	 */
	public static boolean isDoubleByte(final char c) {
		return !((c >>> 8) == 0);
	}

	/**
	 * 输出固定字节长度的字符串.
	 * @param source String
	 * @param len int
	 * @param exChar String
	 * @param exStr String
	 * @return String
	 */
	public static String getSubStr(final String source, int len, final String exChar, String exStr) {
		if (source == null || getByteLength(source) <= len) {
			return source;
		}
		StringBuffer result = new StringBuffer();
		char c = '\u0000';
		int i = 0, j = 0;
		for (; i < len; j++) {
			result.append(c);
			c = source.charAt(j);
			i += isDoubleByte(c) ? 2 : 1;
		}
		/**
		 * 到这里i有两种情况：等于len或是len+1，如果是len+1. 说明是双字节，并多出一个字节 这时候就只能append(exChar)，否则就append(c).
		 */
		if (i > len) {
			result.append(exChar);
		} else {
			result.append(c);
		}
		result.append(exStr);

		return result.toString();
	}

	public static String getSubStr(final String source, int len) {
		return getSubStr(source, len, ".", "...");
	}

	/**
	 * 判断输入参数是否为null返回一个非null的值.
	 * @param s String 判断的值
	 * @return String
	 */
	public static String valueOf (final String s) {
		return (s == null) ? "" : s.trim();
	}

	/**
	 * 判断输入参数是否为null返回一个非null的值.
	 * @param s String 判断的值
	 * @return String
	 */
	public static String valueOf(Object o) {
		if (o == null)
			return "";

		return String.valueOf(o).trim();
	}

	/**
	 * Valueof.
	 * @Title: valueOf
	 * @param @param i
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String valueOf(final int i) {
		return String.valueOf(i);
	}

 

	/**
	 * 属性添加标识符. 例如：金额 =>$[金额]
	 * @param sourceStr 源数据
	 * @return 属性字符
	 */
	public static String propDrapeOn(final String sourceStr) {
		final StringBuffer sb = new StringBuffer();
		sb.append("$[").append(sourceStr).append("]");
		return sb.toString();
	}

	/**
	 * 实体添加标识符. 例如：凭证 =>${凭证}
	 * @param sourceStr 源数据
	 * @return 属性字符
	 */
	public static String entityDrapeOn(final String sourceStr) {
		final StringBuffer sb = new StringBuffer();
		sb.append("${").append(sourceStr).append("}");
		return sb.toString();
	}

	/**
	 * 读取控制台的输入参数.
	 * @param prompt
	 * @return
	 */
	public static String readConsoleString(String prompt) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str = null;
		try {
			str = br.readLine();
		} catch (IOException e) {
		}
		return str;
	}

	/**
	 * 方法的作用.
	 * @param str
	 * @return boolean
	 * @throws
	 */
	public static boolean isEmptyString(final String str) {
		return str == null || str.length() < 1;
	}

	/**
	 * 方法的作用.
	 * @param str
	 * @return boolean
	 * @throws
	 */
	public static boolean isNotEmptyString(final String str) {
		return str != null && str.length() > 0;
	}

	/**
	 * 方法的作用.
	 * @param patterns
	 * @param arguments
	 * @return String[]
	 * @throws
	 */
	public static String[] formatStrings(final String[] patterns, final Object[] arguments) {
		final String[] results = new String[patterns.length];
		for (int i = 0; i < patterns.length; i++) {
			results[i] = MessageFormat.format(patterns[i], arguments);
		}
		return results;
	}

	/**
	 * 方法的作用.
	 * @param value
	 * @return String
	 * @throws
	 */
	public static String trim(final String value) {
		if (value == null) {
			return null;
		} else {
			return value.trim();
		}
	}

	// 如 \<data>&abc<data>转换为<data>abc<data>.
	/**
	 * 转换XML，将XML字符串去掉转义字符&.
	 * @param value 字符串.
	 * @return 去掉转义字符&的字符串.
	 */
	public static String switchXml(final String value) {
		if (value == null) {
			return null;
		}

		return value.replaceAll("&", "&amp;");
	}

	/**
	 * 转换字符串为XML节点值.
	 * @param value 字符串.
	 * @return XML节点值
	 */
	public static String switchStringToXml(final String value) {
		if (value == null) {
			return "";
		}

		return value.replaceAll("<", "").replaceAll(">", "").replaceAll("\'", "&apos;").replaceAll("\"", "&quot;")
				.replaceAll("&", "&amp;");
	}

	/**
	 * 按一定长度截取字符串.
	 * @param text 文本.
	 * @param length 截取长度.
	 * @return 截取后的文本.
	 */
	public static String cutStringByLength(final String text, final int length) {
		if (text == null) {
			return null;
		}
		if (text.length() > length) {
			return text.substring(0, length);
		} else {
			return text;
		}
	}

	/**
	 * 是否为空字符串或NULL. 如str = "    ", str = null 返回true
	 * @param str 字符串
	 * @return 是否为空字符串
	 */
	public static boolean isNullOrEmpty(final String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * 是否为空对象，如：null或" ".<br>
	 * @author chenxiangbai 2012-10-23 上午8:42:38 <br> 
	 * @param obj Object
	 * @return 是否为空对象.
	 */
	public static boolean isNullOrEmpty(final Object obj) {
		return obj == null || obj.toString().trim().length() == 0;
	} 
	
	/**
	 * 判断obj 是否为空
	 * @param obj
	 * @return
	 */
	public static String isNullOrEmptyNew(final Object obj){
		
		return obj ==null ? "" :obj.toString();
	}
	
	
	/**
	 * 去掉字符串末尾的符号
	 * @param value
	 * @return
	 */
	public static  String trimStr(String value,String spit){
		if(value.lastIndexOf(spit) !=-1){
			return value.substring(0,value.lastIndexOf(spit));
		}
		return value;
	}
	
	/**
     * 去掉指定的东西
     */
    public static String replaceAll(String context , String ...regxs ){
        
        String info =context;
        if(StringUtils.isNotEmptyString(context)){
            
            if(regxs !=null &&  regxs.length >0){
                for (String regx :regxs){
                    info =info.replaceAll(regx, "");
                }
            }
            return info;
        }
        return info;
    }
	/**
	 * 是否含有数字 
	 * @param 单个字符串
	 * @return 是否含有数字
	 */
	public static boolean containNum(String s) {
		Pattern p = Pattern.compile("[0-9]+");
		Matcher result = p.matcher(s);
		return result.find();
	}
	
	public static String removeHtml(String htmlStr){
		 String regEx_html = "<[^>]+>";
		 Pattern p_style = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
	     Matcher m_style = p_style.matcher(htmlStr);  
	     htmlStr = m_style.replaceAll("");
		return htmlStr;
	}
	public static String removePicStr(String htmlStr){
		 String regEx_html = "\\[[^\\]]+\\]";
		 Pattern p_style = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
	     Matcher m_style = p_style.matcher(htmlStr);  
	     htmlStr = m_style.replaceAll("");
		return htmlStr;
	}
}
