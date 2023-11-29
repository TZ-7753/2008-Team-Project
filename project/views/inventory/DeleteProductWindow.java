package project.views.inventory;

import project.model.DatabaseConnectionHandler;
import project.model.DatabaseOperations;
import project.views.MainScreenView;
import project.views.StaffRegistryView;
import project.views.StaffViewWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteProductWindow extends JFrame{
    private Connection connection;
    private Container contentPane;
    private ProductCheckPanel productCheckForm;
    private ProductDeletePanel productDeleteForm;
    private JButton delete;
    private JTextField deleteProduct;
    public DeleteProductWindow(Connection con, String userID, String userRole) throws HeadlessException{
        connection = con;
        setTitle("Delete Product Window");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        productCheckForm = new ProductCheckPanel(this, connection );
        contentPane.add(productCheckForm, BorderLayout.CENTER);
        productDeleteForm = new ProductDeletePanel(this, connection);
        contentPane.add(productDeleteForm, BorderLayout.SOUTH);

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


        navigationBar.addSeparator();
        JMenuItem iv = new JMenuItem("Back");
        navigationBar.add(iv);

        iv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InventoryWindow iw = new InventoryWindow(connection, userID, userRole);
            }
        });



        JMenuBar menuBar = new JMenuBar();
        menuBar.add(navigationBar);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);
    }

    public static void main(String[] args) {
        DatabaseConnectionHandler databaseConnectionHandler = new DatabaseConnectionHandler();
        try {
            databaseConnectionHandler.openConnection();
            DeleteProductWindow npr = new DeleteProductWindow( databaseConnectionHandler.getConnection(), "werwer", "Manager");

        }catch (Throwable t) {
            databaseConnectionHandler.closeConnection();
            throw new RuntimeException(t);
        }
    }
}
