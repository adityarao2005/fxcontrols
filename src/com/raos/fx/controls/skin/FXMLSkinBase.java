/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raos.fx.controls.skin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SkinBase;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.util.Callback;

/**
 *
 * @author Raos
 * @param <E>
 */
public abstract class FXMLSkinBase<E extends Control> extends SkinBase<E> implements Initializable {
	
	protected FXMLSkinBase(Factory<E> factory) {
		super(factory.e);
	}
	
	public static <F extends Control, E extends FXMLSkinBase<F>> E create(F f, URL fxml) {
		Factory<F> factory = new Factory<>(f);
		FXMLLoader fxmlLoader = new FXMLLoader(fxml);
		fxmlLoader.setControllerFactory(factory);
		try {
			Node root = fxmlLoader.load();
			E skin = fxmlLoader.getController();
			skin.construct(root);
			return skin;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	final void construct(Node root) {
		this.getChildren().add(root);
	}
	
	protected static final class Factory<E> implements Callback<Class<?>, Object> {
		private E e;

		public Factory(E e) {
			this.e = e;
		}
		
		@Override
		public Object call(Class<?> param) {
			try {
				return param.getDeclaredConstructor(Factory.class).newInstance(this);
			} catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
}
