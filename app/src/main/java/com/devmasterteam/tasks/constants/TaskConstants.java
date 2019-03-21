package com.devmasterteam.tasks.constants;

import android.content.Intent;

public class TaskConstants {


    public static class HEADER {
        public static final String TOKEN_KEY = "token";
        public static final String PERSON_KEY = "personkey";
    }

    public static class USER {
        public static final String NAME = "user";
        public static final String EMAIL = "name";
    }

    public static class ENDPOINT {
        public static final String ROOT = "http://www.devmasterteam.com/CursoAndroidAPI";

        public static final String AUTHENTICATION_CREATE = "Authentication/Create";
        public static final String AUTHENTICATION_LOGIN = "Authentication/Login";

        public static final String TASK_GET_LIST_NO_FILTER = "Task";
        public static final String TASK_GET_LIST_OVERDUE = "Task/Overdue";
        public static final String TASK_GET_LIST_NEXT_7_DAYS = "Task/Next7Days";
        public static final String TASK_GET_SPECIFIC = "Task";
        public static final String TASK_DELETE = "Task";
        public static final String TASK_UPDATE = "Task";
        public static final String TASK_INSERT = "Task";
        public static final String TASK_COMPLETE = "Task/Complete";
        public static final String TASK_UNCOMPLETE = "Task/Uncomplete";

        public static final String PRIORITY_GET = "priority";
    }

    public static class OPERATION_METHOD {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    public static class STATUS_CODE {
        public static final int SUCCESS = 200;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int INTERNET_NOT_AVAILIABLE = 901;
    }

    public static class API_PARAMETER {
        public static final String ID = "id";
        public static final String PRIORITYID = "priorityid";
        public static final String DESCRIPTION = "description";
        public static final String DUEDATE = "duedate";
        public static final String COMPLETE = "complete";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String RECEIVE_NEWS = "receivenews";
    }


    public static class TASK_FILTER {

        public static final String KEY = "taskfilterkey";

        public enum VALUES {
            NO_FILTER, OVERDUE, NEXT_7_DAYS
        }
    }

    public static class BUNDLE{
        public static final String TASK_ID = "taskID";
    }

}
