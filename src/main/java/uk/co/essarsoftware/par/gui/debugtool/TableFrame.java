package uk.co.essarsoftware.par.gui.debugtool;

import uk.co.essarsoftware.par.engine.Player;
import uk.co.essarsoftware.par.engine.Table;
import uk.co.essarsoftware.par.gui.panels.TablePanel;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: sroberts
 * Date: 03/07/12
 * Time: 14:57
 * To change this template use File | Settings | File Templates.
 */
public class TableFrame extends JInternalFrame
{
    private Table table;

    TablePanel pnlTable;

    public TableFrame(Table table) {
        super("Table", true);
        this.table = table;

        initComponents();
        drawComponents();

        pack();
    }

    private void initComponents() {
        pnlTable = new TablePanel();
    }

    private void drawComponents() {
        setContentPane(pnlTable);
    }

    void addPlayer(Player player) {
        pnlTable.addPlayer(player, table.getPlays(player));
    }

    void refresh() {
        pnlTable.setDiscard(table.getDiscard());
    }

    void refresh(Player player) {
        refresh();
        pnlTable.setPlays(player, table.getPlays(player));
        pack();
    }
}
