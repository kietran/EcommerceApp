package com.example.EcommerceApp.domain.user;

import android.content.Context;

import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.utils.ShopUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductRepository {
    private FirebaseFirestore db;
    private CollectionReference productsCollection;


    public ProductRepository(Context ccontext) {
        db = FirebaseFirestore.getInstance();
        productsCollection = db.collection("test_product");
    }
    // Phương thức để lấy toàn bộ danh sách các item từ Firestore


    public Task<List<Product>> getAllProductsAsList() {
        return productsCollection.get().continueWith(task -> {
            List<Product> productsList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    product.setId(document.getId());
                    if(!Objects.equals(ShopUtil.getShopId(), product.getShop_id()))
                        productsList.add(product);
                }
            }
            return productsList;
        });
    }
    public Task<List<Product>> getAllProductByNamesAsList(String name) {
        return productsCollection
                .whereArrayContains("name", name)
                .get().continueWith(task -> {
            List<Product> productsList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    product.setId(document.getId());
                    if(!Objects.equals(ShopUtil.getShopId(), product.getShop_id()))
                        productsList.add(product);
                }
            }
            return productsList;
        });
    }

    public Task<List<Product>> get2ProductsAsList() {
        return productsCollection.get().continueWith(task -> {
            List<Product> productsList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    product.setId(document.getId());

                    if(!Objects.equals(ShopUtil.getShopId(), product.getShop_id()))
                        productsList.add(product);
                    if (productsList.size() == 2) {
                        break;
                    }
                }
            }
            return productsList;
        });
    }

    public Task<List<Product>> getAllProductsAsListByShopId(String shopId) {
        return productsCollection
                .whereEqualTo("shop_id", shopId)
                .get().continueWith(task -> {
                    List<Product> productsList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            product.setId(document.getId());
                            if(!Objects.equals(ShopUtil.getShopId(), product.getShop_id()))
                                productsList.add(product);
                        }
                    }
                    return productsList;
                });
    }

    public Task<List<Product>> get4ProductsAsListByShop(String shopId) {
        return productsCollection
                .whereEqualTo("shop_id", shopId)
                .limit(4)
                .get().continueWith(task -> {
            List<Product> productsList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (Product product : task.getResult().toObjects(Product.class)) {
                    productsList.add(product);
                    // Limit the loop to 4 iterations to ensure only 4 products are added
                    if (productsList.size() == 4) {
                        break;
                    }
                }
            }
            return productsList;
        });
    }

    public Task<Integer> countProductsByCategory(String categoryId) {
        return productsCollection
                .whereEqualTo("category_id", categoryId)
                .get().continueWith(task -> {
                    int count = 0;
                    if (task.isSuccessful()) {
                        count = task.getResult().size();
                    }
                    return count;
                });
    }

    public Task<List<Product>> getAllProductsAsListByCategoryID(String categoryID) {
        return productsCollection
                .whereEqualTo("category_id", categoryID)
                .get().continueWith(task -> {
                    List<Product> productsList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            product.setId(document.getId());
                            if(!Objects.equals(ShopUtil.getShopId(), product.getShop_id()))
                                productsList.add(product);
                        }
                    }
                    return productsList;
                });
    }

    public Task<Product> getProductByProductId(String productId) {
        return productsCollection
                .document(productId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Product product = document.toObject(Product.class);
                            product.setId(document.getId());
                            if(!Objects.equals(ShopUtil.getShopId(), product.getShop_id()))
                                return product;
                            else
                                return null;
                        }
                    }
                    return null;
                });
    }



//    public Task<QuerySnapshot> getAllProducts() {
//        return productsCollection.get();
//    }

//    public Task<Void> addItem(Item item) {
//        return productsCollection.document().set(item);
//    }
//
//    public Task<Void> removeItem(String itemId) {
//        return productsCollection.document(itemId).delete();
//    }
//
//    public Task<Void> updateItem(String itemId, Item updatedItem) {
//        return productsCollection.document(itemId).set(updatedItem);
//    }
}
