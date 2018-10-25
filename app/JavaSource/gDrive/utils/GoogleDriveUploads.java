package gDrive.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
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
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.NamePrefix;
import com.google.gdata.data.extensions.NameSuffix;
import com.google.gdata.data.extensions.PhoneNumber;

import gDrive.beans.ContactBean;

public class GoogleDriveUploads {

	public static Logger log = LogManager.getLogger(GoogleDriveUploads.class);

	private static final String APPLICATION_NAME = "Google Drive API";
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static String SCOPE;

	private static final String SCOPE_MANAGE_CONTACTS = "MANAGE_CONTACTS";
	private static final String SCOPE_MANAGE_FILES = "MANAGE_FILES";

	private static final String GOOGLE_CLIENT_ID = "606861000631-aik46h8ufjbn5m1r623kp6ivmk72taum.apps.googleusercontent.com";
	private static final String GOOGLE_CLIENT_SECRET = "UrkgPtY7WpH9gIx6QNxPSdRO";

	private static final String[] GOOGLE_REDIRECT_URIS = new String[] { "urn:ietf:wg:oauth:2.0:oob",
			"http://localhost" };

	private static List<String> getScopes(String scope) {

		if (scope == null || scope.trim().length() == 0 || scope.equalsIgnoreCase("null")) {
			throw new CustomException("No scope mentioned");
		}

		List<String> scopes = new ArrayList<>();
		if (scope.equalsIgnoreCase(SCOPE_MANAGE_CONTACTS)) {
			scopes.add("https://www.google.com/m8/feeds/");
		} else if (scope.equalsIgnoreCase(SCOPE_MANAGE_FILES)) {
			scopes.add(DriveScopes.DRIVE);
		}
		return scopes;
	}

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
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, getScopes(SCOPE))
				.build();
		credentials = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		if (!credentials.refreshToken()) {
			throw new CustomException("Unable to refresh the token");
		}
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

		SCOPE = SCOPE_MANAGE_FILES;
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

		SCOPE = SCOPE_MANAGE_FILES;
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

	public static ContactsService getContactService() throws Exception {

		ContactsService contactsService = new ContactsService("Google Contacts");

		NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		Credential credential = getCredentials(httpTransport);
		contactsService.setOAuth2Credentials(credential);
		return contactsService;
	}

	@SuppressWarnings("unchecked")
	public List<ContactBean> getContacts() throws Exception {

		SCOPE = SCOPE_MANAGE_CONTACTS;
		ContactsService contactsService = getContactService();
		URL url = new URL("https://www.google.com/m8/feeds/contacts/default/full");
		Query query = new Query(url);
		query.setMaxResults(1000);
		ContactFeed feed = contactsService.query(query, ContactFeed.class);

		List<ContactEntry> emptyContactList = new ArrayList<>();
		List<ContactBean> contactBeans = new ArrayList<>();

		for (ContactEntry entry : feed.getEntries()) {

			if (entry.hasName()) {
				ContactBean contactBean = new ContactBean();
				Name name = entry.getName();
				if (name.hasFullName()) {
					contactBean.setContactId(entry.getId());
					contactBean.setFullName(name.getFullName().getValue());
				}
				if (name.hasNamePrefix()) {
					contactBean.setPrefix(name.getNamePrefix().getValue());
				}
				if (name.hasNameSuffix()) {
					contactBean.setSuffix(name.getNameSuffix().getValue());
				}

				List<PhoneNumber> phoneNumbers = entry.getPhoneNumbers();
				List<String> numbers = new ArrayList<>(phoneNumbers.size());
				for (PhoneNumber phoneNumber : phoneNumbers) {
					if (phoneNumber.getPhoneNumber() != null && phoneNumber.getPhoneNumber().trim().length() > 0) {
						numbers.add(phoneNumber.getPhoneNumber());
					}
				}
				contactBean.setPhoneNumbers(numbers);

				List<Email> emailsList = entry.getEmailAddresses();
				List<String> emails = new ArrayList<>(emailsList.size());
				for (Email email : emailsList) {
					if (email.getAddress() != null && email.getAddress().trim().length() > 0) {
						emails.add(email.getAddress());
					}
				}
				contactBean.setEmails(emails);
				contactBeans.add(contactBean);
			} else {
				emptyContactList.add(entry);
			}
		}

		for (ContactEntry entry : emptyContactList) {
			ContactEntry contact = contactsService.getEntry(new URL(entry.getId()), ContactEntry.class);
			contact.delete();
		}

		ObjectComparator comparator = new ObjectComparator();
		comparator.setSortingData("fullName", "asc", false);
		Collections.sort(contactBeans, comparator);
		return contactBeans;
	}

	public void createContact(ContactBean contactBean) throws Exception {

		ContactEntry contactEntry = new ContactEntry();

		Name name = new Name();
		name.setFullName(new FullName(contactBean.getFullName(), null));
		name.setNamePrefix(new NamePrefix(contactBean.getPrefix()));
		name.setNameSuffix(new NameSuffix(contactBean.getSuffix()));
		contactEntry.setName(name);

		for (String number : contactBean.getPhoneNumbers()) {
			PhoneNumber phoneNumber = new PhoneNumber();
			phoneNumber.setPhoneNumber(number);
			phoneNumber.setRel("http://schemas.google.com/g/2005#work");
			contactEntry.addPhoneNumber(phoneNumber);
		}

		for (String emailAddrs : contactBean.getEmails()) {
			Email email = new Email();
			email.setAddress(emailAddrs);
			email.setRel("http://schemas.google.com/g/2005#work");
			contactEntry.addEmailAddress(email);
		}

		URL saveUrl = new URL("https://www.google.com/m8/feeds/contacts/karanamsrihari45@gmail.com/full");
		ContactsService contactsService = getContactService();
		contactsService.insert(saveUrl, contactEntry);
	}

	public static void main(String[] args) throws Exception {
		List<ContactBean> list = new GoogleDriveUploads().getContacts();
		log.debug("Total Contacts : " + list.size());
		for (ContactBean bean : list) {
			log.debug("CONTACT : " + " " + bean.getPhoneNumbers().size() + " " + bean.getFullName());
		}
	}

}
