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

public class Co_List_Adapter extends RecyclerView.Adapter<Co_List_Adapter.MyViewHolder> implements Filterable {

    private List<Co_List_ReportClass> configureLmList;
    private List<Co_List_ReportClass> configureLmListFiltered;
    private Co_List_Adapter.ContactsAdapterListener listener;

    public Context context;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        final TextView getPHONE_NO;
        final TextView getMOBILE_NO;
        final TextView textViewgetEmail;
        final TextView getUpdatedOn;
        final TextView getCoApprovedDate;
        final TextView coRemarks;



        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            getPHONE_NO = view.findViewById(R.id.getPHONE_NO);
            getMOBILE_NO = view.findViewById(R.id.getMOBILE_NO);
            textViewgetEmail = view.findViewById(R.id.textViewgetEmail);
            getUpdatedOn = view.findViewById(R.id.getUpdatedOn);
            getCoApprovedDate = view.findViewById(R.id.getCoApprovedDate);
            coRemarks = view.findViewById(R.id.coRemarks);




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback

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

    Co_List_Adapter(Context context, List<Co_List_ReportClass> configureLmList, Co_List_Adapter.ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.configureLmList = configureLmList;
        this.configureLmListFiltered = configureLmList;
    }


    public interface ContactsAdapterListener {
        void onContactSelected(Co_List_ReportClass contact);
    }

    @Override
    public Co_List_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lm_worklist_rejection_report_class, parent, false);

        return new Co_List_Adapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Co_List_Adapter.MyViewHolder holder, final int position) {
        final Co_List_ReportClass co_list_reportClass = configureLmListFiltered.get(position);


        holder.getPHONE_NO.setText(co_list_reportClass.getPHONE_NO());
        holder.getMOBILE_NO.setText(co_list_reportClass.getMOBILE_NO());
        holder.textViewgetEmail.setText(co_list_reportClass.getEMAIL_ID());
        holder.coRemarks.setText(co_list_reportClass.getCO_REMARKS());
        holder.getUpdatedOn.setText(co_list_reportClass.getUPDATED_ON());
        holder.getCoApprovedDate.setText(co_list_reportClass.getCO_APPROVED_DATE());




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
                    List<Co_List_ReportClass> filteredList = new ArrayList<>();
                    for (Co_List_ReportClass row : configureLmList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPHONE_NO().toLowerCase().contains(charString.toLowerCase())
                                || row.getMOBILE_NO().contains(charString.toUpperCase())
                                || row.getCO_REMARKS().contains(charString.toLowerCase())) {
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
                configureLmListFiltered = (ArrayList<Co_List_ReportClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
