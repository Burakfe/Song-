/*
 * Stack implementation using array
 * push() - O(1)
 * pop() - O(1)
 * peek() - O(1)
 * isEmpty() - O(1)
 * size() - O(1)
 */

public class Stack {

    private Object[] stack;
    private int top;
    private int size;

    public Stack(int size) {
        this.size = size;
        stack = new Object[size];
        top = -1;
    }

    public void push(Object data) {
        if (stack.length == size) {
            Object[] temp = new Object[size * 2];
            System.arraycopy(stack, 0, temp, 0, size);
            stack = temp;  //stack = Arrays.copyOf(stack, size * 2);
            stack[++top] = data;
            size *= 2;
        } else {
            stack[++top] = data;
        }
    }

    public Object pop() {
        if (stack.length == 0) {
            return null;
        }
        Object out = stack[top--];
        stack[top--] = null;

        if (stack.length == size / 4) {
            Object[] temp = new Object[size / 2];
            System.arraycopy(stack, 0, temp, 0, size / 2);
            stack = temp;  //stack = Arrays.copyOf(stack, size / 2);
            size /= 2;

        }
        return out;
    }

    public Object peek() {
        if (!isEmpty()) {
            return stack[top];
        } else {
            try {
                throw new Exception();
            } catch (Exception e) {
                System.out.println("Stack is empty.");
            }
            return null;
        }
    }

    // Check if the stack is empty
    public boolean isEmpty() {
        return top == -1;
    }

    // Current size of the stack
    public int size() {
        return top + 1;
    }
}
