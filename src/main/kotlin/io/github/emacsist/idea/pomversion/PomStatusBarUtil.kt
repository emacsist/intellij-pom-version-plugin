package io.github.emacsist.idea.pomversion

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.WindowManager
import com.intellij.psi.PsiManager
import com.intellij.psi.xml.XmlDocument
import com.intellij.psi.xml.XmlFile
import javax.swing.JComponent


object PomStatusBarUtil {
    private fun getModule(project: Project): Module {
        val selectedFiles = FileEditorManager.getInstance(project).selectedFiles
        return if (selectedFiles.isNotEmpty()) {
            val currentFile = selectedFiles[0]
            ModuleUtilCore.findModuleForFile(currentFile, project)!!
        } else {
            ModuleManager.getInstance(project).modules[0]
        }
    }

    private fun getRootModule(project: Project): Module {
        return ModuleManager.getInstance(project).modules[0]
    }

    private fun getCurrentModulePomFile(project: Project): VirtualFile? {
        return getModulePomFile(getModule(project));
    }

    private fun getModulePomFile(module: Module): VirtualFile? {
        val rootDir = ModuleRootManager.getInstance(module).contentRoots[0]
        return rootDir.findFileByRelativePath("pom.xml")
    }

    fun getVersionValue(project: Project): String? {
        var pomFile = getCurrentModulePomFile(project);
        if (pomFile == null) {
            pomFile = getModulePomFile(getRootModule(project));
        }
        if (pomFile == null) {
            return null
        }
        return getVersionValue(pomFile, project);
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
                            return versionTag.value.trimmedText;
                        }
                    }
                }
            }
        }
        return null
    }
}