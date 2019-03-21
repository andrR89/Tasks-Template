package com.devmasterteam.tasks.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.devmasterteam.tasks.bussiness.PriorityBussiness;
import com.devmasterteam.tasks.entities.PriorityEntity;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.infra.operation.OperationResult;

import java.util.List;

public class PriorityManager {

    PriorityBussiness priorityBussiness;

    public PriorityManager(Context context) {
        this.priorityBussiness = new PriorityBussiness(context);
    }

    public void getList(final OperationListener listener) {
        AsyncTask<Void, Void, OperationResult<Boolean>> task = new AsyncTask<Void, Void, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return priorityBussiness.getList();
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

    public List<PriorityEntity> getListLocal(){
        return this.priorityBussiness.getListLocal();
    }
}
