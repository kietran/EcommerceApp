package com.example.EcommerceApp.utils;

import com.example.EcommerceApp.domain.user.ShoppingCartItemRepository;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.auth.FirebaseAuth;

public class CartNumberUtil {

    static int qty_in_cart;

    public CartNumberUtil() {
        shoppingCartItemRepository = new ShoppingCartItemRepository();
    }

    static ShoppingCartItemRepository shoppingCartItemRepository;

    public static BadgeDrawable getBadge_mycart() {
        return badge_mycart;
    }

    public static void setBadge_mycart(BadgeDrawable badge_mycart) {
        CartNumberUtil.badge_mycart = badge_mycart;
    }

    static BadgeDrawable badge_mycart;

    public static int getQty_in_cart() {
        return qty_in_cart;
    }

    public static void setQty_in_cart(int qty_in_cart) {
        CartNumberUtil.qty_in_cart = qty_in_cart;
    }
    public static void getCartNumber(){
        shoppingCartItemRepository.getQTYinCartByUserID(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(task -> {
            int qty_in_Cart = task.getResult();
            if (qty_in_Cart > 0){
                CartNumberUtil.setQty_in_cart(qty_in_Cart);
                CartNumberUtil.getBadge_mycart().setNumber(CartNumberUtil.getQty_in_cart());
                CartNumberUtil.getBadge_mycart().setVisible(true);
            }
            else{
                CartNumberUtil.setQty_in_cart(0);
                CartNumberUtil.getBadge_mycart().setVisible(false);
            }
        });
    }
}
