package com.example.equationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.equationmanager.adapters.PolynomialAdapter;
import com.example.equationmanager.database.PolynomialDatabase;
import com.example.equationmanager.models.Polynomial;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PolynomialAdapter adapter;
    private PolynomialDatabase database;
    private Button addPolynomialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // אתחול הרכיבים ב-XML
        recyclerView = findViewById(R.id.recyclerView);
        addPolynomialButton = findViewById(R.id.addButton);

        // יצירת חיבור למסד הנתונים
        database = new PolynomialDatabase(this);

        // הגדרת RecyclerView עם Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // טוען את רשימת הפולינומים ממסד הנתונים
        loadPolynomials();

        // מעבר למסך הוספת פולינום
        addPolynomialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "שגיאה: לא ניתן לפתוח את מסך ההוספה", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

   /* private void loadPolynomials() {
        List<Polynomial> polynomialList = database.getAllPolynomials();

        if (polynomialList.isEmpty()) {
            Toast.makeText(this, "אין פולינומים להצגה", Toast.LENGTH_SHORT).show();
        }

        adapter = new PolynomialAdapter(this, polynomialList, database);
        recyclerView.setAdapter(adapter);
    }*/

    private void loadPolynomials() {
        List<Polynomial> polynomialList = database.getAllPolynomials();

        if (polynomialList.isEmpty()) {
            // אם הרשימה ריקה, הצג הודעה במקום לסגור את האפליקציה
            Toast.makeText(this, "No polynomials available", Toast.LENGTH_SHORT).show();
        }

        adapter = new PolynomialAdapter(this, polynomialList, database);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadPolynomials();  // מרענן את הרשימה כשחוזרים למסך הראשי
    }
}
