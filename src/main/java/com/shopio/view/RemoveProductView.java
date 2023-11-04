package com.shopio.view;

import com.shopio.product.entity.Product;
import com.shopio.product.service.ProductService;
import com.shopio.view.dialog.ProductDetailDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Set;

@PageTitle("Remove product")
@Route("product/remove")
@PermitAll
final class RemoveProductView extends VerticalLayout {

    private final ProductService productService;
    private LogoView logoView;
    private Grid<Product> grid;
    private Set<Product> selected;
    private Button remove;
    private Button cancel;

    RemoveProductView(ProductService productService){
        this.productService = productService;
        initializeComponents();
        configureLayout();
        loadDataFromDb();
    }

    private void handleRemoveButtonClick(){
        selected = grid.getSelectedItems();
        if (selected.isEmpty()) {
            Notification.show("Select at least one product to remove.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            productService.deleteProducts(selected);
            grid.setItems(productService.getAllProducts());
            Notification.show("Product(s) removed successfully.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS, NotificationVariant.LUMO_PRIMARY);
        }
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

    private Grid<Product> createProductGrid(){
        grid = new Grid<>(Product.class);
        grid.setSizeFull();
        grid.setColumns("name", "price");
        grid.addComponentColumn(product -> createInventoryStatusIcon(product.getAmount())).setHeader("Inventory Status");

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                openProductDetailDialog(event.getValue());
            }
        });
        return grid;
    }

    private void openProductDetailDialog(Product product){
        ProductDetailDialog dialog = new ProductDetailDialog(product);
        dialog.open();
    }

    private void initializeComponents(){
        logoView = new LogoView();
        grid = createProductGrid();
        remove = new Button("Remove");
        cancel = new Button("Cancel");

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void configureLayout(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        add(logoView, createToolbar(), grid);
    }

    private Component createToolbar(){
        remove.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        remove.addClickListener(e -> handleRemoveButtonClick());

        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        cancel.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));

        return new HorizontalLayout(remove, cancel);
    }

    private void loadDataFromDb(){
        grid.setItems(productService.getAllProducts());
    }

}
