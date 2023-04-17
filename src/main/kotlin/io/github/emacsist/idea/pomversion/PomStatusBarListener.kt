package io.github.emacsist.idea.pomversion

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager

/**
 * @author emacsist
 */
class PomStatusBarListener : FileEditorManagerListener {

    override fun selectionChanged(event: FileEditorManagerEvent) {
        for (project in ProjectManager.getInstance().openProjects) {
            val statusBar = WindowManager.getInstance().getStatusBar(project)
            statusBar?.updateWidget(PomStatusBarConstant.ID)
        }
    }
}
