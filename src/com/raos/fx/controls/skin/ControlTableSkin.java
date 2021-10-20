package com.raos.fx.controls.skin;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.raos.fx.controls.ControlTable;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

public class ControlTableSkin<E extends Control, V> extends FXRootSkinBase<ControlTable<E, V>, BorderPane> {
	@FXML
	private ListView<V> listView;
	@FXML
	private Label titleLabel;

	@SuppressWarnings("unchecked")
	public ControlTableSkin(ControlTable<E, V> e, ReadOnlyListWrapper<V> properties) {
		super(e, ControlTable.class.getResource("/com/raos/fx/controls/fxml/ControlTable.fxml"), BorderPane::new,
				Arrays.asList(new Pair<String, Object>("readOnlyProps", properties)).toArray(new Pair[1]));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ReadOnlyListWrapper<V> properties = (ReadOnlyListWrapper<V>) resources.getObject("readOnlyProps");
		this.getSkinnable().skinProperty().addListener((obs, oldv, newv) -> {
			if (properties.isBound() && oldv instanceof ControlTableSkin) {
				properties.unbindBidirectional(listView.itemsProperty());
				titleLabel.textProperty().unbindBidirectional(this.getSkinnable().titleProperty());
			}
			if (newv.equals(this)) {
				properties.bindBidirectional(listView.itemsProperty());
				titleLabel.textProperty().bindBidirectional(this.getSkinnable().titleProperty());
			}
		});
		listView.setCellFactory(view -> new ListCell<V>() {
			private E v;
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				v = getSkinnable().getControlGenerator().get();
			}

			protected void updateItem(V item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setGraphic(null);
				} else {
					getSkinnable().getControlImplementor().accept(v, item);
					setGraphic(v);
				}
			}
		});
		listView.getItems().addListener((ListChangeListener<V>) e -> listView.refresh());
	}

	@FXML
	public void handleRowSubtracted(ActionEvent event) {
		int index = listView.getSelectionModel().getSelectedIndex();
		System.out.println(index);
		if (index != -1) {
			listView.getItems().remove(index);
		}
	}

	@FXML
	public void handleRowAdded(ActionEvent event) {
		listView.getItems().add(this.getSkinnable().getItemFactory().get());
		System.out.println(listView.getItems());
	}

	public ObjectProperty<ObservableList<V>> getRawList() {
		return listView.itemsProperty();
	}
}
