package io.github.emacsist.idea.pomversion

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import java.util.concurrent.atomic.AtomicBoolean


class PomStatusBarFactory : StatusBarWidgetFactory {
    @Volatile
    var isAvailable: AtomicBoolean = AtomicBoolean(false);

    init {
        ApplicationManager.getApplication().messageBus.connect().subscribe(ProjectManager.TOPIC, PomStatusBarProjectListener(isAvailable));
        ApplicationManager.getApplication().messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, PomStatusBarListener());
    }

    override fun getId(): @NonNls String {
        return PomStatusBarConstant.ID
    }

    override fun getDisplayName(): @Nls String {
        return PomStatusBarConstant.NAME
    }

    override fun isAvailable(project: Project): Boolean {
        return isAvailable.get();
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return PomStatusBarWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {}
    override fun canBeEnabledOn(statusBar: StatusBar): Boolean {
        return true
    }
}
