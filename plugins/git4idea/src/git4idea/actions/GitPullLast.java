// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package git4idea.actions;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.commands.GitLineHandler;
import git4idea.i18n.GitBundle;
import git4idea.merge.GitPullDialog;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class GitPullLast extends GitMergeAction {

  @Override
  @NotNull
  protected String getActionName() {
    return GitBundle.getString("pullLast.action.name");
  }

  @Override
  protected DialogState displayDialog(@NotNull Project project, @NotNull List<VirtualFile> gitRoots,
                                                                        @NotNull VirtualFile defaultRoot) {
    final GitPullDialog dialog = new GitPullDialog(project, gitRoots, defaultRoot);
    if (dialog.initMarkBranchs(PropertiesComponent.getInstance(project))) {
      if (!dialog.showAndGet()) {
        return null;
      }
    }
    GitRepositoryManager repositoryManager = GitUtil.getRepositoryManager(project);
    GitRepository repository = repositoryManager.getRepositoryForRoot(dialog.gitRoot());
    assert repository != null : "Repository can't be null for root " + dialog.gitRoot();
    String remoteOrUrl = dialog.getRemote();
    if (remoteOrUrl == null) {
      return null;
    }

    GitRemote remote = GitUtil.findRemoteByName(repository, remoteOrUrl);
    final List<String> urls = remote == null ? Collections.singletonList(remoteOrUrl) : remote.getUrls();
    Computable<GitLineHandler> handlerProvider = () -> dialog.makeHandler(urls);
    return new DialogState(dialog.gitRoot(), GitBundle.message("pulling.title", dialog.getRemote()), handlerProvider);
  }

}
