package com.example.equationmanager;

import com.example.equationmanager.database.PolynomialDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;


public class AddActivity extends AppCompatActivity {

    private EditText degreeInput;
    private Button actionButton, saveButton;
    private LinearLayout coefficientsContainer;
    private List<EditText> coefficientFields;
    private int selectedDegree = 1; // ברירת מחדל

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // אתחול רכיבים
        degreeInput = findViewById(R.id.degreeInput);
        actionButton = findViewById(R.id.actionButton);
        saveButton = findViewById(R.id.saveButton);
        coefficientsContainer = findViewById(R.id.coefficientsContainer);
        coefficientFields = new ArrayList<>();

        // מוסתרים עד לחיצה על "Action"
        coefficientsContainer.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);

        // כאשר המשתמש לוחץ על "Action"
        actionButton.setOnClickListener(v -> {
            String inputText = degreeInput.getText().toString().trim();

            // בדיקה האם השדה ריק
            if (inputText.isEmpty()) {
                Toast.makeText(this, "Please enter a valid degree (1-99)", Toast.LENGTH_SHORT).show();
                return;
            }

            // בדיקה האם המשתמש הכניס מספר חוקי (1-99)
            try {
                selectedDegree = Integer.parseInt(inputText);
                if (selectedDegree < 1 || selectedDegree > 99) {
                    Toast.makeText(this, "Degree must be between 1 and 99", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid input. Please enter a number", Toast.LENGTH_SHORT).show();
                return;
            }

            // יצירת שדות קלט למקדמים
            updateCoefficientFields(selectedDegree);
            coefficientsContainer.setVisibility(View.VISIBLE); // הצגת השדות
            saveButton.setVisibility(View.VISIBLE); // הצגת כפתור השמירה
        });

        // שמירת הפולינום
        saveButton.setOnClickListener(v -> savePolynomial());
    }

    // יצירת שדות קלט דינאמיים לפי דרגת הפולינום
   /* private void updateCoefficientFields(int degree) {
        coefficientsContainer.removeAllViews();
        coefficientFields.clear();

        for (int i = 0; i <= degree; i++) { // מספר המקדמים הוא (דרגה + 1)
            EditText editText = new EditText(this);
            editText.setHint("Coefficient " + i);
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);
            coefficientsContainer.addView(editText);
            coefficientFields.add(editText);

        }
    }*/
    private void updateCoefficientFields(int degree) {
        coefficientsContainer.removeAllViews(); // מסיר את השדות הקודמים
        coefficientFields.clear();

        for (int i = 0; i <= degree; i++) { // מספר המקדמים הוא (דרגה + 1)
            EditText editText = new EditText(this);
            editText.setHint("Coefficient " + i);
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);

            // הגדרות עיצוב כדי להבליט את השדות
            editText.setTextColor(Color.BLACK);  // הופך את הטקסט ללבן
            editText.setHintTextColor(Color.GRAY); // הופך את ה־hint לאפור בהיר
            editText.setTextSize(28); // גודל טקסט גדול יותר
            editText.setTypeface(null, Typeface.BOLD); // הופך את הטקסט למודגש
            editText.setGravity(Gravity.CENTER); // מרכז את הטקסט בשדה
            editText.setMinHeight(56); // מגדיר גובה מינימלי לשדה
            editText.setPadding(16, 16, 16, 16); // מוסיף מרווח פנימי לנוחות

            // מוסיף את השדה לתצוגה
            coefficientsContainer.addView(editText);
            coefficientFields.add(editText);
        }
    }


    // שמירת הפולינום למסד הנתונים
   /* private void savePolynomial() {
        StringBuilder coefficients = new StringBuilder();
        for (EditText field : coefficientFields) {
            String value = field.getText().toString().trim();
            if (value.isEmpty()) {
                value = "0"; // ברירת מחדל אם לא נכתב כלום
            }
            coefficients.append(value).append(",");
        }

        // הסרת פסיק מיותר בסוף
        if (coefficients.length() > 0) {
            coefficients.setLength(coefficients.length() - 1);
        }

        // שמירת הנתונים למסד הנתונים (כאן צריך להוסיף את המחלקה PolynomialDatabase)
        PolynomialDatabase database = new PolynomialDatabase(this);
        database.addPolynomial(selectedDegree, coefficients.toString());

        Toast.makeText(this, "Polynomial Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }*/
    private void savePolynomial() {
        StringBuilder coefficients = new StringBuilder();
        for (EditText field : coefficientFields) {
            String value = field.getText().toString().trim();

            // המרה למספר ואז חזרה למחרוזת כדי להסיר אפסים מובילים
            if (!value.isEmpty()) {
                value = String.valueOf(Integer.parseInt(value));
            } else {
                value = "0"; // ברירת מחדל אם לא נכתב כלום
            }

            coefficients.append(value).append(",");
        }

        // הסרת פסיק מיותר בסוף
        if (coefficients.length() > 0) {
            coefficients.setLength(coefficients.length() - 1);
        }

        // שמירת הנתונים למסד
        PolynomialDatabase database = new PolynomialDatabase(this);
        database.addPolynomial(selectedDegree, coefficients.toString());

        Toast.makeText(this, "Polynomial Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }

}
