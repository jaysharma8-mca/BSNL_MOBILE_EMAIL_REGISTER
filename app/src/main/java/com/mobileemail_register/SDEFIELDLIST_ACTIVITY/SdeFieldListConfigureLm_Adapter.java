package com.mobileemail_register.SDEFIELDLIST_ACTIVITY;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileemail_register.LINEMAN_WL_ACTIVITY.LinemanWorklistDashBoard_Activity;
import com.mobileemail_register.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.mobileemail_register.MISC.constants.INSERT_FLAG;
import static com.mobileemail_register.MISC.constants.SYNC_STATUS_FAILED;

@SuppressWarnings({"Convert2Lambda"})
public class SdeFieldListConfigureLm_Adapter extends RecyclerView.Adapter<SdeFieldListConfigureLm_Adapter.MyViewHolder> implements Filterable {

    private final List<SdeFieldListConfigureLm_ReportClass> configureLmList;
    private List<SdeFieldListConfigureLm_ReportClass> configureLmListFiltered;

    public Context context;
    private final String[] spinnerValues = {"SELECT LMC"};
    private static final String SP_MEREG = "SP_MEREG";
    private static final String PERNOLM = "PERNOLM";
    private static final String SDELMWL = "SDELMWL";
    private SharedPreferences sharedPreferences;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;
        final TextView textViewLmCode;
        final TextView textViewPendingCount;
        final TextView textViewCompletedCount;
        final TextView textViewAssignLm;
        final Spinner spinnerAssignLm;
        final ImageView imageViewSyncStatus;


        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            textViewLmCode = view.findViewById(R.id.textViewLmCode);
            textViewPendingCount = view.findViewById(R.id.textViewPendingCount);
            textViewCompletedCount = view.findViewById(R.id.textViewCompletedCount);
            textViewAssignLm = view.findViewById(R.id.textViewAssignLm);
            spinnerAssignLm = view.findViewById(R.id.spinnerAssignLm);
            imageViewSyncStatus = view.findViewById(R.id.imageViewSyncStatus);



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

    SdeFieldListConfigureLm_Adapter(Context context, List<SdeFieldListConfigureLm_ReportClass> configureLmList) {
        this.context = context;
        this.configureLmList = configureLmList;
        this.configureLmListFiltered = configureLmList;
    }

    public interface ContactsAdapterListener {
        void onContactSelected(SdeFieldListConfigureLm_ReportClass contact);
    }

    @Override
    public SdeFieldListConfigureLm_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.configurelm_report_class, parent, false);

        return new SdeFieldListConfigureLm_Adapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SdeFieldListConfigureLm_Adapter.MyViewHolder holder, final int position) {
        final SdeFieldListConfigureLm_ReportClass sdeFieldListConfigureLm_reportClass = configureLmListFiltered.get(position);

        SdeFieldList_SQLiteHelper dbBackend = new SdeFieldList_SQLiteHelper(context);

        String lm_Perno = (sdeFieldListConfigureLm_reportClass.getLM_PERNO());
        holder.textViewAssignLm.setVisibility(View.INVISIBLE);
        holder.textViewLmCode.setText(sdeFieldListConfigureLm_reportClass.getLM_CODE());
        holder.textViewPendingCount.setText(sdeFieldListConfigureLm_reportClass.getTTL_CON());
        holder.textViewCompletedCount.setText(sdeFieldListConfigureLm_reportClass.getTGT());

        int sync_status = configureLmList.get(position).getFLAG();
        String sync_flag = configureLmList.get(position).getASSIGNED_FLAG();

        //Toast.makeText(context,""+sync_status,Toast.LENGTH_LONG).show();


        if (sync_status == SYNC_STATUS_FAILED && sync_flag.equals(INSERT_FLAG)) {
            holder.imageViewSyncStatus.setImageResource(R.drawable.sync);
            //Toast.makeText(context,""+sync_status,Toast.LENGTH_LONG).show();
        }


        String lm_code = holder.textViewLmCode.getText().toString().trim();

        if (lm_Perno != null) {
            holder.textViewLmCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String lm_perno = lm_Perno.substring(1);
                    sharedPreferences = context.getSharedPreferences(SP_MEREG, Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(PERNOLM, lm_perno);
                    editor.putString(SDELMWL, "SDELMWL");
                    editor.apply();

                    context.startActivity(new Intent(context,
                            LinemanWorklistDashBoard_Activity.class));
                    ((Activity) context).finishAffinity();


                    //Toast.makeText(context,lm_perno,Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Toast.makeText(context, "Per No Not Found !!!", Toast.LENGTH_LONG).show();
        }
        //String lm_perno = lm_code.length() > csCountryCode.length() ? phoneNumber.substring(0, csCountryCode.length()) : "";
        //
        String[] spinnerLists = dbBackend.SelectAllData();

            if (spinnerLists != null) {
                String[] both;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    both = Stream.concat(Arrays.stream(spinnerValues), Arrays.stream(spinnerLists)).toArray(String[]::new);

                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                            context, R.layout.custome_spinner_configurelm, both) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;

                            if (position == 0) {
                                tv.setTextColor(Color.GRAY);

                            } else {
                                tv.setTextColor(ContextCompat.getColor(context, R.color.colorText));
                            }
                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.custome_spinner_configurelm);


                    holder.spinnerAssignLm.setAdapter(spinnerArrayAdapter);
                }

                else
                {
                    ArrayList<String> stringArrayList = new ArrayList<>(Arrays.asList(spinnerValues));
                    stringArrayList.addAll(Arrays.asList(spinnerLists));
                    stringArrayList.toArray(new String[0]);


                    final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                           context, R.layout.custome_spinner_configurelm, stringArrayList) {
                        @Override
                        public boolean isEnabled(int position) {
                            return position != 0;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;

                            if (position == 0) {
                                tv.setTextColor(Color.GRAY);

                            } else {
                                tv.setTextColor(ContextCompat.getColor(context, R.color.colorText));
                            }
                            return view;
                        }
                    };
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.custome_spinner_configurelm);


                    holder.spinnerAssignLm.setAdapter(spinnerArrayAdapter);
                }

                holder.spinnerAssignLm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView tv = (TextView) view;
                        String lm_config = "ASSIGNED TO"+"/"+lm_code;
                        String assigned_flag = "I";
                        String sync_status_flag = "1";
                        if(position > 0)
                        {
                            String selectedItemText = (String) parent.getItemAtPosition(position);

                            int rowCount = dbBackend.getTaskCount(selectedItemText);

                            if (lm_code.equals(selectedItemText)) {
                                Toast.makeText(context, "Assignment Not Possible", Toast.LENGTH_LONG).show();
                                tv.setTextColor(Color.GRAY);
                            }

                            else if(rowCount > 0)
                            {

                                Toast.makeText(context,"Kindly DeAssign The WL of"+" "+selectedItemText+" "+"Before Reassignment",Toast.LENGTH_LONG).show();

                                //Toast.makeText(context,selectedItemText,Toast.LENGTH_LONG).show();
                                //Toast.makeText(context,""+rowCount,Toast.LENGTH_LONG).show();
                                //dbBackend.updateData(selectedItemText,lm_code,lm_config,assigned_flag,sync_status_flag);
                                //dbBackend.updateAssignedData(selectedItemText,lm_code,lm_config,deassigned_flag,sync_status_flag);

                            }


                            else
                            {

                                dbBackend.updateData(selectedItemText,lm_code,lm_config,assigned_flag,sync_status_flag);
                                //dbBackend.updateAssignedData(selectedItemText,lm_code,lm_config,deassigned_flag,sync_status_flag);
                                //dbBackend.updateData(selectedItemText,lm_code,lm_config,assigned_flag,sync_status_flag);
                                //Toast.makeText(context, lm_code+" "+selectedItemText, Toast.LENGTH_LONG).show();
                            }

                            context.startActivity(new Intent(context,
                                    SdeFieldListConfigureLm_Activity.class));
                            ((Activity) context).overridePendingTransition(0, 0);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

        else
        {
            holder.spinnerAssignLm.setVisibility(View.INVISIBLE);
            holder.textViewAssignLm.setVisibility(View.VISIBLE);
            holder.textViewLmCode.setText(sdeFieldListConfigureLm_reportClass.getLM_CODE());
            holder.textViewPendingCount.setText(sdeFieldListConfigureLm_reportClass.getTTL_CON());
            holder.textViewCompletedCount.setText(sdeFieldListConfigureLm_reportClass.getTGT());
            holder.textViewAssignLm.setText(sdeFieldListConfigureLm_reportClass.getLMC_ASSIGNED_TO_TEXT());
        }

        holder.spinnerAssignLm.setEnabled(holder.spinnerAssignLm.getCount() != 2);

        if(!holder.textViewAssignLm.getText().toString().trim().equals(""))
        {

            holder.textViewLmCode.setTextColor(Color.parseColor("#E47E71"));
            holder.textViewPendingCount.setTextColor(Color.parseColor("#E47E71"));
            holder.textViewCompletedCount.setTextColor(Color.parseColor("#E47E71"));
            holder.textViewAssignLm.setTextColor(Color.parseColor("#E47E71"));


        }


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
                    List<SdeFieldListConfigureLm_ReportClass> filteredList = new ArrayList<>();
                    for (SdeFieldListConfigureLm_ReportClass row : configureLmList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLM_CODE().toLowerCase().contains(charString.toLowerCase())
                                || row.getTTL_CON().contains(charString.toUpperCase())
                                || row.getTGT().contains(charString.toLowerCase())) {
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
                configureLmListFiltered = (ArrayList<SdeFieldListConfigureLm_ReportClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}