    package com.example.EcommerceApp.domain.user;

    import static android.content.ContentValues.TAG;

    import android.content.Context;
    import android.util.Log;

    import com.example.EcommerceApp.model.Search;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.Timestamp;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.firestore.CollectionReference;
    import com.google.firebase.firestore.FieldValue;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.Query;
    import com.google.firebase.firestore.QueryDocumentSnapshot;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Objects;

    public class SearchRepository {
        private FirebaseFirestore db;
        private CollectionReference searchCollection;
        private FirebaseAuth auth;

        public SearchRepository(Context context) {
            db = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            searchCollection = db.collection("search");
        }

        public void add(String content) {
            searchCollection.whereEqualTo("content", content)
                    .whereEqualTo("user_id",auth.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty())
                            {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentId = document.getId();
                                    updateContent(documentId);
                                }
                            }
                            else
                                addNewContent(content);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        }

        void addNewContent(String content) {
            Map<String, Object> newContent = new HashMap<>();
            newContent.put("content", content);
            newContent.put("timestamp", Timestamp.now());
            newContent.put("search_count", 1);
            newContent.put("user_id", auth.getUid());
            Log.println(Log.ASSERT,"new","a");
            searchCollection.add(newContent)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                    });
        }

        void updateContent(String documentId) {
            Log.println(Log.ASSERT,"update","a");

            searchCollection.document(documentId)
                    .update("timestamp", Timestamp.now(),
                            "search_count", FieldValue.increment(1))
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        }

        public Task<List<Search>> getRecentSearches(int limit) {
            Query query = searchCollection
                    .whereEqualTo("user_id", auth.getUid())
                    .orderBy("timestamp", Query.Direction.DESCENDING);

            return query.get().continueWith(task -> {
                List<Search> searchList = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (Search search : task.getResult().toObjects(Search.class)) {
                        searchList.add(search);
                    }
                } else {
                    // Xử lý lỗi
                    Exception e = task.getException();
                    Log.println(Log.ASSERT, "load search", Objects.requireNonNull(e.getMessage()).toString());
                }
                if(searchList.size()<=limit)
                    return searchList;
                else return searchList.subList(0,limit);
            });
        }
        public Task<List<Search>> getTopMostSearchedContents(int n) {
            return searchCollection
                    .orderBy("content")
                    .get()
                    .continueWith(task -> {
                        Map<String, Long> searchCounts = new HashMap<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Search search = document.toObject(Search.class);
                                String content = search.getContent();
                                long count = search.getSearch_count();
                                if (searchCounts.containsKey(content)) {
                                    searchCounts.put(content, searchCounts.get(content) + count);
                                } else {
                                    searchCounts.put(content, count);
                                }
                            }
                            List<Map.Entry<String, Long>> sortedSearchCounts = new ArrayList<>(searchCounts.entrySet());
                            sortedSearchCounts.sort((o1, o2) -> (int) (o2.getValue() - o1.getValue())); // Sắp xếp theo số lượng giảm dần
                            List<Search> topNContents = new ArrayList<>();
                            for (int i = 0; i < Math.min(n, sortedSearchCounts.size()); i++) {
                                String content = sortedSearchCounts.get(i).getKey();
                                long count = sortedSearchCounts.get(i).getValue();
                                Search search = new Search();
                                search.setContent(content);
                                search.setSearch_count((int)count);
                                topNContents.add(search);
                            }
                            return topNContents;
                        } else {
                            // Xử lý lỗi
                            Exception e = task.getException();
                            // Log error
                            return null;
                        }
                    });
        }
    }
