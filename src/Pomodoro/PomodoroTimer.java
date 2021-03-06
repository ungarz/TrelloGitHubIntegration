package Pomodoro;

import trellogitintegration.persist.config.ProjectConfig.PomodoroConfig;

/**
 * This class creates a Pomodoro Timer
 * 
 * Created: Oct 22, 2016
 * @author Conner Higashino
 *
 */

public class PomodoroTimer {
	
	/**
	 * Start a pomodoro timer that repeats.
	 * 
	 * @param config The PomodoroConfig that will generate the times for the pomodoro
	 *
	 */
	public PomodoroTimer(PomodoroConfig config) {		
		this.config = config;
		this.refreshPomodoroTimer();
	}
	
	public void refreshPomodoroTimer() {
		this.setPomodoroTime(this.config.getPomodoroTime());
		this.setPomodoros(this.config.getPomodoroCount());
		this.setBreakTime(this.config.getBreakTime());
		this.setLongBreakTime(this.config.getLongBreakTime());
		this.setLongBreakFreq(this.config.getLongBreakFreq());
	}

	public long getPomodoroTime() {
		return pomodoroTime;
	}
	public void setPomodoroTime(long pomodoroTime) {
		this.pomodoroTime = pomodoroTime;
	}
	public int getPomodoros() {
		return pomodoros;
	}
	public void setPomodoros(int pomodoros) {
		this.pomodoros = pomodoros;
	}
	public long getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(long breakTime) {
		this.breakTime = breakTime;
	}
	public long getLongBreakTime() {
		return longBreakTime;
	}
	public void setLongBreakTime(long longBreakTime) {
		this.longBreakTime = longBreakTime;
	}
	public long getLongBreakFreq() {
		return longBreakFreq;
	}
	public void setLongBreakFreq(long longBreakFreq) {
		this.longBreakFreq = longBreakFreq;
	}

	/* private variables */
	private long pomodoroTime;
	private int pomodoros;
	private long breakTime;
	private long longBreakTime;
	private long longBreakFreq;
	private PomodoroConfig config;
}