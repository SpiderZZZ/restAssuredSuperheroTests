package ru.util;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class CollectionUtil {
    private static Random random = new Random();

    @SuppressWarnings("unchecked")
    public static <T> T getRandomElement(Collection<T> collection)
    {
        int size = collection.size();
        return (T) collection.toArray()[random.nextInt(size)];
    }

    public static <T> T getRandomElement(T[] array)
    {
        int length = array.length;
        return (T) array[random.nextInt(length)];
    }

    public static <T> String concatenation(Collection<T> collection, CharSequence separator)
    {
        StringBuilder sb = new StringBuilder();
        for (T item : collection)
        {
            sb.append(item.toString());
            sb.append(separator);
        }
        if (sb.length() > 0)
        {
            sb.setLength(sb.length() - separator.length());
        }
        return sb.toString();
    }

    public static LocalDate getRandomDate()
    {
        Random random = new Random();
        int minDay = (int) LocalDate.of(1700, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2099, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}
