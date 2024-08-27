package xyz.pary.webp.jna;

import com.sun.jna.FromNativeContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;

public class EnumConverter<T extends Enum<T> & JnaEnum<T>> implements TypeConverter {

    @Override
    @SuppressWarnings("unchecked")
    public T fromNative(Object input, FromNativeContext context) {
        Integer intValue = (Integer) input;
        Class<T> clazz = (Class<T>) context.getTargetType();
        T[] constants = clazz.getEnumConstants();
        for (T t : constants) {
            if (t.getValue() == intValue) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown enum value. Enum class " + clazz.getName() + " value " + intValue);
    }

    @Override
    public Integer toNative(Object input, ToNativeContext context) {
        if (input == null) {
            return null;
        }
        JnaEnum e = (JnaEnum) input;
        return e.getValue();
    }

    @Override
    public Class<Integer> nativeType() {
        return Integer.class;
    }
}
