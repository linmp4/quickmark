package com.cwp.cmoneycharge;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class DialogShowUtil {
	private Context ctx;
	private Activity act;
	NiftyDialogBuilder dialogBuilder = null;
	private Effectstype effect; // 自定义Dialog
	String[] VoiceSave;
	static String type;
	static String VoiceDefault;

	public DialogShowUtil(Activity act, Context ctx, String[] VoiceSave,
			String type, String VoiceDefault) {
		this.ctx = ctx;
		this.act = act;
		this.VoiceSave = VoiceSave;
		this.type = type;
		this.VoiceDefault = VoiceDefault;
	}

	public void dialogShow(String showtype, String style,
			final String context1, String context2) {
		dialogBuilder = new NiftyDialogBuilder(ctx, R.style.dialog_untran); // 自定义dialogBuilder
		switch (showtype) {
		case "rotatebottom":
			effect = Effectstype.RotateBottom;
			break;
		case "shake":
			effect = Effectstype.Shake;
			break;
		}

		switch (style) {
		case "first":
			dialogBuilder.withTitle("语音记账")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage("语音格式：\n早餐在餐厅食了20元。\n\n")
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |//
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("取消") // def gone
					.withButton2Text("开始语音") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							((AddPay) act).VoiceRecognition();
						}
					}).show();
			break;
		case "notype":
			dialogBuilder.withTitle("识别成功")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage("你刚刚说了“ " + context1 + "”\n\n" + context2)
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |//
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("取消") // def gone
					.withButton2Text("是") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							VoiceDefault = "notype";
							VoiceSave[3] = VoiceSave[3];
							dialogShow("shake", "judge", context1, "");
						}
					}).show();
			break;
		case "wrong":
			dialogBuilder
					.withTitle("识别失败")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage(
							"你刚刚说了“ " + context1 + "”不符合格式，请再试一次\n\n"
									+ context2)
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("取消") // def gone
					.withButton2Text("再次语音") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							((AddPay) act).VoiceRecognition();
						}
					}).show();
			break;
		case "OK":
			dialogBuilder.withTitle("识别成功")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage("成功！\n你刚刚说了“" + context1 + "”，\n是否确定要记录这条数据?")
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("取消") // def gone
					.withButton2Text("确定") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							((AddPay) act).VoiceSuccess();
						}
					}).show();
			break;
		case "judge":
			dialogBuilder
					.withTitle("识别成功")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage(
							"成功！\n你刚刚说了“" + context1 + "”，\n<" + VoiceSave[3]
									+ ">类别需要你请确认该笔是<开支>还是<收入>?\n")
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("开支") // def gone
					.withButton2Text("收入") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							type = "pay";
							((AddPay) act).VoiceSuccess();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
							type = "income";
							((AddPay) act).VoiceSuccess();
						}
					}).show();
			break;
		case "quit":
			dialogBuilder.withTitle("退出程序")
					// .withTitle(null) no title
					.withTitleColor("#FFFFFF")
					// def
					.withDividerColor("#11000000")
					// def
					.withMessage("是否要退出程序？\n\n")
					// .withMessage(null) no Msg
					.withMessageColor("#FFFFFF")
					// def
					.withIcon(ctx.getResources().getDrawable(R.drawable.icon))
					.isCancelableOnTouchOutside(false) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("取消") // def gone
					.withButton2Text("退出") // def gone
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							SysApplication.getInstance().exit();
						}
					}).show();
			break;
		}

	}

	public static String dialoggettype() {
		return type;
	}

	public static String dialogVoiceDefault() {
		return VoiceDefault;
	}
}