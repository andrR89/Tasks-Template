package com.devmasterteam.tasks.bussiness;

import android.content.Context;

import com.devmasterteam.tasks.R;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.infra.InternetNotAvailableException;
import com.devmasterteam.tasks.infra.SecurityPreferences;
import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.HashMap;

public class BaseBusiness {

    protected Context context;

    public BaseBusiness(Context context) {
        this.context = context;
    }

    protected int handleExceptionCode(Exception e) {
        if (e instanceof InternetNotAvailableException) {
            return TaskConstants.STATUS_CODE.INTERNET_NOT_AVAILIABLE;
        }
        return TaskConstants.STATUS_CODE.INTERNAL_SERVER_ERROR;
    }

    protected String handleExceptionMessage(Exception e) {
        if (e instanceof InternetNotAvailableException) {
            return this.context.getString(R.string.INTERNET_NOT_AVAILABLE);
        }
        return this.context.getString(R.string.UNEXPECTED_ERROR);
    }

    protected String handleErrorMessage(String json) {
        return new Gson().fromJson(json, String.class);
    }

    protected int handleStatusCode(int value) {
        if (value == TaskConstants.STATUS_CODE.FORBIDDEN) {
            return TaskConstants.STATUS_CODE.FORBIDDEN;
        } else if (value == TaskConstants.STATUS_CODE.NOT_FOUND) {
            return TaskConstants.STATUS_CODE.NOT_FOUND;
        } else {
            return TaskConstants.STATUS_CODE.INTERNAL_SERVER_ERROR;
        }
    }

    protected AbstractMap<String, String> getHeaderParams() {

        AbstractMap<String, String> params = new HashMap<>();
        SecurityPreferences preferences = new SecurityPreferences(this.context);
        params.put(TaskConstants.HEADER.PERSON_KEY, preferences.getStoredString(TaskConstants.HEADER.PERSON_KEY));
        params.put(TaskConstants.HEADER.TOKEN_KEY, preferences.getStoredString(TaskConstants.HEADER.TOKEN_KEY));
        params.put(TaskConstants.USER.NAME, preferences.getStoredString(TaskConstants.USER.NAME));
        params.put(TaskConstants.USER.EMAIL, preferences.getStoredString(TaskConstants.USER.EMAIL));

        return  params;
    }
}
