package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Gor on 24.07.2017.
 */

class SimpleViewHolder extends RecyclerView.ViewHolder {

    private TextView currancyName;
    private TextView currancyRate;

    public SimpleViewHolder(View itemView) {
        super(itemView);

        currancyName = (TextView) itemView.findViewById(R.id.id_text_currency_name);
        currancyRate = (TextView) itemView.findViewById(R.id.id_text_exchanged_rate);
    }

    public void setCurrancyName(String currancyNameString){ currancyName.setText(currancyNameString); }
    public void setCurrancyRate(double currancyRateNumber){
        currancyRate.setText("1 = *" + currancyRateNumber); }
}
