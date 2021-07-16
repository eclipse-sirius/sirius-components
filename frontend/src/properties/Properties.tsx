/*******************************************************************************
 * Copyright (c) 2019, 2021 Obeo.
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
import Avatar from '@material-ui/core/Avatar';
import { makeStyles } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Tooltip from '@material-ui/core/Tooltip';
import Typography from '@material-ui/core/Typography';
import { Page } from 'properties/Page';
import { PageList } from 'properties/pagelist/PageList';
import { FormProps } from 'properties/Properties.types';
import React from 'react';

const usePropertiesStyles = makeStyles((theme) => ({
  properties: {
    display: 'flex',
    flexDirection: 'column',
    paddingLeft: theme.spacing(1),
    paddingRight: theme.spacing(1),
  },
  content: {
    overflowX: 'hidden',
    overflowY: 'auto',
  },
  subscribers: {
    marginLeft: 'auto',
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    '& > *': {
      marginLeft: theme.spacing(0.5),
      marginRight: theme.spacing(0.5),
    },
  },
  avatar: {
    fontSize: '1rem',
    width: theme.spacing(3),
    height: theme.spacing(3),
  },
  container: {
    display: 'grid',
    gridTemplateRows: '1fr',
    gridTemplateColumns: 'min-content 1fr',
  },
}));

export const Properties = ({ editingContextId, form, subscribers, widgetSubscriptions, readOnly }: FormProps) => {
  const classes = usePropertiesStyles();
  const { id, label, pages } = form;

  let content;
  if (pages.length > 1) {
    content = (
      <div className={classes.container}>
        <PageList pages={pages} />
        <Page
          editingContextId={editingContextId}
          formId={id}
          page={pages[0]}
          widgetSubscriptions={widgetSubscriptions}
          readOnly={readOnly}
        />
      </div>
    );
  } else if (pages.length === 1) {
    content = (
      <Page
        editingContextId={editingContextId}
        formId={id}
        page={pages[0]}
        widgetSubscriptions={widgetSubscriptions}
        readOnly={readOnly}
      />
    );
  }
  return (
    <div data-testid="properties" className={classes.properties}>
      <Toolbar variant="dense" disableGutters>
        <Typography variant="h6" noWrap>
          {label}
        </Typography>
        <div className={classes.subscribers}>
          {subscribers.map((subscriber) => (
            <Tooltip title={subscriber.username} arrow key={subscriber.username}>
              <Avatar classes={{ root: classes.avatar }}>{subscriber.username.substring(0, 1).toUpperCase()}</Avatar>
            </Tooltip>
          ))}
        </div>
      </Toolbar>
      <div className={classes.content}>{content}</div>
    </div>
  );
};
