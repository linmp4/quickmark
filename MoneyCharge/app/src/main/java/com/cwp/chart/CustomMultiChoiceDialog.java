package com.cwp.chart;

import com.cwp.cmoneycharge.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CustomMultiChoiceDialog extends Dialog {

	public CustomMultiChoiceDialog(Context context) {
		super(context);
	}

	public CustomMultiChoiceDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private ListView listView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private OnItemClickListener onItemClickListener;

		private String[] items;
		private boolean[] checkedItems;
		private boolean showSelectAll;
		private static boolean isMultiChoice = false;
		public static String contact_name;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public ListView getListView() {
			return listView;
		}

		public boolean[] getCheckedItems() {
			if (listView != null) {
				CheckAdapter adapter = (CheckAdapter) listView.getAdapter();
				checkedItems = adapter.getCheckedItem();
			}
			return checkedItems;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * 
		 * 
		 * @param items
		 * @param checkedItems
		 * @param onItemClickListener
		 *            Listening the item click event.
		 * @param showSelectAll
		 *            Whether to display the full checkbox.
		 * @return
		 */
		public Builder setMultiChoiceItems(boolean isMultiChoice,
				String[] items, boolean[] checkedItems,
				OnItemClickListener onItemClickListener, boolean showSelectAll) {
			this.isMultiChoice = isMultiChoice;
			this.items = items;
			this.checkedItems = checkedItems;
			this.onItemClickListener = onItemClickListener;
			this.showSelectAll = showSelectAll;
			return this;
		}

		public CustomMultiChoiceDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomMultiChoiceDialog dialog = new CustomMultiChoiceDialog(
					context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_multichoice_layout,
					null);

			// set the dialog title
			TextView multichoicTitle = (TextView) layout
					.findViewById(R.id.multichoic_title);
			multichoicTitle.setText(title);
			ImageView checkAll = (ImageView) layout
					.findViewById(R.id.chk_selectall);
			LinearLayout dialog_bottom = (LinearLayout) layout
					.findViewById(R.id.dialog_bottom);

			Button positiveButton = (Button) layout
					.findViewById(R.id.positiveButton);
			Button negativeButton = (Button) layout
					.findViewById(R.id.negativeButton);

			listView = (ListView) layout.findViewById(R.id.multichoiceList);

			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			// set the confirm button
			if (positiveButtonText != null) {
				positiveButton.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					positiveButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
									dialog.dismiss();
								}
							});
				} else {
					positiveButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				positiveButton.setVisibility(View.GONE);
				dialog_bottom.setVisibility(View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				negativeButton.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					negativeButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
									dialog.dismiss();
								}
							});
				} else {
					negativeButton
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				negativeButton.setVisibility(View.GONE);
			}

			// is the Multichoice(澶氶?妗?
			if (isMultiChoice == true) {

				final CheckAdapter checkAdapter = new CheckAdapter(context,
						items, checkedItems);

				listView.setAdapter(checkAdapter);
				listView.setItemsCanFocus(true);

				if (onItemClickListener != null) {
					listView.setOnItemClickListener(onItemClickListener);
				} else {
					listView.setOnItemClickListener(new OnMultiItemClick());
				}

				// show the all selectButton or not
				if (showSelectAll) {
					// checkAll.setVisibility( View.VISIBLE);
					// checkAll.setOnCheckedChangeListener(new
					// OnCheckedChangeListener() {
					//
					// @Override
					// public void onCheckedChanged(CompoundButton buttonView,
					// boolean isChecked) {
					// int count = listView.getAdapter().getCount();
					// if (isChecked) {
					// // update the status of all checkbox to checked
					// if (count > 0) {
					// for (int i = 0; i < count; i++) {
					// CheckBox itemCheckBox = (CheckBox) listView
					// .getAdapter()
					// .getView(i, null, null)
					// .findViewById(
					// R.id.chk_selectone);
					// itemCheckBox.setChecked(true);
					// }
					// }
					// } else {
					// // update the status of checkbox to unchecked
					// if (count > 0) {
					// for (int i = 0; i < count; i++) {
					// CheckBox itemCheckBox = (CheckBox) listView
					// .getAdapter()
					// .getView(i, null, null)
					// .findViewById(
					// R.id.chk_selectone);
					// itemCheckBox.setChecked(false);
					// }
					// }
					// }
					// }
					// });
					checkAll.setOnClickListener(new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				} else {
					checkAll.setVisibility(View.GONE);
				}
			} else { // 如果是单选模式
				final CheckAdapter checkAdapter = new CheckAdapter(context,
						items, checkedItems);

				listView.setAdapter(checkAdapter);
				listView.setItemsCanFocus(true);

				for (int i = 0; i < items.length; i++) { // 隐藏CheckBox
					CheckBox itemCheckBox = (CheckBox) listView.getAdapter()
							.getView(i, null, null)
							.findViewById(R.id.chk_selectone);
					itemCheckBox.setVisibility(View.GONE);
				}
				listView.setOnItemClickListener(onItemClickListener);
				// show the all selectButton or not

				if (showSelectAll) {
					checkAll.setOnClickListener(new android.view.View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				} else {
					checkAll.setVisibility(View.GONE);
				}
			}

			// set the content message
			if (message != null) {
				// ((TextView)layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				// if no message set add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

		class OnMultiItemClick implements OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int positon, long arg3) {
				CheckBox box = (CheckBox) view.findViewById(R.id.chk_selectone);
				if (box.isChecked()) {
					box.setChecked(false);
				} else {
					box.setChecked(true);
				}
			}
		}

		public static boolean getisMultiChoice() {
			return isMultiChoice;
		}

		public static String getcontact_name() {
			return contact_name;
		}
	}
}
