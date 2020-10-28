package com.jnelsonjava.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert
    public void saveTask(Task task);

    @Query("SELECT * FROM Task")
    List<Task> getTasks();

    @Query("SELECT * FROM Task ORDER BY id DESC")
    List<Task> getTasksSortByRecent();
}
