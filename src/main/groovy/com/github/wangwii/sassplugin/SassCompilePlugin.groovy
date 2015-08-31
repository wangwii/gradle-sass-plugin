package com.github.wangwii.sassplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class SassCompilePlugin implements Plugin<Project> {
  void apply(Project project) {
    project.task('sass') << {
      println "Hello from the GreetingPlugin"
    }
  }
}
