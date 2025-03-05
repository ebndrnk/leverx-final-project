package org.ebndrnk.leverxfinalproject.util.component;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * A generic patcher that updates non-null fields of an incomplete object into an existing object.
 * <p>
 * This utility uses reflection to copy non-null field values from one object to another.
 * Both objects must be of the same type.
 * </p>
 *
 */
@Component
public class Patcher {

    /**
     * Patches the existing object with non-null values from the incomplete object.
     * <p>
     * Both objects must be of the same type. If a field in the incomplete object is not null,
     * its value will be copied to the corresponding field of the existing object.
     * </p>
     *
     * @param existing   The existing object to be updated.
     * @param incomplete The incomplete object containing new values.
     * @param <T>        The type of the object.
     * @throws IllegalAccessException if a field cannot be accessed.
     * @throws IllegalArgumentException if either object is null or if they are of different types.
     */
    public static <T> void patchEntity(T existing, T incomplete) throws IllegalAccessException {
        if (existing == null || incomplete == null) {
            throw new IllegalArgumentException("Neither the existing object nor the incomplete object can be null.");
        }
        if (!existing.getClass().equals(incomplete.getClass())) {
            throw new IllegalArgumentException("Both objects must be of the same type.");
        }

        Field[] fields = existing.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(incomplete);
            if (value != null) {
                field.set(existing, value);
            }
            field.setAccessible(false);
        }
    }
}
