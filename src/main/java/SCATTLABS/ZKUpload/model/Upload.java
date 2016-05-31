package SCATTLABS.ZKUpload.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_upload")
public class Upload {

	@Id
	@Column(name = "upload_id")
	private int upload_id;
	@Column(name = "file_name")
	private String file_name;
	@Column(name = "file_size")
	private long file_size;
	@Column(name = "part_amount")
	private int part_amount;
	@Column(name = "part_size")
	private int part_size;
	@Column(name = "last_part")
	private int last_part;
	@Column(name = "last_upload_percent")
	private int last_upload_percent;
	@Column(name = "upload_status")
	private int upload_status;

	public Upload() {
		// TODO Auto-generated constructor stub
	}

	public int getUpload_id() {
		return upload_id;
	}

	public void setUpload_id(int upload_id) {
		this.upload_id = upload_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public long getFile_size() {
		return file_size;
	}

	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}

	public int getPart_amount() {
		return part_amount;
	}

	public void setPart_amount(int part_amount) {
		this.part_amount = part_amount;
	}

	public int getPart_size() {
		return part_size;
	}

	public void setPart_size(int part_size) {
		this.part_size = part_size;
	}

	public int getLast_part() {
		return last_part;
	}

	public void setLast_part(int last_part) {
		this.last_part = last_part;
	}

	public int getLast_upload_percent() {
		return last_upload_percent;
	}

	public void setLast_upload_percent(int last_upload_percent) {
		this.last_upload_percent = last_upload_percent;
	}

	public int getUpload_status() {
		return upload_status;
	}

	public void setUpload_status(int upload_status) {
		this.upload_status = upload_status;
	}

}
