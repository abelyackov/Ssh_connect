import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.ArrayList;

public class SshConnectStart {
    public static void main(String[] args) {
        String user = "*****";
        String password = "*******";
        String host = "********";
        int port = 22;

        ArrayList<String> fileText = new ArrayList<>();

        String remoteFile = "/root/sshd_config";
        String thisFile = "/home/sinner/ssh_config.txt";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Установка соединения с " + host);
            session.connect();
            System.out.println("Соединение установлено.");
            System.out.println("Организовываю SFTP-канал.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP-канал организован.");


            InputStream out = null;
            out = sftpChannel.get(remoteFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(out));
            String line;
            while ((line = br.readLine()) != null)
                fileText.add(line);
            br.close();

            try(FileWriter writer = new FileWriter(thisFile, false))
            {
                // запись всей строки
                for (String o: fileText) {
                    writer.write(o);
                    writer.write("\n");
                }
                System.out.println("Запись файла прошла успешно.");
                writer.flush();
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
            session.disconnect();
        } catch (Exception e) {
            System.err.print(e);
        }

    }
}


