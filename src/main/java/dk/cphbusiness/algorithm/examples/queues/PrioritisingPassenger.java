/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.cphbusiness.algorithm.examples.queues;

import dk.cphbusiness.airport.template.Category;
import dk.cphbusiness.airport.template.Passenger;
import dk.cphbusiness.airport.template.Plane;
import dk.cphbusiness.airport.template.Time;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class PrioritisingPassenger implements PriorityQueue<Passenger> {

    private final Passenger[] items;
    private int size = 0;

    public static void main(String[] args) {
        PrioritisingPassenger pp = new PrioritisingPassenger(100);
        for (int i = 0; i < 30; i++) {
            pp.enqueue(new Passenger(i, new Time(new Date().getTime()), pp.randomCat(), new Plane(new Time(new Date().getTime()))));
        }
        pp.printQueue();
        for (int i = 0; i < 30; i++) {
            System.out.println(pp.dequeue().getCategory());
            System.out.println("");
        }
    }

    private Category randomCat() {
        int rand = (int) (Math.random() * 5);
        if (rand == 0) {
            return Category.Monkey;
        }
        if (rand == 1) {
            return Category.Disabled;
        }
        if (rand == 2) {
            return Category.Family;
        }
        if (rand == 3) {
            return Category.BusinessClass;
        }
        return Category.LateToFlight;
    }

    public PrioritisingPassenger(int capacity) {
        items = new Passenger[capacity+1];
    }

    @Override
    public void enqueue(Passenger item) {
        //Passenger temp = null;
        items[++size] = item;
        int position = size;
        while (items[parent(position)] != null && items[position].compareTo(items[parent(position)]) < 0) {
            swap(position, parent(position));
            position = parent(position);
        }

    }

    @Override
    public Passenger dequeue() {
        // Reference for item with highest priority
        Passenger first = items[1];
        // Move last item on top
        items[1] = items[size];
        items[size] = null;
        size--;
        int pos = 1;
        // Trickle down until childelements are either higher or null
        // While both children are there...
        while (leftChild(pos)<= size  && rightChild(pos)< size ) {
            //Find smallest child and swap
            if (items[leftChild(pos)].compareTo(items[rightChild(pos)]) <= 0 
                    && items[pos].compareTo(items[leftChild(pos)])>0){
//                System.out.println("Left child: "+items[leftChild(pos)].getCategory() 
//                        + "should be higher up than pos " +items[pos].getCategory());
                swap(pos,leftChild(pos));
                pos = leftChild(pos);
            } 
            else if (items[leftChild(pos)].compareTo(items[rightChild(pos)]) > 0
                    && items[pos].compareTo(items[rightChild(pos)])>0){
//      System.out.println("Right child: "+items[rightChild(pos)].getCategory() 
//                        + "should be higher up than pos " +items[pos].getCategory());
                swap(pos,rightChild(pos));
                pos = rightChild(pos);
            }
            else break;
            
        }
        if (leftChild(pos)<= size && items[pos].compareTo(items[leftChild(pos)])>0) swap(pos,leftChild(pos));
    
        printQueue();
        //System.out.println("First in line is a: "+first.getCategory());
        return first;
    }
    private int parent(int position){
        return position/2;
    }
    private int leftChild(int node){
        return node*2;
    }
    private int rightChild(int node){
        return node*2+1;
    }

    @Override
    public Passenger peek() {
        return items[0];
    }

    @Override
    public int size() {
        return size;
    }

    private void swap(int pos1, int pos2) {
        items[0] = items[pos1];
        items[pos1] = items[pos2];
        items[pos2] = items[0];
    }
void printQueue(){
    if (true) return;
    String line = "";
    int numsOnLine = 1;
    for (int i = 1; i < size; i++) {
        if (i == numsOnLine){
            line+= System.lineSeparator();
            numsOnLine*=2;
        }
        for (int j = 0; j < size/(numsOnLine); j++) {
            line += "  ";
        }
        line+= items[i].getCategory();
        for (int j = 0; j < size/(numsOnLine); j++) {
            line += "  ";
        }
    }
    System.out.println(line);
}
}
