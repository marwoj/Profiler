package com.example.hellojni;

import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class ClcButtonListener implements OnClickListener {
	ProgressDialog progress;

	static Context context;

	public static Context getContext() {
		return context;
	}

	@Override
	public void onClick(View view) {

		context = view.getContext();
		TextView tv = (TextView) view.getRootView()
				.findViewById(R.id.textView1);

		Integer func = getFuncSelection(view);
		Integer interrupts = getInterruptsNr(view);

		EditText maxEditText = ((EditText) view.getRootView().findViewById(
				R.id.max));
		Integer max = getIterationsNr(view, maxEditText);

		progress = ProgressDialog.show(view.getContext(), "Computing",
				"Please be patient...");

		// RunMultipleLibs runNative = new RunMultipleLibs();
		// runNative.execute(func, max, interrupts);
		//
		//
		// try {
		// tv.setText(runNative.get());
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// e.printStackTrace();
		// }

		RunMultipleThreads runThreads = new RunMultipleThreads();
		runThreads.execute(func, max, interrupts);

		try {
			tv.setText(runThreads.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	private Integer getIterationsNr(View view, EditText maxEditText) {
		Integer max = 0;
		try {
			max = Integer.valueOf(maxEditText.getText().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(
					view.getContext());
			builder.setTitle("Ups...");
			builder.setMessage("Too big value: "
					+ maxEditText.getText().toString());
			builder.setPositiveButton("Damn it!", null);
			builder.create().show();
		}
		return max;
	}

	private Integer getInterruptsNr(View view) {
		Integer interrupts = 1;
		if (((RadioButton) view.getRootView().findViewById(R.id.radio_i10))
				.isChecked()) {
			interrupts = 10;
		} else if (((RadioButton) view.getRootView().findViewById(
				R.id.radio_i100)).isChecked()) {
			interrupts = 100;
		} else if (((RadioButton) view.getRootView().findViewById(
				R.id.radio_i500)).isChecked()) {
			interrupts = 500;
		} else if (((RadioButton) view.getRootView().findViewById(
				R.id.radio_i1000)).isChecked()) {
			interrupts = 1000;
		} else if (((RadioButton) view.getRootView().findViewById(
				R.id.radio_i2000)).isChecked()) {
			interrupts = 2000;
		} else if (((RadioButton) view.getRootView().findViewById(
				R.id.radio_i4000)).isChecked()) {
			interrupts = 4000;
		}
		return interrupts;
	}

	private Integer getFuncSelection(View view) {
		Integer func = 1;
		if (((RadioButton) view.getRootView().findViewById(R.id.radio_fa))
				.isChecked()) {
			func = 1;
		} else if (((RadioButton) view.getRootView()
				.findViewById(R.id.radio_fb)).isChecked()) {
			func = 2;
		} else if (((RadioButton) view.getRootView()
				.findViewById(R.id.radio_fc)).isChecked()) {
			func = 3;
		} else if (((RadioButton) view.getRootView()
				.findViewById(R.id.radio_fd)).isChecked()) {
			func = 4;
		} else if (((RadioButton) view.getRootView()
				.findViewById(R.id.radio_fe)).isChecked()) {
			func = 5;
		}
		return func;
	}

	private class RunMultipleThreads extends
			AsyncTask<Integer, Integer, String> {
		long start;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			start = System.currentTimeMillis();
		}

		@Override
		protected String doInBackground(Integer... params) {
			NativeRunnable runA = new NativeRunnable(params[0],  params[1],
					params[2]);

			NativeRunnable runB = new NativeRunnable(params[0] - 1 , params[1],
					params[2]);


			Thread threadA = new Thread(runA);
			Thread threadB = new Thread(runB);


			try {
				threadA.join();
				threadB.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			threadA.start();
			threadB.start();

			return "Finished threads";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress.dismiss();

			AlertDialog.Builder builder = new AlertDialog.Builder(
					ClcButtonListener.getContext());
			long elapsed = System.currentTimeMillis() - start;
			int seconds = (int) (elapsed / 1000);
			int milis = (int) (elapsed % 1000);
			builder.setTitle("Finished!");
			builder.setMessage("Elapsed time: " + seconds + "s " + milis + "ms");
			builder.setPositiveButton("OK", null);
			builder.create().show();
		}

	}

	private class NativeRunnable implements Runnable {
		private int probeNr;
		private int max;
		private int funcSelect;

		public NativeRunnable(int funcSelect, int max, int probeNr) {
			this.funcSelect = funcSelect;
			this.max = max;
			this.probeNr = probeNr;
		}

		@Override
		public void run() {
			long start = System.currentTimeMillis();
			Connector cn = new Connector();
			String res = cn.firstTest(funcSelect, max, probeNr);
			Log.i("GPROF", "[gprof] Thread finished with result: " + res
					+ " Func nr: " + funcSelect);
			
			long elapsed = System.currentTimeMillis() - start;
			int seconds = (int) (elapsed / 1000);
			int milis = (int) (elapsed % 1000);
			Log.i("GPROF", "[gprof] elapsed time for func: " + funcSelect + " is " + seconds +"s "+milis + "ms");
		}
	}

	private class RunMultipleLibs extends AsyncTask<Integer, Integer, String> {

		long start;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			start = System.currentTimeMillis();
		}

		@Override
		protected String doInBackground(Integer... params) {
			Connector cn = new Connector();
			Debug.startMethodTracing("/storage/sdcard0/javaTrace.trace");
			String result = cn.firstTest(params[0], params[1], params[2]);
			String secondResult = cn.secondTest(params[0] - 1, params[1],
					params[2]);
			javaA();
			Debug.stopMethodTracing();
			return result + " " + secondResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress.dismiss();

			AlertDialog.Builder builder = new AlertDialog.Builder(
					ClcButtonListener.getContext());
			long elapsed = System.currentTimeMillis() - start;
			int seconds = (int) (elapsed / 1000);
			int milis = (int) (elapsed % 1000);
			builder.setTitle("Finished!");
			builder.setMessage("Elapsed time: " + seconds + "s " + milis + "ms");
			builder.setPositiveButton("OK", null);
			builder.create().show();
		}

	}

	private int maxVal = 21474831;

	public void javaA() {
		for (int i = 0; i < maxVal; i++)
			;
		javaD();
		javaC();
		javaB();
	}

	public void javaB() {
		for (int i = 0; i < maxVal; i++)
			;

	}

	public void javaC() {
		for (int i = 0; i < maxVal; i++)
			;
		javaB();
	}

	public void javaD() {
		for (int i = 0; i < maxVal; i++)
			;
		javaC();
		javaB();
	}
}
