/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.infobar;

import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;
import be.naturalsciences.bmdc.ears.rest.RestClientNav;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ontology.EarsException;
import be.naturalsciences.bmdc.ontology.writer.StringUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.net.ConnectException;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.openide.awt.NotificationDisplayer;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author Yvan Stojanov
 */
public final class InfoBar implements LookupListener {

    /**
     *
     * @param le
     */
    @Override
    public void resultChanged(LookupEvent le) {
        if (le.getSource().equals(messageResult)) {
            Collection allInstances = messageResult.allInstances();
            if (allInstances.size() > 0) {
                Message msg = (Message) new ArrayList<>(allInstances).get(0);
                if (msg.isBubble()) {
                    bubble(msg);
                } else {
                    report(msg);
                }
            }
        }

    }

    /**
     *
     */
    public enum ConnectionState {

        /**
         *
         */
        NOCONNECTION,
        /**
         *
         */
        WS_ENABLED,
        /**
         *
         */
        WS_DISABLED
    }

    private static ImageIcon imgNoConnection;
    private static ImageIcon imgEnabled;
    private static ImageIcon imgDisabled;
    private static ImageIcon imgWarning;
    private static ImageIcon imgBug;

    /**
     *
     */
    //public static FileHandler FH;
    private static final InputOutput MESSAGES = IOProvider.getDefault().getIO("Messages", true);
    private static final InputOutput EXCEPTIONS = IOProvider.getDefault().getIO("Exceptions", true);

    private JPanel panel = new JPanel();

    private Lookup.Result<Message> messageResult;
    private JLabel timeLabel;
    

    private static ConnectionState latestConnectionState;

    private static final InfoBar instance = new InfoBar();

    private final CircularFifoQueue<Message> queue;

    RestClientNav restNav;

    private static final String SEPARATOR = " | ";

    /**
     *
     */
    protected InfoBar() {

        imgNoConnection = new ImageIcon(
                ImageUtilities.loadImage(
                        "images/icon.png"));
        imgEnabled = new ImageIcon(
                ImageUtilities.loadImage(
                        "images/enabled.gif"));
        imgDisabled = new ImageIcon(
                ImageUtilities.loadImage(
                        "images/disabled.gif"));
        imgWarning = new ImageIcon(
                ImageUtilities.loadImage(
                        "images/warning.png"));
        imgBug = new ImageIcon(
                ImageUtilities.loadImage(
                        "images/bug.png"));
        queue = new CircularFifoQueue<>(3);
        messageResult = Utilities.actionsGlobalContext().lookupResult(Message.class);

        try {
            restNav = new RestClientNav(false);
        } catch (ConnectException ex) {
            Messaging.report("Can't connect to the navigation web service", ex, this.getClass(), true);
        } catch (EarsException ex) {
            Messaging.report("Problem with the navigation web service", ex, this.getClass(), true);
        }

        if (messageResult.allInstances().size() > 0) {
            for (Message message : messageResult.allInstances()) {
                bubble(message);
            }
        }
        messageResult.addLookupListener(this);

        this.timeLabel = new JLabel();

        Border border = new LineBorder(Color.BLUE);
        JPanel timeLabelPanel = new JPanel();
        timeLabelPanel.setBorder(border);
        timeLabelPanel.add(this.timeLabel);
        panel.add(timeLabelPanel);

        Timer t1 = new Timer(5 * 100, (ActionEvent event) -> {  //0.5 seconds
            OffsetTime localTimeInUtc = OffsetTime.now(Clock.systemUTC());
            OffsetTime localTime = OffsetTime.now(Clock.systemDefaultZone());
            timeLabel.setText("Computer UTC: " + StringUtils.DTF_TIME_FORMAT_HOURS_MINS_SECS_ZONE.format(localTimeInUtc) + SEPARATOR + "Computer local: " + StringUtils.DTF_TIME_FORMAT_HOURS_MINS_SECS_ZONE.format(localTime) + " ");
        });

        t1.start();
    }

    /**
     *
     * @return
     */
    public Component getComponent() {
        return this.panel;
    }

    /**
     *
     * @return
     */
    public static InfoBar getInstance() {
        return instance;
    }

    private String getConnectionStateName(ConnectionState state) {
        return NbBundle.getMessage(InfoBar.class, state.name());
    }

    /**
     *
     * @return
     */
    public Message.State getLatestState() {
        return queue.get(queue.size() - 1).getState();
    }

    /**
     *
     * @return
     */
    public boolean noProblemsLately() {
        return this.getLatestState() != Message.State.BAD && this.getLatestState() != Message.State.EXCEPTION;
    }

    /**
     *
     * @return
     */
    public ConnectionState getLatestConnectionState() {
        return latestConnectionState;
    }

    /**
     *
     * @param state
     */
    /* public void setLatestConnectionState(ConnectionState state) {
        latestConnectionState = state;
        // propertyChangeSupport ...
        Icon icon;
        switch (state) {
            case WS_ENABLED:
                icon = imgEnabled;
                latestConnectionState = ConnectionState.WS_ENABLED;
                break;
            case WS_DISABLED:
                icon = imgDisabled;
                latestConnectionState = ConnectionState.WS_DISABLED;
                break;
            default:
                icon = imgNoConnection;
                latestConnectionState = ConnectionState.NOCONNECTION;
                break;
        }
        this.connectionLabel.setIcon(icon);
        this.connectionLabel.setText(NbBundle.getMessage(InfoBar.class, getConnectionStateName(latestConnectionState)));
    }*/
    private void bubble(Message message) {
        GlobalActionContextProxy.getInstance().removeAll(Message.class);
        if (message.getEx() != null) {
            exceptionBubble(message.getMsg(), message.getEx(), message.getCls());
        } else if (message.getState() == Message.State.BAD) {
            badBubble(message.getMsg(), message.getCls());
        } else {
            goodBubble(message.getMsg(), message.getCls());
        }
    }

    /**
     * *
     * Prints a bad message in the infobar.
     *
     * @param msg
     * @param caller
     */
    private void badBubble(String msg, Class caller) {
        Message message = new Message(msg, null, caller, true, Message.State.BAD);
        queue.add(message);
        NotificationDisplayer.getDefault().notify("Warning: ", imgWarning, msg, null);
        report(message);
    }

    /**
     * *
     * Prints a good message in the infobar.
     *
     * @param msg
     * @param caller
     */
    private void goodBubble(String msg, Class caller) {
        Message message = new Message(msg, null, caller, true, Message.State.GOOD);
        queue.add(message);
        NotificationDisplayer.getDefault().notify("Notification: ", imgEnabled, msg, null);
        report(message);
    }

    /**
     * *
     * Prints the occurence of an exception in the infobar.
     *
     * @param msg
     * @param ex
     * @param caller
     */
    private void exceptionBubble(String msg, Throwable ex, Class caller) {
        Message message = new Message(msg, ex, caller, true, Message.State.EXCEPTION);
        queue.add(message);
        String exMsg = ex.getMessage();

        if (msg != null && !msg.isEmpty()) {
            StringBuilder sb = new StringBuilder(msg);
            if (exMsg != null) {
                sb.append(": ");
                sb.append(exMsg);
            }
            NotificationDisplayer.getDefault().notify(ex.getClass().getName() + ". ", imgWarning, sb.toString(), null);
        } else {
            NotificationDisplayer.getDefault().notify(ex.getClass().getName() + ". ", imgWarning, exMsg, null);
        }
        report(message);
    }

    /**
     * *
     * Print a message in the Output screen.
     *
     * @param message
     */
    private void report(Message message) {
        GlobalActionContextProxy.getInstance().removeAll(Message.class);
        report(message.getMsg(), message.getEx(), message.getCls());
    }

    /**
     * *
     * Print a message in the Output screen.
     *
     * @param msg
     * @param ex
     * @param caller
     */
    private void report(String msg, Throwable ex, Class caller) {
        if (ex == null) {
            report(msg, caller);
        } else {
            /*Logger logger = Logger.getLogger(caller.getName());
            logger.log(Level.INFO, msg, ex);*/ //extra logging not needed

            String thisMoment = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneOffset.UTC).format(Instant.now());

            String exMsg = ex.getMessage();
            StringBuilder sb = new StringBuilder(thisMoment);
            sb.append(": ");
            sb.append(ex.getClass().getName());
            sb.append(": ");
            if (msg != null && !msg.isEmpty()) {
                sb.append(msg);
                if (exMsg != null) {
                    sb.append(": ");
                    sb.append(exMsg);
                }

            } else {
                sb.append(exMsg);
            }
            EXCEPTIONS.getOut().println(sb.toString());
        }
    }

    /**
     * *
     * Print a message in the Output screen.
     *
     * @param msg
     * @param caller
     */
    private void report(String msg, Class caller) {
        Logger logger = Logger.getLogger(caller.getName());
        logger.log(Level.INFO, msg);
        String thisMoment = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneOffset.UTC).format(Instant.now());
        StringBuilder sb = new StringBuilder(thisMoment);
        sb.append(": ");
        sb.append(msg);
        MESSAGES.getOut().println(sb.toString());
    }

}
