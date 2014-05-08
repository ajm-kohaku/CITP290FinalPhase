package murph32.controller;

import murph32.model.InventoryModel;
import murph32.view.InventoryView;

/**
 * Created by Dukat on 5/7/2014.
 */
public class InventoryController {
    private InventoryView view;
    private InventoryModel model = new InventoryModel();

    public InventoryController(InventoryView view) {
        this.view = view;
        //view.displayProducts(model.getProducts());
    }
}
