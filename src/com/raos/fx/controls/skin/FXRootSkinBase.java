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
 * @author Raos
 * @param <E>
 * @param <ROOT>
 */
public abstract class FXRootSkinBase<E extends Control, ROOT extends Node> extends SkinBase<E> implements Initializable {

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
