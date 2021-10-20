package com.raos.fx.controls.skin;

import java.net.URL;
import java.util.ResourceBundle;

import com.raos.fx.controls.ControlTable;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class ControlTableSkin<E extends Control, V> extends FXRootSkinBase<ControlTable<E, V>, BorderPane> {
	@FXML
	private ListView<V> listView;
	private BooleanProperty initialized = new SimpleBooleanProperty(false);

	public ControlTableSkin(ControlTable<E, V> e, ReadOnlyListWrapper<V> properties) {
		super(e, ControlTable.class.getResource("/com/raos/fx/controls/fxml/ControlTable.fxml"), BorderPane::new);
		this.getSkinnable().skinProperty().addListener((obs, oldv, newv) -> {
			if (properties.isBound() && oldv instanceof ControlTableSkin) {
				properties.unbindBidirectional(listView.itemsProperty());
			}
			if (newv.equals(this)) {
				initialized.addListener(
						l -> Platform.runLater(() -> properties.bindBidirectional(listView.itemsProperty())));
			}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialized.set(true);
		listView.setCellFactory(view -> new ListCell<V>() {
			{
				setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			}

			protected void updateItem(V item, boolean empty) {
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					setGraphic(ControlTableSkin.this.getSkinnable().getItemFactory().call(item));
				}
			};
		});
	}

	@FXML
	public void handleRowSubtracted(ActionEvent event) {
		int index = listView.getSelectionModel().getSelectedIndex();
		if (index != -1) {
			listView.getItems().remove(index);
		}
	}

	@FXML
	public void handleRowAdded(ActionEvent event) {
		listView.getItems().add(null);
	}

	public ObjectProperty<ObservableList<V>> getRawList() {
		return listView.itemsProperty();
	}
}
