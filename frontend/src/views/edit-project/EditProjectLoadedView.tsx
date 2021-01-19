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
import { forbidExtraProps } from 'airbnb-prop-types';
import { HORIZONTAL, Panels, SECOND_PANEL } from 'core/panels/Panels';
import { ExplorerWebSocketContainer } from 'explorer/ExplorerWebSocketContainer';
import { EditProjectNavbar } from 'navbar/EditProjectNavbar/EditProjectNavbar';
import PropTypes from 'prop-types';
import { PropertiesWebSocketContainer } from 'properties/PropertiesWebSocketContainer';
import React from 'react';
import { RepresentationArea } from 'views/edit-project/RepresentationArea';
import styles from './EditProjectLoadedView.module.css';

const selectionPropType = PropTypes.shape(
  forbidExtraProps({
    id: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    kind: PropTypes.string.isRequired,
  })
);
const propTypes = {
  projectId: PropTypes.string.isRequired,
  subscribers: PropTypes.array.isRequired,
  representations: PropTypes.array.isRequired,
  readOnly: PropTypes.bool.isRequired,
  selection: selectionPropType,
  displayedRepresentation: PropTypes.object,
  setSelection: PropTypes.func.isRequired,
  setSubscribers: PropTypes.func.isRequired,
};
export const EditProjectLoadedView = ({
  projectId,
  subscribers,
  representations,
  readOnly,
  selection,
  displayedRepresentation,
  setSelection,
  setSubscribers,
}) => {
  const explorer = (
    <ExplorerWebSocketContainer editingContextId={projectId} selection={selection} setSelection={setSelection} />
  );

  let representation = (
    <RepresentationArea
      projectId={projectId}
      representations={representations}
      readOnly={readOnly}
      selection={selection}
      displayedRepresentation={displayedRepresentation}
      setSelection={setSelection}
      setSubscribers={setSubscribers}
    />
  );
  let objectId = undefined;
  if (
    selection &&
    !(
      selection.kind === 'Unknown' ||
      selection.kind === 'Diagram' ||
      selection.kind === 'Form' ||
      selection.kind === 'Document'
    )
  ) {
    objectId = selection.id;
  }
  const properties = <PropertiesWebSocketContainer projectId={projectId} objectId={objectId} />;

  const representationAndPropertiesPanel = (
    <div className={styles.main} data-testid="representationAndProperties">
      <Panels
        orientation={HORIZONTAL}
        resizablePanel={SECOND_PANEL}
        firstPanel={representation}
        secondPanel={properties}
        initialResizablePanelSize={300}
      />
    </div>
  );
  return (
    <div className={styles.editProjectView}>
      <EditProjectNavbar subscribers={subscribers} />
      <Panels
        orientation={HORIZONTAL}
        firstPanel={explorer}
        secondPanel={representationAndPropertiesPanel}
        initialResizablePanelSize={300}
      />
    </div>
  );
};
EditProjectLoadedView.propTypes = propTypes;
