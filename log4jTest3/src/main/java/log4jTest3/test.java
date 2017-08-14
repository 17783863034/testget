package log4jTest3;

import org.apache.log4j.Logger;

public class test {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(test.class);

	public static void main(String[] args) {
		hello();

		Logger logger = Logger.getLogger(test.class);
		int a=1;
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - int a=" + a);
		}
		
		logger.debug("debug");
		logger.info("info");
		logger.warn("warn");
		logger.error("error");
	}

	private static void hello() {
		if (logger.isDebugEnabled()) {
			logger.debug("hello() - start");
		}

		// TODO Auto-generated method stub
		
		if (logger.isDebugEnabled()) {
			logger.debug("hello() - end");
		}
	}
}
