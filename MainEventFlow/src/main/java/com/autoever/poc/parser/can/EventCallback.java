package com.autoever.poc.parser.can;

@FunctionalInterface
public interface EventCallback {
	public Object OnCalled(long time, boolean status);
}
