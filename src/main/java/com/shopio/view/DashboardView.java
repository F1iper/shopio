package com.shopio.view;

import com.shopio.product.service.ProductService;
import com.shopio.view.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Shopio")
public class DashboardView extends VerticalLayout {

    private final ProductService productService;

    public DashboardView(ProductService productService) {
        this.productService = productService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getStats(), getProductsChart());
    }

    private Component getStats() {
        Span stats = new Span(productService.countProducts() + " products");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }

    private Component getProductsChart() {
        return null;
    }
}
