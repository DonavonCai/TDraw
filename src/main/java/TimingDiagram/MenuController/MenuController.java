package TimingDiagram.MenuController;

import TimingDiagram.FXMLController;

abstract class MenuController {
    protected FXMLController parent;

    public void setParent(FXMLController p) {parent = p;}
}
