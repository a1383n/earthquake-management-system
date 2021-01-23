package ir.amirsobhan.earthquake.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ir.amirsobhan.earthquake.Fragments.ChartFragment;
import ir.amirsobhan.earthquake.Fragments.HomeFragment;
import ir.amirsobhan.earthquake.Fragments.LocationFragment;
import ir.amirsobhan.earthquake.Fragments.MapFragment;
import ir.amirsobhan.earthquake.Fragments.SearchFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new MapFragment();
            case 2:
                return new LocationFragment();
            case 3:
                return new ChartFragment();
            case 4:
                return new SearchFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
