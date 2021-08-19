package Controller.MenuController;

import Controller.TDrawController;

abstract class MenuController {
    protected TDrawController parent;

    public void setParent(TDrawController p) {parent = p;}
}
