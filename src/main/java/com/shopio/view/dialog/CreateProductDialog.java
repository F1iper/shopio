package com.shopio.view.dialog;

import com.shopio.product.entity.Category;
import com.shopio.product.entity.Product;
import com.shopio.product.service.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CreateProductDialog extends Dialog {
    private final ProductService productService;
    private final TextField nameField;
    private final TextField descriptionField;
    private final TextField priceField;
    private final TextField inventoryField;
    private final ComboBox<Category> categoryComboBox;
    private Button saveButton;
    private Button cancelButton;

    public CreateProductDialog(ProductService productService){
        this.productService = productService;
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);

        FormLayout productForm = new FormLayout();
        nameField = new TextField("Product name");
        descriptionField = new TextField("Description");
        priceField = new TextField("Price");
        inventoryField = new TextField("Amount");

        categoryComboBox = new ComboBox<>("Category");
        categoryComboBox.setItems(Category.values());
        categoryComboBox.setItemLabelGenerator(Category::name);

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

        productForm.add(nameField, descriptionField, priceField, inventoryField, categoryComboBox);

        VerticalLayout container = new VerticalLayout(productForm, buttonLayout);

        saveButton.addClickListener(e -> {
            saveProduct();
            close();
        });
        cancelButton.addClickListener(e -> {
            close();
        });

        add(container);
    }

    private void saveProduct(){
        Product product = new Product();
        product.setName(nameField.getValue().toLowerCase());
        product.setDescription(descriptionField.getValue().toLowerCase());
        product.setPrice(Double.parseDouble(priceField.getValue()));
        product.setInventory(Integer.parseInt(inventoryField.getValue()));
        product.setCategory(categoryComboBox.getValue());

        Product savedProduct = productService.createProduct(product);

        if (savedProduct != null) {
            Notification.show("Product saved successfully!", 3000, Notification.Position.BOTTOM_START);
            close();
        } else {
            Notification.show("Failed to save product!", 3000, Notification.Position.BOTTOM_START);
            close();
        }
    }
}