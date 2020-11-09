package com.mobileemail_register.SDEFIELDLIST_ASSIGNED_ACTIVITY;

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

public class SdeConfiguredLm_Adapter extends RecyclerView.Adapter<SdeConfiguredLm_Adapter.MyViewHolder> implements Filterable {

    private final List<SdeConfiguredLm_ReportClass> configureLmList;
    private List<SdeConfiguredLm_ReportClass> configureLmListFiltered;
    private final SdeConfiguredLm_Adapter.ContactsAdapterListener listener;


    public Context context;

    /*private static final String SP_MEREG = "SP_MEREG";*/
    /*private static final String PRIMARY_LM_CODE = "PRIMARY_LM_CODE";*/
    /*private SharedPreferences sharedPreferences;*/


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        final TextView textViewPrimaryLmCode;
        final TextView textViewAssignedLmCode;
        final TextView textViewDate;



        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            textViewPrimaryLmCode = view.findViewById(R.id.textViewPrimaryLmCode);
            textViewAssignedLmCode = view.findViewById(R.id.textViewAssignedLmCode);
            textViewDate = view.findViewById(R.id.textViewDate);



            view.setOnClickListener(view12 -> {
                // send selected contact in callback
                listener.onContactSelected(configureLmListFiltered.get(getAdapterPosition()));
            });

            view.setOnLongClickListener(view1 -> {

                //listener.onContactSelected(configureLmListFiltered.get(getAdapterPosition()));
                //view.setBackgroundColor(ContextCompat.getColor(context, R.color.test));
                return false;
            });
        }
    }

    SdeConfiguredLm_Adapter(Context context, List<SdeConfiguredLm_ReportClass> configureLmList, SdeConfiguredLm_Adapter.ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.configureLmList = configureLmList;
        this.configureLmListFiltered = configureLmList;
    }

    public interface ContactsAdapterListener {
        void onContactSelected(SdeConfiguredLm_ReportClass contact);
    }

    @Override
    public SdeConfiguredLm_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.configured_lm_report_class, parent, false);

        return new SdeConfiguredLm_Adapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SdeConfiguredLm_Adapter.MyViewHolder holder, final int position) {
        final SdeConfiguredLm_ReportClass sdeConfiguredLm_reportClass = configureLmListFiltered.get(position);



        holder.textViewPrimaryLmCode.setText(sdeConfiguredLm_reportClass.getPRIMARY_LM());
        holder.textViewAssignedLmCode.setText(sdeConfiguredLm_reportClass.getASSIGNED_LM());
        holder.textViewDate.setText(sdeConfiguredLm_reportClass.getPER_NO());

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
                    List<SdeConfiguredLm_ReportClass> filteredList = new ArrayList<>();
                    for (SdeConfiguredLm_ReportClass row : configureLmList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPRIMARY_LM().toLowerCase().contains(charString.toLowerCase()))
                                /*|| row.getASSIGNED_DATE().contains(charString.toLowerCase()))*/ {
                            filteredList.add(row);
                        }
                    }

                    configureLmListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = configureLmListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                configureLmListFiltered = (ArrayList<SdeConfiguredLm_ReportClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}