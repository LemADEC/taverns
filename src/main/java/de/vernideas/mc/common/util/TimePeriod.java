package de.vernideas.mc.common.util;

public final class TimePeriod {
	public static final TimePeriod fullDay = new TimePeriod(0, 24000);
	
	public final int startTick;
	public final int endTick;
	public final double startTime;
	public final double endTime;
	
	public TimePeriod(int sTick, int eTick) {
		sTick = Math.min(Math.max(sTick, 0), 24000);
		eTick = Math.min(Math.max(eTick, 0), 24000);
		if (sTick <= eTick) {
			this.startTick = sTick;
			this.endTick = eTick;
		} else {
			this.startTick = eTick;
			this.endTick = sTick;
		}
		this.startTime = (this.startTick / 24000.0D);
		this.endTime = (this.endTick / 24000.0D);
	}
	
	public TimePeriod(double start, double end) {
		this((int) (start * 24000.0D), (int) (end * 24000.0D));
	}
	
	public TimePeriod(TimePeriod other) {
		if (null != other) {
			this.startTick = other.startTick;
			this.endTick = other.endTick;
			this.startTime = other.startTime;
			this.endTime = other.endTime;
		} else {
			this.startTick = 0;
			this.endTick = 24000;
			this.startTime = 0.0D;
			this.endTime = 1.0D;
		}
	}
	
	public boolean overlaps(TimePeriod other) {
		if (null != other) {
			return (this.startTick < other.endTick) && (this.endTick > other.startTick);
		}
		
		return false;
	}
	
	public boolean contains(int time) {
		return (time >= this.startTick) && (time <= this.endTick);
	}
	
	public boolean contains(double time) {
		return (time >= this.startTime) && (time <= this.endTime);
	}
	
	public static TimePeriod merge(TimePeriod one, TimePeriod two) {
		if ((null != one) && (null != two) && (one.overlaps(two))) {
			return new TimePeriod(Math.min(one.startTick, two.startTick), Math.max(one.endTick, two.endTick));
		}
		
		return null;
	}
}
