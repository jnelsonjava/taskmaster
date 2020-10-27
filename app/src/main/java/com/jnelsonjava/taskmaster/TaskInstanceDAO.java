package com.jnelsonjava.taskmaster;


import androidx.room.Insert;
import androidx.room.Query;

import com.amplifyframework.datastore.generated.model.TaskInstance;

import java.util.List;


public interface TaskInstanceDAO {

    @Insert
    public void saveTask(TaskInstance task);

    @Query("SELECT * FROM task")
    List<TaskInstance> getTasks();

    @Query("SELECT * FROM task ORDER BY id DESC")
    List<TaskInstance> getTasksSortByRecent();
}
