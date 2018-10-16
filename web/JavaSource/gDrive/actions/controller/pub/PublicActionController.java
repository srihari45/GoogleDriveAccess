package gDrive.actions.controller.pub;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import gDrive.forms.UploadForm;
import gDrive.utils.CustomException;
import gDrive.utils.GoogleDriveUploads;
import gDrive.utils.UIFormConstants;

@Controller
public class PublicActionController {

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	private static GoogleDriveUploads googleUploads = new GoogleDriveUploads();

	@GetMapping("/")
	public String showHome(HttpServletRequest request) {
		return UIFormConstants.TILES_INDEX;
	}

	@GetMapping(value = "/pub/uploadToDrive.html")
	public String showUploadPage(HttpServletRequest request, Model model) {
		UploadForm uploadForm = new UploadForm();
		model.addAttribute(UIFormConstants.FORM_UPLOAD_FILES, uploadForm);
		return UIFormConstants.TILES_UPLOAD_FILES_TO_DRIVE;
	}

	@PostMapping(value = "/pub/uploadFilesToDrive.html")
	public String uploadFiles(@RequestParam("file") MultipartFile[] fileArr, HttpServletRequest request,
			HttpServletResponse resonse) throws Exception {
		log.debug("Total Files : " + fileArr.length);
		PrintWriter out = resonse.getWriter();
		List<File> files = new ArrayList<>();
		for (int i = 0; i < fileArr.length; i++) {
			log.debug(fileArr[i].getOriginalFilename());
			File file = new File(fileArr[i].getOriginalFilename());
			fileArr[i].transferTo(file);
			files.add(file);
		}
		try {
			googleUploads.uploadFiles(files);
		} catch (CustomException e) {
			log.debug(e.getMessage());
			out.write("error|" + e.getMessage());
		} catch (Exception e) {
			log.debug("error occured ", e);
			out.write("error|Something went wrong. Please try again later");
		}

		return null;
	}

	@GetMapping(value = "/pub/getFilesMetadata.html")
	public String getFilesMetadata(HttpServletRequest request) {

		try {
			List<String> list = googleUploads.getFiles();
			request.setAttribute("list", list);
		} catch (CustomException e) {
			request.setAttribute("errorList", e.getMessage());
		} catch (Exception e) {
			log.debug("error occured ", e);
		}
		return UIFormConstants.TILES_FILES_METADATA;
	}

}
