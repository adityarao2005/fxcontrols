/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raos.fx.controls.skin;

import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SkinBase;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Control;

/**
 *
 * Skin base for all controls adapting FXML with an fx:root
 * @author Raos
 * @param <E> - the control class that this is the skin for
 * @param <ROOT> - the root node class
 */
public abstract class FXRootSkinBase<E extends Control, ROOT extends Node> extends SkinBase<E> implements Initializable {

	/**
	 * Constructs the FXRootSkinBase class
	 * @param e - the control that the skin is for
	 * @param fxml - the location of the fxml file
	 * @param rootSupplier - the supplier of the root node object
	 */
	public FXRootSkinBase(E e, URL fxml, Supplier<ROOT> rootSupplier) {
		super(e);
		FXMLLoader loader = new FXMLLoader(fxml);
		loader.setController(this);
		loader.setRoot(rootSupplier.get());
		try {
			this.getChildren().add(loader.<ROOT>load());
		} catch (IOException ex) {
			Logger.getLogger(FXRootSkinBase.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
