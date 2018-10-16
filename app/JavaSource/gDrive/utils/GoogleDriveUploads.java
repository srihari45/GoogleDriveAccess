package gDrive.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveUploads {

	public static Logger log = LogManager.getLogger(GoogleDriveUploads.class);

	private static final String APPLICATION_NAME = "Google Drive API";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

	private static final String GOOGLE_CLIENT_ID = "606861000631-kabif4vnsb6e7rmnqei1rov441i22k71.apps.googleusercontent.com";
	private static final String GOOGLE_CLIENT_SECRET = "j7NRiqAHlbtXm2X28cfpKv-D";

	private static final String[] GOOGLE_REDIRECT_URIS = new String[] { "urn:ietf:wg:oauth:2.0:oob", "http://localhost" };

	private static Details getClientSecretDetails() {

		Details details = new Details();
		details.setClientId(GOOGLE_CLIENT_ID);
		details.setClientSecret(GOOGLE_CLIENT_SECRET);

		details.setRedirectUris(Arrays.asList(GOOGLE_REDIRECT_URIS));

		return details;
	}

	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {

		GoogleAuthorizationCodeFlow flow = null;
		Credential credentials = null;

		// Load client secrets.
		GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
		clientSecrets.setWeb(getClientSecretDetails());

		// Build flow and trigger user authorization request.
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH))).build();
		credentials = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		return credentials;
	}

	private static Drive getService() throws Exception {

		NetHttpTransport HTTP_TRANSPORT = null;
		Drive service = null;
		HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
		return service;
	}

	public List<String> getFiles() {

		List<String> list = new ArrayList<String>();
		try {
			Drive service = getService();
			FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)")
					.execute();
			List<File> files = result.getFiles();
			for (File file : files) {
				log.debug(file.getName() + ", " + file.getId());
				list.add(file.getName());
			}
		} catch (GoogleJsonResponseException e) {
			log.debug("Exception : " + e.getDetails().get("message"));
			throw new CustomException(e.getDetails().getMessage());
		} catch (Exception e) {
			log.error("Error occured ", e);
			throw new CustomException("Something went wrong. Please try again later");
		}
		return list;
	}

	public void uploadFiles(List<java.io.File> uploadedFiles) {
		try {
			Drive service = getService();
			for (java.io.File uploadedFile : uploadedFiles) {
				File fileMetadata = new File();
				fileMetadata.setName(uploadedFile.getName());
				InputStream is = new BufferedInputStream(new FileInputStream(uploadedFile));
				FileContent mediaContent = new FileContent(URLConnection.guessContentTypeFromStream(is), uploadedFile);
				File file = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
				log.debug("File ID: " + file.getId());
			}
		} catch (GoogleJsonResponseException e) {
			log.debug("Exception : " + e.getDetails().get("message"));
			throw new CustomException(e.getDetails().getMessage());
		} catch (Exception e) {
			log.error("Error occured ", e);
			throw new CustomException("Something went wrong. Please try again later");
		}
	}

	public static void main(String[] args) throws Exception {

	}

}
