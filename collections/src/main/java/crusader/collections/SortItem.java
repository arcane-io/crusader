package crusader.collections;

import java.util.Objects;

/**
 *
 */
public class SortItem<T> implements Comparable<SortItem> {
    private String label;
    private T item;

    public SortItem(LabelProvider<T> sortValue, T item) {
        this.label = sortValue.getLabel(item);
        this.item = item;
    }

    public SortItem(String label, T item) {
        this.label = label;
        this.item = item;
    }

    public String getLabel() {
        return this.label;
    }

    public T getItem() {
        return this.item;
    }

    @Override
    public int compareTo(SortItem o) {
        String otherLabel = o == null ? null : o.getLabel();
        return String.CASE_INSENSITIVE_ORDER.compare(this.label, otherLabel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SortItem<?> sortItem = (SortItem<?>) o;
        return Objects.equals(label, sortItem.label) && Objects.equals(item, sortItem.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, item);
    }

    @Override
    public String toString() {
        return "SortItem{" +
                " label: '" + label + "'," +
                '}';
    }
}
