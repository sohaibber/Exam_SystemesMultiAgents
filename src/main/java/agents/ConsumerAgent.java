package agents;

import containers.ConsumerContainer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class ConsumerAgent extends GuiAgent {
    private transient  ConsumerContainer gui;
    protected void setup(){
        if(getArguments().length==1){
            gui=(ConsumerContainer)getArguments()[0];
            gui.setConsumerAgent(this);
        }
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage=receive();
                if(aclMessage!=null){
                    switch(aclMessage.getPerformative()){
                        case ACLMessage.CONFIRM:
                            System.out.println(aclMessage.getContent());
                            gui.logMsg(aclMessage);
                            break;
                    }

                }
                else block();
            }
        });
    }


    @Override
    public void onGuiEvent(GuiEvent evt) {
        if(evt.getType()==1){
            String name=(String)evt.getParameter(0);
            ACLMessage aclMessage=new ACLMessage(ACLMessage.REQUEST);
            aclMessage.setContent(name);
            aclMessage.addReceiver(new AID("Trader", AID.ISLOCALNAME));
            send(aclMessage);
        }
    }

    public ConsumerContainer getGui() {
        return gui;
    }

    public void setGui(ConsumerContainer gui) {
        this.gui = gui;
    }
}
