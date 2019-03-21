package com.devmasterteam.tasks.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.devmasterteam.tasks.bussiness.PersonBussiness;
import com.devmasterteam.tasks.infra.operation.OperationListener;
import com.devmasterteam.tasks.infra.operation.OperationResult;

public class PersonManager {

    private PersonBussiness personBussiness;

    public PersonManager(Context context){
        this.personBussiness = new PersonBussiness(context);
    }

    public void create(final String name, final String email, final String password, final OperationListener listener){
        AsyncTask<Void, Void, OperationResult<Boolean>> task = new AsyncTask<Void, Void, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return personBussiness.create(name, email, password);
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

    public void login(final String email, final String password, final OperationListener listener) {

        AsyncTask<Void, Void, OperationResult<Boolean>> task = new AsyncTask<Void, Void, OperationResult<Boolean>>() {
            @Override
            protected OperationResult<Boolean> doInBackground(Void... voids) {
                return personBussiness.login(email, password);
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
}
