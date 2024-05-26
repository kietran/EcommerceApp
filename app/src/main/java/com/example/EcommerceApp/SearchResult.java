package com.example.EcommerceApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.EcommerceApp.adapter.ProductAdapter;
import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResult#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResult extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    float DEFAULT_MIN_VALUE = 0;
    float DEFAULT_MAX_VALUE = 0;
    ProductAdapter productAdapter;
    androidx.recyclerview.widget.RecyclerView rc_result_product;
    ProductRepository productRepository;
    ExtendedFloatingActionButton buttonFilter;
    BottomSheetDialog bottomSheetDialog;
    RadioGroup radioGroup;
    RangeSlider priceSlider;
    TextView minValue;
    TextView maxValue;
    public SearchResult() {
        productRepository = new ProductRepository(getContext());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResult.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResult newInstance(String param1, String param2) {
        SearchResult fragment = new SearchResult();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void setListForAdapter(List<Product> products)
    {
        productAdapter = new ProductAdapter(mContext, new ArrayList<>());
        productAdapter.updateData(products);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        buttonFilter = view.findViewById(R.id.btnFilter);

        ////// RECYCLERVIEW PRODUCT
        rc_result_product = view.findViewById(R.id.rc_result_product);
        GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(),2);
        rc_result_product.setLayoutManager(layoutManagerProduct);
        rc_result_product.setAdapter(productAdapter);
        Log.println(Log.ASSERT, "Load product rs", "fragment");

        setFilterData();
        return view;
    }

    private void setFilterData(){
        List<Product> defaultProducts = productAdapter.resetFilter();
        buttonFilter.setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View dialogView = inflater.inflate(R.layout.bottom_sheet_filter, null);
            boolean sliderNeedsRestoration = getSliderStateNeedsRestoration();

            radioGroup = dialogView.findViewById(R.id.radio_group);
            minValue = dialogView.findViewById(R.id.min_value);
            maxValue = dialogView.findViewById(R.id.max_value);
            priceSlider = dialogView.findViewById(R.id.price_slider);
            priceSlider.setLabelFormatter(new LabelFormatter() {
                @NonNull
                @Override
                public String getFormattedValue(float v) {
                    NumberFormat currency = NumberFormat.getCurrencyInstance();
                    currency.setMaximumFractionDigits(1);
                    currency.setCurrency(Currency.getInstance("USD"));
                    return currency.format(v);
                }
            });

            if (sliderNeedsRestoration) {
                restoreSliderState();
            }

            // Display the bottom sheet
            bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();

            // Handle price range
            priceSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                }

                @Override
                public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                    // Save the state of the range slider
                    saveSliderStateNeedsRestoration(true);

                    List<Float> values = priceSlider.getValues();
                    minValue.setText("$"+formatValue(values.get(0)).toString());
                    maxValue.setText("$"+formatValue(values.get(1)).toString());

                    productAdapter.updateData(defaultProducts);
                    List<Product> filteredProducts = productAdapter.filterPriceRange(values.get(0), values.get(1));
                    productAdapter.updateData(filteredProducts);
                    rc_result_product.setAdapter(productAdapter);

                    saveSliderState();
                }
            });

            // Restore the selection of radio button
            int checkedId = getSavedRadioButtonId();
            if (checkedId != -1) {
                radioGroup.check(checkedId);
            }

            // Handle sorting
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // Save the selected radio button's ID
                    saveRadioButtonId(checkedId);

                    if (checkedId == R.id.AZ) {
                        productAdapter.sortByNameAZ();
                        rc_result_product.setAdapter(productAdapter);
                    } else if (checkedId == R.id.ZA) {
                        productAdapter.sortByNameZA();
                        rc_result_product.setAdapter(productAdapter);
                    } else if (checkedId == R.id.high_to_low_price) {
                        productAdapter.sortPriceHighToLow();
                        rc_result_product.setAdapter(productAdapter);
                    } else if (checkedId == R.id.low_to_high_price) {
                        productAdapter.sortPriceLowToHigh();
                        rc_result_product.setAdapter(productAdapter);
                    }
                }
            });

        });
    }

    private String formatValue(float value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        numberFormat.setMaximumFractionDigits(1); // Adjust this to the required number of decimal places
        return numberFormat.format(value);
    }

    private void saveRadioButtonId(int checkedId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedRadioButtonId", checkedId);
        editor.apply();
    }

    private int getSavedRadioButtonId() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getInt("selectedRadioButtonId", -1);
    }

    // Method to save the state of the range slider to SharedPreferences
    private void saveSliderState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("minSliderValue", priceSlider.getValues().get(0));
        editor.putFloat("maxSliderValue", priceSlider.getValues().get(1));
        editor.apply();
    }

    // Method to restore the state of the range slider from SharedPreferences
    private void restoreSliderState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        float minSliderValue = sharedPreferences.getFloat("minSliderValue", 0);
        float maxSliderValue = sharedPreferences.getFloat("maxSliderValue", 100);
        priceSlider.setValues(minSliderValue, maxSliderValue);
        minValue.setText("$" + formatValue(minSliderValue));
        maxValue.setText("$" + formatValue(maxSliderValue));
    }

    private void resetFilter() {
        // Clear the checked radio button in the radio group
        radioGroup.clearCheck();
        // Reset the range slider to its default state
        priceSlider.setValues(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
        // Update the text fields associated with the range slider
        minValue.setText("$" + formatValue(DEFAULT_MIN_VALUE));
        maxValue.setText("$" + formatValue(DEFAULT_MAX_VALUE));

        List<Product> defaultProducts = productAdapter.resetFilter();
        productAdapter.updateData(defaultProducts);
        rc_result_product.setAdapter(productAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context; // Store the context when the Fragment is attached
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null; // Clear the stored context when the Fragment is detached
    }

    // Method to save the state needs restoration flag
    private void saveSliderStateNeedsRestoration(boolean needsRestoration) {
        if (mContext != null) { // Check if the context is available
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("sliderStateNeedsRestoration", needsRestoration);
            editor.apply();
        }
    }

    // Method to get the state needs restoration flag
    private boolean getSliderStateNeedsRestoration() {
        if (mContext != null) { // Check if the context is available
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            return sharedPreferences.getBoolean("sliderStateNeedsRestoration", false);
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        saveSliderStateNeedsRestoration(false);
        resetFilter();
        super.onDestroyView();
    }

    public void setContext(SearchActivity searchActivity) {
        this.mContext = searchActivity;
    }
    public void setContext1(Context context) {
        this.mContext = context;
    }

}