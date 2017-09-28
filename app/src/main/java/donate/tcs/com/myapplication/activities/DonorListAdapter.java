package donate.tcs.com.myapplication.activities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import donate.tcs.com.myapplication.bean.DataEntry;
import donate.tcs.com.myapplication.R;

/**
 * Created by 351863 on 27-09-2017.
 */

public class DonorListAdapter extends RecyclerView.Adapter<DonorListAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DataEntry item);
    }

    private List<DataEntry> itemsList;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id, bloodGroup, phoneNumber;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.name);
            id =  view.findViewById(R.id.empid);
            bloodGroup = view.findViewById(R.id.group);
            phoneNumber =  view.findViewById(R.id.contact_number);
        }
    }


    public DonorListAdapter(List<DataEntry> donorsList, OnItemClickListener listener) {
        this.itemsList = donorsList;
        this.listener = listener;
    }

    public void setItemsList(List<DataEntry> itemsList) {
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
        final DataEntry item = itemsList.get(position);
        holder.name.setText(item.name);
        holder.id.setText(item.employeeId);
        holder.bloodGroup.setText(item.bloodGroup);
        if(item.phoneNumber != null){
            holder.phoneNumber.setText(item.phoneNumber);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
