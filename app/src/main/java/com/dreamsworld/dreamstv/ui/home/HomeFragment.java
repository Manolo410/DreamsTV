package com.dreamsworld.dreamstv.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dreamsworld.dreamstv.R;
import com.dreamsworld.dreamstv.model.Content;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private RecyclerView featuredRecyclerView;
    private RecyclerView popularRecyclerView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        featuredRecyclerView = view.findViewById(R.id.featured_recycler);
        popularRecyclerView = view.findViewById(R.id.popular_recycler);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Set up observers
        mViewModel.getFeaturedContent().observe(getViewLifecycleOwner(), featuredContents -> {
            // In a real app, set adapter here
            // featuredRecyclerView.setAdapter(new FeaturedContentAdapter(featuredContents));
            System.out.println("Featured content loaded: " + featuredContents.size() + " items");
        });

        mViewModel.getPopularContent().observe(getViewLifecycleOwner(), popularContents -> {
            // In a real app, set adapter here
            // popularRecyclerView.setAdapter(new ContentAdapter(popularContents));
            System.out.println("Popular content loaded: " + popularContents.size() + " items");
        });
    }
}