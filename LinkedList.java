/*
* LinkedList class
* To use this class; LinkedList list = new LinkedList();
* This class contains the following methods:
* 1. insertHead(int data) - insert a new node at the beginning of the list
* 2. insertTail(int data) - insert a new node at the end of the list
* 3. deleteHead() - delete the first node of the list
* 4. deleteTail() - delete the last node of the list
* 5. search(int key) - search for a node with a specific key
* 6. display() - print the list
* This class also contains a Node class which is a nested class.
 */
public class LinkedList {

    // creating a node class
    private static class Node {

        private final int data;
        private Node next;

        private Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    public void insertHead(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    public void insedtTail(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            return;
        }
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    public void deleteHead() {
        if (head == null) {
            return;
        }
        head = head.next;
    }

    public void deleteTail() {
        if (head.next == null) {
            head = null;
            return;
        }
        Node current = head;
        while (current.next.next != null) {
            current = current.next;
        }
        current.next = null;
    }

    // search method
    public boolean search(int key) {
        Node current = head;
        while (current != null) {
            if (current.data == key) {
                return true; // found
            }
            current = current.next;
        }
        return false; // not found
    }

    // Print the list
    public void display() {
        Node current = head;
        if (current == null) {
            System.out.println("List is empty.");
            return;
        }
        while (current != null) {
            System.out.print(current.data);
            current = current.next;
            if (current != null) {
                System.out.print(" - ");
            }
        }
        System.out.println();
    }

}
