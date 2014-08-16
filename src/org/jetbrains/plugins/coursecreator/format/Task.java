package org.jetbrains.plugins.coursecreator.format;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Task {
  public String name;
  public Map<String, TaskFile> taskFiles = new HashMap<String, TaskFile>();

  public Task() {}

  public Task(@NotNull final String name) {
    this.name = name;
  }

  public void addTaskFile(@NotNull final String name) {
    taskFiles.put(name, new TaskFile());
  }

  public TaskFile getTaskFile(@NotNull final String name) {
    return taskFiles.get(name);
  }
}
