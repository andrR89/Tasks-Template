package com.devmasterteam.tasks.repository.api;

import android.content.Context;

import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entities.ApiResponse;
import com.devmasterteam.tasks.entities.FullParameters;
import com.devmasterteam.tasks.infra.InternetNotAvailableException;
import com.devmasterteam.tasks.utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

public class ExternalRepository {

    private Context mContext;

    public ExternalRepository(Context mContext) {
        this.mContext = mContext;
    }

    public ApiResponse execute(FullParameters parameters) throws InternetNotAvailableException {

        if(!NetworkUtils.isConnectionAvailable(this.mContext)){
            throw new InternetNotAvailableException();
        }

        ApiResponse response;
        InputStream inputStream;
        HttpURLConnection conn;

        try {

            URL url;

            if (parameters.getMethod() == TaskConstants.OPERATION_METHOD.GET) {
                url = new URL(parameters.getUrl() + getQuery(parameters.getParameters(), parameters.getMethod()));
            } else {
                url = new URL(parameters.getUrl());
            }

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod(parameters.getMethod());
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);


            if (parameters.getHeaderParameters() != null) {
                Iterator it = parameters.getHeaderParameters().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    conn.setRequestProperty(pair.getKey().toString(), pair.getValue().toString());
                    it.remove();
                }
            }

            if(!parameters.getMethod().equals(TaskConstants.OPERATION_METHOD.GET)){
                String query = getQuery(parameters.getParameters(), parameters.getMethod());
                byte[] postDataBytes = query.getBytes("UTF-8");
                int postaDataBytesLenght = postDataBytes.length;

                conn.setRequestProperty("Content-Length", Integer.toString(postaDataBytesLenght));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);
            }

            conn.connect();

            if (conn.getResponseCode() == TaskConstants.STATUS_CODE.SUCCESS) {
                inputStream = conn.getInputStream();
                response = new ApiResponse(getStringFromInputStream(inputStream), conn.getResponseCode());
            } else {
                inputStream = conn.getErrorStream();
                response = new ApiResponse(getStringFromInputStream(inputStream), conn.getResponseCode());
            }

            inputStream.close();
            conn.disconnect();

        } catch (Exception e) {
            response = new ApiResponse("", TaskConstants.STATUS_CODE.NOT_FOUND);
        }

        return response;
    }

    private String getStringFromInputStream(InputStream inputStream) {

        if (inputStream == null) {
            return "";
        }

        BufferedReader br;
        StringBuilder builder = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
        } catch (Exception e) {
            return "";
        }

        return builder.toString();
    }

    private String getQuery(AbstractMap<String, String> parameters, String method) throws UnsupportedEncodingException {
        if (parameters == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> e : parameters.entrySet()) {
            if (first) {
                if (method == TaskConstants.OPERATION_METHOD.GET) {
                    builder.append("?");
                }
                first = false;
            } else {
                builder.append("&");
            }

            builder.append(URLEncoder.encode(e.getKey(), "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(e.getValue(), "UTF-8"));
        }

        return builder.toString();
    }
}
