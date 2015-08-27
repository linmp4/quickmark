package com.cwp.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cwp.cmoneycharge.AddPay;
import com.cwp.cmoneycharge.DigitUtil;
import com.cwp.cmoneycharge.MainActivity;

import cwp.moneycharge.dao.IncomeDAO;
import cwp.moneycharge.dao.ItypeDAO;
import cwp.moneycharge.dao.PayDAO;
import cwp.moneycharge.dao.PtypeDAO;
import cwp.moneycharge.model.Tb_income;
import cwp.moneycharge.model.Tb_pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

public class Util {
	static String[][] mztype = { { "早餐", "早上吃", "早上食", "今朝食" },
			{ "晚餐", "晚上吃", "今晚食" }, { "午餐", "中午吃" }, { "夜宵", "宵夜" },
			{ "生活用品", "沐浴露", "卫生巾", "洗头水" },
			{ "打的", "出租车", "计程车", "嘀嘀打车", "优步" }, { "电子产品", "IPHONE", "三星" },
			{ "意外财", "捡", "拣" }, { "租金", "租房" } };
	static String[] number = { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"0", "一", "二", "两", "三", "四", "五", "六", "七", "八", "九", "十", "百",
			"千", "万", "亿", "拾", "佰", "仟", "壹", "贰", "叁", "肆", "伍", "陆", "柒",
			"捌", "玖" };
	static char[] number2 = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
			'一', '二', '两', '三', '四', '五', '六', '七', '八', '九', '十', '百', '千',
			'万', '亿', '拾', '佰', '仟', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌',
			'玖' };
	static String[] money = { "元", "块", "钱", "人民币", "rmb", "RMB" };
	static String[] voice_pay = { "买", "吃", "花", "加油", "食" };
	static String[] voice_income = { "卖", "获" };
	public static String[] VoiceSave;
	public static String type;
	private static Intent intent;
	private static int userid = 100000001;
	private static List<Tb_income> list_income;
	private static List<Tb_pay> list_pay;
	private static int ip_sum;
	private static int circle1;
	private static int circle2;
	private static int circle3;

	static final boolean IS_JBMR2 = Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2;
	static final boolean IS_ISC = Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	static final boolean IS_GINGERBREAD_MR1 = Build.VERSION.SDK_INT == Build.VERSION_CODES.GINGERBREAD_MR1;

	/*
	 * 识别结果处理函数
	 * 
	 * @param VoiceSave[0] 收入类别的值
	 * 
	 * @param VoiceSave[1] 金额的值
	 * 
	 * @param VoiceSave[3] 重复类别的值，仅用于显示提醒
	 * 
	 * @param VoiceSave[4] 支出类别的值
	 * 
	 * @param VoiceSave[5] "语音识别"类别的值
	 */
	public static String Recognition(String t, Context context, int userid) {
		VoiceSave = null;
		VoiceSave = new String[6];
		PtypeDAO ptypeDAO = new PtypeDAO(context);
		ItypeDAO itypeDAO = new ItypeDAO(context);

		int mfirst = t.length() + 1, mend = t.length() + 1, temp = 0;
		Boolean ismoney = false, intype = false, outtype = false;
		Boolean voice_ptype = false, voice_intype = false, isrepet = false, isunit = false;
		String w = "", strmoney = "", inname = "1", outname = "2";
		List<String> spdatalist = ptypeDAO.getPtypeName(userid);
		List<String> spdatalist2 = itypeDAO.getItypeName(userid);
		VoiceSave[2] = t;

		for (int i = 0; i < spdatalist.size(); i++) { // 判断是否包含支出
			if (t.indexOf(spdatalist.get(i).toString()) > -1) {
				type = "pay";
				intype = true;
				inname = spdatalist.get(i).toString();
				VoiceSave[0] = Integer.toString(i); // VoiceSave[0]为收入类别的值
			}
		}
		for (int i = 0; i < spdatalist2.size(); i++) { // 判断是否包含收入
			if (t.indexOf(spdatalist2.get(i).toString()) > -1) {
				type = "income";
				outtype = true;
				outname = spdatalist2.get(i).toString();
				VoiceSave[4] = Integer.toString(i); // VoiceSave[4]为支出类别的值
			}
		}
		if (!(intype || outtype)) {
			for (int i = 0; i < mztype.length; i++) {
				for (int j = 0; j < mztype[i].length; j++) {
					if (t.indexOf(mztype[i][j].toString()) > -1) {
						// System.out.println("包含 " + mztype[i][j]);
						for (int k = 0; k < spdatalist.size(); k++) { // 判断是否包含支出
							if (mztype[i][0].indexOf(spdatalist.get(k)
									.toString()) > -1) {
								type = "pay";
								intype = true;
								inname = spdatalist.get(k).toString();
								VoiceSave[0] = Integer.toString(k); // VoiceSave[0]为收入类别的值
							}
						}
						for (int l = 0; l < spdatalist2.size(); l++) { // 判断是否包含收入
							if (mztype[i][0].indexOf(spdatalist2.get(l)
									.toString()) > -1) {
								type = "income";
								outtype = true;
								outname = spdatalist2.get(l).toString();
								VoiceSave[4] = Integer.toString(l); // VoiceSave[4]为支出类别的值
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < voice_pay.length; i++) { // 判断是否包含支出的动词
			if (t.indexOf(voice_pay[i]) > -1) {
				voice_ptype = true;
				type = "pay";
			}
		}
		for (int i = 0; i < voice_income.length; i++) { // 判断是否包含支出的动词
			if (t.indexOf(voice_income[i]) > -1) {
				voice_intype = true;
				type = "income";
			}
		}
		for (int i = 0; i < money.length; i++) { // 判断是否包含单位，获得结尾
			if (t.indexOf(money[i]) > -1) {
				temp = t.indexOf(money[i]);
				isunit = true;
				if (temp < mend) {
					mend = temp;
				}
			}
		}
		if (!isunit) { // 无结尾
			int tmend = -2;
			for (int i = 0; i < number.length; i++) { // 判断是否包含阿拉伯数字开头，获得结尾
				if (t.lastIndexOf(number[i]) > -1) {
					temp = t.lastIndexOf(number[i]);
					if (temp > tmend) {
						tmend = temp;
					}
				}
			}
			mend = tmend + 1;
			for (int i = 0; i < number.length; i++) { // 判断是否包含阿拉伯数字金额，获得开头
				if (t.indexOf(number[i]) > -1) {
					temp = t.indexOf(number[i]);
					if ((temp < mfirst) && (temp <= mend)) {
						mfirst = temp;
					}
				}
			}
		} else {// 有结尾
			char[] c = t.toCharArray();
			int flag = t.length() + 1;
			boolean b = true;
			for (int i = mend; i > 0; i--) { // 判断是否包含阿拉伯数字金额，获得开头
				if (b) {
					for (int j = 0; j < number.length; j++) {
						if (c[i] == number2[j]) {
							flag = i;
							boolean a = true;
							for (int k = 0; k < number.length; k++) {
								if (c[i - 1] == number2[k]) {
									flag = i - 1;
									a = false;
								}
							}
							if (a) {
								b = false;
							}
						}
					}
				} else {
					break;
				}
			}
			mfirst = flag;
		}
		if (!(mfirst == (t.length() + 1) || mend == (t.length() + 1) || mend == -1)) { // 转换为阿拉伯数字
			ismoney = true;
			strmoney = t.substring(mfirst, mend);
//			System.out.println("strmoney " + strmoney + " mfirst " + mfirst
//					+ " mend " + mend);
			// 判断语句是否包含非数字
			char[] chs = strmoney.toCharArray();
			List<String> num = Arrays.asList(number);
			List<String> mon = Arrays.asList(money);
			for (int l = 0; l < chs.length; l++)
				if (!num.contains(String.valueOf(chs[l])))
					if (!mon.contains(String.valueOf(chs[l])))
						ismoney = false;
			if (ismoney) {
				DigitUtil Util = new DigitUtil();
				VoiceSave[1] = Util.mixnumtostring(strmoney); // 调用工具类处理汉字的金额
			}
		}
		if (intype && outtype) { // 如果同时含有收入/支出的类别
			if (outname.equals(inname)) {
				if (ismoney) {
					if (voice_intype || voice_ptype) {
						// dialogShowUtil.dialogShow("rotatebottom", "OK", t,
						// w);
						return "OK ";
					} else {
						VoiceSave[3] = outname; // VoiceSave[3]为重复类别的值，仅用于显示提醒
						w = "提示：请选择：<" + outname + ">为收入还是支出";
						// dialogShowUtil.dialogShow("shake", "judge", t, w); //
						// 如果含有金额
						return "judge " + w;
					}
				} else {
					w = "提示：你的话中没有包含消费或开支的<金额>";
					// dialogShowUtil.dialogShow("shake", "wrong", t, w);
					return "wrong " + w;
				}
			} else {
				w = "**提示：一次只能记录一条记录哦"; // 如果含有收入并且支出的类别
				// dialogShowUtil.dialogShow("shake", "wrong", t, w);
				return "wrong " + w;
			}
		} else {
			if (!((intype || outtype) || ismoney)) { // 如果不含金额
				w = "**提示：没有包含的<金额>\n**提示：\n没有包含<类别>\n（类别有："
						+ listToString(spdatalist, '，') + "，"
						+ listToString(spdatalist2, '，') + "）";
				// dialogShowUtil.dialogShow("shake", "wrong", t, w);
				return "wrong " + w;
			} else if ((intype || outtype) && (!ismoney)) {
				w = "提示：\n没有包含消费或开支的<金额>\n或者出现多次<金额>";
				// dialogShowUtil.dialogShow("shake", "wrong", t, w);
				return "wrong " + w;
			} else if ((!(intype || outtype)) && ismoney) {
				for (int i = 0; i < spdatalist.size(); i++) { // 判断是否包含支出
					if ("语音识别".indexOf(spdatalist.get(i).toString()) > -1) {
						VoiceSave[5] = Integer.toString(i);
						VoiceSave[3] = "语音识别";
					}
				}
				if (voice_intype || voice_ptype) {
					// dialogShowUtil.dialogShow("rotatebottom", "OK", t,
					// w);
					return "notype ";
				} else {
					w = "将会记录为<语音识别>类别\n**提示：没有包含<（默认）类别>（"
							+ listToString(spdatalist, '，') + "）\n\n\n";
					// dialogShowUtil.dialogShow("shake", "notype", t, w);
					return "judge " + w;
				}
			} else {
				// dialogShowUtil.dialogShow("rotatebottom", "OK", t, w);
				return "OK " + w;
			}
		}
	}

	private static String setTimeFormat(String newtxtTime) { // 设置日期格式
		final Calendar c = Calendar.getInstance();// 获取当前系统日期
		int mYear = c.get(Calendar.YEAR);// 获取年份
		int mMonth = c.get(Calendar.MONTH);// 获取月份
		int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取天数
		String date = new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).toString();
		int y, m, d;
		String sm, sd;
		int i = 0, j = 0, k = 0;
		for (i = 0; i < date.length(); i++) {
			if (date.substring(i, i + 1).equals("-") && j == 0)
				j = i;
			else if (date.substring(i, i + 1).equals("-"))
				k = i;
		}
		y = Integer.valueOf(date.substring(0, j));
		m = Integer.valueOf(date.substring(j + 1, k));
		d = Integer.valueOf(date.substring(k + 1));
		if (m < 10) {
			sm = "0" + String.valueOf(m);
		} else
			sm = String.valueOf(m);
		if (d < 10) {
			sd = "0" + String.valueOf(d);
		} else
			sd = String.valueOf(d);
		return String.valueOf(y) + "-" + sm + "-" + sd;
	}

	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
			if (i < list.size() - 1) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	public static int save(final Context context, String type, int userid,
			String strMoney, int Position, String textreAddres,
			String textreMark, String textphoto) {
		// System.out.println("type " + type + " userid " + userid +
		// " strMoney "
		// + strMoney + " Position " + Position + " textreAddres "
		// + textreAddres + " textreMark " + textreMark + " textphoto "
		// + textphoto + " setTimeFormat " + setTimeFormat(null));
		PayDAO payDAO = new PayDAO(context);
		IncomeDAO incomeDAO = new IncomeDAO(context);
		String typemode = "add";
		// 为保存按钮设置监听事件
		if (typemode.equals("add")) { // 添加模式
			if (type.equals("pay")) { // 支出
				// 创建InaccountDAO对象
				// PayDAO payDAO = new PayDAO(context);
				// 创建Tb_inaccount对象
				Tb_pay tb_pay = new Tb_pay(userid, payDAO.getMaxNo(userid) + 1,
						AddPay.get2Double(strMoney), setTimeFormat(null),
						Position, textreAddres, textreMark, textphoto);
				payDAO.add(tb_pay);// 添加收入信息
				new Thread() {
					public void run() {
						Looper.prepare();
						Toast.makeText(context, "〖新增支出〗数据添加成功！",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					};
				}.start();
				intent = new Intent(context, MainActivity.class);
				intent.putExtra("cwp.Fragment", "1");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				return 1;
			} else { // 收入
				Tb_income tb_income = new Tb_income(userid,
						incomeDAO.getMaxNo(userid) + 1,
						AddPay.get2Double(strMoney), setTimeFormat(null),
						Position,
						// txtInhandler.getText().toString(),
						textreAddres, textreMark, textphoto, "支出");
				incomeDAO.add(tb_income);// 添加收入信息
				// 弹出信息提示
				new Thread() {
					public void run() {
						Looper.prepare();
						Toast.makeText(context, "〖新增收入〗数据添加成功！",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					};
				}.start();
				intent = new Intent(context, MainActivity.class);
				intent.putExtra("cwp.Fragment", "1");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				return 1;
			}
		}
		return 0;
	}

	public static String gettotal(final Context context, float budget) {
		IncomeDAO incomeDAO = new IncomeDAO(context);
		PayDAO payDAO = new PayDAO(context);

		final Calendar c = Calendar.getInstance();// 获取当前系统日期
		int defaultMonth = c.get(Calendar.MONTH) + 1;// 获取月份
		int defaultYear = c.get(Calendar.YEAR);// 获取年份
		String dmonth;
		int pay_sum = 0;
		int income_sum = 0;

		if (defaultMonth < 10) {
			dmonth = "0" + Integer.toString(defaultMonth);
		} else {
			dmonth = Integer.toString(defaultMonth);
		}
		String date1 = Integer.toString(defaultYear) + "-" + dmonth + "-01";
		String date2 = Integer.toString(defaultYear) + "-" + dmonth + "-31";
		list_income = incomeDAO.getScrollData(userid, 0, // 取每年的收入数据
				(int) incomeDAO.getCount(userid), date1, date2);
		list_pay = payDAO.getScrollData(userid, 0, // 取每年的支出数据
				(int) payDAO.getCount(userid), date1, date2);
		Integer[] str = new Integer[list_income.size() + list_pay.size()];
		if ((list_income.size() == 0) && (list_pay.size() == 0)) {
			ip_sum = 0;
		} else {
			for (Tb_pay tb_pay : list_pay) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				pay_sum += tb_pay.getMoney();
			}
			for (Tb_income tb_income : list_income) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				income_sum += tb_income.getMoney();
			}
			ip_sum = income_sum - pay_sum;
		}
		String ip_sum_str;
		if (Math.abs(ip_sum) > 100000000) {
			ip_sum_str = Integer.toString(ip_sum / 100000000) + "亿";
		} else if (Math.abs(ip_sum) > 10000000) {
			ip_sum_str = Integer.toString(ip_sum / 10000000) + "千万";
		} else if (Math.abs(ip_sum) > 1000000) {
			ip_sum_str = Integer.toString(ip_sum / 1000000) + "百万";
		} else if (Math.abs(ip_sum) > 100000) {
			ip_sum_str = Integer.toString(ip_sum / 100000) + "十万";
		} else if (Math.abs(ip_sum) > 10000) {
			ip_sum_str = Integer.toString(ip_sum / 10000) + "万";
		} else {
			ip_sum_str = Integer.toString(ip_sum);
		}

		// ip_sum = -2220;
		// budget = 3000;
		float d = budget / 3;
		if (Math.abs(ip_sum) > budget) {
			if (ip_sum < 0) {
				circle1 = -60;
				circle2 = -60;
				circle3 = -60;
			} else {
				circle1 = 60;
				circle2 = 60;
				circle3 = 60;
			}
		} else if (Math.abs(ip_sum) > budget * 2 / 3) {
			if (ip_sum < 0) {
				circle1 = -60;
				circle2 = -60;
				circle3 = -(int) ((Math.abs(ip_sum) - 2 * d) * 60 / d);
			} else {
				circle1 = 60;
				circle2 = 60;
				circle3 = (int) ((ip_sum - 2 * d) * 60 / d);
			}
		} else if (Math.abs(ip_sum) > d) {
			if (ip_sum < 0) {
				circle1 = -60;
				circle2 = -(int) ((Math.abs(ip_sum) - d) * 60 / d);
			} else {
				circle1 = 60;
				circle2 = (int) ((ip_sum - d) * 60 / d);
			}
			circle3 = 0;
		} else {
			if (ip_sum < 0) {
				circle1 = -(int) (Math.abs(ip_sum) * 60 / d);
			} else {
				circle1 = (int) (ip_sum * 60 / d);
			}
			circle2 = 0;
			circle3 = 0;
		}
		return defaultMonth + "|" + ip_sum_str + "|" + circle1 + "|" + circle2
				+ "|" + circle3;
	}
}
