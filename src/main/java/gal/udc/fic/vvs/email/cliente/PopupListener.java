package gal.udc.fic.vvs.email.cliente;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

public class PopupListener extends MouseAdapter {

    public PopupListener(JPopupMenu popupMenu) {
        _popupMenu = popupMenu;
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            _popupMenu.show(e.getComponent(),
                            e.getX(), e.getY());
        }
    }

    private JPopupMenu _popupMenu;
}
