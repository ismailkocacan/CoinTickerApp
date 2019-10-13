package com.app.binancealarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterAlarmRuleBuy
       extends ArrayAdapter<AlarmRule>
       implements View.OnClickListener {

    private Context mContext;
    private List<AlarmRule> mList;

    public AdapterAlarmRuleBuy(Context context, int resource,List<AlarmRule> list) {
        super(context, resource, list);
        mContext = context;
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row = inflater.inflate(R.layout.listview_rowitem_alarmrule, parent, false);

        AlarmRule alarmRule = mList.get(position);
        TextView txtValue = row.findViewById(R.id.textViewValue);
        ImageView imageViewAlarmRule = row.findViewById(R.id.imageViewAlarmRule);
        row.findViewById(R.id.farkLayout).setVisibility(View.INVISIBLE);

        txtValue.setText("");
        if (alarmRule.getValueAsDouble() != null){
            String fmtValue = AppFmt.formatPrice(alarmRule);
            txtValue.setText(fmtValue);
        }

        ImageButton btnDeleteAlarm = row.findViewById(R.id.btnDeleteAlarm);
        btnDeleteAlarm.setTag(position);
        btnDeleteAlarm.setOnClickListener(this);

        int resourceId = alarmRule.getAlarmOperator().getImageResourceId();
        imageViewAlarmRule.setBackgroundResource(resourceId);
        return row;
    }

    @Override
    public void onClick(View view) {
        try{
            int index = Integer.parseInt(view.getTag().toString());
            int id = mList.get(index).getId();
            mList.remove(index);
            App.getDbSetting().deleteById(id);
            notifyDataSetChanged();
        }catch (Exception e){
            App.showMessage(e.getMessage());
        }
    }
}