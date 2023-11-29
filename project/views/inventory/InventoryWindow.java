package project.views.inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.MainScreenView;
import project.views.StaffRegistryView;
import project.views.StaffViewWindow;
import project.views.inventory.InventoryProductSearchResultsPanel;
import project.views.inventory.InventoryProductSearchWindow;
import project.views.inventory.NewProductWindow;
import project.views.queue.QueueWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class InventoryWindow extends JFrame {
    private static final long serialVersionUID = 3L;

    public InventoryWindow(Connection connection, String userID, String userRole) throws HeadlessException {
        setTitle("Inventory Window");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width / 2, screenSize.height / 2);
        setLocation(screenSize.width / 4, screenSize.height / 4);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1, 0));

        JButton crp = new JButton("Create Product");
        JButton dp = new JButton("Delete Product");
        JButton ud = new JButton("Update Details");

        contentPane.add(crp);
        contentPane.add(dp);
        contentPane.add(ud);

        crp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                NewProductWindow npw = new NewProductWindow(connection, userID, userRole);
            }
        });

        dp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DeleteProductWindow dpw = new DeleteProductWindow(connection, userID, userRole);
            }
        });

        ud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InventoryProductSearchWindow ipsw = new InventoryProductSearchWindow(connection, userID, userRole);
            }
        });

        JMenu navigationBar = new JMenu("Menu");

        JMenuItem dv = new JMenuItem("Main Screen");
        navigationBar.add(dv);
        dv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    MainScreenView msv = new MainScreenView(connection, userID, userRole);
                    msv.setVisible(true);
                } catch (SQLException err) {
                    err.printStackTrace();
                }
            }
        });

        if (userRole.equals("Manager") || userRole.equals("Staff")) {
            JMenuItem sv = new JMenuItem("Staff View");
            navigationBar.addSeparator();
            navigationBar.add(sv);
            sv.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    StaffViewWindow sfw = new StaffViewWindow(connection, userID, userRole);
                }
            });

        }

        if (userRole.equals("Manager")) {
            JMenuItem sr = new JMenuItem("Staff Registry");
            navigationBar.addSeparator();
            navigationBar.add(sr);
            sr.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        StaffRegistryView srv = new StaffRegistryView(connection, userID, userRole);
                        srv.setVisible(true);
                    } catch (SQLException err) {
                        err.printStackTrace();
                    }
                }
            });
        }


        JMenuBar menuBar = new JMenuBar();
        menuBar.add(navigationBar);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);
    }
}


