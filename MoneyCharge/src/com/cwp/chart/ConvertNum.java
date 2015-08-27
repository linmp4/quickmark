package com.cwp.chart;

import java.io.*;
import java.lang.IllegalArgumentException;

public class ConvertNum {
	/**
	 * 把金额阿拉伯数字转换为汉字表示，小数点后四舍五入保留两位
	 * 还有一种方法可以在转换的过程中不考虑连续0的情况，然后对最终的结果进行一次遍历合并连续的零
	 */
	public static String[] ChineseNum = new String[] { "零", "一", "二", "三", "四",
			"五", "六", "七", "八", "九" };

	// public static String[] ChineseNum = new String[] { "零", "壹", "贰", "叁",
	// "肆",
	// "伍", "陆", "柒", "捌", "玖" };

	public static String NumToChinese(double num) {
		if (num > 99999999999999.99 || num < -99999999999999.99)
			throw new IllegalArgumentException(
					"参数值超出允许范围 (-99999999999999.99 ～ 99999999999999.99)！");
		boolean negative = false;// 正负标号
		if (num < 0) {
			negative = true;
			num = num * (-1);
		}
		long temp = Math.round(num * 100);
		int numFen = (int) (temp % 10);// 分
		temp = temp / 10;
		int numJiao = (int) (temp % 10);// 角
		temp = temp / 10;
		// 此时temp只包含整数部分
		int[] parts = new int[20];// 将金额整数部分分为在0-9999之间数的各个部分
		int numParts = 0;// 记录把原来金额整数部分分割为几个部分
		for (int i = 0;; i++) {
			if (temp == 0)
				break;
			int part = (int) (temp % 10000);
			parts[i] = part;
			temp = temp / 10000;
			numParts++;
		}
		boolean beforeWanIsZero = true;// 标志位，记录万的下一级是否为0
		String chineseStr = "";
		for (int i = 0; i < numParts; i++) {
			String partChinese = partConvert(parts[i]);
			if (i % 2 == 0) {
				if ("".equals(partChinese))
					beforeWanIsZero = true;
				else
					beforeWanIsZero = false;
			}
			if (i != 0) {
				if (i % 2 == 0)// 亿的部分
					chineseStr = "亿" + chineseStr;
				else {
					if ("".equals(partChinese) && !beforeWanIsZero)// 如果“万”对应的
																	// part 为
																	// 0，而“万”下面一级不为
																	// 0，则不加“万”，而加“零”
						chineseStr = "零" + chineseStr;
					else {
						if (parts[i - 1] < 1000 && parts[i - 1] > 0)// 如果万的部分不为0，而万前面的部分小于1000大于0，则万后面应该跟零
							chineseStr = "零" + chineseStr;
						chineseStr = "万" + chineseStr;
					}
				}
			}
			chineseStr = partChinese + chineseStr;
		}
		if ("".equals(chineseStr))// 整数部分为0，则表示为零元
			chineseStr = ChineseNum[0];
		else if (negative)// 整数部分部位0，但是为负数
			// chineseStr = "负" + chineseStr;
			chineseStr = chineseStr;
		if (numFen == 0 && numJiao == 0) {
			chineseStr = chineseStr;
		} else if (numFen == 0) {// 0分
			// chineseStr = chineseStr + ChineseNum[numJiao] + "角";
		} else {
			// if (numJiao == 0)
			// chineseStr = chineseStr + "零" + ChineseNum[numFen] + "分";
			// else
			// chineseStr = chineseStr + ChineseNum[numJiao] + "角"
			// + ChineseNum[numFen] + "分";
		}
		return chineseStr;
	}

	// 转换拆分后的每个部分，0-9999之间
	public static String partConvert(int partNum) {
		if (partNum < 0 || partNum > 10000) {
			throw new IllegalArgumentException("参数必须是大于等于0或小于10000的整数");
		}
		String[] units = new String[] { "", "十", "百", "千" };
		int temp = partNum;
		String partResult = new Integer(partNum).toString();
		int partResultLength = partResult.length();
		boolean lastIsZero = true;// 记录上一位是否为0
		String chineseStr = "";
		for (int i = 0; i < partResultLength; i++) {
			if (temp == 0)// 高位无数字
				break;
			int digit = temp % 10;
			if (digit == 0) {
				if (!lastIsZero)// 如果前一个数字不是0则在当前汉字串前加零
					chineseStr = "零" + chineseStr;
				lastIsZero = true;
			} else {
				chineseStr = ChineseNum[digit] + units[i] + chineseStr;
				lastIsZero = false;
			}
			temp = temp / 10;
		}
		return chineseStr;
	}

	// public static void main(String args[]) {
	// System.out.println("其他测试：");
	// System.out.println("100120: " + NumToChinese(100120));
	// System.out.println("25000000000005.999: "
	// + NumToChinese(25000000000005.999));
	// System.out.println("45689263.626: " + NumToChinese(45689263.626));
	// System.out.println("0.69457: " + NumToChinese(0.69457));
	// System.out.println("253.0: " + NumToChinese(253.0));
	// System.out.println("0: " + NumToChinese(0));
	// }
}