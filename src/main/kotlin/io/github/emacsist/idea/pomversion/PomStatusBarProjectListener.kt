package io.github.emacsist.idea.pomversion

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import java.util.concurrent.atomic.AtomicBoolean

class PomStatusBarProjectListener : ProjectManagerListener {
    override fun projectOpened(project: Project) {
        PomStatusBarUtil.updatePomVersionStatusBar();
    }
}
