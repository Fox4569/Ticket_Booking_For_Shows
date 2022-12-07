/**
 *  @author: Bruno Sousa
 *  @author: Jorge Santos
 *  @author: João Baptista
 * #####################################################################################################################
 * # This class is responsible for handling the threads that will create the Multicast paradigm.                       #
 * #####################################################################################################################
 * #                            Receiver (One receiver per server.)                                                    #
 * #                          /      |                                                                                 #
 * #     Multicast -----------       |-------------> Sender (HeartBeat Thread, will send a Heartbeat every 10 seconds) #
 * #         |                       |                       (StreamSender)                                            #
 * #         |                       |-------------> Sender2(Sends a Prepare Message for Multicast when needed)        #
 * #         |                       |                       (StreamSenderPrepare)                                     #
 * #         |                       |-------------> Sender3(Sends a Commit Message for Multicast when needed)         #
 * #         |                       |                       (StreamSenderCommit)                                      #
 * #         |                       |-------------> Sender4(Sends a Abort Message for Multicast when needed)          #
 * #         |                       |                       (StreamSenderAbort)                                       #
 * #         |                       |-------------> Sender5(Might do for a debugger (Object not registered.))         #
 * #         |                                                                                                         #
 * #         |-------------------------------------> Sender will be created after 30 seconds of receiving Hbs.         #
 * #                                                                                                                   #
 * #####################################################################################################################
 */


package pt.isec.g45.tp.model.UDP;

import pt.isec.g45.tp.model.ServerManager;
import pt.isec.g45.tp.model.UDP.multicast.StreamReceiver;
import pt.isec.g45.tp.model.UDP.multicast.StreamSender;
import pt.isec.g45.tp.utils.HeartBeat;

import java.net.*;

public class MulticastUDP extends Thread{


    // Informaçoes para aceder ao multicast
    InetAddress grupo;
    int porto;
    String interf;


    StreamReceiver receiver;
    StreamSender sender;
    public MulticastUDP(int porto, InetAddress grupo, String interf, ServerManager server) {
        this.porto = porto;
        this.grupo = grupo;
        this.interf = interf;
        receiver = new StreamReceiver(grupo, porto,server);
        sender = new StreamSender(grupo, porto);
    }

    /**
     * This method allows to initiate the timer heartbeat mechanism
     * @param hb    HeartBeat -> reference for the current ServerManager HeartBeat
     */
    public synchronized void createSender(HeartBeat hb) {
        sender.sender(hb);
    }

    @Override
    public void run() {
        sender.start();  // Creates the thread to send the HeartBeat
        receiver.start();   // Creates the thread to receive HeartBeats and other Multicast Messages
    }
}

