package com.example.equationmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.equationmanager.database.PolynomialDatabase;
import com.example.equationmanager.models.Polynomial;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;


public class UpdateActivity extends AppCompatActivity {

    private EditText degreeInput;
    private Button actionButton, saveButton;
    private LinearLayout coefficientsContainer;
    private List<EditText> coefficientFields;
    private int polynomialId;
    private PolynomialDatabase database;
    private int currentDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // אתחול רכיבים
        degreeInput = findViewById(R.id.degreeInput);
        actionButton = findViewById(R.id.actionButton);
        saveButton = findViewById(R.id.saveButton);
        coefficientsContainer = findViewById(R.id.coefficientsContainer);
        coefficientFields = new ArrayList<>();
        database = new PolynomialDatabase(this);

        // קבלת ID מהאינטנט
        polynomialId = getIntent().getIntExtra("POLYNOMIAL_ID", -1);
        if (polynomialId == -1) {
            Toast.makeText(this, "Error loading polynomial", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // טעינת הפולינום מהמסד
        Polynomial polynomial = database.getPolynomialById(polynomialId);
        if (polynomial == null) {
            Toast.makeText(this, "Polynomial not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentDegree = polynomial.getDegree();
        degreeInput.setText(String.valueOf(currentDegree));

        // כאשר המשתמש משנה את הדרגה ולוחץ על "Action"
        actionButton.setOnClickListener(v -> {
            int newDegree;
            try {
                newDegree = Integer.parseInt(degreeInput.getText().toString().trim());
                if (newDegree < 1 || newDegree > 99) {
                    Toast.makeText(this, "Degree must be between 1-99", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid degree", Toast.LENGTH_SHORT).show();
                return;
            }

            updateCoefficientFields(newDegree, polynomial.getCoefficientsAsList());
            coefficientsContainer.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
        });

        // שמירת הפולינום
        saveButton.setOnClickListener(v -> savePolynomial());

        // הוספת כפתור חזרה למסך הראשי
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // סוגר את עמוד העדכון כדי למנוע חזרה אליו
        });

    }

    // יצירת שדות קלט דינאמיים לפי דרגת הפולינום
    private void updateCoefficientFields(int degree, List<String> oldCoefficients) {
        coefficientsContainer.removeAllViews();
        coefficientFields.clear();

        for (int i = 0; i <= degree; i++) {
            EditText editText = new EditText(this);
            editText.setHint("Coefficient " + i);
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);

            // אם קיים ערך קודם, נטען אותו
            if (i < oldCoefficients.size()) {
                editText.setText(String.valueOf(oldCoefficients.get(i)));
            }

            coefficientsContainer.addView(editText);
            coefficientFields.add(editText);
        }
    }

    // שמירת הפולינום למסד הנתונים
    private void savePolynomial() {
        int newDegree;
        try {
            newDegree = Integer.parseInt(degreeInput.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid degree", Toast.LENGTH_SHORT).show();
            return;
        }

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

        // עדכון במסד הנתונים
        Polynomial updatedPolynomial = new Polynomial(polynomialId, newDegree, coefficients.toString());
        database.updatePolynomial(updatedPolynomial);

        Toast.makeText(this, "Polynomial Updated!", Toast.LENGTH_SHORT).show();
        finish();
    }




}
