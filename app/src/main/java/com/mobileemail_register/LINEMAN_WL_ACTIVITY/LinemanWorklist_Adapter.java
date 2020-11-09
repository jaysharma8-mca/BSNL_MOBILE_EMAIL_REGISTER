package com.mobileemail_register.LINEMAN_WL_ACTIVITY;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileemail_register.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"Convert2Lambda"})
public class LinemanWorklist_Adapter extends RecyclerView.Adapter<LinemanWorklist_Adapter.MyViewHolder> implements Filterable {

    private final List<LinemanWorklist_ReportClass> worklist;
    private List<LinemanWorklist_ReportClass> worklistFiltered;
    private final ContactsAdapterListener listener;

    private AlertDialog alert;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView getName;
        final TextView getOS;
        final TextView getPHONE_NO;
        final TextView getMOBILE_NO;
        final TextView getADDRESS;
        final TextView getPillarData;
        final TextView getPHONE_NO_Status;
        final ImageView imageViewCall;
        final RelativeLayout relativeLayoutOne;



        MyViewHolder(View view) {
            super(view);
            getName = view.findViewById(R.id.getName);
            getOS = view.findViewById(R.id.getOS);
            getPHONE_NO = view.findViewById(R.id.getPHONE_NO);
            getPHONE_NO_Status = view.findViewById(R.id.getPHONE_NO_Status);
            getMOBILE_NO = view.findViewById(R.id.getMOBILE_NO);
            getADDRESS = view.findViewById(R.id.getADDRESS);
            getPillarData = view.findViewById(R.id.getPillarData);
            imageViewCall = view.findViewById(R.id.imageViewCall);
            relativeLayoutOne = view.findViewById(R.id.relativeLayoutOne);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(worklistFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    LinemanWorklist_Adapter(List<LinemanWorklist_ReportClass> worklist, ContactsAdapterListener listener) {
        this.listener = listener;
        this.worklist = worklist;
        this.worklistFiltered = worklist;
    }

    public interface ContactsAdapterListener {
        void onContactSelected(LinemanWorklist_ReportClass contact);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linemanworklist_report_class, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final LinemanWorklist_ReportClass linemanWorklistReportClass = worklistFiltered.get(position);

        String std_code = (linemanWorklistReportClass.getStdCode());
        String serviceOperStatus = linemanWorklistReportClass.getServiceOperStatus();
        holder.getName.setText(linemanWorklistReportClass.getCustomerName());
        holder.getOS.setText(linemanWorklistReportClass.getOsAmount());
        holder.getPHONE_NO.setText(std_code+"-"+linemanWorklistReportClass.getPhoneNo());
        holder.getMOBILE_NO.setText(linemanWorklistReportClass.getMobile());
        holder.getADDRESS.setText(linemanWorklistReportClass.getAddress());
        holder.getPillarData.setText(linemanWorklistReportClass.getAreaCode());
        holder.getPHONE_NO_Status.setText("("+serviceOperStatus+")");


        /*int strokeWidth = 5;
        int strokeColor = Color.parseColor("#FF028678");
        int fillColor = Color.parseColor("#FF028678");
        GradientDrawable gD = new GradientDrawable();
        gD.setColor(fillColor);
        gD.setShape(GradientDrawable.OVAL);
        gD.setStroke(strokeWidth, strokeColor);
        holder.imageViewCall.setBackground(gD);
        holder.imageViewCall.setImageResource(R.drawable.phone_receiver);*/

        if(serviceOperStatus.equals("Active"))
        {
            holder.getPHONE_NO_Status.setTextColor(Color.parseColor("#81c639"));
        }

        else
        {
            holder.getPHONE_NO_Status.setTextColor(Color.parseColor("#ce3146"));
        }



        holder.imageViewCall.setOnClickListener(v -> {
            String LL_NO  = holder.getPHONE_NO.getText().toString().trim();
            String MOB_NO = holder.getMOBILE_NO.getText().toString().trim();

            final AlertDialog.Builder build = new AlertDialog.Builder(v.getContext());

            build.setTitle("CALLING OPTIONS");
            build.setIcon(R.drawable.phone_receiver);


            final String[] call_options = new String[]{
                    "LANDLINE NO"+ " " + "-" + LL_NO,
                    "MOBILE NO" + " " + "-" + MOB_NO
            };

            // Item click listener

            build.setSingleChoiceItems(call_options, // Items list
                    -1, // Index of checked item (-1 = no selection)
                    (dialogInterface, i) -> {
                        // Get the alert dialog selected item's text
                        String selectedItem = Arrays.asList(call_options).get(i);

                        if (selectedItem.equals("LANDLINE NO"+ " " + "-" + LL_NO)) {


                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            String p = "tel:" + LL_NO;
                            intent.setData(Uri.parse(p));
                            v.getContext().startActivity(intent);
                            alert.dismiss();


                        } else if (selectedItem.equals("MOBILE NO" + " " + "-" + MOB_NO)) {

                            if (!holder.getMOBILE_NO.getText().toString().trim().equals("NA")) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                String p = "tel:" + MOB_NO;
                                intent.setData(Uri.parse(p));
                                v.getContext().startActivity(intent);
                                alert.dismiss();
                            } else {
                                Toast.makeText(v.getContext(),"Call Not Possible !!!",Toast.LENGTH_LONG).show();
                            }

                        }
                    });

            build.setNegativeButton("Cancel", (dialogInterface, i) -> alert.dismiss());

            // Create the alert dialog
            alert = build.create();

            // Finally, display the alert dialog
            alert.show();

        });

    }

    @Override
    public int getItemCount() {
        return worklistFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    worklistFiltered = worklist;
                } else {
                    List<LinemanWorklist_ReportClass> filteredList = new ArrayList<>();
                    for (LinemanWorklist_ReportClass row : worklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPhoneNo().contains(charString.toLowerCase())
                                || row.getAddress().contains(charString.toUpperCase())
                                || row.getMobile().contains(charString.toLowerCase())
                                || row.getOsAmount().contains(charString.toLowerCase())
                                || row.getAreaCode().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    worklistFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = worklistFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                worklistFiltered = (ArrayList<LinemanWorklist_ReportClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
