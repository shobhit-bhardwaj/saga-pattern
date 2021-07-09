package com.shobhit.process;

public interface WorkFlowStep<T> {
	public T process();
	public T revert();
}