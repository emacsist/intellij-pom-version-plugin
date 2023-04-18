package io.github.emacsist.idea.pomversion

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.WindowManager
import java.util.concurrent.TimeUnit

private val LOG = logger<PomStatusBarListener>()

/**
 * @author emacsist
 */
class PomStatusBarListener : FileEditorManagerListener, ProjectManagerListener, FileDocumentManagerListener,
    BulkFileListener {

    override fun after(events: MutableList<out VFileEvent>) {
        for (event in events) {
            val file = event.file;
            if (file != null) {
                if (file.name == "pom.xml") {
                    ApplicationManager.getApplication().executeOnPooledThread {
                        TimeUnit.MILLISECONDS.sleep(100)
                        PomStatusBarUtil.updatePomVersionStatusBar()
                        LOG.info("after update pom.xml $event")
                    }
                }
            }
        }
    }

    override fun projectOpened(project: Project) {
        WindowManager.getInstance().getStatusBar(project)?.updateWidget(PomStatusBarConstant.ID)
        LOG.info("opened project $project")
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        PomStatusBarUtil.updatePomVersionStatusBar();
        LOG.info("switch editor file $event")
    }

    override fun fileContentReloaded(file: VirtualFile, document: Document) {
        if (file.name == "pom.xml") {
            LOG.info("fileContentReloaded file $file")
            PomStatusBarUtil.updatePomVersionStatusBar();
        }
    }

}
