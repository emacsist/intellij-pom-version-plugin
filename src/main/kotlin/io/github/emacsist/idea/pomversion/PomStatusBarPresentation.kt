package io.github.emacsist.idea.pomversion

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.wm.StatusBarWidget.MultipleTextValuesPresentation
import com.intellij.util.Consumer
import java.awt.event.MouseEvent


class PomStatusBarPresentation(private val project: Project) : MultipleTextValuesPresentation {

    private val LOG = Logger.getInstance(PomStatusBarPresentation::class.java)

    private var currentVersion: String = "未知";
    override fun getPopupStep(): ListPopup? {
        return null
    }

    override fun getSelectedValue(): String {
        val version = PomStatusBarUtil.getVersionValue(project) ?: return currentVersion
        currentVersion = version;
        LOG.info("change version: $version")
        return currentVersion
    }

    override fun getTooltipText(): String {
        return currentVersion;
    }

    override fun getClickConsumer(): Consumer<MouseEvent>? {
        return null
    }
}
