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
 * Skin base for all controls adapting FXML without an fx:root
 * @author Raos
 * @param <E> - The control that should be returned from the FXML file
 */
public abstract class FXMLSkinBase<E extends Control> extends SkinBase<E> implements Initializable {
	
	/**
	 * Abstract constructor from a controller factory
	 * @param factory
	 */
	protected FXMLSkinBase(Factory<E> factory) {
		super(factory.e);
	}
	
	/**
	 * Creates the the FXML Skin from the Control and URL of .fxml
	 * @param <F> - the Control class
	 * @param <E> - the Skin base class
	 * @param f - the Control
	 * @param fxml - the URL of the fxml file
	 * @return
	 */
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
	
	/**
	 * constructing the skin
	 * @param root - the root node of fxml
	 */
	final void construct(Node root) {
		this.getChildren().add(root);
	}
	
	/**
	 * 
	 * @author Raos
	 *
	 * @param <E> - the control class
	 */
	public static final class Factory<E> implements Callback<Class<?>, Object> {
		private E e;

		public Factory(E e) {
			this.e = e;
		}
		
		@Override
		public Object call(Class<?> param) {
			try {
				// use reflection to get the class
				return param.getDeclaredConstructor(Factory.class).newInstance(this);
			} catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
				throw new RuntimeException(ex);
			}
		}
		
	}
}
