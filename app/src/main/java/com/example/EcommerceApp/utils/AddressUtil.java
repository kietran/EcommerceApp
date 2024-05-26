package com.example.EcommerceApp.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.AddressSelect;
import com.example.EcommerceApp.api.ApiService;
import com.example.EcommerceApp.model.District;
import com.example.EcommerceApp.model.DistrictRequest;
import com.example.EcommerceApp.model.DistrictResponse;
import com.example.EcommerceApp.model.Province;
import com.example.EcommerceApp.model.ProvinceRequest;
import com.example.EcommerceApp.model.ProvinceResponse;
import com.example.EcommerceApp.model.Ward;
import com.example.EcommerceApp.model.WardResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressUtil {
    static Map<String, Integer> provinceMap;
    static Map<String, Integer> districtMap;
    static ArrayList<String> provinceNames;
    static ArrayList<String> wardNames;

    static ArrayList<String> districtNames;
    public static AddressSelect addressSelect;
    public static String province, ward, district;

    public static void initial() {
        provinceMap = new HashMap<>();
        districtMap = new HashMap<>();
        provinceNames = new ArrayList<>();
        districtNames = new ArrayList<>();
        wardNames = new ArrayList<>();
        addressSelect = AddressSelect.newInstance(new ArrayList<>());
    }

    public static void initialProvince() {
        ApiService.apiService.getProvinceResponse().enqueue(new Callback<ProvinceResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProvinceResponse> call, @NonNull Response<ProvinceResponse> response) {
                Log.i("API Call", "Received API response");
                if (response.isSuccessful() && response.body() != null) {
                    List<Province> provinces = response.body().getData();
                    for (Province province : provinces) {
                        provinceNames.add(province.getProvinceName());
                        provinceMap.put(province.getProvinceName(), province.getProvinceID());
                        Log.i("API", "Province: " + province.getProvinceName());
                    }
                    if (addressSelect != null) {
                        addressSelect.updateItems(provinceNames);
                    }

                } else {
                    Log.e("Province Error", "Response Code: " + response.code());
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProvinceResponse> call, @NonNull Throwable t) {
                Log.e("Province Error", "onFailure: " + t.getMessage());
            }
        });
    }


    public static void initialDistrict(String provinceName) {
        ApiService.apiService.getDistrictResponse(new ProvinceRequest(provinceMap.get(provinceName))).enqueue(new Callback<DistrictResponse>() {
            @Override
            public void onResponse(@NonNull Call<DistrictResponse> call, @NonNull Response<DistrictResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    districtMap = new HashMap<>();
                    List<District> districts = response.body().getData();
                    for (District district : districts) {
                        districtNames.add(district.getDistrictName());
                        districtMap.put(district.getDistrictName(), district.getDistrictID());
                        if (addressSelect != null) {
                            addressSelect.updateItems(districtNames);
                        }
                        Log.i("API", "District: " + district.getDistrictName());

                        }
                    }
                else{
                    Log.e("District Error", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DistrictResponse> call, Throwable t) {

            }
        });
    }
    public static void initialWard(String districtName) {
        ApiService.apiService.getWardResponse(new DistrictRequest(districtMap.get(districtName))).enqueue(new Callback<WardResponse>() {
            @Override
            public void onResponse(@NonNull Call<WardResponse> call, @NonNull Response<WardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Ward> wards = response.body().getData();
                    for (Ward ward : wards) {
                        wardNames.add(ward.getWardName());
                        if (addressSelect != null) {
                            addressSelect.updateItems(wardNames);
                        }
                        Log.i("API", "District: " + ward.getWardName());

                    }
                }
                else{
                    Log.e("Ward Error", "Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WardResponse> call, Throwable t) {

            }
        });
    }
}
