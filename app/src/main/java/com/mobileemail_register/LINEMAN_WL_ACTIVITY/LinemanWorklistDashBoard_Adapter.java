package com.mobileemail_register.LINEMAN_WL_ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileemail_register.R;

import java.util.ArrayList;
import java.util.List;

public class LinemanWorklistDashBoard_Adapter extends RecyclerView.Adapter<LinemanWorklistDashBoard_Adapter.MyViewHolder> implements Filterable {

    private List<LinemanWorklistDashBoard_ReportClass> configureLmList;
    private List<LinemanWorklistDashBoard_ReportClass> configureLmListFiltered;
    private LinemanWorklistDashBoard_Adapter.ContactsAdapterListener listener;


    public Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        final TextView textViewLmCode;
        final TextView textViewLmCount;
        final TextView textViewAreaCode;
        final TextView textViewExchangeCode;
        //final TextView textViewCustomerCategory;




        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            textViewLmCode = view.findViewById(R.id.textViewLmCode);
            textViewLmCount = view.findViewById(R.id.textViewLmCount);
            textViewAreaCode = view.findViewById(R.id.textViewAreaCode);
            textViewExchangeCode = view.findViewById(R.id.textViewExchangeCode);
            //textViewCustomerCategory = view.findViewById(R.id.textViewCustomerCategory);





            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(configureLmListFiltered.get(getAdapterPosition()));
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    //listener.onContactSelected(configureLmListFiltered.get(getAdapterPosition()));
                    //view.setBackgroundColor(ContextCompat.getColor(context, R.color.test));
                    return false;
                }
            });
        }
    }

    LinemanWorklistDashBoard_Adapter(Context context, List<LinemanWorklistDashBoard_ReportClass> configureLmList, LinemanWorklistDashBoard_Adapter.ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.configureLmList = configureLmList;
        this.configureLmListFiltered = configureLmList;
    }

    public interface ContactsAdapterListener {
        void onContactSelected(LinemanWorklistDashBoard_ReportClass contact);
    }

    @Override
    public LinemanWorklistDashBoard_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linemanworklist_dashboard_report_class, parent, false);

        return new LinemanWorklistDashBoard_Adapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LinemanWorklistDashBoard_Adapter.MyViewHolder holder, final int position) {
        final LinemanWorklistDashBoard_ReportClass linemanWorklistDashBoard_reportClass = configureLmListFiltered.get(position);



        holder.textViewLmCode.setText(linemanWorklistDashBoard_reportClass.getASSIGNED_LM());
        holder.textViewLmCount.setText(linemanWorklistDashBoard_reportClass.getASSIGNED_LM_COUNT());
        holder.textViewAreaCode.setText(linemanWorklistDashBoard_reportClass.getAREA_CODE());
        holder.textViewExchangeCode.setText(linemanWorklistDashBoard_reportClass.getEXCHANGE_CODE());
        //holder.textViewCustomerCategory.setText(linemanWorklistDashBoard_reportClass.getCUSTOMER_CATEGORY());


        /*holder.textViewAssignedLmCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = context.getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(PRIMARY_LM_CODE,holder.textViewPrimaryLmCode.getText().toString());
                editor.apply();

                Intent intent = new Intent(context,SdeFieldList_FetchReport_Activity.class);
                context.startActivity(intent);
                //Toast.makeText(context,holder.textViewPrimaryLmCode.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });*/
        //holder.textViewDate.setText(sdeConfiguredLm_reportClass.getASSIGNED_DATE());


    }

    @Override
    public int getItemCount() {
        return configureLmListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    configureLmListFiltered = configureLmList;
                } else {
                    List<LinemanWorklistDashBoard_ReportClass> filteredList = new ArrayList<>();
                    for (LinemanWorklistDashBoard_ReportClass row : configureLmList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                    }

                    configureLmListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = configureLmListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                configureLmListFiltered = (ArrayList<LinemanWorklistDashBoard_ReportClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}