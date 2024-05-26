package com.example.EcommerceApp.domain.user;

import android.util.Log;

import com.example.EcommerceApp.model.Address;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressRepository {
    private FirebaseFirestore db;
    private static CollectionReference addresses;

    public AddressRepository() {
        db = FirebaseFirestore.getInstance();
        addresses=db.collection("address");
    }

    public void create(String province, String ward, String district, String addressLine, String receiverName, String phone, String userId, Boolean isDefault) {
        if (isDefault) {
            addresses.whereEqualTo("user_id", userId)
                    .whereEqualTo("isDefault", true)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    addresses.document(document.getId()).update("isDefault", false);
                                }
                            }
                            addNewAddress(province, ward, district, addressLine, receiverName, phone, userId, true);
                        } else {
                            System.err.println("Error when query addresses: " + task.getException());
                        }
                    })
                    .addOnFailureListener(e -> {
                        System.err.println("Error when query address: " + e);
                    });
        } else {
            // Thêm địa chỉ mới mà không cần cập nhật các địa chỉ cũ
            addNewAddress(province, ward, district, addressLine, receiverName, phone, userId, false);
        }
    }

    private void addNewAddress(String province, String ward, String district, String addressLine, String receiverName, String phone, String userId, Boolean isDefault) {
        Address address = new Address();
        address.setProvince(province);
        address.setWard(ward);
        address.setDistrict(district);
        address.setAddress_line(addressLine);
        address.setReceiver_name(receiverName);
        address.setPhone(phone);
        address.setUser_id(userId);
        address.setIsDefault(isDefault);

        addresses.add(address)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentReference documentReference = task.getResult();
                        String id = documentReference.getId();
                        System.out.println("Address is added with ID: " + id);
                        address.setId(id);
                    } else {
                        System.err.println("Error when add data: " );
                    }
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error when add data: " + e);
                });
    }
    public Task<List<Address>> getAddressByUserId(String id) {
        return addresses.whereEqualTo("user_id",id).get().continueWith(task -> {
            List<Address> listAddress = new ArrayList<>();
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Address address = document.toObject(Address.class);
                        address.setId(document.getId());
                        Log.i("default", String.valueOf(address.getIsDefault()) );
                        listAddress.add(address);
                    }
                }
            } else {
                Log.i("fetch address", "not success");
            }
            return listAddress;
        });
    }
    public Task<Address> getDefaultAddress(String id) {
        return addresses.whereEqualTo("user_id", id)
                .whereEqualTo("isDefault", true)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        Address address = document.toObject(Address.class);
                        if (address != null) {
                            address.setId(document.getId());
                        }
                        return address;
                    } else {
                        Log.i("getDefaultAddress", "No default address found for user ID: " + id);
                        return null;
                    }
                });

    }
    public Task<Address> getAddressById(String address_id) {
        return addresses.document(address_id).get().continueWith(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                Address address = document.toObject(Address.class);
                if (address != null) {
                    address.setId(document.getId());
                }
                return address;
            } else {
                Log.i("getAddressById", "No address found with ID: " + address_id);
                return null;
            }
        });

    }
    public Task<Void> updateAddress(String addressId, String province, String ward, String district, String addressLine, String receiverName, String phone, String user_id, Boolean isDefault) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("province", province);
        updateData.put("ward", ward);
        updateData.put("district", district);
        updateData.put("address_line", addressLine);
        updateData.put("receiver_name", receiverName);
        updateData.put("phone", phone);
        updateData.put("isDefault", isDefault);
        updateData.put("id",addressId);
        updateData.put("user_id",user_id);

        return addresses.document(addressId).update(updateData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("AddressRepository", "Address with ID " + addressId + " updated successfully.");
                })
                .addOnFailureListener(e -> {
                    Log.e("AddressRepository", "Error updating address with ID " + addressId, e);
                });
    }


    public Task<Void> deleteAddress(String addressId) {
        return addresses.document(addressId).delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("AddressRepository", "Address with ID " + addressId + " deleted successfully.");
                })
                .addOnFailureListener(e -> {
                    Log.e("AddressRepository", "Error deleting address with ID " + addressId, e);
                });
    }


}
