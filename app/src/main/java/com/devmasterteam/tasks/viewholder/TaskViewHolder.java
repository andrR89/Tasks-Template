package com.devmasterteam.tasks.viewholder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.constants.PriorityCacheConstants;
import com.devmasterteam.tasks.entities.TaskEntity;
import com.devmasterteam.tasks.listener.TaskListInterationListener;

import java.text.SimpleDateFormat;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextDescription;
    private TextView mTextPriorityId;
    private TextView mTextPriority;
    private TextView mTextDueDate;
    private ImageView mImageTask;

    private Context context;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public TaskViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        this.mTextDescription = itemView.findViewById(R.id.text_description);
        this.mTextPriorityId = itemView.findViewById(R.id.text_priority_id);
        this.mTextPriority = itemView.findViewById(R.id.text_priority);
        this.mTextDueDate = itemView.findViewById(R.id.text_due_date);
        this.mImageTask = itemView.findViewById(R.id.image_task);
    }

    public void bindData(final TaskEntity entity, final TaskListInterationListener taskListListener) {

        this.mTextDescription.setText(entity.Description);
        this.mTextPriorityId.setText(String.valueOf(entity.PriorityId));
        this.mTextDueDate.setText(SDF.format(entity.DueDate));

        if(entity.Complete){
            this.mImageTask.setImageResource(R.drawable.ic_done);
        } else{
            this.mImageTask.setImageResource(R.drawable.ic_todo);
        }

        this.mTextPriority.setText(PriorityCacheConstants.get(entity.PriorityId));

        this.mTextDescription.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                taskListListener.onListClick(entity.Id);
            }
        });

        this.mImageTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!entity.Complete){
                    taskListListener.onCompleteClick(entity.Id);
                }else{
                    taskListListener.onUncompleteClick(entity.Id);
                }
            }
        });

        this.mTextDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showConfirmDialog(entity, taskListListener);
                return true;
            }
        });

    }

    private void showConfirmDialog(final TaskEntity entity, final TaskListInterationListener taskListListener) {

        new AlertDialog.Builder(this.context)
        .setTitle(R.string.remocao_de_tarefa)
        .setMessage("Deseja realmente remover " + entity.Description + "?")
        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                taskListListener.onDeleteClick(entity.Id);
            }
        }).setNegativeButton(R.string.cancelar, null)
        .show();


    }
}
