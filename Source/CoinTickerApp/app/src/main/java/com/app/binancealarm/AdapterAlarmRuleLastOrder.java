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

public class AdapterAlarmRuleLastOrder
       extends ArrayAdapter<AlarmRuleLastOrder>
       implements View.OnClickListener {

    private Context mContext;
    private List<AlarmRuleLastOrder> mList;

    public AdapterAlarmRuleLastOrder(Context context, int resource,List<AlarmRuleLastOrder> list) {
        super(context, resource, list);
        mContext = context;
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row = inflater.inflate(R.layout.listview_rowitem_alarmrule, parent, false);

        AlarmRuleLastOrder alarmRule = mList.get(position);
        TextView txtValue = row.findViewById(R.id.textViewValue);
        TextView txtDifferent = row.findViewById(R.id.textViewFark);
        ImageView imageViewAlarmRule = row.findViewById(R.id.imageViewAlarmRule);

        row.findViewById(R.id.farkLayout).setVisibility(View.VISIBLE);

        txtValue.setText("");
        txtDifferent.setText("");
        if (alarmRule.getValueAsDouble() != null){
            String fmtValue = AppFmt.formatPrice(alarmRule);
            txtValue.setText(fmtValue);
        }
        if (alarmRule.getPriceChange() != null){
            String fmChange = AppFmt.format(AppFmt.DECIMAL_FMT_DIGIT2,
                                             alarmRule.getPriceChange(),
                                             AppConst.SIGN_PERCENT);
            txtDifferent.setText(fmChange);
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