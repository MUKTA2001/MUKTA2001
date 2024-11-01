package com.example.ecofriendlyproductappfinder;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.EditText;
import android.widget.Button;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class FoodListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> foodList;
    private ArrayAdapter<String> adapter;
    private SearchView searchView;
    private Spinner spinner;
    private EditText newFoodEditText;
    private Button addFoodButton;
    private Map<String, String> foodCategoryMap;

    @SuppressLint( "MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_list);
        listView = findViewById(R.id.listView);

        searchView = findViewById(R.id.searchView);
        spinner = findViewById(R.id.spinner);
        newFoodEditText = findViewById(R.id.newFoodEditText);
        addFoodButton = findViewById(R.id.addFoodButton);

        initializeFoodListAndCategories();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                foodList
        );
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = foodList.get(position);
                if ("Apples".equals(selectedItem)) {
                    Toast.makeText(getApplicationContext(), "Apples is clicked ", Toast.LENGTH_SHORT).show();



                    Intent intent = new Intent(getApplicationContext(), FoodDetailActivity.class);
                    intent.putExtra("food_item", selectedItem);
                    startActivity(intent);


                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }

        });
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.food_categories,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterFoodList(searchView.getQuery().toString(), parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFood = newFoodEditText.getText().toString().trim();
                if (!newFood.isEmpty()) {
                    addFoodItemAndNotify(newFood, "Fruits");  // Assuming "Fruits" as category
                    newFoodEditText.setText("");
                    Toast.makeText(FoodListActivity.this, newFood + " added to the list", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FoodListActivity.this, "Please enter a food item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
         private void initializeFoodListAndCategories() {
             foodList = new ArrayList<>();
             foodCategoryMap = new HashMap<>();

             // Example food items with categories
             addFoodItem("Apples", "Fruits");
             addFoodItem("Spinach", "Vegetables");
             addFoodItem("Lentils", "Grains");
             addFoodItem("Wild Alaskan Salmon", "Seafood");
             addFoodItem("Quinoa", "Grains");
             addFoodItem("Permaculture-grown Herbs", "Herbs");

         }


    private void addFoodItem(String food, String category) {
        foodList.add(food);
        foodCategoryMap.put(food, category);
    }
    private void addFoodItemAndNotify(String newFood, String fruits) {
            addFoodItem("Orange","Fruits");
            adapter.notifyDataSetChanged();
            sendEmailNotifcation("Orange","fruits");

    }

         private void sendEmailNotifcation(String orange, String fruits) {
            String email="meharunmukta498@gmail.com";
            String subject="New Food Item Added";
         String message="A new food item has been added:\n\n" +
                    "Food: " + orange + "\n" +
                    "Category: " + fruits;

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"meharunmukta498@gmail.com"});
             intent.putExtra(Intent.EXTRA_SUBJECT, "A new food item has been added");
             intent.putExtra(Intent.EXTRA_TEXT,"Grab the product Qucikly");
             intent.putExtra(Intent.EXTRA_TEXT,"Connect with us for more update");
             intent.setType("message/rfc822");
             startActivity(Intent.createChooser(intent, "Send By Mukta"));
         }




    private void filterFoodList(String query, String category) {
        ArrayList<String> filteredList = new ArrayList<>();
        for (String item : foodList) {
            String itemCategory = foodCategoryMap.get(item);
            if (item.toLowerCase().contains(query.toLowerCase()) &&
                    (category.equals("All") || itemCategory.equals(category))) {
                filteredList.add(item);
            }
        }
        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}