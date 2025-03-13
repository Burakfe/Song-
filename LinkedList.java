
public class LinkedList {

    private String[] data;  // holds the string for the i-th node
    private int[] next;     // for the index of the next node in the list
    private int head;       // index of the head node iof -1 it means its empty

    public LinkedList() {
        // Start empty
        data = new String[0];
        next = new int[0];
        head = -1;
    }

    /**
     * Insert a new value at the front of the list.
     */
    public void insertFront(String value) {
        int oldLength = data.length;
        int newLength = oldLength + 1;

        // Create larger arrays
        String[] newData = new String[newLength];
        int[] newNext = new int[newLength];

        // Copying
        for (int i = 0; i < oldLength; i++) {
            newData[i] = data[i];
            newNext[i] = next[i];
        }

        // The new node goes into the last slot
        newData[newLength - 1] = value;
        // Link the new node's 'next' to the old head
        newNext[newLength - 1] = head;

        // The new node is now the head
        head = newLength - 1;

        // Replace old arrays
        data = newData;
        next = newNext;
    }

    /**
     * Insert a new value at the end of the list.
     */
    public void insertEnd(String value) {
        int oldLength = data.length;
        int newLength = oldLength + 1;

        // Create larger arrays
        String[] newData = new String[newLength];
        int[] newNext = new int[newLength];

        // Copy old data and next into new arrays
        for (int i = 0; i < oldLength; i++) {
            newData[i] = data[i];
            newNext[i] = next[i];
        }

        // The new node goes in the last slot
        newData[newLength - 1] = value;
        newNext[newLength - 1] = -1; // new tail has no next

        if (head == -1) {
            // If list was empty, the new node becomes head
            head = newLength - 1;
        } else {
            // Otherwise, find the current tail (the node with next == -1)
            int current = head;
            while (next[current] != -1) {
                current = next[current];
            }
            // Link the old tail to the new tail
            newNext[current] = newLength - 1;
        }

        // Replace old arrays
        data = newData;
        next = newNext;
    }

    /**
     * Delete the first occurrence of the given value in the list. Returns true
     * if deletion happened, false if not found.
     */
    public boolean deleteValue(String value) {
        if (head == -1) {
            // List is empty
            return false;
        }

        // SPECIAL CASE: if head node is the one to delete
        if (data[head].equals(value)) {
            deleteAtIndex(head, true); // pass 'true' to signal "removing head"
            return true;
        }

        // Otherwise, find the node whose next points to the target
        int current = head;
        while (current != -1) {
            int nextIndex = next[current];
            if (nextIndex == -1) {
                break; // no more nodes
            }
            if (data[nextIndex].equals(value)) {
                // We want to remove 'nextIndex'
                deleteAtIndex(nextIndex, false);
                // Update the 'next' pointer of current to skip the removed node.
                // Because everything shifts, we’ll re-calculate that pointer after shift.
                // The tricky part is that once we remove nextIndex, the arrays shift.
                // We need to figure out what the new index for the node after "nextIndex" is.
                //
                // A simpler solution: after deleting, we re-traverse from head to find
                // where 'current' is in the new array. Then link it properly.

                // Let's do that carefully:
                int newCurrent = findIndexAfterDeletion(data[current]);
                if (newCurrent != -1) {
                    // Re-link to the node that replaced "nextIndex" or the node after it
                    // in the new shifted arrays
                    int nodeAfterRemoved = findIndexAfterDeletion(value);
                    // The node that took the place (or next in chain) might have changed index
                    next[newCurrent] = nodeAfterRemoved;
                }
                return true;
            }
            current = nextIndex;
        }
        return false;
    }

    /**
     * A helper to remove the node at 'removeIndex' from the arrays and shift.
     * We also handle if we are removing the 'head'.
     *
     * Because we shift arrays to remove an element, every index > removeIndex
     * decreases by 1. So we must fix: - The 'head' (if we removed head). - Any
     * 'next' pointers that referred to an index >= removeIndex.
     *
     * Steps: 1) Create new arrays of size (oldLength - 1). 2) Copy over every
     * index except removeIndex. 3) For elements after removeIndex, shift them
     * down by 1. 4) Re-map 'head' and all 'next' pointers accordingly if their
     * index >= removeIndex.
     */
    private void deleteAtIndex(int removeIndex, boolean isRemovingHead) {
        int oldLength = data.length;
        int newLength = oldLength - 1;
        if (newLength == 0) {
            // The list had only one node.
            data = new String[0];
            next = new int[0];
            head = -1;
            return;
        }

        // New arrays, one slot smaller
        String[] newData = new String[newLength];
        int[] newNext = new int[newLength];

        // We will copy everything *except* removeIndex
        // and shift elements after removeIndex by -1 in index.
        for (int oldIdx = 0, newIdx = 0; oldIdx < oldLength; oldIdx++) {
            if (oldIdx == removeIndex) {
                // skip this slot (the removed node)
                continue;
            }
            // If oldIdx < removeIndex, it copies straight over.
            // If oldIdx > removeIndex, it shifts to newIdx = oldIdx - 1.
            int adjustedIndex = (oldIdx < removeIndex) ? oldIdx : (oldIdx - 1);

            newData[adjustedIndex] = data[oldIdx];
            newNext[adjustedIndex] = next[oldIdx];
        }

        // Now we must fix 'next' pointers if they refer to an index >= removeIndex
        for (int i = 0; i < newLength; i++) {
            int oldNext = newNext[i];
            if (oldNext >= 0) {
                if (oldNext == removeIndex) {
                    // If something pointed exactly to removeIndex, that node is now gone.
                    // We want to skip it. Let’s set next[i] = -1 by default,
                    // or we might link to whatever used to come after removeIndex.
                    //
                    // But let's do a simpler approach: if something pointed to that node,
                    // it effectively is skipping it now. We'll point to the node that
                    // was *after* removeIndex in the old arrays, which is next[removeIndex].
                    // But since we've removed that node, let's see if there was a
                    // node after it:
                    if (removeIndex < oldLength) {
                        // old next after removed node
                        int afterRemoved = next[removeIndex];
                        // But after removal, that 'afterRemoved' might shift by -1 if > removeIndex
                        if (afterRemoved > removeIndex) {
                            afterRemoved--;
                        }
                        newNext[i] = afterRemoved;
                    } else {
                        // If removeIndex was the last slot, just set -1
                        newNext[i] = -1;
                    }
                } else if (oldNext > removeIndex) {
                    // If next[i] pointed to something after removeIndex,
                    // shift it back by 1
                    newNext[i] = oldNext - 1;
                }
            }
        }

        // Finally fix 'head' if needed
        if (isRemovingHead) {
            // The old head is removed. The new head is what used to be next[oldHead].
            // But we must see how that next pointer shifts.
            // Actually, we already have that in `newNext` for the *new* arrays if head != -1.
            // The old head was removeIndex, so let's see:
            int oldNextOfHead = next[removeIndex];
            if (oldNextOfHead == -1) {
                // no next
                head = -1;
            } else if (oldNextOfHead > removeIndex) {
                head = oldNextOfHead - 1;
            } else {
                head = oldNextOfHead;
            }
        } else {
            // If we remove a node in the middle or end, 'head' might need adjusting
            // only if head > removeIndex. But typically, head is <= removeIndex if 
            // the list is correct. 
            if (head > removeIndex) {
                head--;
            }
        }

        // Assign the new arrays
        data = newData;
        next = newNext;

        // If we removed the old head and the new head might have changed
        // to reflect shifting, that is handled above.
    }

    /**
     * Utility: After a deletion (and shift) we may need to find the "new index"
     * of a node that had a certain String before shifting. Essentially we do a
     * quick traversal to find which index has that String now. Returns -1 if
     * not found.
     */
    private int findIndexAfterDeletion(String str) {
        int current = head;
        while (current != -1) {
            if (data[current].equals(str)) {
                return current;
            }
            current = next[current];
        }
        return -1;
    }

    /**
     * Print the list contents from head to tail.
     */
    public void printList() {
        System.out.print("List: ");
        int current = head;
        while (current != -1) {
            System.out.print(data[current] + " ");
            current = next[current];
        }
        System.out.println();
    }

}
