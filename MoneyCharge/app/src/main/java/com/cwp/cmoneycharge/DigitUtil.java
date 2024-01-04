package com.cwp.cmoneycharge;

/**
 *  @author loiy
 *  www.agrilink.cn
 *  2012.11.30   
 */
import java.util.Hashtable;

public class DigitUtil {

	/**
	 * 汉字中的数字字符
	 */
	private static final Character[] SCDigits = { '零', '一', '二', '两', '三', '四',
			'五', '六', '七', '八', '九' };

	/**
	 * 阿拉伯数字
	 */
	private static final Character[] araDigits = { '0', '1', '2', '2', '3',
			'4', '5', '6', '7', '8', '9' };

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
			} else if (ch[i] == '十') {
				num = ywqbs(num, bool, 10, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '十';
				lastshow = '十';
			} else if (ch[i] == '千') {
				num = ywqbs(num, bool, 1000, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '千';
				lastshow = '千';
			} else if (ch[i] == '百') {
				num = ywqbs(num, bool, 100, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '百';
				lastshow = '百';
			} else if (ch[i] == '万') {
				num = ywqbs(num, bool, 10000, ch.length - i, ch[i], lastshow);
				bool = true;
				lastchar = '万';
				lastshow = '万';
			}
		}
		ch = null;
		return num;
	}

	// public static void main(String args[]) {
	// String wordes[] = {
	// "一千二百二十一万",
	// "一千二百二十一",
	// "一千零十",
	// "一万零一百",
	// "一千零十一",
	// "一万零一百十一",
	// "一千二百十九",
	// "一千万",
	// "五十五"
	// };
	// for(int i = 0; i < wordes.length; i++) {
	// DigitUtil t = new DigitUtil();
	// int num = t.parse(wordes[i]);
	// System.out.println(wordes[i] + " " + num);
	// }
	// }

}