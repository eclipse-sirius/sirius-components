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
import { FormHelperText } from '@material-ui/core';
import FormControl from '@material-ui/core/FormControl';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import { httpOrigin } from 'common/URL';
import { ListPropertySectionProps } from 'properties/propertysections/ListPropertySection.types';
import { PropertySectionLabel } from 'properties/propertysections/PropertySectionLabel';
import React from 'react';

const useListPropertySectionStyles = makeStyles((theme) => ({
  table: {
    borderStyle: 'solid',
    borderWidth: '1px',
    borderColor: theme.palette.divider,
  },
  cell: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
  },
  icon: {
    width: '16px',
    height: '16px',
    marginRight: theme.spacing(2),
  },
}));

export const ListPropertySection = ({ widget, subscribers, hasDiagnostic }: ListPropertySectionProps) => {
  const classes = useListPropertySectionStyles();

  let items = widget.items;
  if (items.length === 0) {
    items.push({
      id: 'none',
      imageURL: '',
      label: 'None',
    });
  }

  return (
    <FormControl error={hasDiagnostic}>
      <PropertySectionLabel label={widget.label} subscribers={subscribers} />
      <Table className={classes.table}>
        <TableBody>
          {widget.items.map((item) => (
            <TableRow key={item.id}>
              <TableCell className={classes.cell}>
                {item.imageURL ? (
                  <img
                    className={classes.icon}
                    width="16"
                    height="16"
                    alt={item.label}
                    src={httpOrigin + item.imageURL}
                  />
                ) : null}
                {item.label}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <FormHelperText>{hasDiagnostic ? widget.diagnostics[0].message : null}</FormHelperText>
    </FormControl>
  );
};
