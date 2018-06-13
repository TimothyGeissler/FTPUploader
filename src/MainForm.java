import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class MainForm {
    static Connect connect = new Connect();
    static dirUpload dir = new dirUpload();
    private JPanel MainPanel;
    private JLabel header;
    private JTextField addressField;
    private JTextField userField;
    private JTextField lPathField;
    private JTextField rPathField;
    private JPasswordField passField;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JButton UploadButton;
    private JCheckBox dirCheckBox;

    public MainForm() {
        String hostname = "", user = "", pass = "", lPath = "", rPath = "";
        int isDir = -1;
        try {
            Scanner scan = new Scanner(new File("data.txt"));
            System.out.println("Data file found");
            while (scan.hasNext()) {
                Scanner delim = new Scanner(scan.nextLine()).useDelimiter("!");
                hostname = delim.next();
                user = delim.next();
                pass = delim.next();
                lPath = delim.next();
                rPath = delim.next();
                isDir = delim.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.print("Error");
        }

        addressField.setText(hostname);
        userField.setText(user);
        passField.setText(pass);
        lPathField.setText(lPath);
        rPathField.setText(rPath);
        if (isDir == 1) {
            dirCheckBox.setSelected(true);
        } else if (isDir == 0) {
            dirCheckBox.setSelected(false);
        }

        String finalHostname = hostname;
        String finalUser = user;
        String finalPass = pass;
        String finalRPath = rPath;
        String finalLPath = lPath;

        UploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String password = new String(passField.getPassword());
                int isDir = -1;
                if (dirCheckBox.isSelected()) {
                    isDir = 1;
                } else if (!dirCheckBox.isSelected()) {
                    isDir = 0;
                }
                System.out.println("Host: " + addressField.getText() + ", User: " + userField.getText() + ", Pass: " + password + ", Local path: " + lPathField.getText() + ", Remote path: " + rPathField.getText() + ", isDir: " + isDir);
                try (PrintWriter out = new PrintWriter(new FileWriter("data.txt"))) {
                    out.print(addressField.getText() + "!" + userField.getText() + "!" + password + "!" + lPathField.getText() + "!" + rPathField.getText() + "!" + isDir);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                if (dirCheckBox.isSelected()) {
                    try {
                        dir.zipDir(lPathField.getText());
                        connect.FTPconnect(addressField.getText(), userField.getText(), password, dir.dirName(lPathField.getText()), rPathField.getText());
                        try {
                            File file = new File(dir.dirName(lPathField.getText()));
                            if (file.delete()) {
                                System.out.println(file.getName() + " is deleted!");
                            } else {
                                System.out.println("Delete operation is failed.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    connect.FTPconnect(addressField.getText(), userField.getText(), password, lPathField.getText(), rPathField.getText());
                }

            }
        });
    }

    public static void main(String[] args) {
        String hostname = "", user = "", pass = "", lPath = "", rPath = "";
        int isDir = -1;
        int response = JOptionPane.showConfirmDialog(null, "Use current data", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            JFrame frame = new JFrame("MainForm");
            frame.setContentPane(new MainForm().MainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setTitle("FTP Uploader by T.Geissler");
            frame.setVisible(true);
        } else if (response == JOptionPane.YES_OPTION) {
            try {
                Scanner scan = new Scanner(new File("data.txt"));
                System.out.println("Data file found");
                while (scan.hasNext()) {
                    Scanner delim = new Scanner(scan.nextLine()).useDelimiter("!");
                    hostname = delim.next();
                    user = delim.next();
                    pass = delim.next();
                    lPath = delim.next();
                    rPath = delim.next();
                    isDir = delim.nextInt();
                    System.out.println("Hostname: " + hostname + ", User: " + user + ", Pass: " + pass + ", lPath: " + lPath + ", rPath: " + rPath + ", isDir: " + isDir);
                }
                if (isDir == 1) {
                    System.out.println("Is dir...");
                    try {
                        dir.zipDir(lPath);
                        connect.FTPconnect(hostname, user, pass, dir.dirName(lPath), rPath);
                        try {
                            File file = new File(dir.dirName(lPath));
                            if (file.delete()) {
                                System.out.println(file.getName() + " is deleted!");
                            } else {
                                System.out.println("Delete operation is failed.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (isDir == 0) {
                    System.out.println("Not dir...");
                    connect.FTPconnect(hostname, user, pass, lPath, rPath);
                }
            } catch (FileNotFoundException e) {
                //response = JOptionPane.NO_OPTION;
                System.out.println("data file not found");
            }
        }
    }
}
