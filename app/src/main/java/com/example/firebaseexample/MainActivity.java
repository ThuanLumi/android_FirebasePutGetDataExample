package com.example.firebaseexample;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // Access a Cloud Firestore instance from your Activity
      FirebaseFirestore db = FirebaseFirestore.getInstance();

      CollectionReference cities = db.collection("cities");

      Map<String, Object> data1 = new HashMap<>();
      data1.put("name", "San Francisco");
      data1.put("state", "CA");
      data1.put("country", "USA");
      data1.put("capital", false);
      data1.put("population", 860000);
      data1.put("regions", Arrays.asList("west_coast", "norcal"));
      cities.document("SF").set(data1);

      Map<String, Object> data2 = new HashMap<>();
      data2.put("name", "Los Angeles");
      data2.put("state", "CA");
      data2.put("country", "USA");
      data2.put("capital", false);
      data2.put("population", 3900000);
      data2.put("regions", Arrays.asList("west_coast", "socal"));
      cities.document("LA").set(data2);

      Map<String, Object> data3 = new HashMap<>();
      data3.put("name", "Washington D.C.");
      data3.put("state", null);
      data3.put("country", "USA");
      data3.put("capital", true);
      data3.put("population", 680000);
      data3.put("regions", Arrays.asList("east_coast"));
      cities.document("DC").set(data3);

      Map<String, Object> data4 = new HashMap<>();
      data4.put("name", "Tokyo");
      data4.put("state", null);
      data4.put("country", "Japan");
      data4.put("capital", true);
      data4.put("population", 9000000);
      data4.put("regions", Arrays.asList("kanto", "honshu"));
      cities.document("TOK").set(data4);

      Map<String, Object> data5 = new HashMap<>();
      data5.put("name", "Beijing");
      data5.put("state", null);
      data5.put("country", "China");
      data5.put("capital", true);
      data5.put("population", 21500000);
      data5.put("regions", Arrays.asList("jingjinji", "hebei"));
      cities.document("BJ").set(data5);

      // Update one field, creating the document if it does not already exist.
      Map<String, Object> data = new HashMap<>();
      data.put("capital", true);

      db.collection("cities").document("BJ")
              .set(data, SetOptions.merge());

      // Get one document
      DocumentReference docRef = db.collection("cities").document("SF");
      docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
               DocumentSnapshot document = task.getResult();
               if (document.exists()) {
                  Log.d("FirebaseGetDocument", "DocumentSnapshot data: " + document.getData());
               } else {
                  Log.d("FirebaseGetDocument", "No such document");
               }
            } else {
               Log.d("FirebaseGetDocument", "get failed with ", task.getException());
            }
         }
      });

      // Get custom object
      DocumentReference docRef1 = db.collection("cities").document("BJ");
      docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
         @Override
         public void onSuccess(DocumentSnapshot documentSnapshot) {
            City city = documentSnapshot.toObject(City.class);
            Log.d("FirebaseGetCustomObject", "Document Snapshot data: " +
                    documentSnapshot.getData());
         }
      });

      // Get multiple documents
      db.collection("cities")
              .whereEqualTo("capital", true)
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()) {
                          Log.d("FirebaseGetMultipleDocs",
                                  document.getId() + " => " + document.getData());
                       }
                    } else {
                       Log.d("FirebaseGetMultipleDocs", "Error getting documents: ",
                               task.getException());
                    }
                 }
              });

      // Get all documents
      db.collection("cities")
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : task.getResult()) {
                          Log.d("FirebaseGetAllDocuments",
                                  document.getId() + " => " + document.getData());
                       }
                    } else {
                       Log.d("FirebaseGetAllDocuments", "Error getting documents: ",
                               task.getException());
                    }
                 }
              });


      CollectionReference citiesRef = db.collection("cities");

      Map<String, Object> ggbData = new HashMap<>();
      ggbData.put("name", "Golden Gate Bridge");
      ggbData.put("type", "bridge");
      citiesRef.document("SF").collection("landmarks").add(ggbData);

      Map<String, Object> lohData = new HashMap<>();
      lohData.put("name", "Legion of Honor");
      lohData.put("type", "museum");
      citiesRef.document("SF").collection("landmarks").add(lohData);

      Map<String, Object> gpData = new HashMap<>();
      gpData.put("name", "Griffith Park");
      gpData.put("type", "park");
      citiesRef.document("LA").collection("landmarks").add(gpData);

      Map<String, Object> tgData = new HashMap<>();
      tgData.put("name", "The Getty");
      tgData.put("type", "museum");
      citiesRef.document("LA").collection("landmarks").add(tgData);

      Map<String, Object> lmData = new HashMap<>();
      lmData.put("name", "Lincoln Memorial");
      lmData.put("type", "memorial");
      citiesRef.document("DC").collection("landmarks").add(lmData);

      Map<String, Object> nasaData = new HashMap<>();
      nasaData.put("name", "National Air and Space Museum");
      nasaData.put("type", "museum");
      citiesRef.document("DC").collection("landmarks").add(nasaData);

      Map<String, Object> upData = new HashMap<>();
      upData.put("name", "Ueno Park");
      upData.put("type", "park");
      citiesRef.document("TOK").collection("landmarks").add(upData);

      Map<String, Object> nmData = new HashMap<>();
      nmData.put("name", "National Museum of Nature and Science");
      nmData.put("type", "museum");
      citiesRef.document("TOK").collection("landmarks").add(nmData);

      Map<String, Object> jpData = new HashMap<>();
      jpData.put("name", "Jingshan Park");
      jpData.put("type", "park");
      citiesRef.document("BJ").collection("landmarks").add(jpData);

      Map<String, Object> baoData = new HashMap<>();
      baoData.put("name", "Beijing Ancient Observatory");
      baoData.put("type", "museum");
      citiesRef.document("BJ").collection("landmarks").add(baoData);

      // Collection group queries
      db.collectionGroup("landmarks").whereEqualTo("type", "museum").get()
              .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                 @Override
                 public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    // ...
                 }
              });

      // Use a document snapshot to define the query cursor
      db.collection("cities").document("SF")
              .get()
              .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                 @Override
                 public void onSuccess(DocumentSnapshot documentSnapshot) {
                    // Get all cities with a population bigger than San Francisco.
                    Query biggerThanSf = db.collection("cities")
                            .orderBy("population")
                            .startAt(documentSnapshot);
                    Log.d("FirebaseQueryCursor", biggerThanSf.toString());
                    // ...
                 }
              });

   }
}