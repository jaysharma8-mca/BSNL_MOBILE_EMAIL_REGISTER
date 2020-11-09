package com.mobileemail_register.SYNC_UNSYNC_ACTIVITY;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobileemail_register.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mobileemail_register.MISC.constants.SYNC_STATUS_OK;

public class UnsyncdData_Adapter extends RecyclerView.Adapter<UnsyncdData_Adapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<UnsyncdData_ReportClass> unsyncdData;
    private List<UnsyncdData_ReportClass> unsyncdDataFiltered;
    private ContactsAdapterListener listener;



    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMobileLLNo,textViewMobileNo, textViewEmailId;
        ImageView Sync_Status,imageViewKYCdoc;


        MyViewHolder(View view) {
            super(view);
            textViewMobileLLNo = view.findViewById(R.id.textViewMobileLLNo);
            textViewMobileNo = view.findViewById(R.id.textViewMobileNo);
            textViewEmailId = view.findViewById(R.id.textViewEmailId);
            imageViewKYCdoc = view.findViewById(R.id.imageViewKYCdoc);
            Sync_Status = view.findViewById(R.id.imgSync);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(unsyncdDataFiltered.get(getAdapterPosition()));
                }
            });


        }
    }

    public interface ContactsAdapterListener {
        void onContactSelected(UnsyncdData_ReportClass contact);
    }


    UnsyncdData_Adapter(Context context, List<UnsyncdData_ReportClass> unsyncdData, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.unsyncdData = unsyncdData;
        this.unsyncdDataFiltered = unsyncdData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unsyncddata_report_class, parent, false);

        return new MyViewHolder(itemView);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final UnsyncdData_ReportClass contact = unsyncdDataFiltered.get(position);

        UnsyncdData_ReportClass me_upd_lm_worklist_offline_data_report = unsyncdData.get(position);

        holder.textViewMobileLLNo.setText(contact.getPhoneNo());
        holder.textViewMobileNo.setText(contact.getMobileNo());
        holder.textViewEmailId.setText(contact.getEmailId());

        //String dateTime = contact.getDateTime();


        int sync_status = unsyncdData.get(position).getSync_status();

        //String update_type = unsyncdData.get(position).getUpdateType();


            byte[] kycDocImage = me_upd_lm_worklist_offline_data_report.getImage();

            if(kycDocImage != null)
            {
                Glide.with(context).load(kycDocImage).into(holder.imageViewKYCdoc);
            }

            else
            {
                Glide
                        .with(context)
                        .load(R.drawable.ic_go_green_image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_go_green_image)
                        .into(holder.imageViewKYCdoc);
            }
            //Bitmap bitmap = BitmapFactory.decodeByteArray(kycDocImage, 0, kycDocImage.length);
            //holder.imageViewKYCdoc.setImageBitmap(bitmap);


        /*if(update_type.equals("EMAIL ID"))
        {
            Glide.with(context).load(R.drawable.email_icon).asBitmap().into(holder.imageViewKYCdoc);
        }*/


        if(sync_status == SYNC_STATUS_OK)
        {
            holder.Sync_Status.setImageResource(R.drawable.ok);
        }

        else
        {
            holder.Sync_Status.setImageResource(R.drawable.sync);
        }






       /* holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sqLiteHelper = new ME_UPD_LM_SQLITE_HELPER(context);
                //Toast.makeText(context,"Selected"+position,Toast.LENGTH_LONG).show();

                CharSequence[] items = {"Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {

                            sqLiteHelper.deleteRow(dateTime);
                            sqLiteHelper.close();
                            updateDataList();

                        }
                    }
                });
                dialog.show();
            }
        });*/




        /*Glide.with(context).load(kycDocImage)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageViewKYCdoc);*/
        holder.imageViewKYCdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(context);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                Objects.requireNonNull(builder.getWindow()).setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });

                Bitmap bitmap;

                if(me_upd_lm_worklist_offline_data_report.getImage() != null)
                {
                    byte[] kycDocImage = me_upd_lm_worklist_offline_data_report.getImage();
                    bitmap = BitmapFactory.decodeByteArray(kycDocImage, 0, kycDocImage.length);

                    ImageView imageView = new ImageView(context);
                    imageView.setImageBitmap(bitmap);                //set the image in dialog popup
                    //below code fullfil the requirement of xml layout file for dialoge popup
                    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    builder.show();

                }

               /* if(contact.getUpdateType().equals("UPDATE EMAIL ID"))
                {
                    Toast.makeText(context,"No Image Found", Toast.LENGTH_LONG).show();
                }
*/
                //holder.imageViewKYCdoc.setImageBitmap(bitmap);

            }
        });
    }

    @Override
    public int getItemCount() {
        return unsyncdDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    unsyncdDataFiltered = unsyncdData;
                } else {
                    List<UnsyncdData_ReportClass> filteredList = new ArrayList<>();
                    for (UnsyncdData_ReportClass row : unsyncdData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getMobileNo().toLowerCase().contains(charString.toLowerCase()) || row.getEmailId().contains(charSequence) || row.getPhoneNo().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    unsyncdDataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = unsyncdDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                unsyncdDataFiltered = (ArrayList<UnsyncdData_ReportClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
