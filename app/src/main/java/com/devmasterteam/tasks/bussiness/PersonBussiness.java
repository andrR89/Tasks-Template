package com.devmasterteam.tasks.bussiness;

import android.content.Context;

import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entities.ApiResponse;
import com.devmasterteam.tasks.entities.FullParameters;
import com.devmasterteam.tasks.entities.HeaderEntity;
import com.devmasterteam.tasks.infra.SecurityPreferences;
import com.devmasterteam.tasks.infra.UrlBuilder;
import com.devmasterteam.tasks.infra.operation.OperationResult;
import com.devmasterteam.tasks.repository.api.ExternalRepository;
import com.devmasterteam.tasks.utils.FormatUrlParameters;
import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.HashMap;

public class PersonBussiness extends BaseBusiness {

    private ExternalRepository externalRepository;


    public PersonBussiness(Context context) {
        super(context);
        this.externalRepository = new ExternalRepository(this.context);
    }

    public OperationResult<Boolean> create(String name, String email, String password) {
        OperationResult<Boolean> result = new OperationResult<>();
        try {
            UrlBuilder urlBuilder = new UrlBuilder(TaskConstants.ENDPOINT.ROOT);
            urlBuilder.addResource(TaskConstants.ENDPOINT.AUTHENTICATION_CREATE);

            AbstractMap<String, String> params = new HashMap<>();
            params.put(TaskConstants.API_PARAMETER.NAME, name);
            params.put(TaskConstants.API_PARAMETER.EMAIL, email);
            params.put(TaskConstants.API_PARAMETER.PASSWORD, password);
            params.put(TaskConstants.API_PARAMETER.RECEIVE_NEWS, FormatUrlParameters.formatBoolean(true));

            FullParameters full = new FullParameters(TaskConstants.OPERATION_METHOD.POST, urlBuilder.getUrl(), null, params);
            ApiResponse response = this.externalRepository.execute(full);

            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                HeaderEntity entity = new Gson().fromJson(response.getJson(), HeaderEntity.class);
                SecurityPreferences preferences = new SecurityPreferences(this.context);
                preferences.storeString(TaskConstants.HEADER.PERSON_KEY, entity.getPersonKey());
                preferences.storeString(TaskConstants.HEADER.TOKEN_KEY, entity.getToken());
                preferences.storeString(TaskConstants.USER.NAME, entity.getName());
                preferences.storeString(TaskConstants.USER.EMAIL, email);

                result.setResult(true);
            } else {
                result.setError(super.handleStatusCode(response.getStatusCode()), super.handleErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            result.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return result;
    }
}
