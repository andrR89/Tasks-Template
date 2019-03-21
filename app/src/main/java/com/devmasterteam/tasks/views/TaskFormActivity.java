package com.devmasterteam.tasks.views;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.bussiness.PriorityBussiness;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entities.PriorityEntity;
import com.devmasterteam.tasks.entities.TaskEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.manager.PriorityManager;
import com.devmasterteam.tasks.manager.TaskManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskFormActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    private ViewHolder mViewHolder = new ViewHolder();
    private PriorityManager priorityManager;
    private TaskManager taskManager;
    private Context context;
    private List<Integer> listPriorityIDs = new ArrayList<>();
    private List<PriorityEntity> priorityList;
    private int taskID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        this.priorityManager = new PriorityManager(this);
        this.taskManager = new TaskManager(this);
        this.context = this;


        // Inicializa variáveis
        this.mViewHolder.editDescription = (EditText) this.findViewById(R.id.edit_description);
        this.mViewHolder.checkComplete = (CheckBox) this.findViewById(R.id.check_complete);
        this.mViewHolder.spinnerPriority = (Spinner) this.findViewById(R.id.spinner_priority);
        this.mViewHolder.buttonDate = (Button) this.findViewById(R.id.button_date);
        this.mViewHolder.buttonSave = (Button) this.findViewById(R.id.button_save);
        this.mViewHolder.progressDialog = new ProgressDialog(this);
        this.mViewHolder.imageBack = (ImageView) this.findViewById(R.id.image_toolbar_back);
        this.mViewHolder.formHeader = (TextView) this.findViewById(R.id.text_task_form_header);

        // Atribui eventos
        this.mViewHolder.buttonSave.setOnClickListener(this);
        this.mViewHolder.buttonDate.setOnClickListener(this);
        this.mViewHolder.imageBack.setOnClickListener(this);

        this.loadPriorities();
        this.loadDataFromActivity();
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.button_save) {
            this.handleSave();
        } else if (id == R.id.button_date) {
            this.showDatePicker();
        } else if (id == R.id.image_toolbar_back) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        this.mViewHolder.buttonDate.setText(SDF.format(calendar.getTime()));
    }

    private void loadPriorities() {
        this.priorityList = this.priorityManager.getListLocal();
        List<String> listDescription = new ArrayList<>();

        for(PriorityEntity entity: this.priorityList){
            listDescription.add(entity.getDescription());
            this.listPriorityIDs.add(entity.getId());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listDescription);
        this.mViewHolder.spinnerPriority.setAdapter(adapter);
    }

    private void showDatePicker() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, this, year, month, day).show();
    }

    private void handleSave() {
        showLoading(true, getString(R.string.salvando), getString(R.string.salvando_tarefa));
        try {
            TaskEntity entity = new TaskEntity();

            entity.Id = taskID;
            entity.Description = this.mViewHolder.editDescription.getText().toString();
            entity.Complete = this.mViewHolder.checkComplete.isChecked();
            entity.PriorityId =  this.listPriorityIDs.get(this.mViewHolder.spinnerPriority.getSelectedItemPosition());


            if(!this.mViewHolder.buttonDate.getText().toString().equals("")){
                entity.DueDate = SDF.parse(this.mViewHolder.buttonDate.getText().toString());
            }

            if(entity.Id == 0){
                this.taskManager.insert(entity, this.taskSaveListener());
            } else{
                this.taskManager.update(entity, this.taskSaveListener());
            }



        } catch (ParseException e) {
            Toast.makeText(context, R.string.UNEXPECTED_ERROR, Toast.LENGTH_LONG).show();
            showLoading(false, null, null);
        }


    }

    private void showLoading(boolean show, String title, String message){
        if(show){
            this.mViewHolder.progressDialog.setTitle(title);
            this.mViewHolder.progressDialog.setMessage(message);
            this.mViewHolder.progressDialog.show();
        }else{
            this.mViewHolder.progressDialog.hide();
            this.mViewHolder.progressDialog.dismiss();
        }
    }

    private OperationListener taskSaveListener(){
        return new OperationListener<Boolean>(){

            @Override
            public void onSuccess(Boolean result){
                showLoading(false, null, null);
                if(taskID == 0){
                    Toast.makeText(context, R.string.tarefa_incluida_com_sucesso, Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(context, R.string.tarefa_atualizada_com_sucesso, Toast.LENGTH_LONG).show();
                }

                finish();
            }

            @Override
            public void onError(int errorCode, String errorMessage){
                showLoading(false, null, null);
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        };
    }

    private OperationListener taskLoadedListener(){
        return new OperationListener<TaskEntity>(){

            @Override
            public void onSuccess(TaskEntity result){
                mViewHolder.editDescription.setText(result.Description);
                mViewHolder.buttonDate.setText(SDF.format((result.DueDate)));
                mViewHolder.checkComplete.setChecked(result.Complete);
                mViewHolder.spinnerPriority.setSelection(getSpinerIndex(result.PriorityId));
            }

            @Override
            public void onError(int errorCode, String errorMessage){
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
            }
        };
    }

    private int getSpinerIndex(int id){
        int index = 0;
        for (int i = 0; i <  this.priorityList.size(); i++){
            if(this.priorityList.get(i).getId() == id){
                index = i;
                break;
            }
        }
        return index;
    }

    private void loadDataFromActivity() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.taskID = bundle.getInt(TaskConstants.BUNDLE.TASK_ID);

            if(this.taskID !=  0){
                this.taskManager.get(taskID, this.taskLoadedListener());
                this.mViewHolder.formHeader.setText(R.string.atualizar_tarefa);
            }
        } else{
            this.mViewHolder.formHeader.setText(R.string.adicionar_nova_tarefa);
        }
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        private EditText editDescription;
        private CheckBox checkComplete;
        private Spinner spinnerPriority;
        private Button buttonDate;
        private Button buttonSave;
        private ProgressDialog progressDialog;
        private ImageView imageBack;
        private TextView formHeader;
    }
}
