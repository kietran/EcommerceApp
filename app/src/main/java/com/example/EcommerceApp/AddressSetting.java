package com.example.EcommerceApp;

import static com.example.EcommerceApp.R.id.select_address;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.EcommerceApp.domain.user.AddressRepository;
import com.example.EcommerceApp.model.Address;
import com.example.EcommerceApp.utils.AddressUtil;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AddressSetting extends AppCompatActivity {
    TextView address;
    Button confirm, delete;
    ImageView btBacka;
    CheckBox defaultAdd;
    com.google.android.material.textfield.TextInputLayout phone, name, addressLine;
    EditText edName, edPhone, edAddress;
    boolean isEdit;
    String addressId;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addressId="";
        initwidgets();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("address_id")) {
            isEdit=true;
            addressId = intent.getStringExtra("address_id");
            confirm.setText("SAVE");
            delete.setVisibility(View.VISIBLE);
            setTextAddress(addressId);
        }
        else {
            isEdit=false;
        }
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressUtil.initial();
                AddressUtil.addressSelect.setAddress(address);
                AddressUtil.addressSelect.show(getSupportFragmentManager(), "dialog");
                AddressUtil.initialProvince();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });
        btBacka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddress();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this address?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            AddressRepository addressRepository = new AddressRepository();
            addressRepository.deleteAddress(addressId)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Address deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to delete address: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            navigateToAddress();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setTextAddress(String addressId) {
        AddressRepository addressRepository = new AddressRepository();
        addressRepository.getAddressById(addressId).addOnCompleteListener(new OnCompleteListener<Address>() {
            @Override
            public void onComplete(@NonNull Task<Address> task) {
                Address address1 = task.getResult();
                if(address1!=null){
                    String text_address = address1.getWard()+", "+address1.getDistrict()+", "+address1.getProvince();
                    edName.setText(address1.getReceiver_name());
                    edAddress.setText(address1.getAddress_line());
                    edPhone.setText(address1.getPhone());
                    address.setText(text_address);
                    defaultAdd.setChecked(address1.getIsDefault());

                    AddressUtil.province=address1.getProvince();
                    AddressUtil.ward= address1.getWard();
                    AddressUtil.district=address1.getDistrict();
                }
            }
        });
    }

    private void checkFields() {
        boolean isValid = true;

        String nameInput = edName.getText().toString().trim();

        if (nameInput.isEmpty()) {
            name.setError("Name cannot be empty");
            edName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(charSequence!="") {
                        name.setError("");
                        name.setHelperText("Name is valid");
                    }
                    else
                        name.setError("Name cannot be empty");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            isValid = false;
        } else {
            name.setError(null);
        }

        String phoneInput = edPhone.getText().toString().trim();
        if (!isValidPhone(phoneInput)) {
            phone.setError("Invalid phone number");
            edPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(isValidPhone((String) charSequence)) {
                        phone.setError("");
                        phone.setHelperText("Phone is valid");
                    }
                    else
                        phone.setError("Invalid phone number");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            isValid = false;
        } else {
            phone.setError(null);
        }

        String addressInput = edAddress.getText().toString().trim();
        if (!isValidAddress(addressInput)) {
            addressLine.setError("Address line must contain at least two words");
            edAddress.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(isValidAddress((String) charSequence.toString())) {
                        addressLine.setError("");
                        addressLine.setHelperText("Address line is valid");
                    }
                    else
                        addressLine.setError("Address line must contain at least two words");
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            isValid = false;
        } else {
            addressLine.setError(null);
        }

        if(address.getText()=="")
        {
            isValid=false;
            Toast.makeText(this,"Please select address", Toast.LENGTH_LONG).show();
        }
        boolean isDefault= defaultAdd.isChecked();

        if (isValid) {
            AddressRepository addressRepository = new AddressRepository();

            if(isEdit) {
                if(isDefault)
                    addressRepository.getDefaultAddress(FirebaseUtil.currentUserId()).addOnCompleteListener(new OnCompleteListener<Address>() {
                        @Override
                        public void onComplete(@NonNull Task<Address> task) {
                            Address address1 = task.getResult();
                            if(address1!=null)
                                addressRepository.updateAddress(address1.getId(), address1.getProvince(), address1.getWard(), address1.getDistrict(), address1.getAddress_line(), address1.getReceiver_name(), address1.getPhone(), FirebaseUtil.currentUserId(), false);
                            addressRepository.updateAddress(addressId, AddressUtil.province, AddressUtil.ward, AddressUtil.district, addressInput, nameInput, phoneInput, FirebaseUtil.currentUserId(), isDefault);
                        }
                    });
            }
            else
                addressRepository.create(AddressUtil.province,AddressUtil.ward,AddressUtil.district,addressInput,nameInput,phoneInput, FirebaseUtil.currentUserId(),isDefault);


            if(isEdit)
                Toast.makeText(this,UPDATE, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,CREATE, Toast.LENGTH_LONG).show();

            navigateToAddress();
        }
    }
    String CREATE ="Create address successfully";
    String UPDATE ="Update address successfully";
    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10,15}");
    }

    private boolean isValidAddress(String address) {
        return address.trim().split("\\s+").length >= 2;
    }


    private void navigateToAddress() {
        finish();
    }

    private void initwidgets() {
        address = findViewById(select_address);
        confirm = findViewById(R.id.confirm);
        btBacka = findViewById(R.id.btBacka);
        defaultAdd = findViewById(R.id.defaultAdd);
        phone = findViewById(R.id.layout_as_ed_ph);
        name = findViewById(R.id.layout_as_ed_na);
        addressLine = findViewById(R.id.layout_as_ed_ar);
        edName = findViewById(R.id.edName);
        edAddress = findViewById(R.id.edAddress);
        edPhone = findViewById(R.id.edPhone);
        delete = findViewById(R.id.deleteAddress);
    }


}