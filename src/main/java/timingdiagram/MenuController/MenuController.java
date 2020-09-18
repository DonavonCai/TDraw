package timingdiagram.MenuController;

import timingdiagram.FXMLController;

abstract class MenuController {
    protected FXMLController parent;

    public void setParent(FXMLController p) {parent = p;}
}
