package SCATTLABS.ZKUpload.zkoss;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zkplus.spring.SpringUtil;

import SCATTLABS.ZKUpload.model.Upload;
import SCATTLABS.ZKUpload.service.CRUDService;

public class FileUploadVM {

	static String server = "172.18.2.75";
	static int port = 21;
	static String user = "swahp";
	static String pass = "103040132";

	static Long fileSize = 0L;
	static int countUpload = 0;
	static int persenFileUpload = 0;

	static Media media;
	private String fileName;

	static CRUDService CRUDService;

	List<Upload> uploads = new ArrayList<>();
	static Upload upload = new Upload();

	public FileUploadVM() {
		CRUDService = (CRUDService) SpringUtil.getBean("CRUDService");
		uploads = CRUDService.getAll(Upload.class);
		// TODO Auto-generated constructor stub
	}

	@Command
	@NotifyChange("fileName")
	public void onSelectedFile(@BindingParam("media") Media media) {
		FileUploadVM.media = media;
		fileName = media.getName();
	}

	public Upload checkFileUpload(String fileName) {
		Upload upload = null;
		for (Upload up : uploads) {
			if (up.getFile_name().equals(fileName)) {
				upload = up;
				break;
			}
		}
		return upload;
	}

	@Command
	@NotifyChange("fileName")
	public void onUpload() throws IOException {
		if (media != null) {
			System.out.println("media getName() : " + media.getName());
			InputStream is;
			InputStream isSize;
			if (media.isBinary()) {
				is = media.getStreamData();
				isSize = media.getStreamData();
			} else {
				is = new ReaderInputStream(media.getReaderData());
				isSize = new ReaderInputStream(media.getReaderData());
			}
			fileSize = getFileSize(isSize);
			boolean statusUpload = false;
			if (checkFileUpload(media.getName()) != null) {
				upload = checkFileUpload(media.getName());
				System.out.println("naha last upload 128 : " + upload.getLast_part());
				statusUpload = false; // melanjutkan
			} else {
				upload = new Upload();
				upload.setUpload_id(1);
				upload.setFile_name(media.getName());
				upload.setFile_size(fileSize);
				upload.setLast_part(0);
				upload.setLast_upload_percent(0);
				upload.setPart_amount(1000000);
				upload.setPart_size(1000000);
				upload.setUpload_status(0);
				CRUDService.Save(upload);
				statusUpload = true; // new upload
			}

			splitFile(is, fileSize, media.getName(), statusUpload);
			Messagebox.show("File Sucessfully uploaded in the path [ ." + media.getName() + " : " + media.getName()
					+ ":" + fileSize + " ]");
			fileName = "";
		}
	}

	public static void splitFile(InputStream inputFile, long fileSize, String fileName, boolean uploadStatus) {
		int read = 0, readLength = 1000000; // 1MB
		System.out.println("BULUYKEUN : " + uploadStatus);
		byte[] byteChunkPart;
		int lastPart = upload.getLast_part();
		try {
			InputStream inputStream = inputFile;
			while (fileSize > 0) {
				System.out.println("fileSize loop : " + fileSize);
				System.out.println("nChunks : " + countUpload);
				if (fileSize <= readLength) {
					readLength = (int) fileSize;
				}
				byteChunkPart = new byte[readLength];
				read = inputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;
				assert (read == byteChunkPart.length);
				if (uploadStatus) {
					upload.setLast_part(countUpload);
					uploadFile(connectFTP(), byteChunkPart, fileName + ".part" + Integer.toString(countUpload), upload);
				} else {
					System.out.println("lastpart : " + lastPart);
					if (countUpload >= lastPart) {
						upload.setLast_part(countUpload);
						uploadFile(connectFTP(), byteChunkPart, fileName + ".part" + Integer.toString(countUpload),
								upload);
					} else {
						System.out.println("Sudah di upload sebelumnya");
					}
				}
				countUpload++;
				byteChunkPart = null;
			}
			System.out.println(countUpload);
			inputStream.close();
			System.out.println("SELESAI");
			upload.setUpload_status(1);
			CRUDService.Save(upload);
		} catch (IOException exception) {
			System.out.println("ex :" + exception.getMessage());
			exception.printStackTrace();
		}
	}

	public static long getFileSize(InputStream inputStream) {
		int maxByte = 1000000000; // 1 GB
		byte[] byteTemp = new byte[maxByte];
		long fileSize = 0;
		long fileSizeTemp = 0;
		try {
			do {
				fileSizeTemp = inputStream.read(byteTemp);
				fileSize += fileSizeTemp;
			} while (fileSizeTemp == maxByte);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileSize;
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

	@NotifyChange("persenFileUpload")
	public static void uploadFile(FTPClient ftpClient, byte[] byteChunkPart, String secondRemoteFile, Upload upload) {
		try {
			System.out.println(secondRemoteFile);
			OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
			outputStream.write(byteChunkPart);
			outputStream.close();
			boolean completed = ftpClient.completePendingCommand();
			if (completed) {
				CRUDService.Save(upload);
				persenFileUpload = (int) ((countUpload * 1000000) * 100 / fileSize);
				System.out.println("persenFileUpload : " + persenFileUpload);
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

	@NotifyChange("persenFileUpload")
	public int getPersenFileUpload() {
		return persenFileUpload;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public int getCountUpload() {
		return countUpload;
	}

	public static void setFileSize(Long fileSize) {
		FileUploadVM.fileSize = fileSize;
	}

	public static void setCountUpload(int countUpload) {
		FileUploadVM.countUpload = countUpload;
	}

	public static void setPersenFileUpload(int persenFileUpload) {
		FileUploadVM.persenFileUpload = persenFileUpload;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}