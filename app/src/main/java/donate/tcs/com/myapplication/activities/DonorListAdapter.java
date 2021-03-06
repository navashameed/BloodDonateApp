package donate.tcs.com.myapplication.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import donate.tcs.com.myapplication.bean.MemberDetails;
import donate.tcs.com.myapplication.R;

/**
 * Created by 351863 on 27-09-2017.
 */

public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.MyViewHolder> {

    private boolean isDeleteMode = false;

    public interface OnItemClickListener {
        void onItemClick(MemberDetails item);
        void onDeleteItemClick(MemberDetails item);
    }

    private List<MemberDetails> itemsList;
    private OnItemClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id, bloodGroup, phoneNumber, emailId;
        public ImageView deleteBtn;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            id = view.findViewById(R.id.empid);
            bloodGroup = view.findViewById(R.id.group);
            phoneNumber = view.findViewById(R.id.contact_number);
            deleteBtn = view.findViewById(R.id.delete_button);
            emailId = view.findViewById(R.id.email_id);
        }
    }


    public DonorListAdapter(List<MemberDetails> donorsList, OnItemClickListener listener) {
        this.itemsList = donorsList;
        this.listener = listener;
    }

    public void setItemsList(List<MemberDetails> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donor_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MemberDetails item = itemsList.get(position);
        holder.name.setText(item.name);
        holder.id.setText("Emp ID: " + item.employeeId);
        holder.bloodGroup.setText(item.bloodGroup);
        if (item.phoneNumber != null) {
            holder.phoneNumber.setText("Phone: " + item.phoneNumber);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteItemClick(item);
            }
        });

        holder.deleteBtn.setVisibility(isDeleteMode? View.VISIBLE: View.GONE);

        if(item.emailId.equals("null") || item.emailId.isEmpty()){
            holder.emailId.setVisibility(View.GONE);
        }
        else{
            holder.emailId.setVisibility(View.VISIBLE);
            holder.emailId.setText("Email: " + item.emailId);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void setDeleteMode() {
        isDeleteMode = true;
        notifyDataSetChanged();
    }

    public void resetDeleteMode() {
        isDeleteMode = false;
        notifyDataSetChanged();
    }
}
