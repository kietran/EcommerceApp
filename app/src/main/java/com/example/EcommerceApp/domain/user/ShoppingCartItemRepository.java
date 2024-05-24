package com.example.EcommerceApp.domain.user;

import android.content.Context;
import android.util.Log;

import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.model.ProductItem;
import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.model.ShoppingCart;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartItemRepository {
    private FirebaseFirestore db;
    private static CollectionReference shoppingCartItem;
    public ShoppingCartItemRepository() {
        this.db = FirebaseFirestore.getInstance();
        shoppingCartItem = db.collection("shopping_cart_item");
        Map<String, List<ShoppingCartItem>> listCartItem = new HashMap<>();
    }
    public void addNewCartItem(ShoppingCart shoppingCart, ProductItem productItem, Product product, Shop shop, int qty){
        Map<String, Object> shoppingCartItem = new HashMap<>();
        Map<String, Object> cart = new HashMap<>();
        Map<String, Object> _productItem = new HashMap<>();
        Map<String, Object> _product = new HashMap<>();
        Map<String, Object> _shop = new HashMap<>();
        cart.put("id", shoppingCart.getId());
        cart.put("user_id", shoppingCart.getUserID());
        _product.put("id", product.getId());
        _product.put("name", product.getName());
        _product.put("price", product.getPrice());
        _product.put("product_image", product.getProduct_image());
        _shop.put("id", shop.getShopId());
        _shop.put("name", shop.getShopName());
        _product.put("shop", _shop);
        _productItem.put("id", productItem.getId());
        _productItem.put("product", _product);
        _productItem.put("color", productItem.getColor());
        _productItem.put("size", productItem.getSize());
        _productItem.put("qty_in_stock", productItem.getQty_in_stock());
        shoppingCartItem.put("cart", cart);
        shoppingCartItem.put("product_item", _productItem);
        shoppingCartItem.put("qty", qty);
        FirebaseFirestore.getInstance().collection("shopping_cart_item").add(shoppingCartItem)
                .addOnSuccessListener(documentReference -> Log.d("FirebaseUtil", "Shoppingcartitem added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("FirebaseUtil", "Error adding shopping_cart_item", e));

    }

    public Task<String> getCartItem(String product_item_id, String cart_id){
        return shoppingCartItem
                .whereEqualTo("cart.id", cart_id)
                .whereEqualTo("product_item.id", product_item_id)
                .get().continueWith(task -> {
                    String cartItemID = null;
                    if(task.isSuccessful()){
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (!documents.isEmpty()) {
                            DocumentSnapshot document = documents.get(0);
                            cartItemID = document.getId();
                        }
                    }
                    return cartItemID;
                });
    }

    public static void updateQty2(String cartItemId, int qtyToAdd) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("shopping_cart_item").document(cartItemId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long currentQty = documentSnapshot.getLong("qty");
                if (currentQty != null) {
                    long newQty = currentQty + qtyToAdd;
                    docRef.update("qty", newQty)
                            .addOnSuccessListener(aVoid -> Log.d("ShoppingCartItemRepo", "Quantity updated successfully"))
                            .addOnFailureListener(e -> Log.e("ShoppingCartItemRepo", "Failed to update quantity: " + e.getMessage()));
                } else {
                    Log.e("ShoppingCartItemRepo", "Current quantity is null");
                }
            } else {
                Log.e("ShoppingCartItemRepo", "Document does not exist");
            }
        }).addOnFailureListener(e -> Log.e("ShoppingCartItemRepo", "Failed to get document: " + e.getMessage()));
    }

    public static void updateQty(String cartItemId, int qty){
        shoppingCartItem.document(cartItemId).update("qty", qty)
                .addOnSuccessListener(aVoid -> Log.d("ShoppingCartItemRepo", "Quantity updated successfully"))
                .addOnFailureListener(e -> Log.e("ShoppingCartItemRepo", "Failed to update quantity: " + e.getMessage()));
    }
    public static void delete(String cartItemId){
        shoppingCartItem.document(cartItemId).delete()
                .addOnSuccessListener(aVoid -> Log.d("ShoppingCartItemRepo", "Item deleted successfully"))
                .addOnFailureListener(e -> Log.e("ShoppingCartItemRepo", "Failed to delete item: " + e.getMessage()));
    }
    public static void delete(List<ShoppingCartItem> list){
        for(ShoppingCartItem cartItem: list) {
            shoppingCartItem.document(cartItem.getId()).delete()
                    .addOnSuccessListener(aVoid -> Log.d("ShoppingCartItemRepo", "Item deleted successfully"))
                    .addOnFailureListener(e -> Log.e("ShoppingCartItemRepo", "Failed to delete item: " + e.getMessage()));
        }
    }


    public Task<Map<String, List<ShoppingCartItem>>> getCartItemGroupByShop() {
        Query query = shoppingCartItem.whereEqualTo("cart.user_id",FirebaseAuth.getInstance().getCurrentUser().getUid());
        return query.get().continueWith(task -> {
            Map<String, List<ShoppingCartItem>> groupedCartItems = new HashMap<>();


            Log.println(Log.INFO,"cartItem", "start");
            QuerySnapshot querySnapshot = task.getResult();

            if (querySnapshot != null) {
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    ShoppingCartItem cartItem = document.toObject(ShoppingCartItem.class);
                    if (cartItem != null) {
                        cartItem.setId(document.getId());
                        Map<String, Object> cart = (Map<String, Object>) document.getData().get("cart");
                        Map<String, Object> product_item = (Map<String, Object>) document.getData().get("product_item");
                        Map<String, Object> product = (Map<String, Object>) product_item.get("product");
                        Map<String, Object> shop = (Map<String, Object>) product.get("shop");;
                        String shopName = (String)shop.get("name");

                        Log.println(Log.ASSERT,"shop",shopName);
                        cartItem.setShop(shop);
                        cartItem.setCart(cart);
                        cartItem.setProduct_item(product_item);

                        if (!groupedCartItems.containsKey(shopName)) {
                            groupedCartItems.put(shopName, new ArrayList<>());
                        }
                        groupedCartItems.get(shopName).add(cartItem);
                        Log.println(Log.INFO,shopName + " size",String.valueOf(groupedCartItems.get(shopName).size()));
                    }
                }
            }
            return groupedCartItems;
        });
    }
}
