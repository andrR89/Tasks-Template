package com.devmasterteam.tasks.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.entities.TaskEntity;
import com.devmasterteam.tasks.listener.TaskListInterationListener;
import com.devmasterteam.tasks.viewholder.TaskViewHolder;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<TaskEntity> mListTaskEntities;
    private TaskListInterationListener taskListListener;

    /**
     * Construtor
     */
    public TaskListAdapter(List<TaskEntity> taskList, TaskListInterationListener taskListListener) {
        this.mListTaskEntities = taskList;
        this.taskListListener = taskListListener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        // Infla o layout da linha e faz uso na listagem
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_task_list, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        TaskEntity entity = this.mListTaskEntities.get(position);
        holder.bindData(entity, taskListListener);
    }

    @Override
    public int getItemCount() {
        return this.mListTaskEntities.size();
    }

    public void reloadList(List<TaskEntity> list){
        this.mListTaskEntities = list;
        notifyDataSetChanged();
    }

}
