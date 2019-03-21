package com.devmasterteam.tasks.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.devmasterteam.tasks.bussiness.TaskBusiness;
import com.devmasterteam.tasks.entities.TaskEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.infra.operation.OperationResult;

import java.util.List;

public class TaskManager {

    private TaskBusiness taskBusiness;

    public TaskManager(Context context){
        this.taskBusiness = new TaskBusiness(context);
    }

    public void insert(final TaskEntity entity, final OperationListener listener) {
        AsyncTask<Void, Void, OperationResult<Boolean>> task = new AsyncTask<Void, Void, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return taskBusiness.insert(entity);
            }

            @Override
            protected void onPostExecute(OperationResult<Boolean> result){
                int error = result.getError();

                if(error != OperationResult.NO_ERROR){
                    listener.onError(error, result.getErrorMessage());
                } else{
                    listener.onSuccess(result.getResult());
                }
            }
        };

        // Executa a thread
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getList(final int filter, final OperationListener listener) {
        AsyncTask<Void, Void, OperationResult<List<TaskEntity>>> task = new AsyncTask<Void, Void, OperationResult<List<TaskEntity>>>() {
            @Override
            protected OperationResult<List<TaskEntity>> doInBackground(Void... voids) {
                return taskBusiness.getList(filter);
            }

            @Override
            protected void onPostExecute(OperationResult<List<TaskEntity>> result){
                int error = result.getError();

                if(error != OperationResult.NO_ERROR){
                    listener.onError(error, result.getErrorMessage());
                } else{
                    listener.onSuccess(result.getResult());
                }
            }
        };
        // Executa a thread
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void get(final int taskId, final OperationListener<TaskEntity> listener) {
        AsyncTask<Void, Integer, OperationResult<TaskEntity>> task = new AsyncTask<Void, Integer, OperationResult<TaskEntity>>() {
            @Override
            protected OperationResult<TaskEntity> doInBackground(Void... voids) {
                return taskBusiness.get(taskId);
            }

            @Override
            protected void onPostExecute(OperationResult<TaskEntity> result) {
                int error = result.getError();
                if (error != OperationResult.NO_ERROR) {
                    listener.onError(error, result.getErrorMessage());
                } else {
                    listener.onSuccess(result.getResult());
                }
            }
        };

        // Executa async
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void update(final TaskEntity taskEntity, final OperationListener<Boolean> listener) {
        AsyncTask<Void, Integer, OperationResult<Boolean>> task = new AsyncTask<Void, Integer, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return taskBusiness.update(taskEntity);
            }

            @Override
            protected void onPostExecute(OperationResult<Boolean> result) {
                int error = result.getError();
                if (error != OperationResult.NO_ERROR) {
                    listener.onError(error, result.getErrorMessage());
                } else {
                    listener.onSuccess(result.getResult());
                }
            }
        };

        // Executa async
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void complete(final int id, final Boolean complete, final OperationListener<Boolean> listener) {
        AsyncTask<Void, Integer, OperationResult<Boolean>> task = new AsyncTask<Void, Integer, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return taskBusiness.complete(id, complete);
            }

            @Override
            protected void onPostExecute(OperationResult<Boolean> result) {
                int error = result.getError();
                if (error != OperationResult.NO_ERROR) {
                    listener.onError(error, result.getErrorMessage());
                } else {
                    listener.onSuccess(result.getResult());
                }
            }
        };

        // Executa async
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void delete(final int taskId, final OperationListener<Boolean> listener) {
        AsyncTask<Void, Integer, OperationResult<Boolean>> task = new AsyncTask<Void, Integer, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return taskBusiness.delete(taskId);
            }

            @Override
            protected void onPostExecute(OperationResult<Boolean> result) {
                int error = result.getError();
                if (error != OperationResult.NO_ERROR) {
                    listener.onError(error, result.getErrorMessage());
                } else {
                    listener.onSuccess(result.getResult());
                }
            }
        };

        // Executa async
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
