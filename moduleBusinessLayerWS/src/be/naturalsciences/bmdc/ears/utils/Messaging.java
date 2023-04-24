/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

import be.naturalsciences.bmdc.ears.netbeans.services.GlobalActionContextProxy;

/**
 *
 * @author Thomas Vandenberghe
 */
public class Messaging {

    public static void report(String msg, Throwable ex, Class cls, boolean bubble) {
        if (ex == null) {
            report(msg, Message.State.UNKNOWN, cls, bubble);
        } else if (msg != null && cls != null) {
            Message m = new Message(msg, ex, cls, bubble, Message.State.EXCEPTION);
            //GlobalActionContextProxy.getInstance().removeAll(Message.class);
            GlobalActionContextProxy.getInstance().add(m);
        }
    }

    public static void report(String msg, Message.State state, Class cls, boolean bubble) {
        if (msg != null && cls != null) {
            Message m = new Message(msg, null, cls, bubble, state);
            //GlobalActionContextProxy.getInstance().removeAll(Message.class);
            GlobalActionContextProxy.getInstance().add(m);
        }
    }

    public static void report(Message m) {
        //GlobalActionContextProxy.getInstance().removeAll(Message.class);
        GlobalActionContextProxy.getInstance().add(m);
    }
}
