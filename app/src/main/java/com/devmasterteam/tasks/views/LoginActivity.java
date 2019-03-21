package com.devmasterteam.tasks.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.infra.SecurityPreferences;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.manager.PersonManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private PersonManager mPersonManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializa elementos
        this.mViewHolder.editEmail = (EditText) this.findViewById(R.id.edit_email);
        this.mViewHolder.editPassword = (EditText) this.findViewById(R.id.edit_password);
        this.mViewHolder.buttonLogin = (Button) this.findViewById(R.id.button_login);
        this.mViewHolder.textRegister = (TextView) this.findViewById(R.id.text_register);

        // Inicializa eventos
        this.mViewHolder.buttonLogin.setOnClickListener(this);
        this.mViewHolder.textRegister.setOnClickListener(this);

        this.mViewHolder.editEmail.setText("t3@t3.com");
        this.mViewHolder.editPassword.setText("123456");

        this.context = this;
        this.mPersonManager = new PersonManager(this.context);

        this.verifyLoggedUser();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_login) {
            this.handleLogin();
        } else if (id == R.id.text_register) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }

    private void handleLogin() {
        String email = this.mViewHolder.editEmail.getText().toString();
        String password = this.mViewHolder.editPassword.getText().toString();
        this.mPersonManager.login(email, password, loginListener());
    }

    private OperationListener loginListener(){
        return new OperationListener<Boolean>(){

            @Override
            public void onSuccess(Boolean result){
                goToHome();
            }

            @Override
            public void onError(int errorCode, String errorMessage){
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        };
    }

    private void verifyLoggedUser() {
        SecurityPreferences securityPreferences = new SecurityPreferences(this);
        String token = securityPreferences.getStoredString(TaskConstants.HEADER.TOKEN_KEY);
        String personKey = securityPreferences.getStoredString(TaskConstants.HEADER.PERSON_KEY);

        if(!token.equals("") && !personKey.equals("")){
            this.goToHome();
        }
    }

    private void goToHome(){
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }


    /**
     * ViewHolder
     */
    private static class ViewHolder {
        private EditText editEmail;
        private EditText editPassword;
        private Button buttonLogin;
        private TextView textRegister;
    }
}
