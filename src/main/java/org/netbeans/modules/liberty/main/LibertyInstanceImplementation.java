package org.netbeans.modules.liberty.main;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.server.ServerInstance;
import org.netbeans.spi.server.ServerInstanceImplementation;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;

/**
 * Visual representation of an instance in the Services window
 * @author gwieleng & sknitelius
 */
public final class LibertyInstanceImplementation implements ServerInstanceImplementation {

    private final LibertyInstanceProvider provider;
    private final String serverName;
    private final String instanceName;
    private final boolean removable;
    private ServerInstance serverInstance;
    private LibertyInstanceManagerPanel customizer;
    private final ServerInfo serverInfo;
    private final ServerUtils serverUtils = new ServerUtils();

    @StaticResource
    private static final String ICON = "org/netbeans/modules/liberty/main/logo.png";

    public LibertyInstanceImplementation(LibertyInstanceProvider provider, String serverName, String instanceName, String runtimeLocation, boolean removable) {
        this.provider = provider;
        this.serverName = serverName;
        this.instanceName = instanceName;
        this.removable = removable;

        File userDir = new File(runtimeLocation + "\\usr");
        this.serverInfo = new ServerInfo(userDir, null, serverName, runtimeLocation, new File(System.getProperty("java.home")), 7777);
    }

    @Override
    public Node getFullNode() {
        return new AbstractNode(Children.LEAF) {
            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(ICON);
            }

            @Override
            public String getDisplayName() {
                return instanceName;
            }

            @Override
            public Action[] getActions(boolean context) {
                List<? extends Action> libertyInstanceActions = Utilities.actionsForPath("Servers/Liberty/Actions");
                return libertyInstanceActions.toArray(new Action[libertyInstanceActions.size()]);
                
//                return new Action[]{
//                    new AbstractAction("Start") {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            run();
//                        }
//                    },
//                    new AbstractAction("Start in Debug Mode") {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            debug();
//                        }
//                    },
//                    new AbstractAction("Start in Profile Mode") {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            throw new UnsupportedOperationException();
//                        }
//                    },
//                    new AbstractAction("Restart") {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            throw new UnsupportedOperationException();
//                        }
//                    },
//                    new AbstractAction("Stop") {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            stop();
//                        }
//                    },
//                    new AbstractAction("Refresh") {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            throw new UnsupportedOperationException();
//                        }
//                    },
//                    new AbstractAction("Remove") {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            remove();
//                        }
//                    }
//                };
            }
        };
    }

    @Override
    public Node getBasicNode() {
        return new AbstractNode(Children.LEAF) {
            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(ICON);
            }

            @Override
            public String getDisplayName() {
                return instanceName;
            }
        };
    }

    @Override
    public JComponent getCustomizer() {
        synchronized (this) {
            if (customizer == null) {
                customizer = new LibertyInstanceManagerPanel();
            }
            return customizer;
        }
    }

    @Override
    public String getDisplayName() {
        return instanceName;
    }

    @Override
    public String getServerDisplayName() {
        return serverName;
    }

    @Override
    public boolean isRemovable() {
        return removable;
    }

    @Override
    public void remove() {
        this.provider.getInstances().remove(serverInstance);
    }

    public void run() {
        serverUtils.startServer(serverInfo, ServerUtils.ServerMode.RUN);
    }

    public void debug() {
        serverUtils.startServer(serverInfo, ServerUtils.ServerMode.DEBUG);
    }

    public void stop() {
        serverUtils.stopServer(serverInfo);
    }

}
