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
import java.util.stream.Collectors;

import ir.amirsobhan.earthquake.Models.Province;

public class ProvinceSpinnerAdapter extends ArrayAdapter<Province> {
    List<Province> provinceList;
    Context context;
    public ProvinceSpinnerAdapter(@NonNull Context context, List<Province> provinceList) {
        super(context, android.R.layout.simple_expandable_list_item_1);
        this.context = context;
        this.provinceList = provinceList;
        Collections.sort(provinceList,Comparator.comparing(Province::getFaTitle));
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
        return provinceList.size();
    }

    @Override
    public long getItemId(int position) {
        return provinceList.get(position).getId();
    }

    @Nullable
    @Override
    public Province getItem(int position) {
        return provinceList.get(position);
    }

    private View createItemView(int position, ViewGroup parent){
        View root = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1,parent,false);
        TextView textView = root.findViewById(android.R.id.text1);
        textView.setText(provinceList.get(position).getFaTitle());
        return root;
    }
}
