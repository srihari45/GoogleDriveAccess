package gDrive.forms;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class UploadForm {

	private List<MultipartFile> files;

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "UploadForm [files=" + files + "]";
	}
}
