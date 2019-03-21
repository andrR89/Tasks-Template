package com.devmasterteam.tasks.listener;

public interface TaskListInterationListener {

    void onListClick(int id);

    void onCompleteClick(int id);

    void onUncompleteClick(int id);

    void onDeleteClick(int id);

}
