package com.raos.fx.controls.models;

import java.io.Serializable;

/**
 * Allowes Database support
 * @author Raos
 *
 */
public class Model {
	private Serializable id;

	/**
	 * @return the unique id
	 */
	public final Serializable getId() {
		return id;
	}

	public final void persist(Serializable id) {
		this.id = id;
	}
}
