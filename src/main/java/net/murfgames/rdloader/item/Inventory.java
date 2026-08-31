package net.murfgames.rdloader.item;

import net.murfgames.rdloader.RubyDungLoader;
import net.murfgames.rdloader.agent.wrapper.EntityWrapper;
import net.murfgames.rdloader.entity.component.EntityComponent;
import net.murfgames.rdloader.util.Identifier;
import net.murfgames.rdloader.util.event.Signal;

import java.util.function.Consumer;

public class Inventory {

    private final ItemStack[] items;

    private static final int DEFAULT_INVENTORY_SIZE = 1;

    public Inventory() {
        items = new ItemStack[DEFAULT_INVENTORY_SIZE];
    }

    public Inventory(int size) {
        items = new ItemStack[size];
    }

    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                count += items[i].size;
            }
        }

        return count;
    }

    public Identifier getItemId(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < items.length && items[itemIndex] != null) {
            return items[itemIndex].id;
        }

        throw new IllegalArgumentException("No item found at inventory index: " + itemIndex);
    }

    public int addItem(Identifier item) {
        return addItem(item, 1);
    }

    public int addItem(Identifier item, int count) {
        int remainingCount = count;
        int firstEmpty = -1;

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].id == item) {
                int addedItems = items[i].addItems(remainingCount);
                remainingCount -= addedItems;

                if (remainingCount <= 0)
                    return count;
            }
            else if (firstEmpty < 0 && items[i] == null)
                firstEmpty = i;
        }

        if (firstEmpty >= 0) {
            ItemStack itemStack = new ItemStack(item, remainingCount);
            setItemStack(itemStack, firstEmpty);
            return count;
        }

        return count - remainingCount;
    }

    private void setItemStack(ItemStack itemStack, int position) {
        if (items[position] != null)
            items[position].empty();

        items[position] = itemStack;
        itemStack.onChangeHands(this::removeItemStack);
    }

    private void removeItemStack(ItemStack itemStack) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i] == itemStack) {
                if (!itemStack.isEmpty)
                    itemStack.empty();
                items[i] = null;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ItemStack item : items) {
            if (item != null) {
                if (i > 0) sb.append("\n");
                sb.append(item);
                i++;
            }
        }
        return sb.toString();
    }

    private static class ItemStack {
        private final Identifier id;
        private final int maxSize;
        private int size;

        private static final int DEFAULT_MAX_SIZE = 0;

        private static final Signal<ItemStack> ITEM_EMPTY = new Signal<>();
        private boolean isEmpty = false;

        public ItemStack(Identifier id, int size) {
            this.id = id;
            this.maxSize = DEFAULT_MAX_SIZE;
            this.size = getStackAddition(size, 0, this.maxSize);
        }

        public ItemStack(Identifier id, int size, int maxSize) {
            this.id = id;
            this.maxSize = maxSize;
            this.size = getStackAddition(size, 0, this.maxSize);
        }

        private void setSize(int newSize) {
            if (isEmpty) return;

            this.size = Math.max(0, Math.min(this.maxSize, newSize));
            isEmpty = true;

            if (this.size == 0) {
                ITEM_EMPTY.emit(this);
                ITEM_EMPTY.clear();
            }
        }

        public int addItems(int count) {
            if (isEmpty) return 0;

            int addition = getStackAddition(count, this.size, this.maxSize);
            setSize(size + addition);
            return addition;
        }

        public int removeItems(int count) {
            if (isEmpty) return 0;

            int subtraction = getStackSubtraction(count, this.size, this.maxSize);
            setSize(size - subtraction);
            return subtraction;
        }

        public int moveItems(int count, ItemStack destinationStack) {
            if (isEmpty || destinationStack.isEmpty || this.id != destinationStack.id) return 0;

            int subtraction = getStackAddition(count, this.size, this.maxSize);

            int movedItems = destinationStack.addItems(subtraction);
            return removeItems(movedItems);
        }

        public void empty() {
            setSize(0);
        }

        public void onChangeHands(Consumer<ItemStack> ownerCallback) {
            ITEM_EMPTY.clear();
            ITEM_EMPTY.connect(ownerCallback);
        }

        private static boolean isOutOfBounds(int size, int maxSize) {
            if (maxSize <= 0) return size < 0;
            return size > maxSize || size < 0;
        }

        private static int getStackAddition(int count, int size, int maxSize) {
            int newSize = size + count;
            if (isOutOfBounds(newSize, maxSize))
                return maxSize - size;
            else return count;
        }

        private static int getStackSubtraction(int count, int size, int maxSize) {
            int newSize = size - count;
            if (isOutOfBounds(newSize, maxSize))
                return size;
            else return count;
        }

        @Override
        public String toString() {
            return id.toString() + ", " + size;
        }
    }
}
