package cn.waps.extend;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.waps.AppConnect;
import cn.waps.SDKUtils;

public class QuitPopAd {

	private static AlertDialog.Builder dialog;

	private static QuitPopAd quitPopAd;

	public static QuitPopAd getInstance() {
		if (quitPopAd == null) {
			quitPopAd = new QuitPopAd();
		}
		return quitPopAd;
	}

	/**
	 * 展示退屏广告
	 * 
	 * @param context
	 */
	public void show(final Context context) {

		dialog = new AlertDialog.Builder(context);// 第二个样式参数,可根据自己应用或游戏中的布局进行设置
		// 判断插屏广告是否已初始化完成，用于确定是否能成功调用插屏广告
		if (AppConnect.getInstance(context).hasPopAd(context)) {
			View view = null;
			if (((Activity) context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				view = getQuitView_Portrait(context);
			} else {
				// view = getQuitView_Landscape(context, dialog);
			}
			if (view != null) {
				// dialog.setTitle("确定要退出吗？");
				// dialog.setView(view);
				showCustomDialog(context, view);

			} else {
				new AlertDialog.Builder(context)
						.setTitle(R.string.exit_prompt)
						.setMessage(R.string.exit_msg)
						.setPositiveButton(R.string.ok,
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (dialog != null) {
											dialog.cancel();
										}
										((Activity) context).finish();
									}
								})
						.setNegativeButton(R.string.cancel,
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								}).create().show();

			}
		} else {
			new AlertDialog.Builder(context)
					.setTitle(R.string.exit_prompt)
					.setMessage(R.string.exit_msg)
					.setPositiveButton(R.string.ok,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (dialog != null) {
										dialog.cancel();
									}
									((Activity) context).finish();
								}
							})
					.setNegativeButton(R.string.cancel,
							new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create().show();
		}
	}

	/**
	 * 关闭退屏广告对话框
	 */
	/*
	 * public void close(){ if(dialog != null && dialog.isShowing()){
	 * dialog.cancel(); } }
	 */

	/**
	 * 获取竖屏样式的退出布局
	 * 
	 * @param context
	 * @param dialog
	 *            加载退出布局的dialog
	 * @return
	 */
	private LinearLayout getQuitView_Portrait(final Context context) {
		// 对小屏手机进行屏幕判断
		int displaySize = SDKUtils.getDisplaySize(context);

		// 最外层布局
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		layout.setBackgroundColor(Color.argb(80, 255, 255, 255));
		layout.setGravity(Gravity.CENTER);
		layout.setOrientation(LinearLayout.VERTICAL);

		// 用于排放标题，popAd的布局
		final RelativeLayout r_layout = new RelativeLayout(context);
		r_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		r_layout.setGravity(Gravity.CENTER);

		// 获取插屏布局
		LinearLayout pop_layout = AppConnect.getInstance(context).getPopAdView(
				context);

		if (pop_layout == null) {
			return null;
		}

		pop_layout.setBackgroundColor(Color.argb(200, 255, 255, 255));
		pop_layout.setId((int) (System.currentTimeMillis() + 1));
		pop_layout.setPadding(5, 0, 5, 0);

		// 按钮组布局

		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_LEFT, pop_layout.getId());
		params1.addRule(RelativeLayout.ALIGN_RIGHT, pop_layout.getId());

		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// params2.addRule(RelativeLayout.BELOW, title_layout.getId());

		// r_layout.addView(title_layout, params1);
		r_layout.addView(pop_layout, params2);

		// 用于排放r_layout(标题和popAd布局)和按钮的布局
		LinearLayout l_layout = new LinearLayout(context);
		l_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		l_layout.setOrientation(LinearLayout.VERTICAL);
		l_layout.addView(r_layout);

		layout.addView(l_layout);

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				r_layout.removeAllViews();
			}
		});

		return layout;
	}

	/**
	 * 获取横屏样式的退出布局
	 * 
	 * @param context
	 * @param dialog
	 *            加载退出布局的dialog
	 * @return
	 */
	private LinearLayout getQuitView_Landscape(final Context context,
			final Dialog dialog) {

		// 设置标题布局的两个顶角为圆角
		float num = 10f;
		float[] outerR = new float[] { num, num, 0, 0, 0, 0, num, num };
		ShapeDrawable title_layout_shape = new ShapeDrawable(
				new RoundRectShape(outerR, null, null));
		title_layout_shape.getPaint().setColor(Color.argb(200, 10, 10, 10));

		// 设置按钮布局的两个底角为圆角
		float[] outerR2 = new float[] { 0, 0, num, num, num, num, 0, 0 };
		ShapeDrawable btn_layout_shape = new ShapeDrawable(new RoundRectShape(
				outerR2, null, null));
		btn_layout_shape.getPaint().setColor(Color.argb(200, 20, 20, 20));

		// 最外层布局
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		layout.setBackgroundColor(Color.argb(80, 0, 0, 0));
		layout.setGravity(Gravity.CENTER);
		layout.setOrientation(LinearLayout.HORIZONTAL);

		// 用于排放标题，popAd，按钮组的整体布局
		final RelativeLayout r_layout = new RelativeLayout(context);
		r_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		r_layout.setGravity(Gravity.CENTER);

		// 标题布局
		LinearLayout title_layout = new LinearLayout(context);
		TextView textView = new TextView(context);
		textView.setText("确定要退出吗？");
		textView.setTextSize(18);
		textView.setEms(1);
		textView.setTextColor(Color.WHITE);
		title_layout.setId((int) (System.currentTimeMillis()));
		title_layout.setPadding(10, 10, 10, 0);
		title_layout.setBackgroundDrawable(title_layout_shape);

		title_layout.addView(textView);

		LinearLayout pop_layout = null;
		// 获取插屏布局
		int height_full = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getHeight();

		int height_tmp = height_full - 75;// 75为设备状态栏加标题栏的高度

		int height = height_tmp - 55;// 55为自定义
		if (height_full <= 480) {
			pop_layout = AppConnect.getInstance(context).getPopAdView(context,
					height, height);
		} else {
			pop_layout = AppConnect.getInstance(context).getPopAdView(context);
		}

		if (pop_layout == null) {
			return null;
		}
		pop_layout.setBackgroundColor(Color.argb(200, 40, 40, 40));
		pop_layout.setId((int) (System.currentTimeMillis() + 1));
		pop_layout.setPadding(2, 0, 2, 0);

		// 按钮组布局
		LinearLayout btn_layout = new LinearLayout(context);
		btn_layout.setOrientation(LinearLayout.VERTICAL);
		btn_layout.setBackgroundDrawable(btn_layout_shape);
		btn_layout.setPadding(3, 8, 3, 3);
		btn_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));

		// 按钮布局中顶部的子布局
		LinearLayout top_layout = new LinearLayout(context);
		top_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		top_layout.setOrientation(LinearLayout.VERTICAL);
		top_layout.setGravity(Gravity.TOP);
		// 按钮布局中底部的子布局
		LinearLayout bottom_layout = new LinearLayout(context);
		bottom_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
		bottom_layout.setGravity(Gravity.BOTTOM);

		Button okButton = new Button(context);
		okButton.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		okButton.setText(" 退 出 ");
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.cancel();
				}
				((Activity) context).finish();
			}
		});

		Button cancelButton = new Button(context);
		cancelButton.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		cancelButton.setText(" 取 消 ");
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		Button moreButton = new Button(context);
		moreButton.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		moreButton.setText(" 更 多 ");
		moreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppConnect.getInstance(context).showOffers(context);
				if (dialog != null) {
					dialog.cancel();
				}
			}
		});

		top_layout.addView(okButton);
		top_layout.addView(cancelButton);

		bottom_layout.addView(moreButton);

		btn_layout.addView(top_layout);
		btn_layout.addView(bottom_layout);

		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_TOP, pop_layout.getId());
		params1.addRule(RelativeLayout.ALIGN_BOTTOM, pop_layout.getId());

		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.RIGHT_OF, title_layout.getId());

		r_layout.addView(title_layout, params1);
		r_layout.addView(pop_layout, params2);

		// 用于排放r_layout(标题和popAd布局)和按钮的布局
		LinearLayout l_layout = new LinearLayout(context);
		l_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		l_layout.setOrientation(LinearLayout.HORIZONTAL);
		l_layout.addView(r_layout);
		l_layout.addView(btn_layout);

		layout.addView(l_layout);

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				r_layout.removeAllViews();
			}
		});

		return layout;
	}

	private void showCustomDialog(final Context context, View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(R.string.exit_prompt)
				.setView(view)
				.setPositiveButton(R.string.ok,
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (dialog != null) {
									dialog.cancel();
								}
								((Activity) context).finish();
							}
						})
				.setNegativeButton(R.string.cancel,
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});

		String value = AppConnect.getInstance(context).getConfig(
				"is_show_offer", "off");
		if ("on".equals(value)) {
			builder.setNeutralButton(R.string.more,
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							AppConnect.getInstance(context).showOffers(context);
							if (dialog != null)
								dialog.cancel();
						}
					});
		}
		builder.create().show();
	}

	private String getMateData(Context context, String key) {
		String myApiKey = null;
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;

			myApiKey = bundle.getString(key);
			System.out.println("#################" + myApiKey);

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myApiKey;
	}

	private class ShowQuitPopAdTask extends AsyncTask<Void, Void, LinearLayout> {
		Context context;
		Dialog dialog;

		ShowQuitPopAdTask(Context context, Dialog dialog) {
			this.context = context;
			this.dialog = dialog;
		}

		@Override
		protected LinearLayout doInBackground(Void... params) {

			LinearLayout pop_layout = null;

			if (((Activity) context).getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				pop_layout = getQuitView_Portrait(context);
			} else {
				pop_layout = getQuitView_Landscape(context, dialog);
			}

			return pop_layout;
		}

		@Override
		protected void onPostExecute(LinearLayout result) {
			if (result != null) {
				dialog.setContentView(result);
				dialog.show();
			}
		}
	}

}
