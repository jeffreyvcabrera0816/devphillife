package com.tidalsolutions.phillife.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tidalsolutions.phillife.interfaces.DialogCallBack;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;


public class Dialog_Post extends DialogFragment {


	DialogCallBack dtfcb;
	int editMode;//

	TextView tv_edit, tv_delete, tv_report, tv_spam, tv_not, tv_offensive, tv_cancel;
	int id;


	public static Dialog_Post newInstance(DialogCallBack dtfcb, int editMode, int id){
		Dialog_Post dw = new Dialog_Post();
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

		if(editMode== CONSTANT.DIALOG_POST_OTHERS){
			tv_report.setVisibility(View.VISIBLE);
		}
		else if(editMode== CONSTANT.DIALOG_POST_OWN){
			tv_edit.setVisibility(View.VISIBLE);
			tv_delete.setVisibility(View.VISIBLE);
		}
		else if(editMode==CONSTANT.DIALOG_SHOW_REPORT){
			tv_offensive.setVisibility(View.VISIBLE);
			tv_not.setVisibility(View.VISIBLE);
			tv_spam.setVisibility(View.VISIBLE);
		}
		else if(editMode==CONSTANT.DIALOG_SHOW_REPORT_COMMENT){
			tv_offensive.setVisibility(View.VISIBLE);
			tv_not.setVisibility(View.VISIBLE);
			tv_spam.setVisibility(View.VISIBLE);
		}
		else if(editMode==CONSTANT.DIALOG_SHOW_REPORT_COMMENT_SELF){

			tv_delete.setVisibility(View.VISIBLE);
		}
	}
	
	 @SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 Rect displayRectangle = new Rect();
		 Window window = getActivity().getWindow();
		 window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
		 
		    AlertDialog.Builder dshow = new AlertDialog.Builder(getActivity());

		    LayoutInflater inflater = getActivity().getLayoutInflater();
		    View v= inflater.inflate(R.layout.dialog_post, null);
		    
		    //v.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
		    //v.setMinimumHeight((int) (displayRectangle.height() * 0.9f));

		    
		    dshow.setView(v);


		 	tv_edit = (TextView) v.findViewById(R.id.tv_edit);
		 	tv_delete = (TextView) v.findViewById(R.id.tv_delete);
		  	tv_report = (TextView) v.findViewById(R.id.tv_report);
		 	tv_spam = (TextView) v.findViewById(R.id.tv_spam);
		 	tv_not = (TextView) v.findViewById(R.id.tv_not_interested);
		 	tv_offensive = (TextView) v.findViewById(R.id.tv_offensive);
			tv_cancel = (TextView) v.findViewById(R.id.tv_cancel);


		 	tv_cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Dialog_Post.this.getDialog().cancel();

				}
			});
			 tv_delete.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 Dialog_Post.this.getDialog().cancel();
					 //remove comment and remove post
					 if(editMode==CONSTANT.DIALOG_SHOW_REPORT_COMMENT_SELF){
						 dtfcb.dialogfunction(CONSTANT.DIALOG_ACTION_REMOVE_COMMENT, 0, null, null);
					 } else
						dtfcb.dialogfunction(CONSTANT.DIALOG_ACTION_DELETE, 0, null, null);
				 }
			 });
			 tv_edit.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 Dialog_Post.this.getDialog().cancel();
					 dtfcb.dialogfunction(CONSTANT.DIALOG_ACTION_EDIT,0,null,null);
				 }
			 });
			 tv_report.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 Dialog_Post.this.getDialog().cancel();
					 Dialog_Post.newInstance(dtfcb, CONSTANT.DIALOG_SHOW_REPORT,0).show(getFragmentManager(), null);

				 }
			 });
			 //offensive 1, spam 2, uninterested 3
			 tv_offensive.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 Dialog_Post.this.getDialog().cancel();
				 int action=0;
				 if(editMode==CONSTANT.DIALOG_SHOW_REPORT_COMMENT){
					 action=CONSTANT.DIALOG_ACTION_REPORT_COMMENT;
				 }
				 else
					 action=CONSTANT.DIALOG_ACTION_REPORT;


				 	dtfcb.dialogfunction(action, 1, null, null);
			 }
		 	 });
			 tv_spam.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 Dialog_Post.this.getDialog().cancel();
				 int action=0;
				 if(editMode==CONSTANT.DIALOG_SHOW_REPORT_COMMENT){
					 action=CONSTANT.DIALOG_ACTION_REPORT_COMMENT;
				 }
				 else
					 action=CONSTANT.DIALOG_ACTION_REPORT;


				 dtfcb.dialogfunction(action, 2, null, null);
			 }});
			 tv_not.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 Dialog_Post.this.getDialog().cancel();
				 int action=0;
				 if(editMode==CONSTANT.DIALOG_SHOW_REPORT_COMMENT){
					 action=CONSTANT.DIALOG_ACTION_REPORT_COMMENT;
				 }
				 else
					 action=CONSTANT.DIALOG_ACTION_REPORT;


				 dtfcb.dialogfunction(action, 3, null, null);
			 }
			 });

		 	setVisibility();


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


