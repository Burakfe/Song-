/*
 * Queue implementation using array.
 * enqueue(Object data) - Add data to the queue.
 * dequeue() - Remove and return the front element of the queue.
 * peek() - Return the front element of the queue without removing it.
 * isEmpty() - Return true if the queue is empty, false otherwise.
 * display() - Display the elements of the queue.
 */
public class Queue {
    private final Object[] queue;
    private int front;
    private int rear;
    private final int size;
    public Queue(int size) {
        this.size = size;
        queue = new Object[size];
        front = 0;
        rear = 0;
    }
    public void enqueue(Object data) {
        if (rear == size) {
            System.out.println("Queue is full.");
            return;
        }
        queue[rear++] = data;
    }
    public Object dequeue() {
        if (front == rear) {
            System.out.println("Queue is empty.");
            return null;
        }
        Object out = queue[front];
        queue[front++] = null;
        return out;
    }
    public Object peek() {
        if (front == rear) {
            System.out.println("Queue is empty.");
            return null;
        }
        return queue[front];
    }
    public boolean isEmpty() {
        return front == rear;
    }
    public void display() {
        if (front == rear) {
            System.out.println("Queue is empty.");
            return;
        }
        for (int i = front; i < rear; i++) {
            System.out.print(queue[i] + " ");
        }
        System.out.println();
    }
}