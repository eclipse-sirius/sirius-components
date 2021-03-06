/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
import { ContextMenu, Entry, LEFT_START, Separator } from 'core/contextmenu/ContextMenu';
import { Delete, Edit } from 'icons';
import React from 'react';
import { TreeItemDiagramContextMenuProps } from './TreeItemDiagramContextMenu.types';

export const TreeItemDiagramContextMenu = ({
  x,
  y,
  onDeleteRepresentation,
  onRenameRepresentation,
  onClose,
  readOnly,
}: TreeItemDiagramContextMenuProps) => {
  return (
    <ContextMenu x={x} y={y} caretPosition={LEFT_START} onClose={onClose} data-testid="treeitemdiagram-contextmenu">
      <Entry
        icon={<Edit title="" />}
        label="Rename"
        onClick={onRenameRepresentation}
        data-testid="rename-representation"
        disabled={readOnly}
      />
      <Separator />
      <Entry
        icon={<Delete title="" />}
        label="Delete"
        onClick={onDeleteRepresentation}
        data-testid="delete-representation"
        disabled={readOnly}
      />
    </ContextMenu>
  );
};
