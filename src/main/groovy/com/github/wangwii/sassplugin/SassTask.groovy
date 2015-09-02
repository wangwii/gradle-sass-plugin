package com.github.wangwii.sassplugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.StopExecutionException
import com.vaadin.sass.SassCompiler

class SassTask extends DefaultTask{
  @TaskAction
  public void sass() {
    File dir = project.file(project.sass.sassDir)
    if(!dir.exists()){
      String msg = String.format("No such directory: ${file.absolutePath}")
      throw new StopExecutionException(msg)
    }

    if(!dir.isDirectory()){
      String msg = String.format("Invalid sass directory: ${file.absolutePath}")
      throw new StopExecutionException(msg)
    }

    File cssDir = project.file(project.sass.cssDir).parentFile
    if(!cssDir.exists()){
      cssDir.parentFile.mkdirs()
    }

    project.fileTree(project.sass.sassDir).include('*.scss').visit {
      File cssFile = it.relativePath.getFile(project.file(project.sass.cssDir))
      String cssFileName = cssFile.absolutePath.replaceFirst(".scss", '.css')

      String[] args = [it.file.absolutePath, cssFileName];
      SassCompiler.main(args)
    }
  }
}