package com.devmasterteam.tasks.bussiness;

import android.content.Context;

import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.constants.TaskConstants.TASK_FILTER;
import com.devmasterteam.tasks.entities.ApiResponse;
import com.devmasterteam.tasks.entities.FullParameters;
import com.devmasterteam.tasks.entities.TaskEntity;
import com.devmasterteam.tasks.infra.UrlBuilder;
import com.devmasterteam.tasks.infra.operation.OperationResult;
import com.devmasterteam.tasks.repository.api.ExternalRepository;
import com.devmasterteam.tasks.utils.FormatUrlParameters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;

public class TaskBusiness extends BaseBusiness {

    private ExternalRepository externalRepository;


    public TaskBusiness(Context context) {
        super(context);
        this.externalRepository = new ExternalRepository(context);
    }

    public OperationResult<Boolean> insert(TaskEntity entity) {

        OperationResult<Boolean> result = new OperationResult<>();
        try {
            UrlBuilder urlBuilder = new UrlBuilder(TaskConstants.ENDPOINT.ROOT);
            urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_INSERT);

            AbstractMap<String, String> headerParams = super.getHeaderParams();

            AbstractMap<String, String> params = new HashMap<>();
            params.put(TaskConstants.API_PARAMETER.PRIORITYID, String.valueOf(entity.PriorityId));
            params.put(TaskConstants.API_PARAMETER.DUEDATE, FormatUrlParameters.formatDate(entity.DueDate));
            params.put(TaskConstants.API_PARAMETER.DESCRIPTION, entity.Description);
            params.put(TaskConstants.API_PARAMETER.COMPLETE, FormatUrlParameters.formatBoolean(entity.Complete));

            FullParameters full = new FullParameters(TaskConstants.OPERATION_METHOD.POST, urlBuilder.getUrl(), headerParams, params);
            ApiResponse response = this.externalRepository.execute(full);

            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                result.setResult(new Gson().fromJson(response.getJson(), Boolean.class));
            } else {
                result.setError(super.handleStatusCode(response.getStatusCode()), super.handleErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            result.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return result;
    }

    public OperationResult<List<TaskEntity>> getList(int filter) {

        OperationResult<List<TaskEntity>> result = new OperationResult<>();
        try {
            UrlBuilder urlBuilder = new UrlBuilder(TaskConstants.ENDPOINT.ROOT);

            if (filter == TASK_FILTER.VALUES.NO_FILTER.ordinal()) {
                urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_GET_LIST_NO_FILTER);
            } else if (filter == TASK_FILTER.VALUES.OVERDUE.ordinal()) {
                urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_GET_LIST_OVERDUE);
            } else {
                urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_GET_LIST_NEXT_7_DAYS);
            }

            AbstractMap<String, String> headerParams = super.getHeaderParams();

            FullParameters full = new FullParameters(TaskConstants.OPERATION_METHOD.GET, urlBuilder.getUrl(), headerParams, null);
            ApiResponse response = this.externalRepository.execute(full);

            if (response.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {

                Type collectionType = new TypeToken<List<TaskEntity>>() {
                }.getType();
                List<TaskEntity> priorities = new Gson().fromJson(response.getJson(), collectionType);

                result.setResult(priorities);

            } else {
                result.setError(super.handleStatusCode(response.getStatusCode()), super.handleErrorMessage(response.getJson()));
            }
        } catch (Exception e) {
            result.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return result;
    }

    public OperationResult<TaskEntity> get(int taskId) {

        // Retorno
        OperationResult<TaskEntity> operationResult = new OperationResult<>();

        try {

            // Monta query
            UrlBuilder urlBuilder = new UrlBuilder(TaskConstants.ENDPOINT.ROOT);
            urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_GET_SPECIFIC);
            urlBuilder.addResource(String.valueOf(taskId));

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParams();

            // Cria objeto de requisição com parâmetros
            FullParameters fullParameters = new FullParameters(TaskConstants.OPERATION_METHOD.GET, urlBuilder.getUrl(), headerParameters, (HashMap) null);

            // Executa
            ApiResponse httpResponse = this.externalRepository.execute(fullParameters);

            // Sucesso
            if (httpResponse.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {

                // Faz a conversão do json
                TaskEntity taskEntity = new Gson().fromJson(httpResponse.getJson(), TaskEntity.class);

                // Sucesso
                operationResult.setResult(taskEntity);

            } else {
                operationResult.setError(super.handleStatusCode(httpResponse.getStatusCode()), super.handleErrorMessage(httpResponse.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    public OperationResult<Boolean> delete(int taskId) {

        // Retorno
        OperationResult<Boolean> operationResult = new OperationResult<>();

        try {

            // Monta query
            UrlBuilder urlBuilder = new UrlBuilder(TaskConstants.ENDPOINT.ROOT);
            urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_DELETE);

            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.ID, String.valueOf(taskId));

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParams();

            // Cria objeto de requisição com parâmetros
            FullParameters fullParameters = new FullParameters(TaskConstants.OPERATION_METHOD.DELETE, urlBuilder.getUrl(), (HashMap) headerParameters, (HashMap) parameters);

            // Executa
            ApiResponse httpResponse = this.externalRepository.execute(fullParameters);

            // Sucesso
            if (httpResponse.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                operationResult.setResult(new Gson().fromJson(httpResponse.getJson(), Boolean.class));
            } else {
                operationResult.setError(super.handleStatusCode(httpResponse.getStatusCode()), super.handleErrorMessage(httpResponse.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    public OperationResult<Boolean> update(TaskEntity taskEntity) {

        // Retorno
        OperationResult<Boolean> operationResult = new OperationResult<>();

        try {

            // Monta query
            UrlBuilder urlBuilder = new UrlBuilder(TaskConstants.ENDPOINT.ROOT);
            urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_UPDATE);

            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.ID, String.valueOf(taskEntity.Id));
            parameters.put(TaskConstants.API_PARAMETER.DESCRIPTION, taskEntity.Description);
            parameters.put(TaskConstants.API_PARAMETER.PRIORITYID, String.valueOf(taskEntity.PriorityId));
            parameters.put(TaskConstants.API_PARAMETER.DUEDATE, FormatUrlParameters.formatDate(taskEntity.DueDate));
            parameters.put(TaskConstants.API_PARAMETER.COMPLETE, FormatUrlParameters.formatBoolean(taskEntity.Complete));

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParams();

            // Cria objeto de requisição com parâmetros
            FullParameters fullParameters = new FullParameters(TaskConstants.OPERATION_METHOD.PUT, urlBuilder.getUrl(), (HashMap) headerParameters, (HashMap) parameters);

            // Executa
            ApiResponse httpResponse = this.externalRepository.execute(fullParameters);

            // Sucesso
            if (httpResponse.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                operationResult.setResult(new Gson().fromJson(httpResponse.getJson(), Boolean.class));
            } else {
                operationResult.setError(super.handleStatusCode(httpResponse.getStatusCode()), super.handleErrorMessage(httpResponse.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }

    public OperationResult<Boolean> complete(int id, Boolean complete) {

        // Retorno
        OperationResult<Boolean> operationResult = new OperationResult<>();

        try {

            // Monta query
            UrlBuilder urlBuilder = new UrlBuilder(TaskConstants.ENDPOINT.ROOT);

            if (complete) {
                urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_COMPLETE);
            } else {
                urlBuilder.addResource(TaskConstants.ENDPOINT.TASK_UNCOMPLETE);
            }

            AbstractMap<String, String> parameters = new HashMap<>();
            parameters.put(TaskConstants.API_PARAMETER.ID, String.valueOf(id));

            // Adiciona cabeçalho
            AbstractMap<String, String> headerParameters = super.getHeaderParams();

            // Cria objeto de requisição com parâmetros
            FullParameters fullParameters = new FullParameters(TaskConstants.OPERATION_METHOD.PUT, urlBuilder.getUrl(), (HashMap) headerParameters, (HashMap) parameters);

            // Executa
            ApiResponse httpResponse = this.externalRepository.execute(fullParameters);

            // Sucesso
            if (httpResponse.getStatusCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                operationResult.setResult(new Gson().fromJson(httpResponse.getJson(), Boolean.class));
            } else {
                operationResult.setError(super.handleStatusCode(httpResponse.getStatusCode()), super.handleErrorMessage(httpResponse.getJson()));
            }
        } catch (Exception e) {
            operationResult.setError(super.handleExceptionCode(e), super.handleExceptionMessage(e));
        }

        return operationResult;
    }
}
