package crusader.collections.enums;

import crusader.collections.LabelProvider;

/**
 *
 */
public class EnumLabelProvider<T extends Enum> implements LabelProvider<T> {
    @Override
    public String getLabel(T item) {
        return item == null ? "_" : item.name();
    }
}
