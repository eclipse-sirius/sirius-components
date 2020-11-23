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
import { ContextMenu, Entry, Separator, TOP_END } from 'core/contextmenu/ContextMenu';
import { Help, Logout } from 'icons';
import PropTypes from 'prop-types';
import React from 'react';
import styles from './UserStatusContextMenu.module.css';

const propTypes = {
  x: PropTypes.number.isRequired,
  y: PropTypes.number.isRequired,
  onLoggedOut: PropTypes.func.isRequired,
  onClose: PropTypes.func.isRequired,
};

export const UserStatusContextMenu = ({ x, y, userName, onLoggedOut, onClose }) => {
  return (
    <ContextMenu x={x} y={y} caretPosition={TOP_END} onClose={onClose} data-testid="userstatus-contextmenu">
      <div className={styles.userName} data-testid="username">
        {userName}
      </div>
      <Separator />
      <a href="https://ocpmodeler.com/doc/welcome_help_center.html" rel="noopener noreferrer" target="_blank">
        <Entry icon={<Help title="Help" />} label="Help" data-testid="help" />
      </a>
      <Entry icon={<Logout title="" />} label="Log out" onClick={onLoggedOut} data-testid="logout"></Entry>
    </ContextMenu>
  );
};
UserStatusContextMenu.propTypes = propTypes;
