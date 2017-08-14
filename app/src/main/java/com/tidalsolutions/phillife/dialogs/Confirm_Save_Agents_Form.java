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
import android.widget.Button;

import com.tidalsolutions.phillife.interfaces.DialogCallBack;
import com.tidalsolutions.phillife.phillife.R;
import com.tidalsolutions.phillife.utils.CONSTANT;


public class Confirm_Save_Agents_Form extends DialogFragment {


	DialogCallBack dtfcb;
	int editMode;//
	Button cancel;

	Button confirm;
	int id;


	public static Confirm_Save_Agents_Form newInstance(DialogCallBack dtfcb, int editMode, int id){
		Confirm_Save_Agents_Form dw = new Confirm_Save_Agents_Form();
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
		    View v= inflater.inflate(R.layout.confirm_save_agents_form_dialog, null);
		    
		    //v.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
		    //v.setMinimumHeight((int) (displayRectangle.height() * 0.9f));

		    
		    dshow.setView(v);



			confirm = (Button) v.findViewById(R.id.button_password);
			cancel = (Button) v.findViewById(R.id.button_cancel);

		 	cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Confirm_Save_Agents_Form.this.getDialog().cancel();
					dtfcb.dialogfunction(CONSTANT.DIALOG_AGENT_ACTION_REVOKE, 0, null, null);
				}
			});
		 	confirm.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 Confirm_Save_Agents_Form.this.getDialog().cancel();
					 dtfcb.dialogfunction(CONSTANT.DIALOG_AGENT_ACTION_CONFIRM,0,null,null);
							 }
			 });







		     Dialog d =  dshow.create();
			 d.setCancelable(true);
		     
		    return d;
		  }
	
	
	
	
	
}


