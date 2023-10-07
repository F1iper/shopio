package com.shopio.view.dialog;

import com.shopio.product.entity.Product;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;

public class ProductDetailDialog extends Dialog {

    public ProductDetailDialog(Product product){
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("400px");

        TextField nameField = new TextField("Name");
        nameField.setValue(product.getName());
        nameField.setReadOnly(true);

        TextField descriptionField = new TextField("Description");
        descriptionField.setValue(product.getDescription());
        descriptionField.setReadOnly(true);

        TextField priceField = new TextField("Price");
        priceField.setValue(String.valueOf(product.getPrice()));
        priceField.setReadOnly(true);

        TextField inventoryField = new TextField("In stock");
        inventoryField.setValue(String.valueOf(product.getAmount()));
        inventoryField.setReadOnly(true);

        add(nameField, descriptionField, priceField, inventoryField);
    }
}