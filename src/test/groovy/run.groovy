import com.github.wangwii.sassplugin.internal.DirectoryWatcher

DirectoryWatcher watch = new DirectoryWatcher();
File file = new File("/Users/weiwang/MyWorkspaces/gradle-sass-plugin/build");
println file.exists()
println file.absolutePath
watch.addWatchDirectory(file);
watch.start();