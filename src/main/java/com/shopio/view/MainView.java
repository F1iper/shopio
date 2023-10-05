package com.shopio.view;

import com.shopio.product.entity.Product;
import com.shopio.product.service.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("")
@PageTitle("Main View")
@RolesAllowed("ADMIN")
final class MainView extends VerticalLayout {

    private final ProductService productService;
    private LogoLayout logoLayout;
    private TextField filterField;
    private Grid<Product> grid;

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
    }

    private void configureLayout(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        add(logoLayout, createToolbar(), grid);
    }

    private Grid<Product> createProductGrid(){
        grid = new Grid<>(Product.class);
        grid.setSizeFull();
        grid.setColumns("name", "description", "price", "amount");
        grid.addComponentColumn(product -> createInventoryStatusIcon(product.getAmount()));
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        return grid;
    }

    private Component createToolbar(){
        filterField.setPlaceholder("Filter by name...");
        filterField.setClearButtonVisible(true);
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.addValueChangeListener(e -> updateProducts());

        Button addButton = new Button("Add product");
        Button removeButton = new Button("Remove product(s)");

        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        addButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("/product/create"));
        });
        removeButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("/product/remove"));
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
