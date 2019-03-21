package com.devmasterteam.tasks.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.adapter.TaskListAdapter;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entities.TaskEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.listener.TaskListInterationListener;
import com.devmasterteam.tasks.manager.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private int filter;
    private List<TaskEntity> mTaskEntityList;
    private TaskListAdapter mTaskListAdapter;
    private ViewHolder mViewHolder = new ViewHolder();
    private TaskListInterationListener taskListListener;
    private TaskManager taskManager;

    public static TaskListFragment newInstance(int value) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TaskConstants.TASK_FILTER.KEY, value);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.filter = getArguments().getInt(TaskConstants.TASK_FILTER.KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Infla o layout
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        // Incializa as variáveis
        this.mContext = rootView.getContext();
        this.taskManager = new TaskManager(this.mContext);

        // Inicializa elementos
        this.mViewHolder.floatAddTask = rootView.findViewById(R.id.float_add_task);
        this.mViewHolder.floatMenu = rootView.findViewById(R.id.float_menu_button);

        // Listner Listagem
        this.taskListListener = new TaskListInterationListener() {
            @Override
            public void onListClick(int id) {
                Bundle bundle = new Bundle();
                bundle.putInt(TaskConstants.BUNDLE.TASK_ID, id);

                Intent intent = new Intent(getContext(), TaskFormActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }

            @Override
            public void onCompleteClick(int id) {
                taskManager.complete(id, true, taskUpdateListener());
            }

            @Override
            public void onUncompleteClick(int id) {
                taskManager.complete(id, false, taskUpdateListener());
            }

            @Override
            public void onDeleteClick(int id) {
                taskManager.delete(id, taskDeletedListener());
            }
        };

        // Inicializa eventos
        this.mViewHolder.floatAddTask.setOnClickListener(this);
        this.mViewHolder.floatMenu.setOnClickListener(this);

        // 1 - Obter a recyclerview
        this.mViewHolder.recylerTaskList = (RecyclerView) rootView.findViewById(R.id.recycler_task_list);

        // 2 - Definir adapter passando listagem de itens
        this.mTaskEntityList = new ArrayList<>();
        this.mTaskListAdapter = new TaskListAdapter(this.mTaskEntityList, this.taskListListener);
        this.mViewHolder.recylerTaskList.setAdapter(mTaskListAdapter);

        // 3 - Definir um layout
        this.mViewHolder.recylerTaskList.setLayoutManager(new LinearLayoutManager(this.mContext));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getTasks();

    }

    private void getTasks() {
        this.mTaskEntityList = new ArrayList<>();
        this.taskManager.getList(this.filter, this.taskLoadedListner());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.float_add_task) {
            startActivity(new Intent(this.mContext, TaskFormActivity.class));
        } else if (id == R.id.float_menu_button) {
            boolean visibilit = this.mViewHolder.floatAddTask.getVisibility() == View.VISIBLE;

            if(!visibilit){
                Animation animationHide = AnimationUtils.loadAnimation(this.mContext, R.anim.float_menu_show);
                this.mViewHolder.floatAddTask.startAnimation(animationHide);
                this.mViewHolder.floatAddTask.setVisibility(View.VISIBLE);

            }else{
                Animation animationShow = AnimationUtils.loadAnimation(this.mContext, R.anim.float_menu_hide);
                this.mViewHolder.floatAddTask.startAnimation(animationShow);
                this.mViewHolder.floatAddTask.setVisibility(View.GONE);
            }

        }
    }

    private OperationListener taskLoadedListner() {
        return new OperationListener<List<TaskEntity>>() {

            @Override
            public void onSuccess(List<TaskEntity> result) {
                mTaskEntityList.addAll(result);
                mTaskListAdapter.reloadList(mTaskEntityList);

                int completes = 0;
                for (TaskEntity entity : result) {
                    if (entity.Complete) {
                        completes++;
                    }
                }

                // Permite chamar activits
                ((MainActivity) getActivity()).getCounts(completes, result.size());
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
            }
        };
    }

    private OperationListener taskUpdateListener() {
        return new OperationListener<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                getTasks();
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
            }
        };
    }

    private OperationListener taskDeletedListener() {
        return new OperationListener<Boolean>() {

            @Override
            public void onSuccess(Boolean result) {
                Toast.makeText(mContext, R.string.tarefa_removida_com_sucesso, Toast.LENGTH_LONG).show();
                getTasks();
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        private FloatingActionButton floatAddTask;
        private FloatingActionButton floatMenu;
        private RecyclerView recylerTaskList;
    }
}
