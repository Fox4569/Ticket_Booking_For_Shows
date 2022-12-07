package pt.isec.g45.tp.utils;

import java.io.Serializable;

public class MulticastMsg implements Serializable {
    static final long serialVersionUID = 1L;

    MulticastAction macAction;
    String [] args;
    HeartBeat hb;
    public MulticastMsg(MulticastAction macAction) {
        args = null;
        this.macAction = macAction;
    }

    public HeartBeat getHb() {
        return hb;
    }

    public void setHb(HeartBeat hb) {
        this.hb = hb;
    }

    public MulticastMsg(MulticastAction macAction, String [] args) {
        this.args = args;
        this.macAction = macAction;
    }
    public MulticastMsg(MulticastAction macAction, HeartBeat hb) {
        this.macAction = macAction;
        this.hb = hb;
    }

    public MulticastAction getMacAction() {
        return macAction;
    }

    public void setMacAction(MulticastAction macAction) {
        this.macAction = macAction;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
