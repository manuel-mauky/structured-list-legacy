package eu.lestard.structuredlist.model;

import javax.inject.Singleton;

@Singleton
public class ItemsModel {

    private Item root = new Item("root");

    public Item getRoot(){
        return root;
    }
}
