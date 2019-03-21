package com.devmasterteam.tasks.repository.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.devmasterteam.tasks.constants.DataBaseConstants;
import com.devmasterteam.tasks.constants.TaskConstants;
import com.devmasterteam.tasks.entities.PriorityEntity;

import java.util.ArrayList;
import java.util.List;

public class PriorityRepository {

    private static PriorityRepository INSTANCE;
    private TaskDataBaseHelper taskDataBaseHelper;

    private PriorityRepository(Context ctx) {
        this.taskDataBaseHelper = new TaskDataBaseHelper(ctx);

    }

    public static PriorityRepository getInstance(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = new PriorityRepository(ctx);
        }
        return INSTANCE;
    }

    public void insert(List<PriorityEntity> list) {

        String sql = "insert into " + DataBaseConstants.PRIORITY.TABLE_NAME +
                " ( " + DataBaseConstants.PRIORITY.COLUMNS.ID + ", " +
                DataBaseConstants.PRIORITY.COLUMNS.DESCRIPTION + ") values (?,?)";

        SQLiteDatabase db = this.taskDataBaseHelper.getWritableDatabase();
        db.beginTransaction();

        SQLiteStatement statement = db.compileStatement(sql);
        for (PriorityEntity entity : list) {
            statement.bindLong(1, entity.getId());
            statement.bindString(2, entity.getDescription());

            statement.executeInsert();
            statement.clearBindings();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    public List<PriorityEntity> getList(){
        List<PriorityEntity> list = new ArrayList<>();
        final String sql = "SELECT * FROM " + DataBaseConstants.PRIORITY.TABLE_NAME;

        try{

            Cursor cursor;
            SQLiteDatabase db = this.taskDataBaseHelper.getReadableDatabase();
            cursor = db.rawQuery(sql, null);

            if(cursor != null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    PriorityEntity entity = new PriorityEntity();
                    entity.setId(cursor.getInt(cursor.getColumnIndex(DataBaseConstants.PRIORITY.COLUMNS.ID)));
                    entity.setDescription(cursor.getString(cursor.getColumnIndex(DataBaseConstants.PRIORITY.COLUMNS.DESCRIPTION)));

                    list.add(entity);
                }
            }

            if(cursor != null){
                cursor.close();
            }

        }catch (Exception e){
            return list;
        }

        return list;
    }

    public void clearData(){
        SQLiteDatabase db = this.taskDataBaseHelper.getWritableDatabase();
        db.delete(DataBaseConstants.PRIORITY.TABLE_NAME, null, null);
    }

}
