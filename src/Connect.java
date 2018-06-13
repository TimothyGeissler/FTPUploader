import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import javax.swing.*;
import java.io.*;

public class Connect {
    public void FTPconnect(String hostname, String user, String pass, String lPath, String rPath) {

        int port = 21;
        //lpath = String source = "/Users/tgeissler/Desktop/zipped.zip";
        //rpath = String dest = "/AutoBackup/table.zip";

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(hostname, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File(lPath);

            InputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Upload initiated...");
            boolean done = ftpClient.storeFile(rPath, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("File uploaded successfully.");
                JOptionPane.showMessageDialog(null, "Uploaded successfully");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
