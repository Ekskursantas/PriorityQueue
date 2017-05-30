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


public class PrioritisingPassenger implements PriorityQueue<Passenger> {

    private final Passenger[] items;
    private int size = 0;

    public static void main(String[] args) {
        PrioritisingPassenger enlistedPeople = new PrioritisingPassenger(100);
        for (int i = 0; i < 100; i++) {
            enlistedPeople.enqueue(new Passenger
                (i
                , new Time(new Date().getTime())
                , enlistedPeople.categorizer()
                , new Plane(new Time(new Date().getTime()))));
        }
        
        for (int i = 0; i < 100; i++) {
            System.out.println(i + " " +enlistedPeople.dequeue().getCategory());
            System.out.println("");
        }
    }

    private Category categorizer() {
        int rand = (int) (Math.random() * 5);
        switch (rand){
            case 0:
                return Category.Monkey;
            case 1:
                return Category.Disabled;
            case 2:
                return Category.Family;
            case 3:
                return Category.BusinessClass;
            default:
                return Category.LateToFlight;
                
        }  
    }

    public PrioritisingPassenger(int capacity) {
        items = new Passenger[capacity+1];
    }

    @Override
    public void enqueue(Passenger item) {
        items[++size] = item;
        int position = size;
        while (items[parent(position)] != null 
                && items[position].compareTo(items[parent(position)]) < 0) {
            swap(position, parent(position));
            position = parent(position);
        }

    }

    @Override
    public Passenger dequeue() {
        Passenger first = items[1];
        items[1] = items[size];
        items[size] = null;
        size--;
        int pos = 1;
        while (leftChild(pos)<= size  && rightChild(pos)< size ) {
            
            if (items[leftChild(pos)].compareTo(items[rightChild(pos)]) <= 0 
                    && items[pos].compareTo(items[leftChild(pos)])>0)
            {
                swap(pos,leftChild(pos));
                pos = leftChild(pos);
            } 
            else if (items[leftChild(pos)].compareTo(items[rightChild(pos)]) > 0
                    && items[pos].compareTo(items[rightChild(pos)])>0)
            {
                swap(pos,rightChild(pos));
                pos = rightChild(pos);
            }
            else break;
            
        }
        if (leftChild(pos)<= size && items[pos].compareTo(items[leftChild(pos)])>0) swap(pos,leftChild(pos));
    
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

}
