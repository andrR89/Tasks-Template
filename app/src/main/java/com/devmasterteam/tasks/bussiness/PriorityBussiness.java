package com.devmasterteam.tasks.bussiness;

import android.content.Context;

import com.devmasterteam.tasks.constants.PriorityCacheConstants;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entities.ApiResponse;
import com.devmasterteam.tasks.entities.FullParameters;
import com.devmasterteam.tasks.entities.HeaderEntity;
import com.devmasterteam.tasks.entities.PriorityEntity;
import com.devmasterteam.tasks.infra.SecurityPreferences;
import com.devmasterteam.tasks.infra.UrlBuilder;
import com.devmasterteam.tasks.infra.operation.OperationResult;
import com.devmasterteam.tasks.repository.api.ExternalRepository;
import com.devmasterteam.tasks.repository.local.PriorityRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.List;

public class PriorityBussiness extends BaseBusiness{

    private ExternalRepository externalRepository;
    private PriorityRepository internalRepository;

    public PriorityBussiness(Context context) {
        super(context);
        this.internalRepository = PriorityRepository.getInstance(this.context);
        this.externalRepository = new ExternalRepository(this.context);
    }

    public OperationResult<Boolean> getList() {

        OperationResult<Boolean> result = new OperationResult<>();
        try {
            UrlBuilder urlBuilder = new UrlBuilder(TaskConstants.ENDPOINT.ROOT);
            urlBuilder.addResource(TaskConstants.ENDPOINT.PRIORITY_GET);

            AbstractMap<String, String> headerParams = super.getHeaderParams();

            FullParameters full = new FullParameters(TaskConstants.OPERATION_METHOD.GET, urlBuilder.getUrl(), headerParams, null);
            ApiResponse response = this.externalRepository.execute(full);

            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {

                // Pega do resource
                Type collectionType = new TypeToken<List<PriorityEntity>>(){}.getType();
                List<PriorityEntity> priorities = new Gson().fromJson(response.getJson(), collectionType);

                // Salva no banco
                this.internalRepository.clearData();
                this.internalRepository.insert(priorities);

                //Cacheia
                PriorityCacheConstants.setValues(priorities);

                result.setResult(true);
            } else {
                result.setError(super.handleStatusCode(response.getStatusCode()), super.handleErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            result.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return result;
    }

    public List<PriorityEntity> getListLocal(){
        return this.internalRepository.getList();
    }

}
