package com.jnelsonjava.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.TaskInstance;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    public List<TaskInstance> tasks;
    public TaskListenable taskListener;

    public TaskAdapter(List<TaskInstance> tasks, TaskListenable taskListener) {
        this.tasks = tasks;
        this.taskListener = taskListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);

        final TaskViewHolder taskViewHolder = new TaskViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskListener.taskListener(taskViewHolder.task);
            }
        });

        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = tasks.get(position);

        TextView titleTextView = holder.itemView.findViewById(R.id.taskTitleTextView);
        TextView bodyTextView = holder.itemView.findViewById(R.id.taskBodyTextView);
        TextView stateTextView = holder.itemView.findViewById(R.id.taskStateTextView);

        titleTextView.setText(holder.task.getTitle());
        bodyTextView.setText(holder.task.getBody());
        stateTextView.setText(holder.task.getState());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TaskInstance task;
        public View itemView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public static interface TaskListenable {
        public void taskListener(TaskInstance task);
    }
}
