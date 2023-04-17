package io.github.emacsist.idea.pomversion

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls


class PomStatusBarFactory : StatusBarWidgetFactory {
    init {
        val listenerHandler = PomStatusBarListener()
        ApplicationManager.getApplication().messageBus.connect().subscribe(ProjectManager.TOPIC, listenerHandler);
        ApplicationManager.getApplication().messageBus.connect()
            .subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, listenerHandler);
        ApplicationManager.getApplication().messageBus.connect()
            .subscribe(VirtualFileManager.VFS_CHANGES, listenerHandler);
    }

    override fun getId(): @NonNls String {
        return PomStatusBarConstant.ID
    }

    override fun getDisplayName(): @Nls String {
        return PomStatusBarConstant.NAME
    }

    override fun isAvailable(project: Project): Boolean {
        return PomStatusBarUtil.getVersionValue(project) != null
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return PomStatusBarWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {}
    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        return true
    }
}
