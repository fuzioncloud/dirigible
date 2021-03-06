package org.eclipse.dirigible.api.v3.http.client;

import java.util.ArrayList;
import java.util.List;

public class HttpClientResponse {

	private int statusCode;

	private String statusMessage;

	private byte[] data;

	private String text;

	private String protocol;

	private boolean binary;

	private List<HttpClientHeader> headers = new ArrayList<HttpClientHeader>();

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public List<HttpClientHeader> getHeaders() {
		return headers;
	}

	public void setHeaders(List<HttpClientHeader> headers) {
		this.headers = headers;
	}

	public boolean isBinary() {
		return binary;
	}

	public void setBinary(boolean binary) {
		this.binary = binary;
	}

}
