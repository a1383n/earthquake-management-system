package ir.amirsobhan.earthquake.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ir.amirsobhan.earthquake.Models.Province;
import ir.amirsobhan.earthquake.Models.Region;

public class RegionSpinnerAdapter extends ArrayAdapter<Region> {
    List<Region> regionList;
    Context context;
    public RegionSpinnerAdapter(@NonNull Context context, List<Region> regionList) {
        super(context, android.R.layout.simple_expandable_list_item_1);
        this.context = context;
        this.regionList = regionList;
        Collections.sort(regionList, Comparator.comparing(Region::getFaTitle));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       return createItemView(position,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position,parent);
    }

    @Override
    public int getCount() {
        return regionList.size();
    }

    @Override
    public long getItemId(int position) {
        return regionList.get(position).getId();
    }

    @Nullable
    @Override
    public Region getItem(int position) {
        return regionList.get(position);
    }

    private View createItemView(int position, ViewGroup parent){
        View root = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1,parent,false);
        TextView textView = root.findViewById(android.R.id.text1);
        textView.setText(regionList.get(position).getFaTitle());
        return root;
    }
}
