package io.github.emacsist.idea.pomversion

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget.WidgetPresentation
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import org.jetbrains.annotations.NonNls

class PomStatusBarWidget(project: Project) : EditorBasedWidget(project) {
    override fun ID(): @NonNls String {
        return PomStatusBarConstant.ID
    }

    override fun getPresentation(): WidgetPresentation? {
        return PomStatusBarPresentation(project)
    }
}
