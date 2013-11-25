package com.redv.chbtc.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ROOT")
public class Root extends AbstractObject {

	private static final long serialVersionUID = 20131126L;

	@XmlElement(name = "HEAD")
	private Head head;

	@XmlElement(name = "BODY")
	private Body body;


	/**
	 * @return the title
	 */
	public String getTitle() {
		return head.title;
	}

	/**
	 * @return the state
	 */
	public boolean isState() {
		return body.state;
	}

	/**
	 * @return the des
	 */
	public String getDes() {
		return body.des;
	}

	/**
	 * @return the mainData
	 */
	public String getMainData() {
		return body.mainData;
	}

	private static class Head  extends AbstractObject {
		private static final long serialVersionUID = 2013112601L;

		@XmlElement(name = "title")
		private String title;
	}

	private static class Body extends AbstractObject {

		private static final long serialVersionUID = 2013112601L;

		@XmlElement(name = "State")
		private boolean state;

		@XmlElement(name = "Des")
		private String des;

		@XmlElement(name = "MainData")
		private String mainData;
	}

}
