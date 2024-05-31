package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.EcommerceApp.adapter.ColorsAdapter;
import com.example.EcommerceApp.adapter.SizesAdapter;
import com.example.EcommerceApp.domain.user.ProductItemRepository;
import com.example.EcommerceApp.domain.user.ShopRepository;
import com.example.EcommerceApp.domain.user.ShoppingCartItemRepository;
import com.example.EcommerceApp.domain.user.ShoppingCartRepository;
import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.model.ProductItem;
import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.model.ShoppingCart;
import com.example.EcommerceApp.utils.CartNumberUtil;
import com.google.common.collect.Sets;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DetailTwoAttributeActivity extends AppCompatActivity {
    ImageView btBack, product_image, btnCartDetail;
    Button btn_addtocart;
    ProductItemRepository productItemRepository = new ProductItemRepository(this);
    ShoppingCartItemRepository shoppingCartItemRepository = new ShoppingCartItemRepository();
    ColorsAdapter colorsAdapter;
    SizesAdapter sizesAdapter;
    int count = 1, qty = 0, qty_in_Cart = 0;
    TextView productCount, product_name, product_description, price, status, btnTru, btnCong, qty_in_cart;
    RecyclerView rcColor, rcSize;

    String selectedColor = "", selectedSize = "";
    Product product;
    String product_id;
    boolean isHaveColor, isHaveSize;
    LinearLayout layoutForSizeAndColor, layoutForSize, layoutForColor;
    Button btnViewShop;
    String shopId;
    TextView shopName;
    ImageView shopAvt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_attribute_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        product = (Product) bundle.get("object_product");
        product_id = (String) bundle.get("id");
        shopId = product.getShop_id();
        isHaveColor = (Boolean) bundle.get("haveColor");
        isHaveSize = (Boolean) bundle.get("haveSize");
        product_name = findViewById(R.id.product_name);
        product_description = findViewById(R.id.product_description);
        price = findViewById(R.id.price);
        status = findViewById(R.id.status);
        btnTru = findViewById(R.id.btnTru);
        btnCong = findViewById(R.id.btnCong);
        productCount = findViewById(R.id.productCount);
        product_image = findViewById(R.id.product_image);
        btBack = findViewById(R.id.btnBackDetail);
        btn_addtocart = findViewById(R.id.btn_addtoCart);
        layoutForSizeAndColor = findViewById(R.id.layoutforSizeandColor);
        layoutForColor = findViewById(R.id.layoutforColor);
        layoutForSize = findViewById(R.id.layoutforSize);
        btnViewShop = findViewById(R.id.btnViewShop);
        shopName = findViewById(R.id.shop_name);
        shopAvt = findViewById(R.id.shop_avatar);
        qty_in_cart = findViewById(R.id.qty_in_cart);
        shoppingCartItemRepository.getQTYinCartByUserID(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(task -> {
            qty_in_Cart = task.getResult();
            if (qty_in_Cart > 0){
                CartNumberUtil.setQty_in_cart(qty_in_Cart);
                qty_in_cart.setText(CartNumberUtil.getQty_in_cart()+"");
                qty_in_cart.setVisibility(View.VISIBLE);
            }
            else {
                CartNumberUtil.setQty_in_cart(0);
                qty_in_cart.setVisibility(View.INVISIBLE);
            }
        });



                btnCartDetail = findViewById(R.id.btnCartDetail);
                btnCartDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navigateToCart();
                    }
                });
                btnViewShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToShop();
                    }
                });
                product_name.setText(product.getName());
                product_description.setText(product.getDescription());
                price.setText(product.getPrice()+"");

                Glide.with(this)
                    .load(product.getProduct_image())
                    .override(product_image.getWidth(), product_image.getHeight())
                    .into(product_image);
                if (isHaveSize && isHaveColor) {
                    layoutForSizeAndColor.setVisibility(View.VISIBLE);
                    colorsAdapter = new ColorsAdapter(this, new ArrayList<>());
                    sizesAdapter = new SizesAdapter(this, new ArrayList<>());
                    rcColor = findViewById(R.id.rcColorFor2);
                    rcSize = findViewById(R.id.rcSizeFor2);
                    productItemRepository.getAllProductColorsAsListByProductID(product_id).addOnCompleteListener(task -> {
                        List<String> productColor = task.getResult();
                        selectedColor = productColor.get(0);
                        Set<String> uniqueProductColors = Sets.newHashSet(productColor);
                        List<String> uniqueProductColorList = new ArrayList<>(uniqueProductColors);
                        colorsAdapter.updateData(uniqueProductColorList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                        rcColor.setLayoutManager(linearLayoutManager);
                        rcColor.setAdapter(colorsAdapter);
                    });

                    productItemRepository.getAllProductSizesAsListByProductID(product_id).addOnCompleteListener(task -> {
                        List<String> productSizes = task.getResult();
                        selectedSize = productSizes.get(0);
                        Log.i("dasdadasdasdasd", "" + selectedSize);
                        Set<String> uniqueProductSize = Sets.newHashSet(productSizes);
                        List<String> uniqueProductSizeList = new ArrayList<>(uniqueProductSize);
                        sizesAdapter.updateData(uniqueProductSizeList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                        rcSize.setLayoutManager(linearLayoutManager);
                        rcSize.setAdapter(sizesAdapter);
                        setStatus(product_id, selectedColor, selectedSize);
                    });
                }
                else if (isHaveColor && !isHaveSize){
                    layoutForColor.setVisibility(View.VISIBLE);
                    colorsAdapter = new ColorsAdapter(this, new ArrayList<>());
                    rcColor = findViewById(R.id.rcColorFor1);
                    productItemRepository.getAllProductColorsAsListByProductID(product_id).addOnCompleteListener(task -> {
                        List<String> productColor = task.getResult();
                        selectedColor = productColor.get(0);
                        Set<String> uniqueProductColors = Sets.newHashSet(productColor);
                        List<String> uniqueProductColorList = new ArrayList<>(uniqueProductColors);
                        colorsAdapter.updateData(uniqueProductColorList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                        rcColor.setLayoutManager(linearLayoutManager);
                        rcColor.setAdapter(colorsAdapter);
                        setStatus(product_id, selectedColor, selectedSize);
                    });
                }
                else if (!isHaveColor && isHaveSize){
                    layoutForSize.setVisibility(View.VISIBLE);
                    sizesAdapter = new SizesAdapter(this, new ArrayList<>());
                    rcSize = findViewById(R.id.rcSizeFor1);
                    productItemRepository.getAllProductSizesAsListByProductID(product_id).addOnCompleteListener(task -> {
                        List<String> productSizes = task.getResult();
                        selectedSize = productSizes.get(0);
                        Log.i("dasdadasdasdasd", "" + selectedSize);
                        Set<String> uniqueProductSize = Sets.newHashSet(productSizes);
                        List<String> uniqueProductSizeList = new ArrayList<>(uniqueProductSize);
                        sizesAdapter.updateData(uniqueProductSizeList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
                        rcSize.setLayoutManager(linearLayoutManager);
                        rcSize.setAdapter(sizesAdapter);
                        setStatus(product_id, selectedColor, selectedSize);
                    });
                }
                else{
                    setStatus(product_id, selectedColor, selectedSize);
                }

                btBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                btnTru.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SuspiciousIndentation")
                    @Override
                    public void onClick(View v) {
                        if (count > 0)
                            count = count - 1;
                        productCount.setText(""+count);
                        if (count == 0){
                            status.setText("Not available");
                            status.setTextColor(Color.RED);
                            return;
                        }
                        if (qty >= count){
                            status.setText("Available");
                            status.setTextColor(Color.GREEN);
                        }

                    }
                });
                btnCong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isHaveColor) {
                            selectedColor = colorsAdapter.getSellectedColor();
                        }
                        if (isHaveSize) {
                            selectedSize = sizesAdapter.getSelectedSize();
                        }

                        Log.i("product_id", ""+product_id);
                        Log.i("selectcolor", ""+selectedColor);
                        Log.i("selectsize", ""+selectedSize);
                        productItemRepository.getProductQTYByColorAndSize(product_id, selectedColor, selectedSize)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        count = count + 1;
                                        productCount.setText("" + count);
                                        qty = task.getResult();
                                        Log.i("qty", ""+qty);
                                        if (qty < count){
                                            status.setText("Not available");
                                            status.setTextColor(Color.RED);
                                        }
                                        else{
                                            status.setText("Available");
                                            status.setTextColor(Color.GREEN);
                                        }
                                    }
                                });
                    }
                });

                btn_addtocart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (status.getText() == "Available"){
                            handleAddToCart();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Add to cart failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //Set shop view
                setShopView();
            }

            private void navigateToCart() {
                Intent intent = new Intent(DetailTwoAttributeActivity.this, MyCartActivity.class);
                startActivity(intent);
            }

            private void setShopView() {
                ShopRepository shopRepository = new ShopRepository(this);
                shopRepository.getShopByShopID(shopId).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Shop shop = task.getResult();
                        shopName.setText(shop.getShopName());
                        Picasso.get().load(shop.getProfileImage())
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .into(shopAvt);
                    }
                });
            }

            private void navigateToShop() {
                Intent i = new Intent(DetailTwoAttributeActivity.this, ShopPageActivity.class);
                i.putExtra("shopID", shopId);
                startActivity(i);
            }

            private void handleAddToCart(){
                ShoppingCartRepository shoppingCartRepository = new ShoppingCartRepository(this);
                shoppingCartRepository.getCartId(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String cartID = task.getResult();
                        Log.i("CART ID", ""+cartID);
                        if (cartID == null){
                            shoppingCartRepository.createCart(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(task1 -> {
                                String cartID1 = task1.getResult();
                                Log.i("CART ID1", ""+cartID1);
                                createShoppingCartItem(cartID1);
                            });
                        }
                        else
                            createShoppingCartItem(cartID);
                    }
                });
                qty_in_Cart += count;
                qty_in_cart.setText(qty_in_Cart+"");
                Toast.makeText(getApplicationContext(), "Add to cart success!", Toast.LENGTH_LONG).show();
            }

            private void createShoppingCartItem(String cartID) {
                ShoppingCartItemRepository shoppingCartItemRepository = new ShoppingCartItemRepository();
                Log.i("product_id", ""+product_id);
                Log.i("selectcolor", ""+selectedColor);
                Log.i("selectsize", ""+selectedSize);
                if(colorsAdapter==null||colorsAdapter.getSellectedColor()==null)
                    selectedColor="";
                else
                    selectedColor=colorsAdapter.getSellectedColor();

                if(sizesAdapter==null||sizesAdapter.getSelectedSize()==null)
                    selectedSize="";
                else
                    selectedSize=sizesAdapter.getSelectedSize();
                productItemRepository.getProductItemIdByColorAndSize(product_id, selectedColor, selectedSize).addOnCompleteListener(task -> {
                    ProductItem productItem = task.getResult();
                    Log.i("product_item_id", ""+productItem.getId());
                    if (productItem.getId() != null){
                        shoppingCartItemRepository.getCartItem(productItem.getId(), cartID).addOnCompleteListener(task1 -> {
                            String cartItemId = task1.getResult();
                            Log.i("product_item_id", ""+cartItemId);
                            if (cartItemId == null){
                                ShoppingCart cart = new ShoppingCart(FirebaseAuth.getInstance().getUid(), cartID);
                                ShopRepository shopRepository = new ShopRepository(this);
                                shopRepository.getShopByShopID(product.getShop_id()).addOnCompleteListener(task2 -> {
                                    Shop shop = task2.getResult();
                                    shoppingCartItemRepository.addNewCartItem(cart, productItem, product, shop, count);

                                });
                            }
                            else{
                                shoppingCartItemRepository.updateQty2(cartItemId, count);

                            }
                        });
                    }
                });
            }

            private void setStatus(String product_id, String selectedColor, String selectedSize) {
                Log.i("product_id", ""+product_id);
                Log.i("selectcolor", ""+selectedColor);
                Log.i("selectsize", ""+selectedSize);
                productItemRepository.getProductQTYByColorAndSize(product_id, selectedColor, selectedSize)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                qty = task.getResult();
                                Log.i("qty", ""+qty);
                                if (qty < count){
                                    status.setText("Not available");
                                    status.setTextColor(Color.RED);
                                    status.setVisibility(View.VISIBLE);
                                }
                                else {
                                    status.setText("Available");
                                    status.setTextColor(Color.GREEN);
                                }
                            }
                        });
            }
        }