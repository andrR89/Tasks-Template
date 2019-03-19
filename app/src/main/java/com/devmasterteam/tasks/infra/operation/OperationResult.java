package com.devmasterteam.tasks.infra.operation;

public class OperationResult<T> {

    public static final int NO_ERROR = -1;

    private int error = NO_ERROR;
    private String errorMessage = "";
    private T annonimousType;

    public int getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setError(int error, String message){
        this.error = error;
        this.errorMessage = message;

    }

    public T getResult() {
        return annonimousType;
    }

    public void setResult(T annonimousType) {
        this.annonimousType = annonimousType;
    }
}
