package com.emmo.rideshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    private EditText fname;
    private EditText lname;
    private EditText emailText;
    private EditText password1;
    private EditText password2;
    private boolean passwordTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fname = findViewById(R.id.input_vname);
        lname = findViewById(R.id.input_nname);
        emailText = findViewById(R.id.inputEmail);
        password1 = findViewById(R.id.inputPassword);
        password2 = findViewById(R.id.inputConfirmPassword);
        Button register = findViewById(R.id.btnRegister);
        TextView login1 = findViewById(R.id.textView3);
        TextView login2 = findViewById(R.id.register_to_login_link);
        LinearLayout login = findViewById(R.id.lin_reg);

        password1.addTextChangedListener(textWatcher);
        password2.addTextChangedListener(textWatcher);

        register.setOnClickListener(v -> {
            if(passwordTrue){
                String email = emailText.getText().toString();
                UserDatabase userDatabase = new UserDatabase();
                boolean newUser = userDatabase.checkUserExists(email);
                if(newUser) {
                    saveUser();
                }
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        login1.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        login2.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Überprüfen, ob die E-Mail und das Passwort übereinstimmen
            String passwort1 = password1.getText().toString();
            String passwort2 = password2.getText().toString();
            boolean match = passwort1.equals(passwort2);

            if (!match) {
                passwordTrue = false;
                Toast.makeText(RegisterActivity.this, "Passwort muss gleich sein", Toast.LENGTH_SHORT).show();
            } else {
                passwordTrue = true;
            }
        }
    };

    private void saveUser(){
        String firstname = fname.getText().toString();
        String lastname = lname.getText().toString();
        String email = emailText.getText().toString();
        String pword = password1.getText().toString();

        NewUser user = new NewUser();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(pword);

        DatabaseGlobal database = new DatabaseGlobal();
        // Aufruf der Methode zur Navigation zur nächsten Aktivität
        database.writeToDatabaseUser(user, this::navigateToNextActivity);
    }

    private void navigateToNextActivity() {
        // Code zum Wechseln zur nächsten Aktivität
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional, um die aktuelle Aktivität zu schließen
    }

}