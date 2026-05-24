package com.example.print.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.print.R;
import com.example.print.activities.CategoryDetailActivity;
import com.example.print.activities.DesignGuidelinesActivity;
import com.example.print.adapters.CategoryAdapter;
import com.example.print.adapters.PromotionAdapter;
import com.example.print.firebase.RealtimeDatabaseHelper;
import com.example.print.models.Category;
import com.example.print.models.Promotion;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView welcomeText;
    private RecyclerView categoryRecycler, promoRecycler;
    private CategoryAdapter categoryAdapter;
    private PromotionAdapter promoAdapter;
    private MaterialCardView cardGuidelines;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        welcomeText = view.findViewById(R.id.welcome_text);
        categoryRecycler = view.findViewById(R.id.category_recycler);
        promoRecycler = view.findViewById(R.id.promo_recycler);
        cardGuidelines = view.findViewById(R.id.card_guidelines);

        setupCategories();
        setupPromotions();
        fetchUserName();

        cardGuidelines.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DesignGuidelinesActivity.class));
        });

        return view;
    }

    private void setupCategories() {
        List<Category> categories = new ArrayList<>();

        List<Integer> businessCardSamples = Arrays.asList(
                R.drawable.bc1,
                R.drawable.bc2,
                R.drawable.bc3,
                R.drawable.bc4,
                R.drawable.bc5
        );

        List<Integer> posterSamples = Arrays.asList(
                R.drawable.p1 ,
                R.drawable.p2 ,
                R.drawable.p3 ,
                R.drawable.p4 ,
                R.drawable.p5
        );

        List<Integer> bannerSamples = Arrays.asList(
                R.drawable.b1 ,
                R.drawable.b2 ,
                R.drawable.b3 ,
                R.drawable.b4 ,
                R.drawable.b5
        );

        List<Integer> FlyerSamples = Arrays.asList(
                R.drawable.f1 ,
                R.drawable.f2 ,
                R.drawable.f3 ,
                R.drawable.f4 ,
                R.drawable.f5
        );

        List<Integer> stickersSamples = Arrays.asList(
                R.drawable.s1 ,
                R.drawable.s2 ,
                R.drawable.s3 ,
                R.drawable.s4 ,
                R.drawable.s5
        );

        List<Integer> mSamples = Arrays.asList(
                R.drawable.m1 ,
                R.drawable.m2 ,
                R.drawable.m3 ,
                R.drawable.m4 ,
                R.drawable.m5
        );

        categories.add(new Category(getString(R.string.cat_business_cards), R.drawable.business_cards, getString(R.string.price_starting_format, "$10.00"), getString(R.string.spec_bc), businessCardSamples));
        categories.add(new Category(getString(R.string.cat_posters), R.drawable.posters, getString(R.string.price_starting_format, "$15.00"), getString(R.string.spec_posters), posterSamples));
        categories.add(new Category(getString(R.string.cat_banners), R.drawable.banners, getString(R.string.price_starting_format, "$25.00"), getString(R.string.spec_banners), bannerSamples));
        categories.add(new Category(getString(R.string.cat_flyers), R.drawable.flyers, getString(R.string.price_starting_format, "$20.00"), getString(R.string.spec_flyers), FlyerSamples));
        categories.add(new Category(getString(R.string.cat_stickers), R.drawable.stickers, getString(R.string.price_starting_format, "$5.00"), getString(R.string.spec_stickers), stickersSamples));
        categories.add(new Category(getString(R.string.cat_merchandise), R.drawable.merchandise, getString(R.string.price_starting_format, "$12.00"), getString(R.string.spec_merch), mSamples));

        categoryAdapter = new CategoryAdapter(categories, category -> {
            Intent intent = new Intent(getActivity(), CategoryDetailActivity.class);
            intent.putExtra("CATEGORY_NAME", category.getName());
            intent.putExtra("CATEGORY_PRICING", category.getPricing());
            intent.putExtra("CATEGORY_SPECS", category.getSpecs());
            intent.putExtra("CATEGORY_IMAGE", category.getImageResId());

            List<Integer> samples = category.getSampleImageResIds();
            if (samples != null) {
                int[] sampleArr = new int[samples.size()];
                for (int i = 0; i < samples.size(); i++) {
                    sampleArr[i] = samples.get(i);
                }
                intent.putExtra("CATEGORY_SAMPLES", sampleArr);
            }

            startActivity(intent);
        });

        categoryRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categoryRecycler.setAdapter(categoryAdapter);
    }

    private void setupPromotions() {
        List<Promotion> promos = new ArrayList<>();
        promos.add(new Promotion("1", getString(R.string.promo_bc_off), R.drawable.promo1));
        promos.add(new Promotion("2", getString(R.string.promo_bulk_posters), R.drawable.promo2));
        promos.add(new Promotion("3", getString(R.string.promo_free_delivery_banners), R.drawable.promo3));

        promoAdapter = new PromotionAdapter();
        promoAdapter.setPromotions(promos);

        promoRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        promoRecycler.setAdapter(promoAdapter);
    }

    private void fetchUserName() {
        String uid = RealtimeDatabaseHelper.getInstance().getCurrentUserId();
        if (uid != null) {
            RealtimeDatabaseHelper.getInstance().getUsersRef().child(uid).child("name").get()
                    .addOnSuccessListener(snapshot -> {
                        if (isAdded() && snapshot.exists()) {
                            String name = snapshot.getValue(String.class);
                            welcomeText.setText(getString(R.string.welcome_user_format, name));
                        }
                    });
        }
    }
}
