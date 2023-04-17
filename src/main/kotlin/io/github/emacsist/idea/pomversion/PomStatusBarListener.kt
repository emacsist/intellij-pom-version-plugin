package io.github.emacsist.idea.pomversion

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.WindowManager

/**
 * @author emacsist
 */
class PomStatusBarListener() : FileEditorManagerListener, BulkFileListener,
    ProjectManagerListener {

    override fun projectOpened(project: Project) {
        WindowManager.getInstance().getStatusBar(project)?.updateWidget(PomStatusBarConstant.ID)
    }

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            if (event.file?.name == "pom.xml") {
                PomStatusBarUtil.updatePomVersionStatusBar();
                return
            }
        }
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        PomStatusBarUtil.updatePomVersionStatusBar();
    }

}
