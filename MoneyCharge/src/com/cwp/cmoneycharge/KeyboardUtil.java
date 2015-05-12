package com.cwp.cmoneycharge;

import com.cwp.cmoneycharge.R;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KeyboardUtil {
	private Context ctx;
	private Activity act;
	private KeyboardView keyboardView;
	private Keyboard k2;// 数字键盘
	public boolean isnun = true;// 是否数据键盘
	public boolean a = true;
	public static String c = null;
	private EditText ed;
	String typemode;
	DialogShowUtil dialogShowUtil = null;
	protected boolean count = true;

	public KeyboardUtil(Activity act, Context ctx, EditText edit,
			String typemode) {
		this.ctx = ctx;
		this.ed = edit;
		this.act = act;
		this.typemode = typemode;
		k2 = new Keyboard(ctx, R.xml.symbols_num);
		keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
		keyboardView.setEnabled(true);
		keyboardView.setKeyboard(k2);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);

	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {

		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			goforkey(primaryCode, keyCodes);
		}

		private void goforkey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			if (typemode.equals("ModifyInP")) { // 添加模式获取开始光标
				ed.setSelection(editable.length());
			}
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_DELETE) { // 删除键
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
						if (ed.getText().toString().indexOf(".") < 0) {
							a = true;
						}
					}
				}
			} else if (primaryCode == -8) {
				if (start > 0 && a) {
					editable.insert(start, ".");
					a = false;
				}
			}
			// else if (primaryCode == -11) { // 收入
			// ((AddPay) act).update();
			// }
			// else if (primaryCode == -12) { // 支出
			// AddPay add = new AddPay();
			// add.getupdatepay("income");
			// }
			else if (primaryCode == -7) {
				hideKeyboard();
			} else if (primaryCode == -9) {
				hideKeyboard();
				AddPay.showVoiveDialog();
			} else if (primaryCode == -10) {
				ed.setText("");
				a = true;
			} else {
				if (a) { // 判断是否有小数点
					if (editable.length() < 9) {
						editable.insert(start,
								Character.toString((char) primaryCode));
					}
				} else {
					if (editable.toString().split("\\.").length < 2) {
						editable.insert(start,
								Character.toString((char) primaryCode));
					} else {
						if (editable.toString().split("\\.")[1].length() < 2) { // 处理小数点后是否只能2位数
							editable.insert(start,
									Character.toString((char) primaryCode));
						}
					}
				}
			}
		}
	};

	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}

	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.INVISIBLE);
		}
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		if (wordstr.indexOf(str.toLowerCase()) > -1) {
			return true;
		}
		return false;
	}

	public interface InputFinishListener {
		public void inputHasOver(String text);
	}

}