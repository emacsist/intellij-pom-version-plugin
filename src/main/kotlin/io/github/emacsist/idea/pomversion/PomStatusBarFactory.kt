package io.github.emacsist.idea.pomversion

import com.intellij.AppTopics
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls

private val LOG = logger<PomStatusBarFactory>();

class PomStatusBarFactory : StatusBarWidgetFactory {
    private fun register(project: Project) {
        val listenerHandler = PomStatusBarListener()
        project.messageBus.connect().subscribe(ProjectManager.TOPIC, listenerHandler)
        project.messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, listenerHandler)
        project.messageBus.connect().subscribe(AppTopics.FILE_DOCUMENT_SYNC, listenerHandler)
        project.messageBus.connect().subscribe(VirtualFileManager.VFS_CHANGES, listenerHandler)
    }

    override fun getId(): @NonNls String {
        return PomStatusBarConstant.ID
    }

    override fun getDisplayName(): @Nls String {
        return PomStatusBarConstant.NAME
    }

    override fun isAvailable(project: Project): Boolean {
        if (PomStatusBarUtil.isMaven(project)) {
            register(project);
            LOG.info("isAvailable from $project")
            return true;
        }
        LOG.info("is not available from $project")
        return false;
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return PomStatusBarWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {}
    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        return true
    }
}
