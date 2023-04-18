package io.github.emacsist.idea.pomversion

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.wm.StatusBarWidget.MultipleTextValuesPresentation
import com.intellij.util.Consumer
import java.awt.event.MouseEvent

private val LOG = logger<PomStatusBarPresentation>()

class PomStatusBarPresentation(private val project: Project) : MultipleTextValuesPresentation {

    @Volatile
    private var currentVersion: String = PomStatusBarConstant.UNKNOWN;
    override fun getPopupStep(): ListPopup? {
        return null
    }

    override fun getSelectedValue(): String {
        val version = PomStatusBarUtil.getVersionValue(project)
        if (version != null) {
            currentVersion = version;
        }
        return currentVersion
    }

    override fun getTooltipText(): String {
        return currentVersion;
    }

    override fun getClickConsumer(): Consumer<MouseEvent>? {
        return null
    }
}
