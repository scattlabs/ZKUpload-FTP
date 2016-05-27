package SCATTLABS.ZKUpload;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Messagebox;

public class FileUploadVM {

	static String server = "172.18.2.75";
	static int port = 21;
	static String user = "swahp";
	static String pass = "103040132";

	@Command
	@NotifyChange("fileuploaded")
	public void onUploadPDF(@BindingParam("media") Media media) throws IOException {
		if (media != null) {
			System.out.println("media getName() : " + media.getName());
			InputStream is;
			if (media.isBinary()) {
				is = media.getStreamData();
			} else {
				is = new ReaderInputStream(media.getReaderData());
			}
			splitFile(is, media.getName());
			Messagebox.show("File Sucessfully uploaded in the path [ ." + media.getName() + " : " + is.read() + " ]");
		}
	}

	public static void splitFile(InputStream inputFile, String fileName) {
		int nChunks = 0, read = 0, readLength = 1000000;

		byte[] byteChunkPart;
		try {
			InputStream inputStream = inputFile;
			while (true) {
				System.out.println("nChunks : " + nChunks);
				/*
				 * if (fileSize <= readLength) { readLength = fileSize; }
				 */
				byteChunkPart = new byte[readLength];
				// System.out.println("TESTER 2 : " +
				// inputStream.read(byteChunkPart));
				read = inputStream.read(byteChunkPart, 0, readLength);
				assert (read == byteChunkPart.length);
				if (read <= 0) {
					System.out.println("berhenti : " + read); // ketika hasil
																// read == -1
																// berarti
					// filesize sudah 0 atau habis
					// maka di break
					break;
				} else {
					uploadFile(connectFTP(), byteChunkPart, fileName + ".part" + Integer.toString(nChunks));
				}
				nChunks++;
				byteChunkPart = null;
			}
			System.out.println(nChunks);
			inputStream.close();
			System.out.println("SELESAI");
		} catch (IOException exception) {
			System.out.println("ex :" + exception.getMessage());
			exception.printStackTrace();
		}
	}

	public static FTPClient connectFTP() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			int reply = ftpClient.getReplyCode();
			if (FTPReply.isPositiveCompletion(reply)) {
				if (ftpClient.login(user, pass)) {
					System.out.println("connect");
				} else {
					System.out.println("not connected");
					System.exit(0);
				}
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			} else {
				System.out.println("masuk else reply");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("e : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("e2 : " + e.getMessage());
			// TODO: handle exception
		}
		return ftpClient;
	}

	public static void uploadFile(FTPClient ftpClient, byte[] byteChunkPart, String secondRemoteFile) {
		try {
			System.out.println(secondRemoteFile);
			System.out.println("Start uploading second file");

			if (ftpClient == null) {
				System.out.println("ftp client null");
			} else {
				System.out.println("ftp client not null");
			}
			OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
			if (outputStream == null) {
				System.out.println("output stream is null");
			} else {
				System.out.println("output stream is not null");
			}
			outputStream.write(byteChunkPart);
			outputStream.close();
			boolean completed = ftpClient.completePendingCommand();
			if (completed) {
				System.out.println("The second file is uploaded successfully.");
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				System.out.println("ex : " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}