package com.github.wangwii.sassplugin.internal

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY

class DirectoryWatcher {
  private List<Path> paths = new ArrayList<>()
  private WatchService watcher;
  private WatchQueueReader queue;

  void start() {
    watcher = FileSystems.default.newWatchService();
    paths.each { installWatch(it) }

    queue = new WatchQueueReader(watcher);
    Thread thread = new Thread(queue, "WatchService");
    thread.start();
    thread.join();
  }

  void addWatchDirectory(File dir) {
    if (dir.isDirectory() && dir.exists()) {
      Path path = Paths.get(dir.getAbsolutePath());
      if(queue != null && queue.running){
        installWatch(path);
      }
      paths.add(path);
    }
    assert "invalid directory: ${dir}";
  }

  void addWatchFile(File file) {
    if (file.exists() && file.isFile()) {
      paths.add(Paths.get(file.getAbsolutePath()))
    }
    assert "invalid file: ${file}";
  }

  void addChangeListener() {
  }

  void stop() {
    if(queue != null){
      queue.stop();
    }

    if (watcher != null) {
      watcher.close();
    }
  }

  private void installWatch(Path path){
    if(watcher == null){
      return;
    }
    path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)
  }

  private static class WatchQueueReader implements Runnable {
    private WatchService service;
    private boolean stopped = false;
    private boolean running = false;

    WatchQueueReader(WatchService service) {
      this.service = service
    }

    @Override
    void run() {
      running = true;
      while (!stopped) {
        WatchKey key = service.take();

        for (WatchEvent event : key.pollEvents()) {
          //System.out.printf("Received %s event for file: %s\n", event.kind(), event.context());
          onEvent(event);
        }
        key.reset();
      }

      System.out.println("Stopping watch service.");
    }

    void stop(){
      this.running = false;
      this.stopped = true;
    }

    private void onEvent(WatchEvent event){
      println "event class: ${event.class}: ${event}"
      println "kind: ${event.kind()}"
      println "context: ${event.context()}";
      println "count: ${event.count()}";
    }

  }
}


