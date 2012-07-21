/*
 * Copyright (c) 2012 Steve Roberts / Essar Software. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package uk.co.essarsoftware.par.gui.panels;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: robsteve
 * Date: 14/06/12
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
class LogTable extends JTable
{
    private int ctF, ctE, ctW, ctI, ctD;

    private LogPanel.LogTableTabPanel parent;
    private final LogTableModel model;
    
    public LogTable(LogPanel.LogTableTabPanel parent, Logger log) {
        super();
        this.parent = parent;

        // Set up data model
        model = new LogTableModel();
        setModel(model);
        setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);
        setDefaultRenderer(String.class, new LogTableCellRenderer());

        // Set up column widths
        TableColumnModel tcm = getColumnModel();
        for(int i = 0; i < tcm.getColumnCount(); i ++) {
            if(i == 2) {
                tcm.getColumn(i).setPreferredWidth(400);
            } else if(i == 1) {
                tcm.getColumn(i).setMinWidth(75);
                tcm.getColumn(i).setPreferredWidth(50);
            } else {
                tcm.getColumn(i).setMinWidth(150);
                tcm.getColumn(i).setPreferredWidth(150);
            }
        }
        
        log.addAppender(new LogPanelAppender());
    }

    private class LogPanelAppender extends AppenderSkeleton
    {
        @Override
        protected void append(LoggingEvent loggingEvent) {
            LogEntry le = new LogEntry();
            Date ts = new Date(loggingEvent.getTimeStamp());
            
            le.level = loggingEvent.getLevel().toString();
            le.timestamp = String.format("%td/%tm/%ty %tk:%tM:%tS.%tL", ts, ts, ts, ts, ts, ts, ts);
            le.message = String.format("[%s] %s", loggingEvent.getThreadName(), loggingEvent.getRenderedMessage());

            model.addEntry(le);

            // Update counts
            switch(loggingEvent.getLevel().toInt()) {
                case Level.FATAL_INT: ctF ++; break;
                case Level.ERROR_INT: ctE ++; break;
                case Level.WARN_INT: ctW ++; break;
                case Level.INFO_INT: ctI ++; break;
                case Level.DEBUG_INT: ctD ++; break;
            }
            parent.setCounts(ctF, ctE, ctW, ctI, ctD);
        }

        @Override
        public void close() {
            // Do nothing
        }

        @Override
        public boolean requiresLayout() {
            return false;
        }
    }

    private class LogTableCellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if("FATAL".equalsIgnoreCase(model.getValueAt(row, 1))) {
                c.setBackground(new Color(255, 140, 140));
            } else if("ERROR".equalsIgnoreCase(model.getValueAt(row, 1))) {
                c.setBackground(new Color(255, 220, 220));
            } else if("WARN".equalsIgnoreCase(model.getValueAt(row, 1))) {
                c.setBackground(new Color(255, 255, 220));
            } else {
                c.setBackground(Color.WHITE);
            }

            return c;
        }
    }

    private static class LogTableModel extends AbstractTableModel
    {
        private ArrayList<LogEntry> entries;

        LogTableModel() {
            entries = new ArrayList<LogEntry>();
        }

        void addEntry(LogEntry entry) {
            entries.add(entry);
            fireTableRowsInserted(entries.size() - 1, entries.size() - 1);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public String getColumnName(int column) {
            switch(column) {
                case 0: return "Time";
                case 1: return "Level";
                case 2: return "Message";
                default: throw new IndexOutOfBoundsException("Column index out of bounds");
            }
        }

        @Override
        public int getRowCount() {
            return entries.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getValueAt(int rowIndex, int columnIndex) {
            LogEntry le = entries.get(rowIndex);
            switch(columnIndex) {
                case 0: return le.timestamp;
                case 1: return le.level;
                case 2: return le.message;
                default: throw new IndexOutOfBoundsException("Column index out of bounds");
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }

    static class LogEntry
    {
        private String level, message, timestamp;
    }

}
