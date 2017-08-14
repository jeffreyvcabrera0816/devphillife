package com.tidalsolutions.phillife.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.tidalsolutions.phillife.interfaces.DialogCallBack;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;


public class Forgot_Password_Post extends DialogFragment {


	DialogCallBack dtfcb;
	int editMode;//
	Button cancel;
	EditText email;
	Button confirm;
	int id;


	public static Forgot_Password_Post newInstance(DialogCallBack dtfcb, int editMode, int id){
		Forgot_Password_Post dw = new Forgot_Password_Post();
		dw.setCancelable(false);
		Bundle b = new Bundle();
		b.putSerializable("dtfcb", dtfcb);
		b.putInt("editMode", editMode);
		b.putInt("id", id);
		dw.setArguments(b);
		return dw;
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.dtfcb = (DialogCallBack) getArguments().getSerializable("dtfcb");
		this.editMode= getArguments().getInt("editMode");
		this.id=getArguments().getInt("id");
	}
	
	void setVisibility(){


	}
	
	 @SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 Rect displayRectangle = new Rect();
		 Window window = getActivity().getWindow();
		 window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		 
		    AlertDialog.Builder dshow = new AlertDialog.Builder(getActivity());

		    LayoutInflater inflater = getActivity().getLayoutInflater();
		    View v= inflater.inflate(R.layout.forgot_password_dialog, null);
		    
		    //v.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
		    //v.setMinimumHeight((int) (displayRectangle.height() * 0.9f));

		    
		    dshow.setView(v);


		 	email = (EditText) v.findViewById(R.id.et_email);
			confirm = (Button) v.findViewById(R.id.button_password);
			cancel = (Button) v.findViewById(R.id.button_cancel);

		 	cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Forgot_Password_Post.this.getDialog().cancel();

				}
			});
		 	confirm.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 Forgot_Password_Post.this.getDialog().cancel();
					 //remove comment and remove post

						 dtfcb.dialogfunction(CONSTANT.DIALOG_FORGOT_PASSWORD, 0, email.getText().toString(), null);

				 }
			 });
			 confirm.setOnKeyListener(new View.OnKeyListener() {
				 @Override
				 public boolean onKey(View v, int keyCode, KeyEvent event) {
					 if (event.getAction() == KeyEvent.ACTION_DOWN) {
						 switch (keyCode) {
							 case KeyEvent.KEYCODE_DPAD_CENTER:
							 case KeyEvent.KEYCODE_ENTER:
								 confirm.performClick();
								 return true;
							 default:
								 break;
						 }
					 }
					 return false;

				 }
			 });




		/*
		    addFile = (Button) v.findViewById(R.id.fileSelector);
		    coupon = (EditText) v.findViewById(R.id.email);




		 cancelButton = (RelativeLayout) v.findViewById(R.id.cancelButton);
		 submitButton = (RelativeLayout) v.findViewById(R.id.submitButton);

		 cancelButton.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 DialogCallBack acb = (DialogCallBack) dtfcb;
				 acb.dialogfunction(0, 0, null, null);
			 }
		 });


		 submitButton.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {

				 DialogCallBack acb = (DialogCallBack) dtfcb;

				 //assign data to be sent for viewing
				 $coupon = coupon.getText().toString();



				 Object args[] = {$coupon};

				 acb.dialogfunction(1, 1, args, null);

				 Dialog_Post.this.getDialog().cancel();

			 }
		 });
*/

		     Dialog d =  dshow.create();
			 d.setCancelable(true);
		     
		    return d;
		  }
	
	
	
	
	
}


