package com.github.wangwii.sassplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class SassCompilePlugin implements Plugin<Project> {
  public static final String TASK_GROUP = "Sass"
  public static final String SASS_COMPILE = "sassCompile"
  public static final String SASS_START_WATCH = 'startWatchSass'

  void apply(Project project) {
    project.extensions.create("sass", SassCompilePluginExtension)

    configureSassTask(project)
  }

  private void configureSassTask(Project project) {
    SassTask task = project.getTasks().create(SASS_COMPILE, SassTask.class)
    task.setDescription('Compile sass file to css.');
    task.setGroup(TASK_GROUP);
  }
}