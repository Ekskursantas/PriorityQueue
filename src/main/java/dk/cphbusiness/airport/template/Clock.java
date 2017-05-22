package dk.cphbusiness.airport.template;

import dk.cphbusiness.algorithm.examples.queues.PrioritisingPassenger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

public class Clock implements Runnable {

    private final long sleepingTime = 10;
    private boolean running = true;
    private final PassengerProducer producer;
    private final List<PassengerConsumer> consumer;
    private long millis;
    private List<Plane> planes;

    public Clock(List<Plane> planes, PassengerProducer producer, PassengerConsumer consumer, Time startTime) {
        this.producer = producer;
        this.consumer = new ArrayList();
        this.consumer.add(consumer);
        this.millis = startTime.getMillis();
        this.planes=planes;
    }

    public void stop() {
        running = false;
    }

    public Time getTime() {
        return new Time(millis);
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(sleepingTime);
                producer.tick(this);
                PassengerConsumer longQueue = null;
                int totalCount=0;
                for (PassengerConsumer passengerConsumer : consumer) {
                    totalCount+=passengerConsumer.getPeopleInLine();
                    passengerConsumer.tick(this);
                    if (passengerConsumer.getPeopleInLine() >30) longQueue=passengerConsumer;
                }
                if (totalCount*5<consumer.size()){
                    System.out.println("Line under control - Closing register!");
                    consumer.remove(0);
                }
                System.out.println("There is a total of "+totalCount +" across "+consumer.size()+" registers");
                if (longQueue!=null){
                    System.out.println("Adding new register for all these people!");
                    PrioritisingPassenger pp = new PrioritisingPassenger(1000);
                    consumer.add(new PassengerConsumer(planes,longQueue.getQueue()));
//                    for (int i = 0; i < longQueue.getPeopleInLine()/2; i++) {
//                        pp.enqueue(longQueue.getQueue().dequeue());
//                    }
                }
                millis += 1000;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
