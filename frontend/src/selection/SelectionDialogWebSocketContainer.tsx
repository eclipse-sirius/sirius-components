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
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import IconButton from '@material-ui/core/IconButton';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Snackbar from '@material-ui/core/Snackbar';
import { createStyles, makeStyles } from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import InboxIcon from '@material-ui/icons/Inbox';
import { useMachine } from '@xstate/react';
import React, { useState } from 'react';
import { SelectionDialogWebSocketContainerProps } from './SelectionDialogWebSocketContainer.types';
import {
  HandleSelectionUpdatedEvent,
  HideToastEvent,
  SchemaValue,
  SelectionDialogWebSocketContainerContext,
  SelectionDialogWebSocketContainerEvent,
  selectionDialogWebSocketContainerMachine,
} from './SelectionDialogWebSocketContainerMachine';

const useNewObjectModalStyles = makeStyles((theme) =>
  createStyles({
    root: {
      width: '100%',
      position: 'relative',
      overflow: 'auto',
      maxHeight: 300,
    },
  })
);

/**
 * Connect the SelectionDialog component to the GraphQL API over Web Socket.
 */
export const SelectionDialogWebSocketContainer = ({
  editingContextId,
  open,
  onClose,
  onFinish,
}: SelectionDialogWebSocketContainerProps) => {
  const classes = useNewObjectModalStyles();

  const [{ value }, dispatch] = useMachine<
    SelectionDialogWebSocketContainerContext,
    SelectionDialogWebSocketContainerEvent
  >(selectionDialogWebSocketContainerMachine);

  const { toast } = value as SchemaValue;

  const [selectedIndex, setSelectedIndex] = useState(null);

  const handleListItemClick = (index: number) => {
    setSelectedIndex(index);
    dispatch({ type: 'HANDLE_SELECTION_UPDATED', selectedObjectId: index.toString() } as HandleSelectionUpdatedEvent);
  };

  return (
    <>
      <Dialog open={open} onClose={onClose} aria-labelledby="dialog-title" maxWidth="xs" fullWidth>
        <DialogTitle id="selection-dialog-title">Selection Dialog</DialogTitle>
        <DialogContent>
          <DialogContentText>{'The message to display'}</DialogContentText>
          <List className={classes.root}>
            {[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20].map((item) => (
              <ListItem
                button
                key={`item-${item}`}
                selected={selectedIndex === item}
                onClick={() => handleListItemClick(item)}>
                <ListItemIcon>
                  <InboxIcon />
                </ListItemIcon>
                <ListItemText primary={`Item ${item}`} />
              </ListItem>
            ))}
          </List>
        </DialogContent>
        <DialogActions>
          <Button
            variant="contained"
            disabled={selectedIndex === null}
            data-testid="finish-action"
            color="primary"
            onClick={() => {
              onFinish('retrieveSelectedObjectIdWhenListItemsWillBeFilledWithRealObjects');
            }}>
            Finish
          </Button>
        </DialogActions>
      </Dialog>
      <Snackbar
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        open={toast === 'visible'}
        autoHideDuration={3000}
        onClose={() => dispatch({ type: 'HIDE_TOAST' } as HideToastEvent)}
        message={'retrieveErrorMessage'}
        action={
          <IconButton
            size="small"
            aria-label="close"
            color="inherit"
            onClick={() => dispatch({ type: 'HIDE_TOAST' } as HideToastEvent)}>
            <CloseIcon fontSize="small" />
          </IconButton>
        }
        data-testid="error"
      />
    </>
  );
};
