package com.shopio.view;

import com.shopio.product.entity.Product;
import com.shopio.product.service.ProductService;
import com.shopio.view.dialog.CreateProductDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Set;

@Route("")
@PageTitle("Main View")
@PermitAll
public class MainView extends VerticalLayout {

    private final ProductService productService;
    private LogoLayout logoLayout;
    private TextField filterField;
    private Grid<Product> grid;
    private CreateProductDialog createProductDialog;

    public MainView(ProductService productService){
        this.productService = productService;
        initializeComponents();
        configureLayout();
        loadDataFromDb();
    }

    private void initializeComponents(){
        logoLayout = new LogoLayout();
        filterField = new TextField();
        grid = createProductGrid();
        createProductDialog = new CreateProductDialog(productService);
    }

    private void configureLayout(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        add(logoLayout, createToolbar(), grid);
    }

    private Grid<Product> createProductGrid(){
        grid = new Grid<>(Product.class);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setSizeFull();
        grid.setColumns("name", "description", "price", "inventory");
        grid.addComponentColumn(product -> createInventoryStatusIcon(product.getInventory()));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        return grid;
    }

    private Component createToolbar(){
        filterField.setPlaceholder("Filter by name...");
        filterField.setClearButtonVisible(true);
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.addValueChangeListener(e -> updateProducts());

        Button addButton = new Button("Add product");
        Button removeButton = new Button("Remove product");

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        addButton.addClickListener(e -> {
            createProductDialog.open();
            // TODO: 9/30/23 implement refreshing the list of items after CREATE PRODUCT
            grid.setItems(productService.getAllProducts());
        });
        removeButton.addClickListener(e -> {
            Notification.show("Product removed", 3000, Notification.Position.BOTTOM_START);
            Set<Product> selectedItems = grid.getSelectedItems();
            productService.deleteProducts(selectedItems);
            grid.setItems(productService.getAllProducts());
        });
        return new HorizontalLayout(filterField, addButton, removeButton);
    }

    private Icon createInventoryStatusIcon(int inventory){
        Icon icon;
        if (inventory == 0) {
            icon = VaadinIcon.CLOSE_CIRCLE_O.create();
            icon.setColor("DarkRed");
            icon.getStyle().set("opacity", "0.7");
        } else if (inventory > 0 && inventory < 10) {
            icon = VaadinIcon.CHECK_CIRCLE_O.create();
            icon.setColor("DarkOrange");
            icon.getStyle().set("opacity", "0.7");
        } else {
            icon = VaadinIcon.CHECK_CIRCLE_O.create();
            icon.setColor("DarkGreen");
            icon.getStyle().set("opacity", "0.7");
        }
        return icon;
    }

    private void updateProducts(){
        grid.setItems(productService.findByName(filterField.getValue()));
    }

    private void loadDataFromDb(){
        grid.setItems(productService.getAllProducts());
    }
}
