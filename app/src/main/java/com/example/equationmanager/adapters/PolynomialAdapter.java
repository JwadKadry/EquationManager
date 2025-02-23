package com.example.equationmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.equationmanager.R;
import com.example.equationmanager.UpdateActivity;
import com.example.equationmanager.database.PolynomialDatabase;
import com.example.equationmanager.models.Polynomial;
import java.util.List;
import android.widget.Toast;

public class PolynomialAdapter extends RecyclerView.Adapter<PolynomialAdapter.ViewHolder> {
    private Context context;
    private List<Polynomial> polynomialList;
    private PolynomialDatabase database;

    // בנאי
    public PolynomialAdapter(Context context, List<Polynomial> polynomialList, PolynomialDatabase database) {
        this.context = context;
        this.polynomialList = polynomialList;
        this.database = database;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_polynomial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Polynomial polynomial = polynomialList.get(position);
        holder.degreeText.setText("Degre: " + polynomial.getDegree());
        holder.coefficientsText.setText(" Polynomial: \n" + formatPolynomial(polynomial));

        // כפתור לעדכון פולינום
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("POLYNOMIAL_ID", polynomial.getId());
            context.startActivity(intent);
        });

        // כפתור למחיקת פולינום
       /* holder.deleteButton.setOnClickListener(v -> {
            database.deletePolynomial(polynomial.getId()); // מחיקת הפולינום מהמסד
            polynomialList.remove(position); // הסרת הפריט מה-RecyclerView
            notifyItemRemoved(position); // עדכון התצוגה
        });*/

        /*holder.deleteButton.setOnClickListener(v -> {
            database.deletePolynomial(polynomial.getId()); // מחיקת הפולינום מהמסד
            polynomialList.remove(position); // הסרת הפריט מה-RecyclerView
            notifyItemRemoved(position); // עדכון התצוגה

            if (polynomialList.isEmpty()) {
                Toast.makeText(context, "All polynomials deleted!", Toast.LENGTH_SHORT).show();
            }
        });*/
        // כפתור למחיקת פולינום
        holder.deleteButton.setOnClickListener(v -> {
            database.deletePolynomial(polynomial.getId()); // מחיקת הפולינום מהמסד
            polynomialList.remove(position); // הסרת הפריט מהרשימה

            notifyItemRemoved(position); // עדכון התצוגה
            notifyItemRangeChanged(position, polynomialList.size()); // עדכון האינדקסים

            if (polynomialList.isEmpty()) {
                Toast.makeText(context, "All polynomials deleted!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return polynomialList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView degreeText, coefficientsText;
        ImageButton editButton, deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            degreeText = itemView.findViewById(R.id.degreeText);
            coefficientsText = itemView.findViewById(R.id.coefficientsText);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // פונקציה לעיצוב נכון של הפולינום
    private String formatPolynomial(Polynomial polynomial) {
        StringBuilder formatted = new StringBuilder();
        String[] coefficients = polynomial.getCoefficientsAsString().split(","); // פיצול המקדמים לפי פסיק
        int degree = polynomial.getDegree();

        for (int i = 0; i < coefficients.length; i++) {
            String coefficient = coefficients[i].trim();
            int exp = degree - i;

            if (!coefficient.equals("0")) { // לא להציג איברים עם 0
                if (formatted.length() > 0) {
                    formatted.append(" + ");
                }
                if (!coefficient.equals("1") || exp == 0) { // הצגת 1 רק אם זה המונום האחרון
                    formatted.append(coefficient);
                }
                if (exp > 0) {
                    formatted.append("x");
                    if (exp > 1) {
                        formatted.append("^").append(exp);
                    }
                }
            }
        }

        return formatted.toString();
    }
}
