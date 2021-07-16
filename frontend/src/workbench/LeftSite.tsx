/*******************************************************************************
 * Copyright (c) 2021 Obeo.
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

import MuiAccordion from '@material-ui/core/Accordion';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import MuiAccordionSummary from '@material-ui/core/AccordionSummary';
import MuiCollapse from '@material-ui/core/Collapse';
import { makeStyles, withStyles } from '@material-ui/core/styles';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import { ExplorerWebSocketContainer } from 'explorer/ExplorerWebSocketContainer';
import React from 'react';
import { ValidationWebSocketContainer } from 'validation/ValidationWebSocketContainer';
import { LeftSiteProps } from './LeftSite.types';

const useSiteStyles = makeStyles((theme) => ({
  site: {
    display: 'grid',
    gridTemplateColumns: 'minmax(0,1fr)',
  },
}));

const Accordion = withStyles({
  root: {
    display: 'grid',
    gridTemplateColumns: 'minmax(0,1fr)',
    gridTemplateRows: 'min-content minmax(0,1fr)',
    border: '1px solid rgba(0, 0, 0, .125)',
    boxShadow: 'none',
    '&:not(:last-child)': {
      borderBottom: '0px',
    },
    '&:before': {
      display: 'none',
    },
    '&$expanded': {
      margin: '0px',
    },
  },
  expanded: {},
})(MuiAccordion);

const AccordionSummary = withStyles({
  root: {
    borderBottom: '1px solid rgba(0, 0, 0, .125)',
    marginBottom: -1,
    '&$expanded': {
      minHeight: 48,
    },
  },
  content: {
    '&$expanded': {
      margin: '0px',
    },
  },
  expanded: {},
})(MuiAccordionSummary);

const customCollapse = withStyles({
  entered: {
    overflow: 'auto',
  },
})(MuiCollapse);

export const LeftSite = ({ editingContextId, setSelection, selection, readOnly }: LeftSiteProps) => {
  const classes = useSiteStyles();

  const [explorerExpanded, setExplorerExpanded] = React.useState<boolean>(true);
  const [validationExpanded, setValidationExpanded] = React.useState<boolean>(false);

  const handleChange =
    (setState: React.Dispatch<React.SetStateAction<boolean>>) =>
    (event: React.ChangeEvent<{}>, isExpanded: boolean) => {
      setState(isExpanded);
    };

  let siteStyle: React.CSSProperties = {};
  if (explorerExpanded) {
    siteStyle.gridTemplateRows = 'minmax(0,1fr)';
  } else {
    siteStyle.gridTemplateRows = 'min-content';
  }

  if (validationExpanded) {
    siteStyle.gridTemplateRows = `${siteStyle.gridTemplateRows} minmax(0,1fr)`;
  } else {
    siteStyle.gridTemplateRows = `${siteStyle.gridTemplateRows} min-content`;
  }

  return (
    <div className={classes.site} style={siteStyle}>
      <Accordion
        square
        expanded={explorerExpanded}
        onChange={handleChange(setExplorerExpanded)}
        TransitionComponent={customCollapse as any}>
        <AccordionSummary expandIcon={<ExpandMoreIcon />}>Explorer</AccordionSummary>
        <AccordionDetails>
          <ExplorerWebSocketContainer
            editingContextId={editingContextId}
            setSelection={setSelection}
            selection={selection}
            readOnly={readOnly}
          />
        </AccordionDetails>
      </Accordion>
      <Accordion
        square
        expanded={validationExpanded}
        onChange={handleChange(setValidationExpanded)}
        TransitionComponent={customCollapse as any}>
        <AccordionSummary expandIcon={<ExpandMoreIcon />}>Validation</AccordionSummary>
        <AccordionDetails>
          <ValidationWebSocketContainer editingContextId={editingContextId} />
        </AccordionDetails>
      </Accordion>
    </div>
  );
};
