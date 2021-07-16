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
import { PropertiesWebSocketContainer } from 'properties/PropertiesWebSocketContainer';
import React from 'react';
import { RightSiteProps } from './RightSite.types';

const useSiteStyles = makeStyles((theme) => ({
  site: {
    display: 'grid',
    gridTemplateColumns: 'minmax(0,1fr)',
    gridTemplateRows: 'minmax(0,1fr)',
  },
  accordionDetailsRoot: {
    display: 'block',
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

export const RightSite = ({ editingContextId, selection, readOnly }: RightSiteProps) => {
  const classes = useSiteStyles();

  return (
    <div className={classes.site}>
      <Accordion square expanded={true} TransitionComponent={customCollapse as any}>
        <AccordionSummary>Details</AccordionSummary>
        <AccordionDetails className={classes.accordionDetailsRoot}>
          <PropertiesWebSocketContainer editingContextId={editingContextId} selection={selection} readOnly={readOnly} />
        </AccordionDetails>
      </Accordion>
    </div>
  );
};
