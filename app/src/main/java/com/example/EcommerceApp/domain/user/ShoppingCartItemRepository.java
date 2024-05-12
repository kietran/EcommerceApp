package com.example.EcommerceApp.domain.user;

import android.util.Log;

import com.example.EcommerceApp.model.ShoppingCartItem;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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
