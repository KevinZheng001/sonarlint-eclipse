/*
 * Sonar Eclipse
 * Copyright (C) 2010-2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.ide.eclipse.internal.ui.wizards.associate;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public class TextCellEditorWithContentProposal extends TextCellEditor {

  private ContentProposalAdapter contentProposalAdapter;
  private boolean popupOpen = false; // true, if popup is currently open

  public TextCellEditorWithContentProposal(Composite parent, IContentProposalProvider contentProposalProvider,
      KeyStroke keyStroke, char[] autoActivationCharacters, ProjectAssociationModel sonarProject) {
    super(parent);

    enableContentProposal(contentProposalProvider, keyStroke, autoActivationCharacters, sonarProject);
  }

  private void enableContentProposal(IContentProposalProvider contentProposalProvider, KeyStroke keyStroke,
      char[] autoActivationCharacters, ProjectAssociationModel sonarProject) {
    contentProposalAdapter = new ContentAssistCommandAdapter(
        text,
        new RemoteSonarProjectTextContentAdapter(sonarProject),
        contentProposalProvider,
        ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS,
        null,
        true);
    contentProposalAdapter.setAutoActivationCharacters(null);

    // Listen for popup open/close events to be able to handle focus events correctly
    contentProposalAdapter.addContentProposalListener(new IContentProposalListener2() {

      public void proposalPopupClosed(ContentProposalAdapter adapter) {
        popupOpen = false;
      }

      public void proposalPopupOpened(ContentProposalAdapter adapter) {
        popupOpen = true;
      }
    });
  }

  // /**
  // * Return the {@link ContentProposalAdapter} of this cell editor.
  // *
  // * @return the {@link ContentProposalAdapter}
  // */
  // public ContentProposalAdapter getContentProposalAdapter() {
  // return contentProposalAdapter;
  // }
  //
  // protected void focusLost() {
  // if (!popupOpen) {
  // // Focus lost deactivates the cell editor.
  // // This must not happen if focus lost was caused by activating
  // // the completion proposal popup.
  // super.focusLost();
  // }
  // }
  //
  // protected boolean dependsOnExternalFocusListener() {
  // // Always return false;
  // // Otherwise, the ColumnViewerEditor will install an additional focus listener
  // // that cancels cell editing on focus lost, even if focus gets lost due to
  // // activation of the completion proposal popup. See also bug 58777.
  // return false;
  // }
}