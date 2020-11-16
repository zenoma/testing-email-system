package gal.udc.fic.vvs.email.cliente;

import gal.udc.fic.vvs.email.correo.Correo;
import gal.udc.fic.vvs.email.correo.OperacionInvalida;
import gal.udc.fic.vvs.email.archivador.Archivador;

import java.util.*;
import java.net.URL;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*; 
import javax.swing.border.*;
import javax.swing.event.*;

public class ClienteImp extends JFrame implements Cliente {

    public ClienteImp(Correo correo) {
        super("Sistema de correo electrónico");
        _correo = correo;
        _archivadores = new Vector();
        // Inicializar UI
        GridBagConstraints gridBagConstraints;
        JPanel panel = new JPanel(new GridBagLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        // Tabs
        _panelTabbed = new JTabbedPane();
        _panelTabbed.addTab("Explora", panelExplorador());
        _panelTabbed.addTab("Procura", panelBuscador());
        _panelTabbed.addTab("Arquiva", panelArchivadores());
        _panelTabbed.setSelectedIndex(0);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(_panelTabbed, gridBagConstraints);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent event) {
		    exitApp(event);
		}
	    });
        explorarRaiz();
        pack();
	setVisible(true);
    }

    public void agregarArchivador(Archivador archivador) {
        _archivadores.addElement(archivador);
        _editorArchivadorOrigen.addItem(new ArchivadorItem(archivador, null));
        _editorArchivadorDelegado.addItem(new ArchivadorItem(archivador, null));
        _editorArchivador.addItem(new ArchivadorItem(archivador, null));
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 500);
    }

    private JPanel panelExplorador() {
        GridBagConstraints gridBagConstraints;
        JPanel panel = new JPanel(new GridBagLayout());
        //
        // Explorador
        //
        JPanel panelExplorador = new JPanel(new GridBagLayout());
        panelExplorador.setBorder(new TitledBorder("Correo"));
        // Raiz
        JButton buttonRaizExplorador = new JButton();
        buttonRaizExplorador.setAction(new ActionRaizExplorador());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 10);
        panelExplorador.add(buttonRaizExplorador, gridBagConstraints);
        // Archivador
        JLabel labelArchivador = new JLabel("Arquivador");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 0, 0, 5);
        panelExplorador.add(labelArchivador, gridBagConstraints);
        _editorArchivador = new JComboBox();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 0, 0, 40);
        panelExplorador.add(_editorArchivador, gridBagConstraints);
        // Correos
        _editorListaCorreos = new JList(new DefaultListModel());
        _editorListaCorreos.setCellRenderer(new CorreoRenderer());
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    Correo correo = obtenerCorreoExploradorSeleccionado();
                    if (correo == null) {
                        _editorTextoVisualizacionExplorador.setText("");
                        _editorRutaExplorador.setText("Ruta: -");
                    } else {
                        _editorTextoVisualizacionExplorador.setText(correo.obtenerVisualizacion());
                        _editorRutaExplorador.setText("Ruta: " + correo.obtenerRuta());
                    }
                }
            }
        };
        _editorListaCorreos.addListSelectionListener(listSelectionListener);
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = _editorListaCorreos.locationToIndex(e.getPoint());
                    ListModel dlm = _editorListaCorreos.getModel();
                    Object item = dlm.getElementAt(index);;
                    _editorListaCorreos.ensureIndexIsVisible(index);
                    if (item != null) {
                        Correo correo = ((CorreoItem)item).getCorreo();
                        _editorRutaExplorador.setText("Ruta: -");
                        explorarCorreo(correo);
                    }

                }
            }
        };
        _editorListaCorreos.addMouseListener(mouseListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.gridwidth = 3;
        panelExplorador.add(new JScrollPane(_editorListaCorreos), gridBagConstraints);
        // Menu
        _menuExplorador = new JPopupMenu("Correo");
        JMenuItem item;
        item = new JMenuItem();
        item.setAction(new ActionCambiarMarcaDeLectura(true));
        _menuExplorador.add(item);
        item = new JMenuItem();
        item.setAction(new ActionCambiarMarcaDeLectura(false));
        _menuExplorador.add(item);
        item = new JMenuItem();
        item.setAction(new ActionArchivar());
        _menuExplorador.add(item);
        _editorListaCorreos.addMouseListener(new PopupListener(_menuExplorador));
        // Done
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(panelExplorador, gridBagConstraints);
        //
        // Visualizacion
        //
        JPanel panelVisualizacionExplorador = new JPanel(new GridBagLayout());
        panelVisualizacionExplorador.setBorder(new TitledBorder("Contido"));
        // Ruta
        _editorRutaExplorador = new JLabel("Ruta: -");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panelVisualizacionExplorador.add(_editorRutaExplorador, gridBagConstraints);
        // Texto
        _editorTextoVisualizacionExplorador = new JTextArea();
        _editorTextoVisualizacionExplorador.setEditable(false);
        _editorTextoVisualizacionExplorador.setColumns(25);
        _editorTextoVisualizacionExplorador.setRows(7);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panelVisualizacionExplorador.add(new JScrollPane(_editorTextoVisualizacionExplorador), gridBagConstraints);
        // Done
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panel.add(panelVisualizacionExplorador, gridBagConstraints);
        return panel;
    }

    private JPanel panelBuscador() {
        GridBagConstraints gridBagConstraints;
        JPanel panel = new JPanel(new GridBagLayout());
        //
        // Buscador
        //
        JPanel panelBuscador = new JPanel(new GridBagLayout());
        panelBuscador.setBorder(new TitledBorder("Procura"));
        // Subcadena
        _editorSubcadenaBusqueda = new JTextField();
        _editorSubcadenaBusqueda.setColumns(20);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        panelBuscador.add(_editorSubcadenaBusqueda, gridBagConstraints);
        // Buscar
        JButton buttonBuscar = new JButton();
        buttonBuscar.setAction(new ActionBuscar());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        panelBuscador.add(buttonBuscar, gridBagConstraints);
        // Correos
        _editorListaCorreosEncontrados = new JList(new DefaultListModel());
        _editorListaCorreosEncontrados.setCellRenderer(new CorreoRenderer());
        ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    Correo correo = obtenerCorreoBuscadorSeleccionado();
                    if (correo == null) {
                        _editorTextoVisualizacionBuscador.setText("");
                        _editorRutaBuscador.setText("Ruta: -");
                    } else {
                        _editorTextoVisualizacionBuscador.setText(correo.obtenerVisualizacion());
                        _editorRutaBuscador.setText("Ruta: " + correo.obtenerRuta());
                    }
                }
            }
        };
        _editorListaCorreosEncontrados.addListSelectionListener(listSelectionListener);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        gridBagConstraints.gridwidth = 2;
        panelBuscador.add(new JScrollPane(_editorListaCorreosEncontrados), gridBagConstraints);
        // Done
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTH;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 0.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(panelBuscador, gridBagConstraints);
        //
        // Visualizacion
        //
        JPanel panelVisualizacionBuscador = new JPanel(new GridBagLayout());
        panelVisualizacionBuscador.setBorder(new TitledBorder("Contido"));
        // Ruta
        _editorRutaBuscador = new JLabel("Ruta: -");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panelVisualizacionBuscador.add(_editorRutaBuscador, gridBagConstraints);
        // Texto
        _editorTextoVisualizacionBuscador = new JTextArea();
        _editorTextoVisualizacionBuscador.setEditable(false);
        _editorTextoVisualizacionBuscador.setColumns(25);
        _editorTextoVisualizacionBuscador.setRows(7);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panelVisualizacionBuscador.add(new JScrollPane(_editorTextoVisualizacionBuscador), gridBagConstraints);
        // Done
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panel.add(panelVisualizacionBuscador, gridBagConstraints);
        return panel;
    }

    private JPanel panelArchivadores() {
        GridBagConstraints gridBagConstraints;
        JPanel panel = new JPanel(new GridBagLayout());
        //
        // Archivadores
        //
        JPanel panelArchivadores = new JPanel(new GridBagLayout());
        panelArchivadores.setBorder(new TitledBorder("Arquivadores"));
        // Origen
        JLabel labelArchivadorOrigen = new JLabel("Arquivador");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelArchivadores.add(labelArchivadorOrigen, gridBagConstraints);
        _editorArchivadorOrigen = new JComboBox();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 0, 5);
        panelArchivadores.add(_editorArchivadorOrigen, gridBagConstraints);
        // Delegado
        JLabel labelArchivadorDestino = new JLabel("Delegado");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panelArchivadores.add(labelArchivadorDestino, gridBagConstraints);
        _editorArchivadorDelegado = new JComboBox();
        _editorArchivadorDelegado.addItem(new ArchivadorItem(null, null));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 0, 5);
        panelArchivadores.add(_editorArchivadorDelegado, gridBagConstraints);
        // Establecer
        JButton buttonEstablecerArchivadorDelegado = new JButton();
        _actionEstablecerArchivadorDelegado = new ActionEstablecerArchivadorDelegado();
        buttonEstablecerArchivadorDelegado.setAction(_actionEstablecerArchivadorDelegado);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panelArchivadores.add(buttonEstablecerArchivadorDelegado, gridBagConstraints);
        // Done
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panel.add(panelArchivadores, gridBagConstraints);
        //
        // Estado
        //
        JPanel panelEstado = new JPanel(new GridBagLayout());
        panelEstado.setBorder(new TitledBorder("Estado cadea"));
        // TextoEstado
        _editorTextoEstadoCadena = new JTextArea();
        _editorTextoEstadoCadena.setEditable(false);
        _editorTextoEstadoCadena.setColumns(25);
        _editorTextoEstadoCadena.setRows(7);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelEstado.add(new JScrollPane(_editorTextoEstadoCadena), gridBagConstraints);
        // Consultar estado
        JButton buttonConsultarEstado = new JButton();
        _actionConsultarEstadoCadena = new ActionConsultarEstadoCadena();
        buttonConsultarEstado.setAction(_actionConsultarEstadoCadena);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        panelEstado.add(buttonConsultarEstado, gridBagConstraints);
        // Done
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panel.add(panelEstado, gridBagConstraints);
        return panel;
    }

    private Correo obtenerCorreoExploradorSeleccionado() {
        Object value = _editorListaCorreos.getSelectedValue();
        if (value != null) {
            return ((CorreoItem)value).getCorreo();
        }
        return null;
    }

    private Correo obtenerCorreoBuscadorSeleccionado() {
        Object value = _editorListaCorreosEncontrados.getSelectedValue();
        if (value != null) {
            return ((CorreoItem)value).getCorreo();
        }
        return null;
    }

    private void explorarRaiz() {
        _editorRutaExplorador.setText("Ruta: " + _correo.obtenerRuta());
        Collection correos = new Vector();
        correos.add(_correo);
        actualizarExplorador(correos);
    }

    private void explorarCorreo(Correo correo) {
        try {
            _editorRutaExplorador.setText("Ruta: " + correo.obtenerRuta());
            actualizarExplorador(correo.explorar());
        } catch (OperacionInvalida e) {
            JOptionPane.showMessageDialog(ClienteImp.this, "Correo non explorable", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarExplorador(Collection correos) {
        DefaultListModel model = (DefaultListModel)_editorListaCorreos.getModel();
        model.clear();
        Iterator iCorreos = correos.iterator();
        while(iCorreos.hasNext()) {
            model.addElement(new CorreoItem((Correo)iCorreos.next()));
        }
    }

    private void exitApp(WindowEvent evt) {
        dispose();
        System.exit(0);
    }

    private class ArchivadorItem {
        public ArchivadorItem(Archivador archivador, Object info) {
            _archivador = archivador;
            _info = info;
        }
        public Archivador getArchivador() {
            return _archivador;
        }
        public Object getInfo() {
            return _info;
        }
        public String toString() {
            if (_archivador != null) {
                return _archivador.obtenerNombre();
            } else {
                return "---";
            }
        }
        private Archivador _archivador;
        private Object _info;
    }

    private class CorreoItem {
        public CorreoItem(Correo correo) {
            _correo = correo;
        }
        public Correo getCorreo() {
            return _correo;
        }
        public String toString() {
            if (_correo != null) {
                return _correo.obtenerPreVisualizacion() + " [" + _correo.obtenerTamaño() + " bytes]";
            } else {
                return "---";
            }
        }
        private Correo _correo;
    }

    private class CorreoRenderer extends DefaultListCellRenderer {
        public CorreoRenderer() {
            _iconos = new Hashtable();
            _iconos.put(Correo.ICONO_CARPETA, new ImageIcon(getClass().getClassLoader().getResource("carpeta.png")));
            _iconos.put(Correo.ICONO_MENSAJE, new ImageIcon(getClass().getClassLoader().getResource("mensaje.png")));
            _iconos.put(Correo.ICONO_NUEVO_MENSAJE, new ImageIcon(getClass().getClassLoader().getResource("nuevo_mensaje.png")));
			_iconos.put(Correo.ICONO_MENSAJE_CON_ADJUNTOS, new ImageIcon(getClass().getClassLoader().getResource("mensaje_con_adjuntos.png")));
			_iconos.put(Correo.ICONO_NUEVO_MENSAJE_CON_ADJUNTOS, new ImageIcon(getClass().getClassLoader().getResource("nuevo_mensaje_con_adjuntos.png")));
        }
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
            if (value instanceof CorreoItem) {
                CorreoItem correoItem = (CorreoItem)value;
                label.setIcon((ImageIcon)_iconos.get(correoItem.getCorreo().obtenerIcono()));
            } else {
                // Clear old icon
                label.setIcon(null);
            }
            return(label);
        }
        private Hashtable _iconos;
    }

    private class ActionRaizExplorador extends AbstractAction {
        public ActionRaizExplorador() {
            putValue(Action.NAME, "Raíz");
        }
        public void actionPerformed(ActionEvent event) {
            explorarRaiz();
        }
    }

    private class ActionArchivar extends AbstractAction {
        public ActionArchivar() {
            putValue(Action.NAME, "Arquivar");
        }
        public void actionPerformed(ActionEvent event) {
            Correo correo = obtenerCorreoExploradorSeleccionado();
            if (correo != null) {
                Archivador archivador = ((ArchivadorItem)_editorArchivador.getSelectedItem()).getArchivador();
                if (!archivador.almacenarCorreo(correo)) {
                    JOptionPane.showMessageDialog(ClienteImp.this, "Non foi posible arquivar o correo", null, JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class ActionCambiarMarcaDeLectura extends AbstractAction {
        public ActionCambiarMarcaDeLectura(boolean marca) {
            _marca = marca;
            if (_marca) {
                putValue(Action.NAME, "Marcar como lido");
            } else {
                putValue(Action.NAME, "Marcar como non lido");
            }
        }
        public void actionPerformed(ActionEvent event) {
            Correo correo = obtenerCorreoExploradorSeleccionado();
            if (correo != null) {
                correo.establecerLeido(_marca);
            }
        }
        private boolean _marca;
    }

    private class ActionBuscar extends AbstractAction {
        public ActionBuscar() {
            putValue(Action.NAME, "Procurar");
        }
        public void actionPerformed(ActionEvent event) {
            Collection correos = _correo.buscar(_editorSubcadenaBusqueda.getText());
            DefaultListModel model = (DefaultListModel)_editorListaCorreosEncontrados.getModel();
            model.clear();
            Iterator iCorreos = correos.iterator();
            while(iCorreos.hasNext()) {
                model.addElement(new CorreoItem((Correo)iCorreos.next()));
            }
        }
    }

    private class ActionEstablecerArchivadorDelegado extends AbstractAction {
        public ActionEstablecerArchivadorDelegado() {
            putValue(Action.NAME, "Establecer");
        }
	public void actionPerformed(ActionEvent event) {
            Archivador a1 = ((ArchivadorItem)_editorArchivadorOrigen.getSelectedItem()).getArchivador();
            Archivador a2 = ((ArchivadorItem)_editorArchivadorDelegado.getSelectedItem()).getArchivador();
            if (a1 != null) {
                a1.establecerDelegado(a2);
            }
        }
    }

    private class ActionConsultarEstadoCadena extends AbstractAction {
        public ActionConsultarEstadoCadena() {
            putValue(Action.NAME, "Consultar estado cadea");
        }
        public void actionPerformed(ActionEvent event) {
            _editorTextoEstadoCadena.setText("");
            Iterator iArchivadores = _archivadores.iterator();
            while(iArchivadores.hasNext()) {
                Archivador a = (Archivador)iArchivadores.next();
                Archivador suc = a.obtenerDelegado();
                _editorTextoEstadoCadena.append(a.obtenerNombre() + " (" + a.obtenerEspacioDisponible() + "/" + a.obtenerEspacioTotal() + ") ->" +
                                                (suc==null ? " -" : suc.obtenerNombre() + "(" + suc.obtenerEspacioDisponible() + "/" + suc.obtenerEspacioTotal() + ")"));
                _editorTextoEstadoCadena.append("\n");
            }
        }
    }

    private Correo _correo;
    private Vector _archivadores;
    private JTabbedPane _panelTabbed;
    private JList _editorListaCorreos, _editorListaCorreosEncontrados;
    private JLabel _editorRutaExplorador, _editorRutaBuscador;
    private JPopupMenu _menuExplorador;
    private JTextArea _editorTextoVisualizacionExplorador, _editorTextoVisualizacionBuscador, _editorTextoEstadoCadena;
    private JTextField _editorSubcadenaBusqueda;
    private JComboBox _editorArchivadorOrigen, _editorArchivadorDelegado, _editorArchivador;
    private ActionEstablecerArchivadorDelegado _actionEstablecerArchivadorDelegado;
    private ActionConsultarEstadoCadena _actionConsultarEstadoCadena;

}
