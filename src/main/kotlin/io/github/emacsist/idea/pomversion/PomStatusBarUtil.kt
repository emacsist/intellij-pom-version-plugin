package io.github.emacsist.idea.pomversion

import com.intellij.ide.DataManager
import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.WindowManager
import com.intellij.psi.PsiManager
import com.intellij.psi.xml.XmlDocument
import com.intellij.psi.xml.XmlFile
import java.util.concurrent.atomic.AtomicBoolean


private val LOG = logger<PomStatusBarUtil>()

object PomStatusBarUtil {

    @Volatile
    var update: AtomicBoolean = AtomicBoolean(false)

    fun updatePomVersionStatusBar() {
        val project: Project? = ProjectUtil.getActiveProject();
        var statusBar: StatusBar? = null;
        if (project != null) {
            statusBar = WindowManager.getInstance().getStatusBar(project)
        } else {
            val dataContext = DataManager.getInstance().dataContextFromFocusAsync
            dataContext.onSuccess {
                val focusProject = it.getData(PlatformDataKeys.PROJECT)
                if (focusProject != null) {
                    statusBar = WindowManager.getInstance().getStatusBar(focusProject)
                }
            }
        }
        val widget = statusBar?.getWidget(PomStatusBarConstant.ID)
        if (widget != null) {
            statusBar!!.updateWidget(PomStatusBarConstant.ID)
            LOG.info("update widget by project $project")
        } else {
            LOG.info("widget is null,  project $project")
        }
        update.set(false)
    }

    private fun getModule(project: Project): Module? {
        val selectedFiles = FileEditorManager.getInstance(project).selectedFiles
        return if (selectedFiles.isNotEmpty()) {
            val currentFile = selectedFiles[0]
            ModuleUtilCore.findModuleForFile(currentFile, project)
        } else {
            getRootModule(project)
        }
    }

    private fun getRootModule(project: Project): Module? {
        if (ModuleManager.getInstance(project).modules.isEmpty()) {
            return null;
        }
        return ModuleManager.getInstance(project).modules[0]
    }

    private fun getCurrentModulePomFile(project: Project): VirtualFile? {
        return getModulePomFile(getModule(project));
    }

    private fun getModulePomFile(module: Module?): VirtualFile? {
        if (module == null) {
            return null
        }
        if (ModuleRootManager.getInstance(module).contentRoots.isEmpty()) {
            return null
        }
        val rootDir = ModuleRootManager.getInstance(module).contentRoots[0];
        return rootDir.findFileByRelativePath("pom.xml")
    }

    fun getVersionValue(project: Project): String? {
        var pomFile = getCurrentModulePomFile(project);
        if (pomFile == null) {
            pomFile = getModulePomFile(getRootModule(project));
        }
        if (pomFile == null) {
            LOG.info("get version from $project is null")
            return null
        }
        return getVersionValue(pomFile, project);
    }

    fun isMaven(project: Project): Boolean {
        return getModulePomFile(getRootModule(project)) != null
    }

    private fun getVersionValue(pom: VirtualFile, project: Project): String? {
        val psiManager = PsiManager.getInstance(project)
        val psiFile = psiManager.findFile(pom);
        if (psiFile is XmlFile) {
            val pomFile = psiFile as XmlFile?
            if (pomFile != null) {
                val document: XmlDocument? = pomFile.document
                if (document != null) {
                    val rootTag = document.rootTag
                    if (rootTag != null) {
                        val versionTag = rootTag.findFirstSubTag("version")
                        if (versionTag != null) {
                            val ver = versionTag.value.trimmedText
                            LOG.info("found version tag $ver from $pom, $project")
                            return ver;
                        } else {
                            //get parent version
                            val parentTag = rootTag.findFirstSubTag("parent")
                            if (parentTag != null) {
                                val parentVersion = parentTag.findFirstSubTag("version");
                                if (parentVersion != null) {
                                    val ver = parentVersion.value.trimmedText
                                    LOG.info("found parent version tag $ver from $pom, $project")
                                    return ver
                                }
                            }
                        }
                    }
                }
            }
        }
        LOG.warn("get version value finally null: $pom, $project")
        return null
    }
}
