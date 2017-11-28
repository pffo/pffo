package com.ffo.ipiker.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StringUtil {
	public static final String XML_TAG_AND = "\7";

	public static final String replace( String line, String oldString, String newString )
	{
		if (line == null) {
			return null;
		}
		int i=0;
		if ( ( i=line.indexOf( oldString, i ) ) >= 0 ) {
			char [] line2 = line.toCharArray();
			char [] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while( ( i=line.indexOf( oldString, i ) ) > 0 ) {
				buf.append(line2, j, i-j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	public static final boolean isEmptyString(String s){
		if(s==null) return true;
		if("".equals(s.trim())) return true;
		return false;
	}

	public static final String convertNumberToString(Number value) {
		DecimalFormat formatter = new DecimalFormat();
		formatter.setGroupingUsed(false);
		String result = formatter.format(value.doubleValue());
		return result;
	}
	/**
	 * 将String压缩
	 * example:com.jbbis.aic.alteration.entity.AlterRequisition 中
	 * integrateOpinion,getIntegrateOpinionInBytes(),setIntegrateOpinionInBytes
	 */
	public static byte[] zipString(String s){
		if(s==null) return null;
		try {
			byte[] bytes = s.getBytes("GBK");
			BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(bytes));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(baos));
			byte[] buffer = new byte[1024];
			int len = 0;
			while( (len=in.read(buffer)) != -1){
				out.write(buffer,0,len);
			}
			in.close();
			out.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 解压String
	 */
	public static String unzipString(byte[] bytes){
		if(bytes==null||bytes.length==0) return null;
		try {
			BufferedInputStream in = new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedOutputStream out = new BufferedOutputStream(baos);
			byte[] buffer = new byte[1024];
			int len = 0;
			while( (len=in.read(buffer)) != -1){
				out.write(buffer,0,len);
			}
			in.close();
			out.close();
			bytes = baos.toByteArray();
			return new String(bytes,"GBK");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String stringToHtmlFormat(String s){
		if(s==null) return null;
		String temp = replace(s,"\n","<br>");
		temp = replace(temp,"\t","&nbsp;&nbsp;&nbsp;&nbsp;");
		return replace(temp," ","&nbsp;");
	}

	/**
	 * 对字符串进行空替换，即：如果传入非空字符串，返回原字符串，如果是空(null)，返回空字符串("")
	 * @param s
	 * @return
	 * @author shenyf
	 */
	public static String toStringNullReplaced(String s){
		return s==null?"":s;
	}

	/**
	 * 对l进行字符串转换，并进行空替换，即：如果传入非空l，返回字符串表示的l值，如果是空(null)，返回空字符串("")
	 * @param l
	 * @return
	 * @author shenyf
	 */
	public static String toStringNullReplaced(Long l){
		return l==null?"":l.toString();
	}

	/**
	 * 对i进行字符串转换，并进行空替换，即：如果传入非空i，返回字符串表示的i值，如果是空(null)，返回空字符串("")
	 * @param i
	 * @return
	 * @author shenyf
	 */
	public static String toStringNullReplaced(Integer i){
		return i==null?"":i.toString();
	}

	public static String[] split(String origin, String limit) {
		List container = new ArrayList();
		String org = new String(origin);
		while (org.indexOf(limit) >= 0) {
			String str = org.substring(0, org.indexOf(limit));
			if(str.length() >=0) {
				container.add(str);
			}
			org = org.substring(org.indexOf(limit) + limit.length());
		}
		if(org.length() > 0) {
			container.add(org);
		}
		String[] result = new String[container.size()];
		container.toArray(result);
		return result;
	}

	/**
	 * 将字符串数组根据规则拼成字符串，如{"abc","def","ghi"}转换成'abc','def','ghi'只需调用toString(arr,"'",",")
	 * @param _arr	字符串
	 * @param _wrapper 字符串两边的包裹字符串
	 * @param _separator 分隔符
	 * @return 如果数组为空或长度为0，返回null
	 */
	public static String toString(String[] _arr,String _wrapper,String _separator){
		if(_arr==null || _arr.length<=0){
			return null;
		}

		return toString(_arr, 0, _arr.length, _wrapper, _separator);
	}

	/**
	 * 将字符串数组根据规则拼成字符串，如{"abc","def","ghi"}转换成'abc','def','ghi'只需调用toString(arr,0,3,"'",",")
	 * @param _arr  字符串
	 * @param _fromIndex 第一个元素(包含)的下标
	 * @param _toIndex 最后一个元素(不包含)的下标。若 _toIndex > _arr.length，则相当于 _toIndex = _arr.length
	 * @param _wrapper 字符串两边的包裹字符串
	 * @param _separator 分隔符
	 * @return 如果数组为空或长度为0，返回null
	 */
	public static String toString(String[] _arr, int _fromIndex, int _toIndex, String _wrapper,String _separator){
		if(_arr==null || _arr.length<=0){
			return null;
		}

		StringBuffer strBuff = new StringBuffer();
		for(int i = _fromIndex;i < _arr.length && i < _toIndex;i++){
			strBuff.append(_wrapper+_arr[i]+_wrapper+_separator);
		}
		if (strBuff.length() > 0) {
			strBuff.deleteCharAt(strBuff.length() - 1);
		}
		return strBuff.toString();
	}

	/**
	 * 在_arr中查找匹配的字符串
	 * @param _tobeFound 匹配字符串
	 * @param _arr
	 * @return 如果找到，返回true，否则返回false
	 */
	public static boolean findIn(String _tobeFound,String[] _arr){
		if(_arr==null || _arr.length<=0){
			return false;
		}

		for(int i=0;i<_arr.length;i++){
			if(_arr[i].equals(_tobeFound)){
				return true;
			}
		}

		return false;
	}

	/**
	 * 将一个Set转换成数组。Set中的元素为String型
	 * @param _set
	 * @return
	 */
	public static String[] toArray(Set _set){
		if(_set==null || _set.size()<=0){
			return null;
		}

		String[] arr=new String[_set.size()];
		int i=0;
		for(Iterator itr=_set.iterator();itr.hasNext();){
			arr[i++]=(String)itr.next();
		}

		return arr;
	}

	/**
	 * 去除字符串中的半角或全角空格
	 * @param s 需要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String trim(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}

		StringBuffer sb = new StringBuffer(s);
		for (int i = sb.length() - 1; i >= 0; i--) {
			char c = sb.charAt(i);
			if (c == ' ' || c == '　' || c == '\t' || c == '\r' || c == '\n') {
				sb.deleteCharAt(i);
			}
		}

		return sb.toString();
	}

	/**
	 * 6：小数的核对。
	 *
	 * @param  field        小数
	 * @return boolean      OK:true、NG:false
	 * @throws null
	 */
	public static boolean isFolat(String field)
	{
		int dotCount = 0;
		int plusCount = 0;
		int discountCount = 0;

		for (int i = 0; i < field.length(); i++)
		{
			if (field.charAt(i) >= '0' && field.charAt(i) <= '9')
			{
			}
			else if (field.charAt(i) == '.')
			{
				dotCount++;
			}
			else if (field.charAt(i) == '-')
			{
				discountCount++;
			}
			else if (field.charAt(i) == '+')
			{
				plusCount++;
			}
			else
			{
				return false;
			}
		}

		if (dotCount > 1 || plusCount > 1 || discountCount > 1)
		{
			return false;
		}

		if (field.indexOf("-") > 0)
		{
			return false;
		}
		if (field.indexOf("+") > 0)
		{
			return false;
		}

		Float decimal;
		try
		{
			decimal = Float.valueOf(field);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	/**
	 * 7：整数的核对。
	 *
	 * @param  field                                整数
	 * @return boolean                             OK:true、NG:false
	 * @throws null
	 */
	public static boolean isInt(String field)
	{

		try
		{
			Short.parseShort(field);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	/**
	 * 7：整数的核对。
	 *
	 * @param  field                                整数
	 * @return boolean                             OK:true、NG:false
	 * @throws null
	 */
	public static boolean isLong(String field)
	{

		try
		{
			Long.parseLong(field);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	/**
	 * 1：数字的核对。
	 *
	 * @param  field                               数字
	 * @return boolean                            OK:true、NG:false
	 * @throws null
	 */
	public static boolean isDigital(String field)
	{
		int MAXLENGTH = 256;
		if (field.getBytes().length > MAXLENGTH)
		{
			return false;
		}

		for (int i = 0; i < field.length(); i++)
		{
			if (field.charAt(i) < '0' || field.charAt(i) > '9')
			{
				return false;
			}
		}
		return true;
	}

	public static String getUnicode(String s)
	{
		if (s == null || s.length() == 0)
			return "";
		byte abyte0[] = new byte[s.length()];
		for (int i = 0; i < s.length(); i++)
			abyte0[i] = (byte) s.charAt(i);

		return new String(abyte0);
	}

	public static String getAscii(String s)
	{
		if (s == null || s.length() == 0)
		{
			return "";
		}

		char[] buffer = new char[s.length() * 2];

		char c;
		int j = 0;
		for (int i = 0; i < s.length(); i++)
		{
			if (s.charAt(i) >= 0x100)
			{
				c = s.charAt(i);
				byte[] buf = ("" + c).getBytes();
				buffer[j++] = (char) buf[0];
				buffer[j++] = (char) buf[1];
			}
			else
			{
				buffer[j++] = s.charAt(i);
			}
		}

		return new String(buffer, 0, j);
	}

	public static String ToString(Object o)
	{
		if (o == null)
		{
			return "";
		}
		else
		{
			return o.toString();
		}
	}

	/**
	 * 产生定长的随机字符串,没有设置seed
	 *
	 * @param length 随机字符串的长度
	 * @return 随机字符串
	 */
	public static String randomString(int length)
	{
		Random random = new Random();
		StringBuffer buffer = new StringBuffer(length);
		int iRandom1;
		int iRandom2;
		for (int i = 0; i < length; i++)
		{
			iRandom1 = random.nextInt(4);
			if (iRandom1 != 3)
			{
				iRandom2 = random.nextInt(25);
				buffer.append( (char) ('a' + iRandom2));
			}
			else
			{
				iRandom2 = random.nextInt(9);
				buffer.append( (char) ('0' + iRandom2));
			}
		}
		return buffer.toString();

	}

	/**
	 * 中文转换
	 * @param strT
	 * @return
	 */
	public static String toGBKString(String strT)
	{
		try
		{
			if (strT == null)
				return "";
			else
			{
				String os = System.getProperty("os.name");
				if (os != null && os.toLowerCase().startsWith("windows"))
				{
					strT = new String(strT.getBytes("ISO8859_1"), "GBK");
					System.out.println("~~~~Windows System");
				}else if (os != null && os.toLowerCase().startsWith("aix"))
				{
					System.out.println("~~~~AIX System");
				}
				else
				{
					strT = new String(strT.getBytes("ISO8859_1"), "GBK");
					System.out.println("~~~~Not Windows System");
				}
				return strT;
			}
		}
		catch (Exception e)
		{
			return "";
		}

	}

	public static String compositeString(String[] strAry, String split)
	{
		return compositeString(strAry,split,true);
	}

	public static String compositeString(String[] strAry, String split,boolean removeNull)
	{
		if (strAry == null || strAry.length == 0)
			return "";
		StringBuffer retBuffer = new StringBuffer();
		for (int i = 0; i < strAry.length; i++)
		{
			if (!removeNull || !isNull(strAry[i]))
			{
				if (i > 0) retBuffer.append(split);
				retBuffer.append(strAry[i]);
			}
		}
		return retBuffer.toString();
	}

	public static int splitSignCount(String strSplit,String sign){
		String endStr = strSplit.substring(strSplit.length() - sign.length(),
				strSplit.length());
		if (!endStr.equals(sign))
			strSplit += sign;

		StringTokenizer ST = new StringTokenizer(strSplit,sign);
		return ST.countTokens();
	}

	public static boolean isEmpty(String str)
	{
		boolean flag = false;
		if(str==null | "".equals(str))
		{
			flag = true;
		}
		return flag;
	}

	public static String replaceAll(String str,String sep_str,String rep_str)
	{
		if (str == null) return "";

		int idx = str.indexOf(sep_str);
		int sep_len = sep_str.length();

		StringBuffer strBuff = new StringBuffer();
		while (idx > -1)
		{
			strBuff.append(str.substring(0,idx));
			strBuff.append(rep_str);
			str = str.substring(idx+sep_len);
			idx = str.indexOf(sep_str);
		}
		strBuff.append(str);

		return strBuff.toString();
	}

	public static String[] split_StringTokenizer(String str,String sep_str)
	{
		StringTokenizer strToken = new StringTokenizer(str,sep_str);
		int tokenCount = strToken.countTokens();

		String[] str_list = new String[tokenCount];
		for (int i=0;i<tokenCount;i++)
		{
			str_list[i] = strToken.nextToken();
		}

		return str_list;
	}

	public static List split2List(String str,String sep_str)
	{
		List str_list;
		StringTokenizer strToken = new StringTokenizer(str,sep_str);
		int tokenCount = strToken.countTokens();

		String[] str_array = new String[tokenCount];
		for (int i=0;i<tokenCount;i++)
		{
			str_array[i] = strToken.nextToken();
		}
		str_list = Arrays.asList(str_array);
		return str_list;
	}

	/**
	 *	参数sepflag:是否去除最后一个为空的元素
	 */
	public static String[] split(String str,String sep_str,boolean sepflag)
	{
		int count = str.length() - replaceAll(str,sep_str,"").length() + 1;
		if (sepflag)
		{
			if (str.endsWith(sep_str))
			{
				count--;
			}
		}

		int sep_len = sep_str.length();
		String[] str_list = new String[count];

		for (int i=0;i<count;i++)
		{
			int idx = str.indexOf(sep_str);
			if (idx>-1)
			{
				str_list[i] = str.substring(0,idx);
				str = str.substring(idx+sep_len);
			}else
			{
				str_list[i] = str;
				str = "";
			}
		}

		return str_list;
	}

	public static String[] getMidStr(String str,String beg_str,String end_str)
	{
		if (str == null || "".equals(str))
			return null;
		if (beg_str == null ||"".equals(beg_str.trim()) || end_str == null || "".equals(end_str.trim()))
			return null;

		int strlen = str.length();

		int begstrlen = beg_str.length();
		int endstrlen = end_str.length();

		Vector vec = new Vector();

		int beg_pos = str.indexOf(beg_str);
		int end_pos = str.indexOf(end_str);

		while (str != null && beg_pos > -1 && end_pos > -1 && end_pos > beg_pos)
		{
			vec.add(str.substring(beg_pos+begstrlen,end_pos));
			str = str.substring(end_pos+endstrlen);

			if (str != null)
			{
				beg_pos = str.indexOf(beg_str);
				end_pos = str.indexOf(end_str);
			}else
			{
				beg_pos = -1;
				end_pos = -1;
			}
		}

		String[] array = new String[vec.size()];
		for (int i=0;i<vec.size();i++)
		{
			array[i] = (String)vec.get(i);
		}

		return array;
	}


	//Add 2005-04-08 by Zhang
	/**
	 * 判断字符串是否为空或空格
	 * @param 要检验的字符串
	 * @return boolean 是否为空
	 */
	public static boolean isNull(String param)
	{
		if (param==null || "".equals(param))
		{
			return true;
		}
		return false;
	}
	public static Date parseStringToDate(String timeString,String timeFormat)
	{
		SimpleDateFormat format = new SimpleDateFormat(timeFormat);
		Date timeDate = null;
		try
		{
			if (timeString != null && !"".equals(timeString))
			{
				timeDate = format.parse(timeString);
			}
		}catch(ParseException ex)
		{
			System.out.println("Error when parsing timeString to timeDate");
			ex.printStackTrace();
		}

		return timeDate;

	}

	public static String formatDateToString(Date timeDate,String timeFormat)
	{

		SimpleDateFormat format = new SimpleDateFormat(timeFormat);
		String timeString = null;
		try
		{
			if (timeDate != null)
				timeString = format.format(timeDate);

		}catch(Exception ex)
		{
			System.out.println("Error when parsing timeDate to timeString");
			ex.printStackTrace();
		}

		return timeString;

	}

	public static Date StrToDate(String data, String format) {
		// TODO Auto-generated method stub
		if (isEmpty(data))
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = new Date();
		try {
			date = formatter.parse(data);
		} catch (ParseException e) {
		}
		return new java.util.Date(date.getTime());
	}

	public static String encode(String val){
		if(StringUtil.isEmpty(val)){ return "";}
		try {return URLEncoder.encode(val,"GBK");} catch (UnsupportedEncodingException e) {return "";}
	}
	//去除光电这2个字
	public static String removeWord(String word){
		if(word !=null && word.length()>0){
			if(word.contains("广电")){
				return word.replace("广电", "");
			}else{
				return word;
			}
		}else{
			return "";
		}
	}
}
