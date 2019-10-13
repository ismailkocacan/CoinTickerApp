package com.app.binancealarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

interface FragmentDialogAlarmHandler {
    boolean onPost(Bundle bundle);
}

public class FragmentDialogAlarm
       extends DialogFragment
       implements View.OnClickListener,
                  AdapterView.OnItemSelectedListener{

    private EditText edtFiyat, edtFark;
    private Spinner mSpinner;
    private Button mBtnOK;
    private ArrayAdapter<String> spinnerAdapter;
    private FragmentAlarmBase mFragmentAlarmBase;
    private AlarmOperator mAlarmOperator;
    private String dialogTitle;

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public FragmentDialogAlarm(FragmentAlarmBase baseFragment){
        mFragmentAlarmBase = baseFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_alarm,container, false);
        if (mFragmentAlarmBase.getAlarmPriceType() == null) return view;
        if (!mFragmentAlarmBase.getAlarmPriceType().equals(AlarmPriceType.LAST_ORDER)){
            view.findViewById(R.id.edtFark).setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtFiyat = getView().findViewById(R.id.edtFiyat);
        edtFark = getView().findViewById(R.id.edtFark);
        mSpinner = getView().findViewById(R.id.spinnerOperator);
        ((TextView)getView().findViewById(R.id.dialogTitle)).setText(dialogTitle);
        edtFiyat.requestFocus();

        spinnerAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item,AlarmOperator.OPERATOR_TEXTS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);

        mSpinner.setOnItemSelectedListener(this);
        mBtnOK = getView().findViewById(R.id.btnOK);
        mBtnOK.setOnClickListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mAlarmOperator = AlarmOperator.values()[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (mFragmentAlarmBase == null) return;
        if (!validate()) return;
        boolean result = true;
        FragmentDialogAlarmHandler handler = mFragmentAlarmBase.getHandler();
        if (handler != null) result = handler.onPost(createBundle());
        if (result) FragmentDialogAlarm.this.dismiss();
    }

    private Bundle createBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("coinName", mFragmentAlarmBase.getSymbol());
        bundle.putString("operator", mAlarmOperator.name());
        bundle.putString("alarmPriceType", mFragmentAlarmBase.getAlarmPriceType().name());
        bundle.putString("price",edtFiyat.getText().toString());
        bundle.putString("diff",edtFark.getText().toString());
        return bundle;
    }

    private boolean validate(){
        boolean result = true;
        if (mFragmentAlarmBase.getAlarmPriceType().equals(AlarmPriceType.LAST_ORDER)){
            boolean isEmptyFiyat = edtFiyat.getText().toString().trim().isEmpty();
            boolean isEmptyFark = edtFark.getText().toString().trim().isEmpty();
            if (isEmptyFiyat && isEmptyFark){
                edtFiyat.setError(App.getString(R.string.appConstDialogRequiredPriceOrChange));
                edtFark.setError(App.getString(R.string.appConstDialogRequiredPriceOrChange));
                result = false;
            }else{
                edtFiyat.setError(null);
            }
        }else{
            boolean isEmptyFiyat = edtFiyat.getText().toString().trim().isEmpty();
            if (isEmptyFiyat){
                edtFiyat.setError(App.getString(R.string.appConstDialogRequiredPrice));
                result = false;
            }else{
                edtFiyat.setError(null);
            }
        }
        return result;
    }
}
