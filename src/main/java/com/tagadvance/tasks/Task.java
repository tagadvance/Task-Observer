package com.tagadvance.tasks;

import com.google.common.base.MoreObjects;

public class Task {

	private final int id;
	private final String name;

	private int parentId = -1;
	// TODO: additional properties

	public Task(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(getClass()).add("id", this.id).add("name", this.name)
				.add("parentId", this.parentId).toString();
	}

}
