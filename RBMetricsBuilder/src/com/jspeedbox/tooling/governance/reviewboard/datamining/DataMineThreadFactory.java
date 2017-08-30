package com.jspeedbox.tooling.governance.reviewboard.datamining;

import java.util.concurrent.ThreadFactory;

public class DataMineThreadFactory implements ThreadFactory{

	public Thread newThread(Runnable runnableThread) {
		Thread thread = new  Thread(runnableThread);
		thread.setName("DataMineService");
		return thread;
	}

}
