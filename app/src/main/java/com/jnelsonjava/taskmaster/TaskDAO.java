package com.jnelsonjava.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert
    public void saveTask(Task task);

    @Query("SELECT * FROM task")
    List<Task> getTasks();

    @Query("SELECT * FROM task ORDER BY id DESC")
    List<Task> getTasksSortByRecent();
}
