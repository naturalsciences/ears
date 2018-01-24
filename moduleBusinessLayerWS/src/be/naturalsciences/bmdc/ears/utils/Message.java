/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.utils;

/**
 *
 * @author thomas
 */
public class Message {

    private final String msg;
    private final Throwable ex;
    private final Class cls;
    private final boolean bubble;
    private final State state;

    public enum State {

        GOOD, BAD, EXCEPTION, INFO, UNKNOWN
    }

    public Message(String msg, Throwable ex, Class cls, boolean bubble, State state) {
        this.msg = msg;
        this.ex = ex;
        this.cls = cls;
        this.bubble = bubble;
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public Throwable getEx() {
        return ex;
    }

    public Class getCls() {
        return cls;
    }

    public boolean isBubble() {
        return bubble;
    }

    public State getState() {
        return state;
    }
    
    

}
