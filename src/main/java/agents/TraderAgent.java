package agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.*;

public class TraderAgent extends GuiAgent {
    protected TraderGUI traderAgent;
    public static Map<String, Double> societesInfo = new HashMap<>();
    public Date date = new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime();
    protected void setup(){
        initHashMap();
        if(getArguments().length==1) {
            traderAgent=(TraderGUI)getArguments()[0];
            traderAgent.traderAgent=this;
        }
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);

//AID[] vendeurs;



        /*parallelBehaviour.addSubBehaviour(new TickerBehaviour(this,5000) {
            @Override
            protected void onTick() {
                DFAgentDescription dfAgentDescription=new DFAgentDescription();
                ServiceDescription serviceDescription=new ServiceDescription();
                serviceDescription.setType("Book-selling");
                serviceDescription.setName("JADE book trading");
                dfAgentDescription.addServices(serviceDescription);
                try {
                    DFAgentDescription[] results=DFService.search(myAgent,dfAgentDescription);
                    System.out.print(results.length);
                    vendeurs=new AID[results.length];
                    for(int i=0;i<vendeurs.length;i++){
                        System.out.print(results[i].getName()+"\n");
                        vendeurs[i]=results[i].getName();
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });*/

        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            int counter=0;
            List<ACLMessage> aclMessageList=new ArrayList<ACLMessage>();
            @Override
            public void action() {
                ACLMessage aclMessage=receive();
                if(aclMessage!=null){
                    //CFP
                    ACLMessage aclMessage1=new ACLMessage(ACLMessage.CONFIRM);
                    if(societesInfo.containsKey(aclMessage.getContent()))
                        aclMessage1.setContent("société name :     "+aclMessage.getContent()+
                                "     ,le prix estimé : "+ String.valueOf(societesInfo.get(aclMessage.getContent()))
                        +"      ,date de la cotation : "+date);
                    else
                        aclMessage1.setContent("error!");

                    aclMessage1.addReceiver(new AID("Consumer",AID.ISLOCALNAME));
                    send(aclMessage1);
                    traderAgent.logMsg(aclMessage);


                    /*switch (aclMessage.getPerformative()){
                        case ACLMessage.REQUEST:
                            for(AID aid:vendeurs){
                                System.out.print("============== "+aid.getName()+"\n");
                                aclMessage1.addReceiver(aid);
                                send(aclMessage1);
                            }
                            break;
                        case ACLMessage.PROPOSE:
                            ++counter;
                            aclMessageList.add(aclMessage);
                            if(counter==vendeurs.length){
                                ACLMessage bestOffer=aclMessageList.get(0);
                                double bestPrice=Double.valueOf(bestOffer.getContent());
                                for(ACLMessage aclMsg:aclMessageList){
                                    double price=Double.valueOf(aclMsg.getContent());
                                    if(price<bestPrice){
                                        bestPrice=price;
                                        bestOffer=aclMessage;
                                    }
                                }
                                ACLMessage replyToSelleer=bestOffer.createReply();
                                replyToSelleer.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                replyToSelleer.setContent(String.valueOf(bestPrice));
                                send(replyToSelleer);
                            }
                            break;
                        case ACLMessage.AGREE:
                            ACLMessage replyToConsumer=new ACLMessage(ACLMessage.CONFIRM);
                            replyToConsumer.addReceiver(new AID("Consumer",AID.ISLOCALNAME));
                            replyToConsumer.setContent(aclMessage.getContent());
                            send(replyToConsumer);
                            counter=0;
                            aclMessageList.clear();
                            break;
                        case ACLMessage.REFUSE:
                            break;
                    }*/
                    //traderAgent.logMsg(aclMessage);
                    /*ACLMessage reply=aclMessage.createReply();
                    reply.setContent("LIVRE : "+aclMessage.getContent()+" OK");
                    send(reply);*/


                }
                else block();
            }
        });

    }
    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
    }

    private void initHashMap(){
        societesInfo.put("oracle",50.0);
        societesInfo.put("microsoft",200.0);
        societesInfo.put("google",350.0);
        societesInfo.put("facebook",455.0);
        societesInfo.put("apple",600.0);

    }
}
