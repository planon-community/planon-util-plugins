package edu.planon.lib.client.resource;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;

import edu.planon.lib.client.dto.DownloadFile;

public class DownloadFileResource extends ResourceStreamResource {
	private static final long serialVersionUID = 1L;
	private DownloadFile downloadFile;
	
	public DownloadFileResource() {
		setContentDisposition(ContentDisposition.ATTACHMENT);
		setCacheDuration(Duration.NONE);
	}
	
	public DownloadFile getDownloadFile() {
		return this.downloadFile;
	}
	
	public void setDownloadFile(DownloadFile downloadFile) {
		this.downloadFile = downloadFile;
		setFileName(downloadFile.getFileName());
	}
	
	@Override
	public void respond(Attributes attributes) {
		super.respond(attributes);
	}
	
	@Override
	protected IResourceStream getResourceStream(Attributes attributes) {
		return new AbstractResourceStreamWriter() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void write(OutputStream output) throws IOException {
				output.write(downloadFile.getByteArr());
			}
			
			@Override
			public Bytes length() {
				return downloadFile.length();
			}
		};
	}
	
}