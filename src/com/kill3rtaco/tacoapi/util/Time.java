package com.kill3rtaco.tacoapi.util;

public class Time {
	
	private long	_y, _d, _h, _m, _s, _ms, _total;
	
	public Time(long milliseconds) {
		_total = milliseconds;
		long second = 1000;
		long minute = second * 60;
		long hour = minute * 60;
		long day = hour * 24;
		long year = day * 365;
		
		_y = milliseconds / year;
		_d = milliseconds % year / day;
		_h = milliseconds % year % day / hour;
		_m = milliseconds % year % day % hour / minute;
		_s = milliseconds % year % day % hour % minute / second;
		_ms = milliseconds % year % day % minute % second;
	}
	
	public long getYears() {
		return _y;
	}
	
	public long getDays() {
		return _d;
	}
	
	public long getTotalDays() {
		return _d + (_y * 365);
	}
	
	public long getHours() {
		return _h;
	}
	
	public long getTotalHours() {
		return _h + (getTotalDays() * 24);
	}
	
	public long getMinutes() {
		return _m;
	}
	
	public long getTotalMinutes() {
		return _m + (getTotalHours() * 60);
	}
	
	public long getSeconds() {
		return _s;
	}
	
	public long getTotalSeconds() {
		return _s + (getTotalMinutes() * 60);
	}
	
	public long getMilliseconds() {
		return _ms;
	}
	
	public long getTotalMilliseconds() {
		return _total;
	}
}
