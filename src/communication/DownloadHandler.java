package communication;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Christoffer on 2014-04-30.
 *
 */
public class DownloadHandler {

    private HttpURLConnection conn;
    private String username;
    private String password;
    private String fileName;
    private boolean finished;
    private int totalDownload;
    private int perSecond;

    public DownloadHandler(String username, String password, String fileName) {
        this.username = username;
        this.password = password;
        this.fileName = fileName;
    }

    public boolean download(String url, String localFilePath) {
        try {
            // Use this url in the real version. vvv
            /*
             * URL targetUrl = new URL(
             * "http://scratchy.cs.umu.se:8090/html/download.php?path=" + url);
             */
            File file = new File(localFilePath);
            if (file.isFile()) {
                fileName = file.getName();
            }
            url = url.replaceFirst("\\u003d", "=");

            URL targetUrl = new URL(url);
            String authString = username + ":" + password;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            String authStringEnc = new String(authEncBytes);
            conn = (HttpURLConnection) targetUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
            int responseCode;
            responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out
                        .println("Error wrong response code: " + responseCode);
                return false;
            }
            System.out.println(responseCode);
            InputStream in = conn.getInputStream();
            boolean fileIsBinary = false;
            byte[] buf = new byte[1024];
            int length = in.read(buf, 0, 1024);
            for(int i = 0; i < length; i++) {
                String lineSep = System.getProperty("line.separator");
                if(buf[i] == 0) {
                    fileIsBinary = true;
                    System.out.println("File is binary!");
                    break;
                }
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(buf, 0, length);
            String bufString = new String(buf);
            bufString = bufString.replaceAll("\\r\\n", "");
            bufString = bufString.replaceAll("\\n", "");
            bufString = bufString.replaceAll("\\r", "");
            String buffer;
            totalDownload = 0;
            int previousDownload = 0;
            Long previousTime = System.currentTimeMillis();
            if (!fileIsBinary) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(bufString);
                totalDownload += bufString.length();
                writer.newLine();
                totalDownload += System.getProperty("line.separator").length();
                while ((buffer = reader.readLine()) != null && !isFinished()) {
                    writer.write(buffer);
                    totalDownload += buffer.length();
                    writer.newLine();
                    totalDownload += System.getProperty("line.separator").length();
                    if (System.currentTimeMillis() - previousTime > 1000) {
                        previousTime = System.currentTimeMillis();
                        perSecond = totalDownload - previousDownload;
                        previousDownload = totalDownload;
                    }
                }
                writer.close();
                reader.close();
            } else {
                System.out.println(conn.getContentLength());
                byte[] buff = new byte[conn.getContentLength() + 1];
                int total = 0;
                int count = in.read(buff, 0, conn.getContentLength());
                total += count;
                fileOut.write(buff, 0, count);
                while ((count = in.read(buff)) != -1 && !isFinished()) {
                    total += count;
                    System.out.println(count);
                    if (count > 0) {
                        fileOut.write(buff, 0, count);
                    }
                }
                System.out.println("SSize: " + total + " Expected: " + conn.getContentLength());

            }
            in.close();
            fileOut.close();
            finished = true;
            System.out.println("Size: " + totalDownload + " Expected: "
                    + conn.getContentLength());
            conn.disconnect();

        } catch (MalformedURLException e) {
            System.out.println("ERROR: The url was not correct: " + url);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getTotalSize() {
        if (conn != null) {
            return conn.getContentLength();
        }
        return -1;
    }

    public boolean isBinaryFile() {
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".gif") || fileName.endsWith(".tar.gz") ||
                fileName.endsWith(".tar.xz");
    }

    public int getCurrentProgress() {
        return totalDownload;
    }

    public int getCurrentSpeed() {
        return perSecond;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean isFinished) {
        finished = isFinished;
    }

}
