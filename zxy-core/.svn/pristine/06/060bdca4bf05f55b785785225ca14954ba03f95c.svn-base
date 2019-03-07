package com.jtang.core.log4j;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public abstract class MinuteRollingFileAppender extends FileAppender {

	/**
	 * The date pattern. By default, the pattern is set to "'.'yyyy-MM-dd"
	 * meaning daily rollover.
	 */
	private String datePattern = "'.'yyyyMMdd_HHmm";
	
	private int everyMinute = 60;
	
	/**
	 * The log file will be renamed to the value of the scheduledFilename
	 * variable when the next interval is entered. For example, if the rollover
	 * period is one hour, the log file will be renamed to the value of
	 * "scheduledFilename" at the beginning of the next hour.
	 * 
	 * The precise time when a rollover occurs depends on logging activity.
	 */
	private String scheduledFilename;

	/**
	 * The next time we estimate a rollover should occur.
	 */
	private long nextCheck = System.currentTimeMillis() - 1;

	Date now = new Date();

	SimpleDateFormat sdf;

	RollingCalendar rc;

	// The gmtTimeZone is used only in computeCheckPeriod() method.
	static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

	/**
	 * The default constructor does nothing.
	 */
	public MinuteRollingFileAppender(int minute) {
		this.everyMinute = minute;
		rc = new RollingCalendar(this.everyMinute);
	}

	/**
	 * Instantiate a <code>DailyRollingFileAppender</code> and open the file
	 * designated by <code>filename</code>. The opened filename will become the
	 * ouput destination for this appender.
	 */
	public MinuteRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {
		super(layout, filename, true);
		this.datePattern = datePattern;
		activateOptions();
	}

	/**
	 * The <b>DatePattern</b> takes a string in the same format as expected by
	 * {@code SimpleDateFormat}. This options determines the rollover schedule.
	 */
	public void setDatePattern(String pattern) {
		datePattern = pattern;
	}

	/** Returns the value of the <b>DatePattern</b> option. */
	public String getDatePattern() {
		return datePattern;
	}

	public void activateOptions() {
		super.activateOptions();
		if (datePattern != null && fileName != null) {
			now.setTime(System.currentTimeMillis());
			sdf = new SimpleDateFormat(datePattern);
			File file = new File(fileName);
			scheduledFilename = fileName + sdf.format(new Date(file.lastModified()));

		} else {
			LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
		}
	}
	
	void rollOver() throws IOException {
		if (datePattern == null) {
			errorHandler.error("Missing DatePattern option in rollOver().");
			return;
		}

		String datedFilename = fileName + sdf.format(now);
		// It is too early to roll over because we are still within the
		// bounds of the current interval. Rollover will occur once the
		// next interval is reached.
		if (scheduledFilename.equals(datedFilename)) {
			return;
		}

		// close current file, and rename it to datedFilename
		this.closeFile();

		File target = new File(scheduledFilename);
		if (target.exists()) {
			target.delete();
		}

		File file = new File(fileName);
		boolean result = file.renameTo(target);
		if (result) {
			LogLog.debug(fileName + " -> " + scheduledFilename);
		} else {
			LogLog.error("Failed to rename [" + fileName + "] to [" + scheduledFilename + "].");
		}

		try {
			// This will also close the file. This is OK since multiple
			// close operations are safe.
			this.setFile(fileName, true, this.bufferedIO, this.bufferSize);
		} catch (IOException e) {
			errorHandler.error("setFile(" + fileName + ", true) call failed.");
		}
		scheduledFilename = datedFilename;
	}


	/**
	 * This method differentiates DailyRollingFileAppender from its super class.
	 * 
	 * <p>
	 * Before actually logging, this method will check whether it is time to do
	 * a rollover. If it is, it will schedule the next rollover time and then
	 * rollover.
	 * */
	protected void subAppend(LoggingEvent event) {
		long n = System.currentTimeMillis();
		if (n >= nextCheck) {
			now.setTime(n); //设置上一次时间
			nextCheck = rc.getNextCheckMillis(now);
			try {
				rollOver();
			} catch (IOException ioe) {
				if (ioe instanceof InterruptedIOException) {
					Thread.currentThread().interrupt();
				}
				LogLog.error("rollOver() failed.", ioe);
			}
		}
		super.subAppend(event);
	}
	
}

class RollingCalendar extends GregorianCalendar {
	private static final long serialVersionUID = -3560331770601814177L;

	private int everyMinute;
	
	RollingCalendar(int everyMinute) {
		super();
		this.everyMinute = everyMinute;
	}

	public long getNextCheckMillis(Date now) {
		return getNextCheckDate(now).getTime();
	}

	public Date getNextCheckDate(Date now) {
		this.setTime(now);
		this.set(Calendar.SECOND, 0);
		this.set(Calendar.MILLISECOND, 0);
		this.add(Calendar.MINUTE, everyMinute);
		//LogLog.warn("everyMinute:" + String.valueOf(everyMinute));
		return getTime();
	}
}
