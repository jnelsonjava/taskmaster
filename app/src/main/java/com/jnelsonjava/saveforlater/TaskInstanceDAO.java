//package com.jnelsonjava.saveforlater;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import com.amplifyframework.datastore.generated.model.TaskInstance;
//
//import java.util.List;
//
//@Dao
//public interface TaskInstanceDAO {
//
//    @Insert
//    public void saveTask(TaskInstance taskInstance);
//
//    @Query("SELECT * FROM TaskInstance")
//    List<TaskInstance> getTasks();
//
//    @Query("SELECT * FROM TaskInstance ORDER BY id DESC")
//    List<TaskInstance> getTasksSortByRecent();
//}
