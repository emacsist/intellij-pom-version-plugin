package io.github.emacsist.idea.pomversion

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.vfs.VirtualFileEvent
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.WindowManager
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author emacsist
 */
class PomStatusBarListener(private var isAvailable: AtomicBoolean) : FileEditorManagerListener, BulkFileListener,
    ProjectManagerListener {

    override fun projectOpened(project: Project) {
        isAvailable.set(PomStatusBarUtil.getVersionValue(project) != null);
    }

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            val file = event.file;
            if (file != null) {
                if (file.name == "pom.xml") {
                    updatePomVersionStatusBar();
                    return
                }
            }
        }
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        updatePomVersionStatusBar();
    }

    private fun updatePomVersionStatusBar() {
        for (project in ProjectManager.getInstance().openProjects) {
            val statusBar = WindowManager.getInstance().getStatusBar(project)
            statusBar?.updateWidget(PomStatusBarConstant.ID)
        }
    }
}
