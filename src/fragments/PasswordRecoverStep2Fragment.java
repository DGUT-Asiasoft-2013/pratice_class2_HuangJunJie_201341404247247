package fragments;

import com.example.hello.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import inputcells.SimpleTextInputCellFragment;

public class PasswordRecoverStep2Fragment extends Fragment {
	SimpleTextInputCellFragment verify;
	SimpleTextInputCellFragment password;
	SimpleTextInputCellFragment password_repeat;
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
			
			verify=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);
			password=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
			password_repeat=(SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
		}		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		verify.setLabelText("�û���");
		verify.setHintText("�������û���");
		
		password.setLabelText("������");
		password.setHintText("������������");
		password.setIsPassword(true);
	
		password_repeat.setLabelText("ȷ������");
		password_repeat.setHintText("������ȷ������");
		password_repeat.setIsPassword(true);
	}
}
