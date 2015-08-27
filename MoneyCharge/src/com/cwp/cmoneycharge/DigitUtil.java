package com.cwp.cmoneycharge;

/**
 *  @author loiy
 *  www.agrilink.cn
 *  2012.11.30   
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.cwp.chart.ConvertNum;

public class DigitUtil {

	/**
	 * 汉字中的数字字符
	 */
	private static final Character[] SCDigits = { '零', '一', '二', '两', '三', '四',
			'五', '六', '七', '八', '九'
	// , '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒',
	// '捌', '玖'
	};

	/**
	 * 阿拉伯数字
	 */
	private static final Character[] araDigits = { '0', '1', '2', '2', '3',
			'4', '5', '6', '7', '8', '9'
	// , '0', '1', '2', '3', '4', '5', '6',
	// '7', '8', '9'
	};

	/**
	 * 阿拉伯数字
	 */
	private static Hashtable<Character, Character> araHash = new Hashtable<Character, Character>();

	/**
	 * 阿拉伯数字
	 */
	private static Hashtable<Character, Integer> YWQBSHash = new Hashtable<Character, Integer>();

	static {
		for (int i = 0; i < SCDigits.length; i++) {
			araHash.put(SCDigits[i], araDigits[i]);
		}
		YWQBSHash.put('亿', 100000000);
		YWQBSHash.put('万', 10000);
		// YWQBSHash.put('仟', 1000);
		// YWQBSHash.put('佰', 100);
		// YWQBSHash.put('拾', 10);
		YWQBSHash.put('千', 1000);
		YWQBSHash.put('百', 100);
		YWQBSHash.put('十', 10);
	}

	/**
	 * @param remain
	 *            剩下多少个字符
	 * @param curchar
	 *            当前分析的字符
	 */
	private static int ywqbs(int num, Boolean bool, int no, int remain,
			char curchar, char lastshow) {
		if (num == 0) {
			num += no;
			return num;
		} else {
			String numString = String.valueOf(num); // 将num转换成String
			String last = numString.substring(numString.length() - 1,
					numString.length()); // 获取最后一个字符
			String exceptlast = numString.substring(0, numString.length() - 1); // 获取除了最后一个字符所有字符串
			if (exceptlast.length() == 0) { // 说明num是个位值
				num = Integer.parseInt(last) * no;
				return num;
			} else {
				/***** 针对有连续的这些字符[十百千万亿] start *****/
				if (bool && YWQBSHash.get(curchar) > YWQBSHash.get(lastshow)) { // 说明上个字符属于[十百千万亿]
					num *= no;
					return num;
				}
				/***** 针对有连续的这些字符[十百千万亿] end *****/
				/***** 分析到最后一个字符 start *****/
				/**** 针对这种情况(一千二百二十一万) ****/
				if (remain == 1
						&& YWQBSHash.get(curchar) > YWQBSHash.get(lastshow)) {
					num *= YWQBSHash.get(curchar);
					return num;
				}
				/**** 针对这种情况(一千二百二十一万) ****/
				/***** 分析到最后一个字符 end *****/
				/***** last如果为0 start *****/
				if (last.equals("0")) { // 循环到最后一位字符,last等于0
					last = "1"; //
				}
				/***** last如果为0 end *****/
				exceptlast = exceptlast + "0"; // 缺少最后一位,需要补上
				num = Integer.parseInt(exceptlast) + Integer.parseInt(last)
						* no;
				return num;
			}
		}
	}

	public static int parse(String word) {
		int num = 0;
		char lastchar = '一'; // 上次字符,字符特指[十百千万亿],默认随便填写一个'一'
		char lastshow = '一'; // 上次出现的字符,字符特指[十百千万亿],默认随便填写一个'一'
		char[] ch = word.toCharArray();
		Boolean bool = false; // 是否连续出现[十百千万亿]
		for (int i = 0; i < ch.length; i++) {
			Character find = araHash.get(ch[i]);// 获取阿拉伯数字数组[1,2,3...]
			if (find != null) {
				num += Integer.parseInt(String.valueOf(find.charValue()));
				bool = false;
				lastchar = '一'; // 恢复到默认值
				continue;
			} else if (ch[i] == '十' || ch[i] == '拾') {
				num = ywqbs(num, bool, 10, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '十';
				lastshow = '十';
			} else if (ch[i] == '千' || ch[i] == '仟') {
				num = ywqbs(num, bool, 1000, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '千';
				lastshow = '千';
			} else if (ch[i] == '百' || ch[i] == '佰') {
				num = ywqbs(num, bool, 100, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '百';
				lastshow = '百';
			} else if (ch[i] == '万') {
				num = ywqbs(num, bool, 10000, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '万';
				lastshow = '万';
			} else if (ch[i] == '亿') {
				num = ywqbs(num, bool, 100000000, ch.length - i, ch[i],
						lastshow);
				bool = true;
				lastchar = '亿';
				lastshow = '亿';
			}
		}
		ch = null;
		return num;
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	// 没有考虑兆及更大单位的情况
	public static long chn2digit(String chnStr) {
		// init map
		java.util.Map<String, Integer> unitMap = new java.util.HashMap<String, Integer>();
		unitMap.put("十", 10);
		unitMap.put("百", 100);
		unitMap.put("千", 1000);
		unitMap.put("万", 10000);
		unitMap.put("亿", 100000000);

		java.util.Map<String, Integer> numMap = new java.util.HashMap<String, Integer>();
		numMap.put("零", 0);
		numMap.put("一", 1);
		numMap.put("二", 2);
		numMap.put("三", 3);
		numMap.put("四", 4);
		numMap.put("五", 5);
		numMap.put("六", 6);
		numMap.put("七", 7);
		numMap.put("八", 8);
		numMap.put("九", 9);

		// 队列
		List<Long> queue = new ArrayList<Long>();
		long tempNum = 0;
		for (int i = 0; i < chnStr.length(); i++) {
			char bit = chnStr.charAt(i);
			// 数字
			if (numMap.containsKey(bit + "")) {

				tempNum = tempNum + numMap.get(bit + "");

				// 一位数、末位数、亿或万的前一位进队列
				if (chnStr.length() == 1
						| i == chnStr.length() - 1
						| (i + 1 < chnStr.length() && (chnStr.charAt(i + 1) == '亿' | chnStr
								.charAt(i + 1) == '万'))) {
					queue.add(tempNum);
				}
			}
			// 单位
			else if (unitMap.containsKey(bit + "")) {

				// 遇到十 转换为一十、临时变量进队列
				if (bit == '十') {
					if (tempNum != 0) {
						tempNum = tempNum * unitMap.get(bit + "");
					} else {
						tempNum = 1 * unitMap.get(bit + "");
					}
					queue.add(tempNum);
					tempNum = 0;
				}

				// 遇到千、百 临时变量进队列
				if (bit == '千' | bit == '百') {
					if (tempNum != 0) {
						tempNum = tempNum * unitMap.get(bit + "");
					}
					queue.add(tempNum);
					tempNum = 0;
				}

				// 遇到亿、万 队列中各元素依次累加*单位值、清空队列、新结果值进队列
				if (bit == '亿' | bit == '万') {
					long tempSum = 0;
					if (queue.size() != 0) {
						for (int j = 0; j < queue.size(); j++) {
							tempSum += queue.get(j);
						}
					} else {
						tempSum = 1;
					}
					tempNum = tempSum * unitMap.get(bit + "");
					queue.clear();// 清空队列
					queue.add(tempNum);// 新结果值进队列
					tempNum = 0;
				}
			}
		}

		// output
		long sum = 0;
		for (Long i : queue) {
			sum += i;
		}
		return sum;
	}

	public String mixnumtostring(String str) {
		str = str.replaceAll("廿", "二十");

		str = str.replaceAll("两", "二");
		str = str.replaceAll("玖", "九");
		str = str.replaceAll("捌", "八");
		str = str.replaceAll("柒", "七");
		str = str.replaceAll("陆", "六");
		str = str.replaceAll("伍", "五");
		str = str.replaceAll("肆", "四");
		str = str.replaceAll("叁", "三");
		str = str.replaceAll("贰", "二");
		str = str.replaceAll("壹", "一");

		str = str.replaceAll("仟", "千");
		str = str.replaceAll("佰", "百");
		str = str.replaceAll("拾", "十");
		str = str.replaceAll("亿", "|亿|");
		str = str.replaceAll("万", "|万|");
		str = str.replaceAll("千", "|千|");
		str = str.replaceAll("百", "|百|");
		str = str.replaceAll("十", "|十|");
		String regex = "\\|";
		String[] strs = str.split(regex);
		ConvertNum cn = new ConvertNum();
		for (int i = 0; i < strs.length; i++) {
			if (isNumeric(strs[i])) {
				if (!strs[i].equals("")) {
					// strs[i] = d.test(Integer.parseInt(strs[i]));
					strs[i] = cn.NumToChinese(Double.parseDouble(strs[i]));
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
		}
		long num = chn2digit(sb.toString());
		return Long.toString(num);
	}

	static void sort(int arry[]) {
		// int arry[] = { 1, 9, 3, 4, 5, 7, 2, 0, 6, 8 };
		int temp = 0;
		int i, j;
		for (i = 1; i <= 10; i++)
			for (j = 1; j < 11 - i; j++)
				if (arry[j] < arry[j - 1]) {
					temp = arry[j];
					arry[j] = arry[j - 1];
					arry[j - 1] = temp;
				}

		// for (i = 0; i < 10; i++)
		// System.out.println(arry[i]);

	}

	// public static void main(String args[]) {
	// Test d = new Test();
	// String str = "30200万";
	// str = str.replaceAll("两", "二");
	//
	// str = str.replaceAll("玖", "九");
	// str = str.replaceAll("捌", "八");
	// str = str.replaceAll("柒", "七");
	// str = str.replaceAll("陆", "六");
	// str = str.replaceAll("伍", "五");
	// str = str.replaceAll("肆", "四");
	// str = str.replaceAll("叁", "三");
	// str = str.replaceAll("贰", "二");
	// str = str.replaceAll("壹", "一");
	//
	// str = str.replaceAll("仟", "千");
	// str = str.replaceAll("佰", "百");
	// str = str.replaceAll("拾", "十");
	// str = str.replaceAll("亿", "|亿|");
	// str = str.replaceAll("万", "|万|");
	// str = str.replaceAll("千", "|千|");
	// str = str.replaceAll("百", "|百|");
	// str = str.replaceAll("十", "|十|");
	// System.out.println(str);
	// String regex = "\\|";
	// String[] strs = str.split(regex);
	// ConvertNum cn = new ConvertNum();
	// // System.out.println("asd " + cn.NumToChinese(300));
	// for (int i = 0; i < strs.length; i++) {
	// System.out.println(i + " " + strs[i]);
	// if (isNumeric(strs[i])) {
	// if (!strs[i].equals("")) {
	// strs[i] = cn.NumToChinese(Double.parseDouble(strs[i]));
	// }
	// }
	// }
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < strs.length; i++) {
	// sb.append(strs[i]);
	// }
	// System.out.printf(sb.toString());
	// long num = chn2digit(sb.toString());
	// System.out.printf("" + num);
	// }
}